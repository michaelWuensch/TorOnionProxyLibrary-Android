[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [EventBroadcaster](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`EventBroadcaster()`

Service for sending event logs to the system.

Both `topl-core` and `topl-service` utilize this class to broadcast messages. This
allows for easier separation of messages based on the type, process or class.

See [BaseConsts.BroadcastType](../-base-consts/-broadcast-type/index.md)s

``` kotlin
/**
 * [io.matthewnelson.topl_core.OnionProxyManager] utilizes this customized class for
 * broadcasting things while it is operating (such as Tor's State, operation errors,
 * debugging, etc).
 *
 * [ServiceEventListener] utilizes this class by sending it what Tor is spitting out
 * (selectively curated, ofc).
 *
 * @param [torService] [BaseService] for context.
 * */
internal class ServiceEventBroadcaster private constructor(
    private val torService: BaseService
): EventBroadcaster() {

    companion object {
        @JvmSynthetic
        fun instantiate(torService: BaseService): ServiceEventBroadcaster =
            ServiceEventBroadcaster(torService)
    }

    private val scopeMain: CoroutineScope
        get() = torService.getScopeMain()

    /////////////////
    /// Bandwidth ///
    /////////////////
    @Volatile
    private var bytesRead = 0L
    @Volatile
    private var bytesWritten = 0L

    override fun broadcastBandwidth(bytesRead: String, bytesWritten: String) {
        val read =
            try {
                bytesRead.toLong()
            } catch (e: NumberFormatException) {
                this.bytesRead
            }
        val written =
            try {
                bytesWritten.toLong()
            } catch (e: NumberFormatException) {
                this.bytesWritten
            }

        // Only update the notification if proper State is had & we're bootstrapped.
        if (torState == TorState.ON &&
            torNetworkState == TorNetworkState.ENABLED &&
            isBootstrappingComplete()
        ) {
            if (read != this.bytesRead || written != this.bytesWritten) {
                this.bytesRead = read
                this.bytesWritten = written

                updateBandwidth(read, written)

                if (read == 0L && written == 0L)
                    torService.updateNotificationIcon(NotificationImage.ENABLED)
                else
                    torService.updateNotificationIcon(NotificationImage.DATA)
            }
        }

        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastBandwidth(bytesRead, bytesWritten) }
        }
    }

    /**
     * Do a check for if a message is being displayed in the contentText of the
     * notification, allowing it to remain there unabated until the coroutine
     * finishes.
     * */
    private fun updateBandwidth(download: Long, upload: Long) {
        if (noticeMsgToContentTextJob?.isActive == true) return
        torService.updateNotificationContentText(
            ServiceUtilities.getFormattedBandwidthString(download, upload)
        )
    }


    /////////////
    /// Debug ///
    /////////////
    override fun broadcastDebug(msg: String) {
        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastDebug(msg) }
        }
    }


    //////////////////
    /// Exceptions ///
    //////////////////
    override fun broadcastException(msg: String?, e: Exception) {
        if (!msg.isNullOrEmpty()) {
            if (msg.contains(TorService::class.java.simpleName)) {
                torService.updateNotificationIcon(NotificationImage.ERROR)
                val msgSplit = msg.split("|")
                msgSplit.elementAtOrNull(2)?.let {
                    torService.updateNotificationContentText(it)
                    torService.updateNotificationProgress(false, null)
                }
            }
        }

        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastException(msg, e) }
        }
    }


    ///////////////////
    /// LogMessages ///
    ///////////////////
    override fun broadcastLogMessage(logMessage: String?) {
        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastLogMessage(logMessage) }
        }
    }


    ///////////////
    /// Notices ///
    ///////////////
    private var noticeMsgToContentTextJob: Job? = null

    @Volatile
    private var bootstrapProgress = ""
    private fun isBootstrappingComplete(): Boolean =
        bootstrapProgress == "Bootstrapped 100%"

    @Volatile
    private var controlPort: String? = null
    @Volatile
    private var dnsPort: String? = null
    @Volatile
    private var httpTunnelPort: String? = null
    @Volatile
    private var socksPort: String? = null
    @Volatile
    private var transPort: String? = null

    private fun setAllPortsNull() {
        controlPort = null
        dnsPort = null
        httpTunnelPort = null
        socksPort = null
        transPort = null
    }

    override fun broadcastNotice(msg: String) {

        when {
            // ServiceActionProcessor
            msg.contains(ServiceActionProcessor::class.java.simpleName) -> {
                handleServiceActionProcessorMsg(msg)
            }
            // WARN|OnionProxyManager|No Network Connectivity. Foregoing enabling of Tor Network.
            msg.contains("No Network Connectivity. Foregoing enabling of Tor Network.") -> {
                torService.updateNotificationProgress(false, null)
                torService.updateNotificationContentText("No Network Connectivity. Waiting...")
            }
            // BOOTSTRAPPED
            msg.contains("Bootstrapped") -> {
                handleBootstrappedMsg(msg)
            }
            // Control Port
            // NOTICE|OnionProxyManager|Successfully connected to Control Port: 44201
            msg.contains("Successfully connected to Control Port:") -> {
                controlPort = getPortFromMsg(msg)
                if (isBootstrappingComplete())
                    updateAppEventBroadcasterWithPortInfo()
            }
            // Dns Port
            // NOTICE|OnionProxyManager|Opened DNS listener on 127.0.0.1:5400
            msg.contains("Opened DNS listener ") -> {
                dnsPort = getPortFromMsg(msg)
                if (isBootstrappingComplete())
                    updateAppEventBroadcasterWithPortInfo()
            }
            // Http Tunnel Port
            // NOTICE|BaseEventListener|Opened HTTP tunnel listener on 127.0.0.1:8118
            msg.contains("Opened HTTP tunnel listener ") -> {
                httpTunnelPort = getPortFromMsg(msg)
                if (isBootstrappingComplete())
                    updateAppEventBroadcasterWithPortInfo()
            }
            // Socks Port
            // NOTICE|BaseEventListener|Opened Socks listener on 127.0.0.1:9050
            msg.contains("Opened Socks listener ") -> {
                socksPort = getPortFromMsg(msg)
                if (isBootstrappingComplete())
                    updateAppEventBroadcasterWithPortInfo()
            }
            // Trans Port
            // NOTICE|BaseEventListener|Opened Transparent pf/netfilter listener on 127.0.0.1:9040
            msg.contains("Opened Transparent pf/netfilter listener ") -> {
                transPort = getPortFromMsg(msg)
                if (isBootstrappingComplete())
                    updateAppEventBroadcasterWithPortInfo()
            }
            // NEWNYM
            msg.contains(TorControlCommands.SIGNAL_NEWNYM) -> {
                handleNewNymMsg(msg)
            }
        }

        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastNotice(msg) }
        }
    }

    // NOTICE|BaseEventListener|Bootstrapped 5% (conn): Connecting to a relay
    private fun handleBootstrappedMsg(msg: String) {
        val msgSplit = msg.split(" ")
        msgSplit.elementAtOrNull(2)?.let {
            val bootstrapped = "${msgSplit[0]} ${msgSplit[1]}".split("|")[2]

            if (bootstrapped != bootstrapProgress) {
                torService.updateNotificationContentText(bootstrapped)

                if (bootstrapped == "Bootstrapped 100%") {
                    updateAppEventBroadcasterWithPortInfo()
                    torService.updateNotificationIcon(NotificationImage.ENABLED)
                    torService.updateNotificationProgress(true, 100)
                    torService.updateNotificationProgress(false, null)
                    torService.addNotificationActions()
                } else {
                    val progress: Int? = try {
                        bootstrapped.split(" ")[1].split("%")[0].toInt()
                    } catch (e: Exception) {
                        null
                    }
                    progress?.let {
                        torService.updateNotificationProgress(true, progress)
                    }
                }

                bootstrapProgress = bootstrapped
            }
        }
    }

    private fun getPortFromMsg(msg: String): String =
        "127.0.0.1:${msg.split(":")[1].trim()}"

    private fun handleNewNymMsg(msg: String) {
        val msgToShow: String? =
            when {
                msg.contains(OnionProxyManager.NEWNYM_SUCCESS_MESSAGE) -> {
                    OnionProxyManager.NEWNYM_SUCCESS_MESSAGE
                }
                msg.contains(OnionProxyManager.NEWNYM_NO_NETWORK) -> {
                    OnionProxyManager.NEWNYM_NO_NETWORK
                }
                else -> {
                    val msgSplit = msg.split("|")
                    msgSplit.elementAtOrNull(2)
                }
            }

        if (noticeMsgToContentTextJob?.isActive == true)
            noticeMsgToContentTextJob?.cancel()

        msgToShow?.let {
            displayMessageToContentText(it, 3500L)
        }
    }

    private fun handleServiceActionProcessorMsg(msg: String) {
        val msgSplit = msg.split("|")
        val msgToShow: String? = msgSplit.elementAtOrNull(2)?.let {
            when (it) {
                ServiceActionName.RESTART_TOR -> {
                    "Restarting Tor..."
                }
                ServiceActionName.START -> {
                    // Need to check here if Tor is already on, as startTor can be called
                    // anytime which would overwrite the contentText already showing and
                    // then stay there until something else updates it.
                    if (torState != TorState.ON)
                        "Starting Tor..."
                    else
                        null
                }
                ServiceActionName.STOP -> {
                    "Stopping Service..."
                }
                else -> {
                    null
                }
            }
        }
        msgToShow?.let {
            torService.updateNotificationContentText(it)
        }
    }

    private var broadcastPortInfoJob: Job? = null
    private fun updateAppEventBroadcasterWithPortInfo() {
        if (broadcastPortInfoJob?.isActive == true)
            broadcastPortInfoJob?.cancel()

        TorServiceController.appEventBroadcaster?.let {
            broadcastPortInfoJob = scopeMain.launch {
                delay(100L)
                it.broadcastPortInformation(
                    TorPortInfo(
                        controlPort,
                        dnsPort,
                        httpTunnelPort,
                        socksPort,
                        transPort
                    )
                )
            }
        }
    }

    /**
     * Display a message in the notification's ContentText space for the defined
     * [delayMilliSeconds], after which (if Tor is connected), publish to the Notification's
     * ContentText the most recently broadcast bandwidth via [bytesRead] && [bytesWritten].
     * */
    private fun displayMessageToContentText(message: String, delayMilliSeconds: Long) {
        noticeMsgToContentTextJob = scopeMain.launch {
            torService.updateNotificationContentText(message)
            delay(delayMilliSeconds)

            // Publish the last bandwidth broadcast to overwrite the message.
            if (torNetworkState == TorNetworkState.ENABLED) {
                torService.updateNotificationContentText(
                    ServiceUtilities.getFormattedBandwidthString(bytesRead, bytesWritten)
                )
            } else if (isBootstrappingComplete()){
                torService.updateNotificationContentText(
                    ServiceUtilities.getFormattedBandwidthString(0L, 0L)
                )
            }
        }
    }


    ////////////////
    /// TorState ///
    ////////////////
    @Volatile
    private var torState = TorState.OFF
    @Volatile
    private var torNetworkState = TorNetworkState.DISABLED

    override fun broadcastTorState(@TorState state: String, @TorNetworkState networkState: String) {
        if (torState == TorState.ON && state != torState) {
            bootstrapProgress = ""
            setAllPortsNull()
            updateAppEventBroadcasterWithPortInfo()
            torService.removeNotificationActions()
        }

        if (state != TorState.ON)
            torService.updateNotificationProgress(true, null)

        torService.updateNotificationContentTitle(state)
        torState = state

        if (networkState == TorNetworkState.DISABLED) {
            if (isBootstrappingComplete()) {
                setAllPortsNull()
                updateAppEventBroadcasterWithPortInfo()
            }

            // Update torNetworkState _before_ setting the icon to `disabled` so bandwidth won't
            // overwrite the icon with an update
            torNetworkState = networkState
            torService.updateNotificationIcon(NotificationImage.DISABLED)
        } else {
            if (isBootstrappingComplete())
                torService.updateNotificationIcon(NotificationImage.ENABLED)

            // Update torNetworkState _after_ setting the icon to `enabled` so bandwidth changes
            // occur afterwards and this won't overwrite ImageState.DATA
            torNetworkState = networkState
        }

        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastTorState(state, networkState) }
        }
    }
}
```

