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

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.matthewnelson.topl_core_base.EventBroadcaster
import io.matthewnelson.topl_core.listener.BaseEventListener
import io.matthewnelson.topl_core.receiver.NetworkStateReceiver
import io.matthewnelson.topl_core.settings.TorSettingsBuilder
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_core.broadcaster.TorStateMachine
import io.matthewnelson.topl_core.util.OnionProxyConsts.ConfigFile
import io.matthewnelson.topl_core.util.FileUtilities
import io.matthewnelson.topl_core.util.WriteObserver
import io.matthewnelson.topl_core_base.TorStates
import net.freehaven.tor.control.TorControlCommands
import net.freehaven.tor.control.TorControlConnection
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
 * @param [context] Context.
 * @param [onionProxyContext] [OnionProxyContext]
 * @param [eventListener] [BaseEventListener] For processing Tor OP messages.
 * @param [eventBroadcaster] Your own broadcaster which extends [EventBroadcaster]
 * @param [buildConfigDebug] Send [BuildConfig.DEBUG] which will show Logcat messages for this
 *   module on Debug builds of your Application. If `null`, all the messages will still be
 *   broadcast to the provided [EventBroadcaster] and you can handle them there how you'd like.
 */
class OnionProxyManager(
    private val context: Context,
    private val onionProxyContext: OnionProxyContext,
    private val eventListener: BaseEventListener,
    private val eventBroadcaster: EventBroadcaster,
    buildConfigDebug: Boolean?
): TorStates() {

    companion object {
        private const val OWNER = "__OwningControllerProcess"
        private const val HOSTNAME_TIMEOUT = 30
        const val NEWNYM_SUCCESS_MESSAGE = "You've changed Tor identities!"
        const val NEWNYM_NO_NETWORK = "No network, cannot change Tor identities"
        const val NEWNYM_RATE_LIMIT_PARTIAL_MSG = "Rate limiting NEWNYM request: "
    }

    ///////////////////////
    /// BroadcastLogger ///
    ///////////////////////
    private val buildConfigDebug = buildConfigDebug ?: BuildConfig.DEBUG
    private val broadcastLogger = createBroadcastLogger(OnionProxyManager::class.java)

    /**
     * Helper method for instantiating a [BroadcastLogger] for your class with the values
     * TOPL-Android has been initialized with.
     *
     * @param [clazz] To initialize the `TAG` with your class' name.
     * */
    fun createBroadcastLogger(clazz: Class<*>) =
        BroadcastLogger(clazz, eventBroadcaster, buildConfigDebug, onionProxyContext.torSettings)

    init {
        onionProxyContext.initBroadcastLogger(createBroadcastLogger(OnionProxyContext::class.java))
        try {
            onionProxyContext.createDataDir()
        } catch (e: SecurityException) {
            broadcastLogger.exception(e)
        }
    }

    /**
     * Sets up and installs any files needed to run tor. If the tor files are already on
     * the system this does not need to be invoked.
     */
    @Throws(IOException::class)
    fun setup() = onionProxyContext.torInstaller.setup()

    fun getNewSettingsBuilder(): TorSettingsBuilder {
        val torSettingsBuilder = TorSettingsBuilder(onionProxyContext)
        torSettingsBuilder.initBroadcastLogger(createBroadcastLogger(TorSettingsBuilder::class.java))
        return torSettingsBuilder
    }

    fun getProcessId(): String =
        onionProxyContext.processId

    val torStateMachine =
        TorStateMachine(createBroadcastLogger(TorStateMachine::class.java))

    @Volatile
    private var networkStateReceiver: NetworkStateReceiver? = null

    @Volatile
    private var controlSocket: Socket? = null

    // If controlConnection is not null then this means that a connection exists and the Tor OP
    // will die when the connection fails.
    @Volatile
    private var controlConnection: TorControlConnection? = null

    @Volatile
    private var controlPort = 0

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
                broadcastLogger.warn("TorControlConnection was null even after checking.")
                throw NullPointerException(e.message)
            } catch (ee: IOException) {
                broadcastLogger.warn("Control connection is not responding properly to getInfo")
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

        broadcastLogger.notice("Creating hidden service")
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
            broadcastLogger.warn("TorControlConnection was null even after checking.")
            throw NullPointerException(e.message)
        }
        // TODO: catch IOException from not responding to signals.

        // Wait for the hostname file to be created/updated
        if (!hostNameFileObserver.poll(HOSTNAME_TIMEOUT.toLong(), TimeUnit.SECONDS)) {
            FileUtilities.listFilesToLog(hostnameFile.parentFile)
            throw RuntimeException("Wait for hidden service hostname file to be created expired.")
        }

        // Publish the hidden service's onion hostname in transport properties
        val hostname = String(onionProxyContext.readFile(ConfigFile.HOSTNAME_FILE), Charsets.UTF_8)
            .trim { it <= ' ' }
        broadcastLogger.notice("Hidden service configuration completed.")

        return hostname
    }

    /**
     * Kills the Tor OP Process. Once you have called this method nothing is going
     * to work until you either call startWithRepeat or start
     *
     * @throws [NullPointerException] If [controlConnection] magically changes to null.
     * @throws [IOException] If [controlConnection] is not responding to `shutdownTor`.
     */
    @Synchronized
    @Throws(NullPointerException::class, IOException::class)
    fun stop() {
        if (controlConnection == null) {
            broadcastLogger.notice("Stop command called but no TorControlConnection exists.")

            // Re-sync state if it's out of whack
            torStateMachine.setTorState(TorState.OFF)
            return
        }

        torStateMachine.setTorState(TorState.STOPPING)
        broadcastLogger.debug("Using control port to shutdown Tor")
        try {
            disableNetwork(true)
            controlConnection!!.signal(TorControlCommands.SIGNAL_SHUTDOWN)
        } catch (e: KotlinNullPointerException) {
            torStateMachine.setTorState(TorState.ON)
            broadcastLogger.warn("TorControlConnection was null even after checking.")
            throw NullPointerException(e.message)
        } catch (ee: IOException) {
            broadcastLogger.warn("TorControlConnection not responding to signal. Trying to HALT.")

            try {
                controlConnection!!.shutdownTor(TorControlCommands.SIGNAL_HALT)
            } catch (eee: KotlinNullPointerException) {
                broadcastLogger.warn("TorControlConnection was null even after checking.")
                torStateMachine.setTorState(TorState.ON)
                throw NullPointerException(eee.message)
            } catch (eeee: IOException) {
                broadcastLogger.warn("TorControlConnection not responding to shutdownTor.")
                torStateMachine.setTorState(TorState.ON)
                throw IOException(eeee.message)
            }

        } finally {

            try {
                controlConnection!!.removeRawEventListener(eventListener)
            } catch (e: KotlinNullPointerException) {}

            controlConnection = null
            if (controlSocket != null) {
                try {
                    controlSocket!!.close()
                } finally {
                    controlSocket = null
                }
            }

            torStateMachine.setTorState(TorState.OFF)

            if (networkStateReceiver == null) return

            try {
                context.unregisterReceiver(networkStateReceiver)
            } catch (e: IllegalArgumentException) {
                // There is a race condition where if someone calls stop before
                // installAndStartTorOp is done then we could get an exception because
                // the network state receiver might not be properly registered.
                broadcastLogger.exception(
                    IllegalArgumentException(
                        "Someone tried calling stop before registering of NetworkStateReceiver", e
                    )
                )
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
            isBootstrapped && !isNetworkDisabled
        } catch (e: Exception) {
            false
        }

    private val disableNetworkLock = Object()
    /**
     * Tells the Tor OP if it should accept network connections.
     *
     * Whenever setting Tor's Conf to `DisableNetwork X`, ONLY use this method to do it
     * such that [torStateMachine] will reflect the proper
     * [TorStates.TorNetworkState].
     *
     * @param [disable] If true then the Tor OP will **not** accept SOCKS connections, otherwise yes.
     * @throws [IOException] if having issues with TorControlConnection#setConf
     * @throws [KotlinNullPointerException] if [controlConnection] is null even after checking.
     */
    @Throws(IOException::class, KotlinNullPointerException::class)
    fun disableNetwork(disable: Boolean) {
        synchronized(disableNetworkLock) {
            if (controlConnection == null) return

            val networkIsSetToDisable = try {
                isNetworkDisabled
            } catch (e: Exception) {
                !disable
            }

            if (networkIsSetToDisable == disable) return

            broadcastLogger.debug("Setting Tor conf DisableNetwork: $disable")

            try {
                controlConnection!!.setConf("DisableNetwork", if (disable) "1" else "0")
                torStateMachine.setTorNetworkState(
                    if (disable) TorNetworkState.DISABLED else TorNetworkState.ENABLED
                )
            } catch (e: IOException) {
                broadcastLogger.warn("TorControlConnection is not responding properly to setConf.")
                throw IOException(e.message)
            } catch (ee: KotlinNullPointerException) {
                broadcastLogger.warn("TorControlConnection was null even after checking.")
                throw NullPointerException(ee.message)
            }
        }
    }

    /**
     * Specifies if Tor OP is accepting network connections.
     *
     * @return True = "DisableNetwork 1" (network disabled), false = "DisableNetwork 0" (network enabled)
     * @throws [IOException] if [TorControlConnection] is not responding to getConf.
     * @throws [NullPointerException] if [controlConnection] is null.
     */
    @get:Throws(IOException::class, NullPointerException::class)
    @get:Synchronized
    private val isNetworkDisabled: Boolean
        get() {
            if (controlConnection == null) return true

            val disableNetworkSettingValues = try {
                controlConnection!!.getConf("DisableNetwork")
            } catch (e: KotlinNullPointerException) {
                broadcastLogger.warn("TorControlConnection was null even after checking.")
                throw NullPointerException(e.message)
            } catch (ee: IOException) {
                broadcastLogger.warn("TorControlConnection is not responding properly to getConf.")
                throw IOException(ee.message)
            }

            var result = true

            // It's theoretically possible for us to get multiple values back, if even one is
            // "DisableNetwork 1" then we will assume all are "DisableNetwork 1"
            for (configEntry in disableNetworkSettingValues) {
                result = if (configEntry.value == "1") {
                    return true
                } else {
                    false
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
                    broadcastLogger.debug("isBootstrapped: true")
                    return true
                }
            } catch (e: IOException) {
                broadcastLogger.warn("TorControlConnection is not responding properly to getInfo.")
            }
            return false
        }

    /**
     * Starts tor control service if it isn't already running.
     *
     * @throws [IOException] File errors
     * @throws [SecurityException] Unauthorized access to file/directory.
     * @throws [IllegalArgumentException] if [onionProxyContext] methods are passed incorrect
     *   [ConfigFile] string values
     */
    @Synchronized
    @Throws(IOException::class, SecurityException::class, IllegalArgumentException::class)
    fun start() {
        if (controlConnection != null) {
            broadcastLogger.notice("Start command called but TorControlConnection already exists")

            // Re-sync state if it's out of whack
            torStateMachine.setTorState(TorState.ON)
            return
        }

        torStateMachine.setTorState(TorState.STARTING)

        var torProcess: Process? = null
        var controlConnection = findExistingTorConnection()
        val hasExistingTorConnection = controlConnection != null

        if (!hasExistingTorConnection) {

            try {
                onionProxyContext.deleteFile(ConfigFile.CONTROL_PORT_FILE)
                onionProxyContext.deleteFile(ConfigFile.COOKIE_AUTH_FILE)
            } catch (e: Exception) {
                torStateMachine.setTorState(TorState.OFF)
                throw IOException(e.message)
            }

            torProcess = try {
                spawnTorProcess()
            } catch (e: Exception) {
                torStateMachine.setTorState(TorState.OFF)
                throw IOException(e)
            }

            controlConnection = try {
                waitForFileCreation(ConfigFile.CONTROL_PORT_FILE)
                connectToTorControlSocket()
            } catch (e: Exception) {
                torProcess.destroy()
                torStateMachine.setTorState(TorState.OFF)
                throw IOException(e.message)
            }
        } else {
            broadcastLogger.debug("Using already existing TorProcess")
        }
        try {
            this.controlConnection = controlConnection

            waitForFileCreation(ConfigFile.COOKIE_AUTH_FILE)
            controlConnection!!.authenticate(onionProxyContext.readFile(ConfigFile.COOKIE_AUTH_FILE))
            broadcastLogger.debug("Authentication to TorControlConnection port successful")
            if (hasExistingTorConnection) {
                controlConnection.signal(TorControlCommands.SIGNAL_RELOAD)
                broadcastLogger.debug("Reloaded configuration file")
            }
            controlConnection.takeOwnership()
            controlConnection.resetConf(setOf(OWNER))
            broadcastLogger.debug("Took ownership of Control Port")

            if (eventListener.CONTROL_COMMAND_EVENTS.isNotEmpty()) {
                broadcastLogger.debug("Adding EventListener to Tor Process")
                controlConnection.addRawEventListener(eventListener)
                broadcastLogger.debug("Setting ControlCommand Events to listen for")
                controlConnection.setEvents(listOf(*eventListener.CONTROL_COMMAND_EVENTS))
            }

            disableNetwork(false)
        } catch (e: Exception) {
            torProcess?.destroy()
            this.controlConnection = null
            torStateMachine.setTorState(TorState.OFF)
            broadcastLogger.warn("Failed to start Tor")
            throw IOException(e.message)
        }

        torStateMachine.setTorState(TorState.ON)

        networkStateReceiver = NetworkStateReceiver(this)

        @Suppress("DEPRECATION")
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkStateReceiver, filter)
        broadcastLogger.notice("Completed starting of Tor")
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
            broadcastLogger.notice("Connecting to Control Port: $controlPort")
            controlSocket = Socket(
                controlPortTokens[0].split("=".toRegex()).toTypedArray()[1],
                controlPort
            )
            controlConnection = TorControlConnection(controlSocket!!)
            broadcastLogger.notice("Successfully connected to Control Port")
        } catch (e: IOException) {
            broadcastLogger.warn("Failed to connect to Control Port.")
            throw IOException(e.message)
        } catch (ee: ArrayIndexOutOfBoundsException) {
            broadcastLogger.warn("Failed to connect to Control Port.")
            throw IOException(
                "Failed to read control port: " +
                        String(onionProxyContext.readFile(ConfigFile.CONTROL_PORT_FILE))
            )
        } catch (eee: KotlinNullPointerException) {
            broadcastLogger.warn("controlSocket was null even after being properly set")
            throw NullPointerException(eee.message)
        }
        if (onionProxyContext.torSettings.hasDebugLogs) {
            // TODO: think about changing this to something other than System.out. Maybe
            //  try to pipe it to the BroadcastLogger to keep it out of Logcat on release
            //  builds?
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
    @Throws(
        SecurityException::class,
        IOException::class
    )
    private fun spawnTorProcess(): Process {

        // We want this to throw exceptions if files do not exist so we can propogate
        // the exceptions to where start was called.
        val cmd = arrayOf(
            torExecutable().absolutePath,
            "-f",
            torrc().absolutePath,
            OWNER,
            getProcessId()
        )
        val processBuilder = ProcessBuilder(*cmd)
        setEnvironmentArgsAndWorkingDirectoryForStart(processBuilder)
        broadcastLogger.debug("Spawning Tor Process")

        val torProcess: Process = try {
            processBuilder.start()
        } catch (e: Exception) {
            broadcastLogger.warn("ProcessBuilder failed to start.")
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
    @Throws(
        IOException::class,
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
                throw IllegalArgumentException("$onionProxyConst is not a valid argument")
            }
        }

        val startTime = System.currentTimeMillis()
        broadcastLogger.notice("Waiting for ${file.nameWithoutExtension} file")

        val isCreated: Boolean = onionProxyContext.createNewFileIfDoesNotExist(onionProxyConst)
        val fileObserver: WriteObserver? = onionProxyContext.createFileObserver(onionProxyConst)
        val fileCreationTimeout = onionProxyContext.torConfigFiles.fileCreationTimeout
        if (!isCreated || file.length() == 0L &&
            fileObserver?.poll(fileCreationTimeout.toLong(), TimeUnit.SECONDS) != true
        ) {
            broadcastLogger.warn("${file.nameWithoutExtension} not created")
            torStateMachine.setTorState(TorState.STOPPING)
            throw IOException(
                "${file.nameWithoutExtension} not created: ${file.absolutePath}, len = ${file.length()}"
            )
        }
        broadcastLogger.notice(
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
                            broadcastLogger.exception(IOException("Error with $line"))
                        } else {
                            broadcastLogger.notice(line)
                        }
                    }
                } finally {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        broadcastLogger.exception(IOException("Could not close input stream", e))
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
            // Named to match GuardianProject's binaries, just in case someone
            //  forgets to create/set a custom TorConfigFile if using their dependency.
            torExe = File(torExe.parent, "libtor.so")

        if (!torExe.exists()) {
            torStateMachine.setTorState(TorState.STOPPING)
            throw IOException("Tor executable file not found")
        }
        return torExe
    }

    @Throws(IOException::class, SecurityException::class)
    private fun torrc(): File {
        val torrc = onionProxyContext.torConfigFiles.torrcFile
        if (!torrc.exists()) {
            torStateMachine.setTorState(TorState.STOPPING)
            throw IOException("Torrc file not found")
        }
        return torrc
    }

    /**
     * Sets environment variables and working directory needed for Tor
     *
     * @param [processBuilder] we will call start on this to run Tor.
     * @throws [SecurityException] Unauthorized access to the ProcessBuilder's environment.
     */
    @Throws(SecurityException::class)
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
                disableNetwork(true)
                disableNetwork(false)
            } catch (e: Exception) {
                broadcastLogger.exception(
                    IOException("Connection exception occurred resetting exits", e)
                )
                return false
            }
        } else {
            try {
                controlConnection!!.setConf("GeoIPFile", onionProxyContext.torConfigFiles.geoIpFile.canonicalPath)
                controlConnection!!.setConf("GeoIPv6File", onionProxyContext.torConfigFiles.geoIpv6File.canonicalPath)
                controlConnection!!.setConf("ExitNodes", exitNodes)
                controlConnection!!.setConf("StrictNodes", "1")
                disableNetwork(true)
                disableNetwork(false)
            } catch (e: Exception) {
                broadcastLogger.exception(
                    IOException("Connection exception occurred resetting exits", e)
                )
                return false
            }
        }
        return true
    }

    /**
     * Will signal for a NewNym, then broadcast [NEWNYM_SUCCESS_MESSAGE] if successful.
     *
     * Because there is no way to easily ascertain success, a listener has to be added to
     * see if we've been rate limited. Being rate limited means we were **not** successful
     * when signaling NEWNYM, so we don't want to broadcast the success message.
     *
     * If the [eventListener] you're instantiating [OnionProxyManager] with has it's
     * [BaseEventListener.noticeMsg] being piped to the [EventBroadcaster.broadcastNotice],
     * you will receive the message of being rate limited. The [InternalEventListener] is
     * used specifically for this purpose.
     * */
    @Synchronized
    suspend fun signalNewNym() {
        if (!hasControlConnection() || !isBootstrapped) return
        if (networkStateReceiver?.networkConnectivity != true) {
            broadcastLogger.notice("NEWNYM: $NEWNYM_NO_NETWORK")
            return
        }

        broadcastLogger.debug("Attempting to acquire a new nym")
        eventListener.beginWatchingNoticeMsgs()

        val signalSuccess =
            try {
                signalControlConnection(TorControlCommands.SIGNAL_NEWNYM)
            } catch (e: NullPointerException) {
                false
            }

        val rateLimited = eventListener.doesNoticeMsgBufferContain(NEWNYM_RATE_LIMIT_PARTIAL_MSG)

        if (signalSuccess) {
            if (!rateLimited) {
                broadcastLogger.debug("Successfully acquired a new nym")
                eventBroadcaster.broadcastNotice(
                    "${TorControlCommands.SIGNAL_NEWNYM}|$NEWNYM_SUCCESS_MESSAGE"
                )
            } else {
                broadcastLogger.debug("Was rate limited when trying to acquire a new nym")
            }
        } else {
            broadcastLogger.notice(
                "NOTICE: Failed to acquire a ${TorControlCommands.SIGNAL_NEWNYM}..."
            )
        }
    }

    /**
     * Sends a signal to the  [TorControlConnection]
     *
     * @param [torControlSignalCommand] See [TorControlCommands] for acceptable `SIGNAL_` values.
     * @return `true` if the signal was received by [TorControlConnection], `false` if not.
     * */
    fun signalControlConnection(torControlSignalCommand: String): Boolean {
        return if (!hasControlConnection()) {
            false
        } else {
            return try {
                controlConnection!!.signal(torControlSignalCommand)
                true
            } catch (e: IOException) {
                broadcastLogger.warn("Control connection is not responding properly to signal")
                false
            } catch (ee: KotlinNullPointerException) {
                broadcastLogger.exception(ee)
                false
            }
        }
    }

    // TODO: This does not work properly because of rate limiting. The signal successfully
    //  sends which won't throw an IOException, but the success of attaining a new identity
    //  is dependant on whether or not you've been Rate Limited so the returned value of true
    //  actually means that the signal was successfully sent, not that a new nym was ascertained.
    fun setNewIdentity(): Boolean {
        return if (!hasControlConnection()) {
            false
        } else {
            try {
                controlConnection!!.signal(TorControlCommands.SIGNAL_NEWNYM)
                true
            } catch (e: IOException) {
                broadcastLogger.notice("error requesting newnym: ${e.localizedMessage}")
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

    /**
     * See the
     * <a href="https://torproject.gitlab.io/torspec/control-spec/#getinfo" target="_blank">tor_spec</a>
     * for accepted queries.
     *
     * @param [queryCommand] What data you are querying the [TorControlConnection] for
     * */
    fun getInfo(queryCommand: String): String? {
        return if (!hasControlConnection()) {
            null
        } else try {
            controlConnection!!.getInfo(queryCommand)
        } catch (e: IOException) {
            broadcastLogger.warn("Control connection is not responding properly to getInfo")
            null
        } catch (ee: KotlinNullPointerException) {
            broadcastLogger.exception(ee)
            null
        }
    }

    fun reloadTorConfig(): Boolean {
        if (!hasControlConnection()) return false

        try {
            controlConnection!!.signal(TorControlCommands.SIGNAL_RELOAD)
            return true
        } catch (e: IOException) {
            broadcastLogger.warn("TorControlConnection is not responding properly to signal")
        } catch (ee: KotlinNullPointerException) {
            broadcastLogger.exception(ee)
        }

        try {
            restartTorProcess()
            return true
        } catch (e: Exception) {
            broadcastLogger.exception(e)
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

}