/*
Copyright (C) 2011-2014 Sublime Software Ltd

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
/*
Copyright (c) Microsoft Open Technologies, Inc.
All Rights Reserved
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED,
INCLUDING WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
MERCHANTABLITY OR NON-INFRINGEMENT.

See the Apache 2 License for the specific language governing permissions and limitations under the License.
*/
package io.matthewnelson.topl_android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.matthewnelson.topl_android.broadcaster.DefaultEventBroadcaster
import io.matthewnelson.topl_android.broadcaster.EventBroadcaster
import io.matthewnelson.topl_android.listener.BaseEventListener
import io.matthewnelson.topl_android.listener.DefaultEventListener
import io.matthewnelson.topl_android.util.FileUtilities
import io.matthewnelson.topl_android_settings.TorState
import net.freehaven.tor.control.TorControlCommands
import net.freehaven.tor.control.TorControlConnection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import java.net.Socket
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * This is where all the fun is, this is the class that handles the heavy work. Note that
 * you will most likely need to actually call into the AndroidOnionProxyManager in order
 * to create the right bindings for your environment.
 *
 * This class is thread safe but that's mostly because we hit everything over the head
 * with 'synchronized'. Given the way this class is used there shouldn't be any performance
 * implications of this.
 *
 * This class began life as TorPlugin from the Briar Project
 *
 * @param [context] Context
 * @param [onionProxyContext] [OnionProxyContext]
 * @param [eventBroadcaster] [EventBroadcaster]? will fallback to defaults if null
 * @param [baseEventListener] [BaseEventListener]? will fallback to defaults if null
 */
