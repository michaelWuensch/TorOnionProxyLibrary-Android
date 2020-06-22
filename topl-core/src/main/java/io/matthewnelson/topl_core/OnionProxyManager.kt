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
package io.matthewnelson.topl_core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.matthewnelson.topl_core.broadcaster.DefaultEventBroadcaster
import io.matthewnelson.topl_core.broadcaster.EventBroadcaster
import io.matthewnelson.topl_core.listener.BaseEventListener
import io.matthewnelson.topl_core.listener.DefaultEventListener
import io.matthewnelson.topl_core.settings.TorSettingsBuilder
import io.matthewnelson.topl_core.util.OnionProxyConsts.ConfigFile
import io.matthewnelson.topl_core.util.FileUtilities
import io.matthewnelson.topl_core.util.WriteObserver
import io.matthewnelson.topl_core_base.TorStates
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
): TorStates() {

    val eventBroadcaster = eventBroadcaster ?: DefaultEventBroadcaster(onionProxyContext.torSettings)
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
     * Sets up and installs any files needed to run tor. If the tor files are already on
     * the system this does not need to be invoked.
     */
    @Throws(IOException::class)
    fun setup() = onionProxyContext.torInstaller.setup()

    fun getNewSettingsBuilder(): TorSettingsBuilder =
        TorSettingsBuilder(onionProxyContext)

    fun getProcessId(): String =
        onionProxyContext.processId

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
                controlConnection!!.getInfo("net/listeners/socks").split(" ".toRegex()).toTypedArray()
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
     * @return The hidden service's onion address in the form X.onion.
     * @throws [IOException] File errors
     * @throws [RuntimeException] See [io.matthewnelson.topl_core.util.WriteObserver.poll]
     * @throws [IllegalStateException] If [controlConnection] is null (service isn't running)
     * @throws [NullPointerException] If [controlConnection] is null even after checking
     * @throws [SecurityException] Unauthorized access to file/directory.
     * @throws [IllegalArgumentException] See [io.matthewnelson.topl_core.util.WriteObserver.checkExists]
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
        val hostnameFile = onionProxyContext.torConfigFiles.hostnameFile

        LOG.info("Creating hidden service")
        if (!onionProxyContext.createNewFileIfDoesNotExist(ConfigFile.HOSTNAME_FILE))
            throw IOException("Could not create hostnameFile")

        // Watch for the hostname file being created/updated
        val hostNameFileObserver = onionProxyContext.createFileObserver(ConfigFile.HOSTNAME_FILE)
        if (!onionProxyContext.setHostnameDirPermissionsToReadOnly())
            throw RuntimeException("Unable to set permissions on hostName dir")

        // Use the control connection to update the Tor config
        val config = listOf(
            "HiddenServiceDir ${hostnameFile.parentFile.absolutePath}",
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
        val hostname = String(onionProxyContext.readFile(ConfigFile.HOSTNAME_FILE), Charsets.UTF_8)
            .trim { it <= ' ' }
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
            eventBroadcaster.torStateMachine.setTorState(TorState.OFF)
            return
        }

        eventBroadcaster.torStateMachine.setTorState(TorState.STOPPING)
        eventBroadcaster.broadcastNotice("Using control port to shutdown Tor")
        try {
            controlConnection!!.setConf("DisableNetwork", "1")
            // Try shutting tor down properly first
            controlConnection!!.signal(TorControlCommands.SIGNAL_SHUTDOWN)

        } catch (e: KotlinNullPointerException) {
            // TODO: May need to resync TorSettings...
            eventBroadcaster.broadcastException(e.message, e)
            eventBroadcaster.torStateMachine.setTorState(TorState.OFF)
            throw NullPointerException(e.message)
        } catch (ee: IOException) {
            eventBroadcaster.broadcastException(ee.message, ee)
            eventBroadcaster.broadcastNotice("Failed to shutdown Tor properly. Sending HALT signal.")

            try {
                controlConnection!!.shutdownTor(TorControlCommands.SIGNAL_HALT)
            } catch (eee: KotlinNullPointerException) {
                eventBroadcaster.broadcastException(eee.message, eee)
                eventBroadcaster.torStateMachine.setTorState(TorState.OFF)
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

            eventBroadcaster.torStateMachine.setTorState(TorState.OFF)

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
        } catch (e: Exception) {
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
     * @throws [SecurityException] Unauthorized access to file/directory.
     */
    @Synchronized
    @Throws(IOException::class, SecurityException::class)
    fun start() {
        if (controlConnection != null) {
            eventBroadcaster.broadcastNotice("Start command called but TorControlConnection already exists.")

            // Re-sync state if it's out of whack
            eventBroadcaster.torStateMachine.setTorState(TorState.ON)
            return
        }

        eventBroadcaster.torStateMachine.setTorState(TorState.STARTING)

        var torProcess: Process? = null
        var controlConnection = findExistingTorConnection()
        val hasExistingTorConnection = controlConnection != null

        if (!hasExistingTorConnection) {

            onionProxyContext.deleteFile(ConfigFile.CONTROL_PORT_FILE)
            onionProxyContext.deleteFile(ConfigFile.COOKIE_AUTH_FILE)

            torProcess = spawnTorProcess()
            controlConnection = try {
                waitForFileCreation(ConfigFile.CONTROL_PORT_FILE)
                connectToTorControlSocket()
            } catch (e: IOException) {
                torProcess.destroy()
                eventBroadcaster.torStateMachine.setTorState(TorState.OFF)
                throw IOException(e.message)
            }
        } else {
            LOG.info("Using existing Tor Process")
        }
        try {
            this.controlConnection = controlConnection

            waitForFileCreation(ConfigFile.COOKIE_AUTH_FILE)
            controlConnection!!.authenticate(onionProxyContext.readFile(ConfigFile.COOKIE_AUTH_FILE))
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
            eventBroadcaster.torStateMachine.setTorState(TorState.OFF)
            throw IOException(e.message)
        }

        eventBroadcaster.torStateMachine.setTorState(TorState.ON)

        networkStateReceiver = NetworkStateReceiver()

        @Suppress("DEPRECATION")
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkStateReceiver, filter)
        LOG.info("Completed starting of tor")
    }

    /**
     * Finds existing tor control connection by trying to connect. Returns null if
     *
     * @throws [SecurityException] Unauthorized access to file/directory.
     */
    @Throws(SecurityException::class)
    private fun findExistingTorConnection(): TorControlConnection? {
        return if (onionProxyContext.torConfigFiles.controlPortFile.exists())
            try {
                connectToTorControlSocket()
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
     * @throws [SecurityException] Unauthorized access to file/directory.
     * @throws [NullPointerException] If controlSocket was null even after setting it.
     */
    @Throws(IOException::class, SecurityException::class, NullPointerException::class)
    private fun connectToTorControlSocket(): TorControlConnection {
        val controlConnection: TorControlConnection
        try {
            val controlPortTokens = String(onionProxyContext.readFile(ConfigFile.CONTROL_PORT_FILE))
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
            throw IOException(
                "Failed to read control port: " +
                        String(onionProxyContext.readFile(ConfigFile.CONTROL_PORT_FILE))
            )
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
     * @throws [SecurityException] Unauthorized access to file/directory.
     */
    @Throws(IOException::class, SecurityException::class)
    private fun spawnTorProcess(): Process {
        val cmd = arrayOf(
            torExecutable().absolutePath,
            "-f",
            torrc().absolutePath,
            OWNER,
            getProcessId()
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
     * Waits for the controlPort or cookieAuth file to be created by the Tor process depending on
     * which you send it. If there is any problem creating the file OR if the timeout for the file
     * to be created is exceeded, then an IOException is thrown.
     *
     * @throws [IOException] File problems or timeout
     * @throws [SecurityException] Unauthorized access to file/directory.
     * @throws [IllegalArgumentException] Method only accepts CONTROL_PORT_FILE or COOKIE_AUTH_FILE
     * @throws [RuntimeException] See [io.matthewnelson.topl_core.util.WriteObserver.poll]
     */
    @Throws(IOException::class,
        SecurityException::class,
        IllegalArgumentException::class,
        RuntimeException::class
    )
    private fun waitForFileCreation(@ConfigFile onionProxyConst: String) {
        val file = when (onionProxyConst) {
            ConfigFile.CONTROL_PORT_FILE -> {
                onionProxyContext.torConfigFiles.controlPortFile
            }
            ConfigFile.COOKIE_AUTH_FILE -> {
                onionProxyContext.torConfigFiles.cookieAuthFile
            }
            else -> {
                throw IllegalArgumentException(
                    "$onionProxyConst is not a valid argument for method waitForFileCreation"
                )
            }
        }

        val startTime = System.currentTimeMillis()
        LOG.info("Waiting for ${file.nameWithoutExtension} file")

        val isCreated: Boolean = onionProxyContext.createNewFileIfDoesNotExist(onionProxyConst)
        val fileObserver: WriteObserver? = onionProxyContext.createFileObserver(onionProxyConst)
        val fileCreationTimeout = onionProxyContext.torConfigFiles.fileCreationTimeout
        if (!isCreated || file.length() == 0L &&
            fileObserver?.poll(fileCreationTimeout.toLong(), TimeUnit.SECONDS) != true
        ) {
            eventBroadcaster.broadcastNotice("${file.nameWithoutExtension} not created")
            eventBroadcaster.torStateMachine.setTorState(TorState.STOPPING)
            throw IOException(
                "${file.nameWithoutExtension} not created: " +
                        "${file.absolutePath}, len = ${file.length()}"
            )
        }
        LOG.info(
            "Created ${file.nameWithoutExtension}: time = ${System.currentTimeMillis()-startTime}ms"
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
            eventBroadcaster.torStateMachine.setTorState(TorStates.TorState.STOPPING)
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
            eventBroadcaster.torStateMachine.setTorState(TorStates.TorState.STOPPING)
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
        environment["HOME"] = onionProxyContext.torConfigFiles.configDir.absolutePath
    }

    private val environmentArgsForExec: Array<String>
        get() {
            val envArgs: MutableList<String> = ArrayList()
            envArgs.add("HOME=" + onionProxyContext.torConfigFiles.configDir.absolutePath)
            return envArgs.toTypedArray()
        }

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
                eventBroadcaster.broadcastNotice("error disabling network ${e.localizedMessage}")
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
                eventBroadcaster.broadcastNotice("error requesting newnym: ${e.localizedMessage}")
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