class OnionProxyManager(
    private val context: Context,
    val onionProxyContext: OnionProxyContext,
    eventBroadcaster: EventBroadcaster?,
    baseEventListener: BaseEventListener?
) {

    val eventBroadcaster: EventBroadcaster =
        eventBroadcaster ?: DefaultEventBroadcaster(onionProxyContext.torSettings)
    private val eventListener = baseEventListener ?: DefaultEventListener()

    private companion object {
        const val OWNER = "__OwningControllerProcess"
        const val HOSTNAME_TIMEOUT = 30

        val LOG: Logger = LoggerFactory.getLogger(OnionProxyManager::class.java)
    }

    @Volatile
    private var networkStateReceiver: BroadcastReceiver? = null

    @Volatile
    private var controlSocket: Socket? = null

    // If controlConnection is not null then this means that a connection exists and the Tor OP
    // will die when the connection fails.
    @Volatile
    private var controlConnection: TorControlConnection? = null

    @Volatile
    private var controlPort = 0

    /**
     * This is a blocking call that will try to start the Tor OP, connect it to the network
     * and get it to be fully bootstrapped. Sometimes the bootstrap process just hangs for
     * no apparent reason so the method will wait for the given time for bootstrap to finish
     * and if it doesn't then will restart the bootstrap process the given number of repeats.
     *
     * @param [secondsBeforeTimeOut] Seconds to wait for boot strapping to finish
     * @param [numberOfRetries] Num times to try recycling the Tor OP before giving up
     *
     * @return True if bootstrap succeeded, false if there is a problem or timeout.
     *
     * @throws [IllegalArgumentException] If values passed are out of specified bounds.
     * @throws [InterruptedException] You know, if we are interrupted.
     * @throws [IOException] TorControlConnection or File problems.
     * @throws [SecurityException] See [OnionProxyContext.deleteDataDirExceptHiddenService]
     * @throws [RuntimeException] See [OnionProxyContext.deleteDataDirExceptHiddenService]
     */
    @Synchronized
    @Throws(
        IllegalArgumentException::class,
        InterruptedException::class,
        IOException::class,
        SecurityException::class,
        RuntimeException::class
    )
    fun startWithRepeat(secondsBeforeTimeOut: Int, numberOfRetries: Int): Boolean {
        require(secondsBeforeTimeOut > 0 && numberOfRetries > 0) {
            "secondsBeforeTimeOut was less than 0 || numberOfRetries was less than 0"
        }
        return try {
            for (retryCount in 0 until numberOfRetries) {
                start()

                // We will check every second to see if boot strapping has finally finished
                for (secondsWaited in 0 until secondsBeforeTimeOut) {
                    if (!isBootstrapped) {
                        Thread.sleep(1000, 0)
                    } else {
                        eventBroadcaster.broadcastNotice("Tor started; process id = $torPid")
                        return true
                    }
                }

                // Bootstrapping isn't over so we need to restart and try again
                stop()

                // Experimentally we have found that if a Tor OP has run before and thus has
                // cached descriptors and that when we try to start it again it won't start
                // then deleting the cached data can fix this. But, if there is cached data and
                // things do work then the Tor OP will start faster than it would if we delete
                // everything.
                //
                // So our compromise is that we try to start the Tor OP 'as is' on the first
                // round and after that we delete all the files.
                //
                // It can take a little bit for the Tor OP to detect the connection is dead and
                // kill itself.
                Thread.sleep(1000, 0)
                onionProxyContext.deleteDataDirExceptHiddenService()
            }
            false
        } finally {
            // Make sure we return the Tor OP in some kind of consistent state, even if it's 'off'.
            try {
                if (!isRunning)
                    stop()
            } catch (e: NullPointerException) {}
        }
    }

    /**
     * Returns the socks port on the IPv4 localhost address that the Tor OP is listening on
     *
     * @return Discovered socks port
     * @throws [IOException] TorControlConnection or File errors.
     * @throws [RuntimeException] If Tor is not running or there's no localhost binding for Socks.
     * @throws [NullPointerException] If [controlConnection] is null even after checking.
     */
    @get:Throws(IOException::class, RuntimeException::class, NullPointerException::class)
    @get:Synchronized
    val iPv4LocalHostSocksPort: Int
        get() {
            if (!isRunning) throw RuntimeException("Tor is not running!")

            val socksIpPorts = try {
                // This returns a set of space delimited quoted strings which could be Ipv4,
                // Ipv6 or unix sockets.
                controlConnection!!.getInfo("net/listeners/socks").split(" ".toRegex())
                    .toTypedArray()
            } catch (e: KotlinNullPointerException) {
                eventBroadcaster.broadcastException(e.message, e)
                throw NullPointerException(e.message)
            } catch (ee: IOException) {
                LOG.warn("Control connection is not responding properly to getInfo", ee)
                throw IOException(ee.message)
            }

            for (address in socksIpPorts) {
                if (address.contains("\"127.0.0.1:")) {
                    // Remember, the last character will be a " so we have to remove that.
                    return address.substring(address.lastIndexOf(":") + 1, address.length - 1).toInt()
                }
            }

            throw RuntimeException("We don't have an Ipv4 localhost binding for socks!")
        }

    /**
     * Publishes a hidden service
     *
     * @param [hiddenServicePort] The port that the hidden service will accept connections on
     * @param [localPort] The local port that the hidden service will relay connections to
     *
     * @return The hidden service's onion address in the form X.onion.
     *
     * @throws [java.io.IOException] File errors
     * @throws [RuntimeException] if unable to poll the file observer
     * @throws [IllegalStateException] If [controlConnection] is null (service isn't running)
     * @throws [NullPointerException] If [controlConnection] is null
     * @throws [SecurityException] File errors
     * @throws [IllegalArgumentException] If unable to create the hostname directory observer
     */
    @Synchronized
    @Throws(
        IOException::class,
        RuntimeException::class,
        IllegalStateException::class,
        NullPointerException::class,
        SecurityException::class,
        IllegalArgumentException::class)
    fun publishHiddenService(hiddenServicePort: Int, localPort: Int): String {
        checkNotNull(controlConnection) { "Service is not running." }

        LOG.info("Creating hidden service")
        if (!onionProxyContext.createHostnameFile())
            throw IOException("Could not create hostnameFile")

        // Watch for the hostname file being created/updated
        val hostNameFileObserver = onionProxyContext.createHostnameDirObserver()
        val hostnameFile = onionProxyContext.torConfigFiles.hostnameFile
        val hostnameDir = hostnameFile.parentFile
        if (!FileUtilities.setToReadOnlyPermissions(hostnameDir))
            throw RuntimeException("Unable to set permissions on hostName dir")

        // Use the control connection to update the Tor config
        val config = listOf(
            "HiddenServiceDir ${hostnameDir.absolutePath}",
            "HiddenServicePort $hiddenServicePort 127.0.0.1:$localPort"
        )

        try {
            controlConnection!!.setConf(config)
            controlConnection!!.saveConf()
        } catch (e: KotlinNullPointerException) {
            eventBroadcaster.broadcastException(e.message, e)
            throw NullPointerException(e.message)
        }

        // Wait for the hostname file to be created/updated
        if (!hostNameFileObserver.poll(HOSTNAME_TIMEOUT.toLong(), TimeUnit.SECONDS)) {
            FileUtilities.listFilesToLog(hostnameFile.parentFile)
            throw RuntimeException("Wait for hidden service hostname file to be created expired.")
        }

        // Publish the hidden service's onion hostname in transport properties
        val hostname = String(FileUtilities.read(hostnameFile), Charsets.UTF_8).trim { it <= ' ' }
        LOG.info("Hidden service config has completed.")

        return hostname
    }

    /**
     * Kills the Tor OP Process. Once you have called this method nothing is going
     * to work until you either call startWithRepeat or start
     *
     * @throws [NullPointerException] If controlConnection magically changes to null.
     */
    @Synchronized
    @Throws(NullPointerException::class)
    fun stop() {
        if (controlConnection == null) {
            eventBroadcaster.broadcastNotice("Stop command called but no TorControlConnection exists.")

            // Re-sync state if it's out of whack
            eventBroadcaster.state.setTorState(TorState.OFF)
            return
        }

        eventBroadcaster.state.setTorState(TorState.STOPPING)
        eventBroadcaster.broadcastNotice("Using control port to shutdown Tor")
        try {
            controlConnection!!.setConf("DisableNetwork", "1")
            // Try shutting tor down properly first
            controlConnection!!.signal(TorControlCommands.SIGNAL_SHUTDOWN)

        } catch (e: KotlinNullPointerException) {
            // TODO: May need to resync TorSettings...
            eventBroadcaster.broadcastException(e.message, e)
            eventBroadcaster.state.setTorState(TorState.OFF)
            throw NullPointerException(e.message)
        } catch (ee: IOException) {
            eventBroadcaster.broadcastException(ee.message, ee)
            eventBroadcaster.broadcastNotice("Failed to shutdown Tor properly. Sending HALT signal.")

            try {
                controlConnection!!.shutdownTor(TorControlCommands.SIGNAL_HALT)
            } catch (eee: KotlinNullPointerException) {
                eventBroadcaster.broadcastException(eee.message, eee)
                eventBroadcaster.state.setTorState(TorState.OFF)
                throw NullPointerException(eee.message)
            } catch (eeee: IOException) {}

        } finally {
            controlConnection = null
            if (controlSocket != null) {
                try {
                    controlSocket!!.close()
                } finally {
                    controlSocket = null
                }
            }

            eventBroadcaster.state.setTorState(TorState.OFF)

            if (networkStateReceiver == null) return

            try {
                context.unregisterReceiver(networkStateReceiver)
            } catch (e: IllegalArgumentException) {
                // There is a race condition where if someone calls stop before
                // installAndStartTorOp is done then we could get an exception because
                // the network state receiver might not be properly registered.
                LOG.info("Someone tried to call stop before registering the receiver finished", e)
            }
        }
    }

    /**
     * Checks to see if the Tor OP is running (e.g. fully bootstrapped) and open to
     * network connections.
     *
     * @return True if running
     */
    @get:Synchronized
    val isRunning: Boolean
        get() = try {
            isBootstrapped && isNetworkEnabled
        } catch (e: IOException) {
            false
        } catch (ee: NullPointerException) {
            false
        }

    /**
     * Tells the Tor OP if it should accept network connections
     *
     * @param [enable] If true then the Tor OP will accept SOCKS connections, otherwise not.
     * @throws [IOException] if having issues with TorControlConnection#setConf
     * @throws [NullPointerException] if [controlConnection] is null even after checking.
     */
    @Synchronized
    @Throws(IOException::class, NullPointerException::class)
    fun enableNetwork(enable: Boolean) {
        if (controlConnection == null) return

        eventBroadcaster.broadcastNotice("Enabling network: $enable")

        try {
            controlConnection!!.setConf("DisableNetwork", if (enable) "0" else "1")
        } catch (e: KotlinNullPointerException) {
            eventBroadcaster.broadcastException(e.message, e)
            throw NullPointerException(e.message)
        } catch (ee: IOException) {
            LOG.warn("TorControlConnection is not responding properly to setConf.")
            eventBroadcaster.broadcastException(ee.message, ee)
            throw IOException(ee.message)
        }
    }

    /**
     * Specifies if Tor OP is accepting network connections
     *
     * @return True if network is enabled (that doesn't mean that the device is online, only that the Tor OP is trying to connect to the network)
     * @throws [java.io.IOException]
     * @throws [NullPointerException] if [controlConnection] is null
     */
    @get:Throws(IOException::class, NullPointerException::class)
    @get:Synchronized
    private val isNetworkEnabled: Boolean
        get() {
            if (controlConnection == null) return false

            val disableNetworkSettingValues = try {
                controlConnection!!.getConf("DisableNetwork")
            } catch (e: KotlinNullPointerException) {
                eventBroadcaster.broadcastException(e.message, e)
                throw NullPointerException(e.message)
            } catch (ee: IOException) {
                LOG.warn("TorControlConnection is not responding properly to getConf.")
                eventBroadcaster.broadcastException(ee.message, ee)
                throw IOException(ee.message)
            }

            var result = false

            // It's theoretically possible for us to get multiple values back, if even one is
            // false then we will assume all are false
            for (configEntry in disableNetworkSettingValues) {
                result = if (configEntry.value == "1") {
                    return false
                } else {
                    true
                }
            }
            return result
        }

    /**
     * Determines if the boot strap process has completed.
     *
     * @return True if complete
     */
    @get:Synchronized
    private val isBootstrapped: Boolean
        get() {
            if (controlConnection == null) return false

            try {
                val phase = controlConnection?.getInfo("status/bootstrap-phase")
                if (phase != null && phase.contains("PROGRESS=100")) {
                    LOG.info("Tor has already bootstrapped")
                    return true
                }
            } catch (e: IOException) {
                eventBroadcaster.broadcastException(e.message, e)
                LOG.warn("Control connection is not responding properly to getInfo", e)
            }
            return false
        }

    /**
     * Starts tor control service if it isn't already running.
     *
     * @throws [IOException] File errors
     * @throws [SecurityException] File errors
     */
    @Synchronized
    @Throws(IOException::class, SecurityException::class)
    fun start() {
        if (controlConnection != null) {
            eventBroadcaster.broadcastNotice("Start command called but TorControlConnection already exists.")
            eventBroadcaster.state.setTorState(TorState.ON)
            return
        }

        eventBroadcaster.state.setTorState(TorState.STARTING)

        var torProcess: Process? = null
        var controlConnection = findExistingTorConnection()
        val hasExistingTorConnection = controlConnection != null

        if (!hasExistingTorConnection) {
            val controlPortFile = onionProxyContext.torConfigFiles.controlPortFile
            controlPortFile.delete()
            if (!controlPortFile.parentFile.exists()) controlPortFile.parentFile.mkdirs()

            val cookieAuthFile = onionProxyContext.torConfigFiles.cookieAuthFile
            cookieAuthFile.delete()
            if (!cookieAuthFile.parentFile.exists()) cookieAuthFile.parentFile.mkdirs()

            torProcess = spawnTorProcess()
            controlConnection = try {
                waitForControlPortFileCreation(controlPortFile)
                connectToTorControlSocket(controlPortFile)
            } catch (e: IOException) {
                torProcess.destroy()
                eventBroadcaster.state.setTorState(TorState.OFF)
                throw IOException(e.message)
            }
        } else {
            LOG.info("Using existing Tor Process")
        }
        try {
            this.controlConnection = controlConnection

            val cookieAuthFile = onionProxyContext.torConfigFiles.cookieAuthFile
            waitForCookieAuthFileCreation(cookieAuthFile)
            controlConnection!!.authenticate(FileUtilities.read(cookieAuthFile))
            eventBroadcaster.broadcastNotice("SUCCESS - authenticated tor control port.")
            if (hasExistingTorConnection) {
                controlConnection.signal(TorControlCommands.SIGNAL_RELOAD)
                eventBroadcaster.broadcastNotice("Reloaded configuration file")
            }
            controlConnection.takeOwnership()
            controlConnection.resetConf(setOf(OWNER))
            eventBroadcaster.broadcastNotice("Took ownership of tor control port.")

            if (eventListener.CONTROL_COMMAND_EVENTS.isNotEmpty()) {
                eventBroadcaster.broadcastNotice("adding control port event listener")
                controlConnection.addRawEventListener(eventListener)
                controlConnection.setEvents(listOf(*eventListener.CONTROL_COMMAND_EVENTS))
                eventBroadcaster.broadcastNotice("SUCCESS added control port event listener")
            }

            enableNetwork(true)
        } catch (e: Exception) {
            torProcess?.destroy()
            this.controlConnection = null
            eventBroadcaster.state.setTorState(TorState.OFF)
            throw IOException(e.message)
        }

        eventBroadcaster.state.setTorState(TorState.ON)

        networkStateReceiver = NetworkStateReceiver()

        @Suppress("DEPRECATION")
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkStateReceiver, filter)
        LOG.info("Completed starting of tor")
    }

    /**
     * Finds existing tor control connection by trying to connect. Returns null if
     *
     * @throws [SecurityException] File errors
     */
    @Throws(SecurityException::class)
    private fun findExistingTorConnection(): TorControlConnection? {
        val controlPortFile = onionProxyContext.torConfigFiles.controlPortFile
        return if (controlPortFile.exists())
            try {
                connectToTorControlSocket(controlPortFile)
            } catch (e: IOException) {
                null
            }
        else
            null
    }

    /**
     * Looks in the specified `controlPortFile` for the port and attempts to open a
     * control connection.
     *
     * @throws [IOException] File errors
     * @throws [SecurityException] File errors
     * @throws [NullPointerException] If controlSocket was null even after setting it.
     */
    @Throws(IOException::class, SecurityException::class, NullPointerException::class)
    private fun connectToTorControlSocket(controlPortFile: File): TorControlConnection {
        val controlConnection: TorControlConnection
        try {
            val controlPortTokens = String(FileUtilities.read(controlPortFile))
                .trim { it <= ' ' }.split(":".toRegex()).toTypedArray()
            controlPort = controlPortTokens[1].toInt()
            eventBroadcaster.broadcastNotice("Connecting to control port: $controlPort")
            controlSocket = Socket(
                controlPortTokens[0].split("=".toRegex()).toTypedArray()[1],
                controlPort
            )
            controlConnection = TorControlConnection(controlSocket!!)
            eventBroadcaster.broadcastNotice("SUCCESS connected to Tor control port.")
        } catch (e: IOException) {
            eventBroadcaster.broadcastException(e.message, e)
            throw IOException(e.message)
        } catch (ee: ArrayIndexOutOfBoundsException) {
            throw IOException("Failed to read control port: ${String(FileUtilities.read(controlPortFile))}")
        } catch (eee: KotlinNullPointerException) {
            eventBroadcaster.broadcastException(eee.message, eee)
            throw NullPointerException(eee.message)
        }
        if (onionProxyContext.torSettings.hasDebugLogs) {
            controlConnection.setDebugging(System.out)
        }
        return controlConnection
    }

    /**
     * Spawns the tor native process from the existing Java process.
     *
     * @throws [IOException] File errors.
     * @throws [SecurityException] File errors.
     */
    @Throws(IOException::class, SecurityException::class)
    private fun spawnTorProcess(): Process {
        val pid = onionProxyContext.processId
        val cmd = arrayOf(
            torExecutable().absolutePath,
            "-f",
            torrc().absolutePath,
            OWNER,
            pid
        )
        val processBuilder = ProcessBuilder(*cmd)
        setEnvironmentArgsAndWorkingDirectoryForStart(processBuilder)
        LOG.info("Starting process")
        val torProcess: Process = try {
            processBuilder.start()
        } catch (e: SecurityException) {
            LOG.warn(e.toString(), e)
            throw IOException(e)
        }

        eatStream(torProcess.errorStream, true)
        if (onionProxyContext.torSettings.hasDebugLogs)
            eatStream(torProcess.inputStream, false)
        return torProcess
    }

    /**
     * Waits for the control port file to be created by the Tor process.
     *
     * @throws [IOException] File problems or timeout
     * @throws [SecurityException] File problems
     */
    @Throws(IOException::class, SecurityException::class)
    private fun waitForControlPortFileCreation(controlPortFile: File) {
        val controlPortStartTime = System.currentTimeMillis()
        LOG.info("Waiting for control port")
        val isCreated = controlPortFile.exists() || controlPortFile.createNewFile()
        val controlPortFileObserver = onionProxyContext.createControlPortFileObserver()
        if (!isCreated || controlPortFile.length() == 0L && !controlPortFileObserver.poll(
                onionProxyContext.torConfigFiles.fileCreationTimeout.toLong(), TimeUnit.SECONDS
            )
        ) {
            LOG.warn("Control port file not created")
            FileUtilities.listFilesToLog(onionProxyContext.torConfigFiles.dataDir)
            eventBroadcaster.broadcastNotice("Tor control port file not created")
            eventBroadcaster.state.setTorState(TorState.STOPPING)
            throw IOException(
                "Control port file not created: ${controlPortFile.absolutePath}, "
                        + "len = ${controlPortFile.length()}"
            )
        }
        LOG.info("Created control port file: time = " +
                (System.currentTimeMillis() - controlPortStartTime) + "ms"
        )
    }

    /**
     * Waits for the cookie auth file to be created by the Tor process. If there is any problem creating the file OR
     * if the timeout for the cookie auth file to be created is exceeded, then  an IOException is thrown.
     */
    @Throws(IOException::class)
    private fun waitForCookieAuthFileCreation(cookieAuthFile: File) {
        val cookieAuthStartTime = System.currentTimeMillis()
        LOG.info("Waiting for cookie auth file")
        val isCreated = cookieAuthFile.exists() || cookieAuthFile.createNewFile()
        val cookieAuthFileObserver = onionProxyContext.createCookieAuthFileObserver()
        if (!isCreated || cookieAuthFile.length() == 0L && !cookieAuthFileObserver.poll(
                onionProxyContext.torConfigFiles.fileCreationTimeout.toLong(), TimeUnit.SECONDS
            )
        ) {
            LOG.warn("Cookie Auth file not created")
            eventBroadcaster.broadcastNotice("Cookie Auth file not created")
            eventBroadcaster.state.setTorState(TorState.STOPPING)
            throw IOException(
                "Cookie Auth file not created: ${cookieAuthFile.absolutePath}, " +
                        "len = ${cookieAuthFile.length()}"
            )
        }
        LOG.info(
            "Created cookie auth file: time = ${System.currentTimeMillis() - cookieAuthStartTime}ms"
        )
    }

    private fun eatStream(inputStream: InputStream, isError: Boolean) {
        object : Thread() {
            override fun run() {
                val scanner = Scanner(inputStream)
                try {
                    while (scanner.hasNextLine()) {
                        val line = scanner.nextLine()
                        if (isError) {
                            LOG.error(line)
                            eventBroadcaster.broadcastException(line, Exception())
                        } else {
                            LOG.info(line)
                        }
                    }
                } finally {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        LOG.error("Couldn't close input stream in eatStream", e)
                    }
                }
            }
        }.start()
    }

    @Throws(IOException::class, SecurityException::class)
    private fun torExecutable(): File {
        var torExe = onionProxyContext.torConfigFiles.torExecutableFile
        //Try removing platform specific extension
        if (!torExe.exists())
            torExe = File(torExe.parent, "tor")

        if (!torExe.exists()) {
            eventBroadcaster.broadcastNotice("Tor executable not found")
            eventBroadcaster.state.setTorState(TorState.STOPPING)
            LOG.error("Tor executable not found: ${torExe.absolutePath}")
            throw IOException("Tor executable not found")
        }
        return torExe
    }

    @Throws(IOException::class, SecurityException::class)
    private fun torrc(): File {
        val torrc = onionProxyContext.torConfigFiles.torrcFile
        if (!torrc.exists()) {
            eventBroadcaster.broadcastNotice("Torrc not found")
            eventBroadcaster.state.setTorState(TorState.STOPPING)
            LOG.error("Torrc not found: ${torrc.absolutePath}")
            throw IOException("Torrc not found")
        }
        return torrc
    }

    /**
     * Sets environment variables and working directory needed for Tor
     *
     * @param [processBuilder] we will call start on this to run Tor
     */
    private fun setEnvironmentArgsAndWorkingDirectoryForStart(processBuilder: ProcessBuilder) {
        processBuilder.directory(onionProxyContext.torConfigFiles.configDir)
        val environment = processBuilder.environment()
        environment["HOME"] = onionProxyContext.torConfigFiles.homeDir.absolutePath
    }

    private val environmentArgsForExec: Array<String>
        get() {
            val envArgs: MutableList<String> = ArrayList()
            envArgs.add("HOME=" + onionProxyContext.torConfigFiles.homeDir.absolutePath)
            return envArgs.toTypedArray()
        }

    /**
     * Sets up and installs any files needed to run tor. If the tor files are already on
     * the system this does not need to be invoked.
     *
     * @return true if tor installation is successful, otherwise false
     */
    fun setup(): Boolean =
        onionProxyContext.torInstaller.setup()

    val isIPv4LocalHostSocksPortOpen: Boolean
        get() = try {
            iPv4LocalHostSocksPort
            true
        } catch (e: Exception) {
            false
        }

    /**
     * Sets the exit nodes through the tor control connection
     *
     * @param [exitNodes]
     *
     * @return true if successfully set, otherwise false
     */
    fun setExitNode(exitNodes: String?): Boolean {
        //Based on config params from Orbot project
        if (!hasControlConnection()) return false

        if (exitNodes.isNullOrEmpty()) {
            try {
                val resetBuffer = ArrayList<String>()
                resetBuffer.add("ExitNodes")
                resetBuffer.add("StrictNodes")
                controlConnection!!.resetConf(resetBuffer)
                controlConnection!!.setConf("DisableNetwork", "1")
                controlConnection!!.setConf("DisableNetwork", "0")
            } catch (ioe: Exception) {
                LOG.error("Connection exception occurred resetting exits", ioe)
                return false
            }
        } else {
            try {
                controlConnection!!.setConf("GeoIPFile", onionProxyContext.torConfigFiles.geoIpFile.canonicalPath)
                controlConnection!!.setConf("GeoIPv6File", onionProxyContext.torConfigFiles.geoIpv6File.canonicalPath)
                controlConnection!!.setConf("ExitNodes", exitNodes)
                controlConnection!!.setConf("StrictNodes", "1")
                controlConnection!!.setConf("DisableNetwork", "1")
                controlConnection!!.setConf("DisableNetwork", "0")
            } catch (ioe: Exception) {
                LOG.error("Connection exception occurred resetting exits", ioe)
                return false
            }
        }
        return true
    }

    fun disableNetwork(isEnabled: Boolean): Boolean {
        return if (!hasControlConnection()) {
            false
        } else {
            try {
                controlConnection!!.setConf("DisableNetwork", if (isEnabled) "0" else "1")
                true
            } catch (e: Exception) {
                eventBroadcaster.broadcastDebug("error disabling network ${e.localizedMessage}")
                false
            }
        }
    }

    fun setNewIdentity(): Boolean {
        return if (!hasControlConnection()) {
            false
        } else {
            try {
                controlConnection!!.signal(TorControlCommands.SIGNAL_NEWNYM)
                true
            } catch (e: IOException) {
                eventBroadcaster.broadcastDebug("error requesting newnym: ${e.localizedMessage}")
                false
            }
        }
    }

    fun hasControlConnection(): Boolean =
        controlConnection != null

    val torPid: Int
        get() {
            val pidS = getInfo("process/pid")
            return if (pidS.isNullOrEmpty()) -1 else Integer.valueOf(pidS)
        }

    fun getInfo(info: String): String? {
        return if (!hasControlConnection()) {
            null
        } else try {
            controlConnection!!.getInfo(info)
        } catch (e: IOException) {
            eventBroadcaster.broadcastException(e.message, e)
            LOG.warn("Control connection is not responding properly to getInfo", e)
            null
        } catch (ee: KotlinNullPointerException) {
            eventBroadcaster.broadcastException(ee.message, ee)
            null
        }
    }

    fun reloadTorConfig(): Boolean {
        if (!hasControlConnection()) return false

        try {
            controlConnection!!.signal(TorControlCommands.SIGNAL_RELOAD)
            return true
        } catch (e: IOException) {
            LOG.warn("TorControlConnection is not responding properly to signal.")
            eventBroadcaster.broadcastException(e.message, e)
        } catch (ee: KotlinNullPointerException) {
            eventBroadcaster.broadcastException(ee.message, ee)
        }

        try {
            restartTorProcess()
            return true
        } catch (e: Exception) {
            eventBroadcaster.broadcastException(e.message, e)
        }

        return false
    }

    @Throws(Exception::class)
    fun restartTorProcess() = killTorProcess(-1)

    @Throws(Exception::class)
    fun killTorProcess() = killTorProcess(-9)

    @Throws(Exception::class)
    private fun killTorProcess(signal: Int) {
        //Based on logic from Orbot project
        val torFileName = onionProxyContext.torConfigFiles.torExecutableFile.name
        var procId: Int
        var killAttempts = 0
        while (torPid.also { procId = it } != -1) {

            val pidString = procId.toString()
            execIgnoreException(String.format("busybox killall %d %s", signal, torFileName))
            execIgnoreException(String.format("toolbox kill %d %s", signal, pidString))
            execIgnoreException(String.format("busybox kill %d %s", signal, pidString))
            execIgnoreException(String.format("kill %d %s", signal, pidString))

            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {}

            killAttempts++

            if (killAttempts > 4)
                throw Exception("Cannot kill: ${onionProxyContext.torConfigFiles.torExecutableFile.absolutePath}")
        }
    }

    private fun execIgnoreException(command: String): Process? =
        try {
            Runtime.getRuntime().exec(command)
        } catch (e: Exception) {
            null
        }

    private inner class NetworkStateReceiver : BroadcastReceiver() {

        override fun onReceive(ctx: Context, i: Intent) {

            Thread(Runnable {
                if (!isRunning) return@Runnable

                var online = !i.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                if (online) {
                    // Some devices fail to set EXTRA_NO_CONNECTIVITY, double check
                    val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val net = cm.activeNetworkInfo
                    if (net == null || !net.isConnected)
                        online = false
                }
                LOG.info("Online: $online")

                try {
                    enableNetwork(online)
                } catch (e: Exception) {
                    LOG.warn(e.message, e)
                }

            }).start()
        }
    }

}