package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core_base.EventBroadcaster
import io.matthewnelson.topl_service.notification.NotificationConsts.ImageState
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.service.TorServiceSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.freehaven.tor.control.TorControlCommands
import java.text.NumberFormat
import java.util.*

/**
 * [io.matthewnelson.topl_core.OnionProxyManager] utilizes this customized class for
 * broadcasting things while it is operating (such as Tor's State, operation errors,
 * debugging, etc).
 *
 * [OnionProxyEventListener] utilizes this class by sending it what Tor is spitting out
 * (selectively curated, ofc).
 *
 * @param [torService] [TorService] for context.
 * @param [torServiceSettings] [TorServiceSettings]
 * */
internal class OnionProxyEventBroadcaster(
    private val torService: TorService,
    private val torServiceSettings: TorServiceSettings
): EventBroadcaster() {

    companion object {
        private var appEventBroadcaster: EventBroadcaster? = null
        fun initAppEventBroadcaster(eventBroadcaster: EventBroadcaster?) {
            if (appEventBroadcaster == null)
                appEventBroadcaster = eventBroadcaster
        }
    }

    private val serviceNotification = ServiceNotification.get()

    /////////////////
    /// Bandwidth ///
    /////////////////
    private val numberFormat = NumberFormat.getInstance(Locale.getDefault())
    private var bytesRead = 0L
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
                    serviceNotification.updateIcon(torService, ImageState.ENABLED)
                else
                    serviceNotification.updateIcon(torService, ImageState.DATA)
            }
        }

        appEventBroadcaster?.broadcastBandwidth(bytesRead, bytesWritten)
    }

    /**
     * Do a check for if a message is being displayed in the contentText of the
     * notification, allowing it to remain there unabated until the coroutine
     * finishes.
     * */
    private fun updateBandwidth(download: Long, upload: Long) {
        if (::noticeMsgToContentTextJob.isInitialized && noticeMsgToContentTextJob.isActive) return
        serviceNotification.updateContentText(
            getFormattedBandwidthString(download, upload)
        )
    }

    private fun getFormattedBandwidthString(download: Long, upload: Long): String =
        "${formatBandwidth(download)} ↓ / ${formatBandwidth(upload)} ↑"

    /**
     * Obtained from: https://gitweb.torproject.org/tor-android-service.git/tree/service/
     *                src/main/java/org/torproject/android/service/TorEventHandler.java
     *
     * Original method name: formatCount()
     * */
    private fun formatBandwidth(value: Long): String =
        if (value < 1e6)
            numberFormat.format(
                Math.round( ( ( (value * 10 / 1024 ).toInt() ) /10 ).toFloat() )
            ) + "kbps"
        else
            numberFormat.format(
                Math.round( ( ( (value * 100 / 1024 / 1024).toInt() ) /100 ).toFloat() )
            ) + "mbps"


    /////////////
    /// Debug ///
    /////////////
    override fun broadcastDebug(msg: String) {
        if (torServiceSettings.hasDebugLogs)
            appEventBroadcaster?.broadcastDebug(msg)
    }


    //////////////////
    /// Exceptions ///
    //////////////////
    override fun broadcastException(msg: String?, e: Exception) {
        if (!msg.isNullOrEmpty())
            if (msg.contains(TorService::class.java.simpleName)) {
                serviceNotification.updateIcon(torService, ImageState.ERROR)
                val splitMsg = msg.split("|")
                if (splitMsg.size > 2 && splitMsg[2].isNotEmpty())
                    serviceNotification.updateContentText(splitMsg[2])
            }
        if (torServiceSettings.hasDebugLogs)
            appEventBroadcaster?.broadcastException(msg, e)
    }


    ///////////////////
    /// LogMessages ///
    ///////////////////
    override fun broadcastLogMessage(logMessage: String?) {
        appEventBroadcaster?.broadcastLogMessage(logMessage)
    }


    ///////////////
    /// Notices ///
    ///////////////
    private lateinit var noticeMsgToContentTextJob: Job
    private var bootstrapProgress = ""
    private fun isBootstrappingComplete(): Boolean =
        bootstrapProgress == "Bootstrapped 100%"

    override fun broadcastNotice(msg: String) {

        // BOOTSTRAPPED
        if (msg.contains("Bootstrapped")) {
            val msgSplit = msg.split(" ")
            val bootstrapped = "${msgSplit[0]} ${msgSplit[1]}"

            if (bootstrapped != bootstrapProgress) {
                serviceNotification.updateContentText(bootstrapped)

                if (isBootstrappingComplete()) {
                    serviceNotification.updateIcon(torService, ImageState.ENABLED)
                    serviceNotification.addActions(torService)
                }

                bootstrapProgress = bootstrapped
            }

        // NEWNYM
        } else if (msg.contains(TorControlCommands.SIGNAL_NEWNYM)) {
            val msgToShow =
                when {
                    msg.contains(OnionProxyManager.NEWNYM_SUCCESS_MESSAGE) -> {
                        OnionProxyManager.NEWNYM_SUCCESS_MESSAGE
                    }
                    msg.contains(OnionProxyManager.NEWNYM_NO_NETWORK) -> {
                        OnionProxyManager.NEWNYM_NO_NETWORK
                    }
                    else -> {
                        msg
                    }
                }

            if (::noticeMsgToContentTextJob.isInitialized && noticeMsgToContentTextJob.isActive)
                noticeMsgToContentTextJob.cancel()

            displayMessageToContentText(msgToShow, 3500L)
        }

        appEventBroadcaster?.broadcastNotice(msg)
    }

    /**
     * Display a message in the notification's ContentText space for the defined
     * [delayMilliSeconds], after which (if Tor is connected), publish to the Notification's
     * ContentText the most recently broadcast bandwidth via [bytesRead] && [bytesWritten].
     * */
    private fun displayMessageToContentText(message: String, delayMilliSeconds: Long) {
        noticeMsgToContentTextJob = torService.scopeMain.launch {
            serviceNotification.updateContentText(message)
            delay(delayMilliSeconds)

            // Publish the last bandwidth broadcast to overwrite the message.
            if (torNetworkState == TorNetworkState.ENABLED) {
                serviceNotification.updateContentText(
                    getFormattedBandwidthString(bytesRead, bytesWritten)
                )
            } else {
                if (isBootstrappingComplete())
                    serviceNotification.updateContentText(getFormattedBandwidthString(0L, 0L))
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
            serviceNotification.removeActions(torService, state)
        } else {
            serviceNotification.updateContentTitle(state)
        }

        torState = state

        if (networkState == TorNetworkState.DISABLED) {
            // Update torNetworkState _before_ setting the icon to `disabled` so bandwidth won't
            // overwrite the icon with an update
            torNetworkState = networkState
            serviceNotification.updateIcon(torService, ImageState.DISABLED)
        } else {
            if (isBootstrappingComplete())
                serviceNotification.updateIcon(torService, ImageState.ENABLED)

            // Update torNetworkState _after_ setting the icon to `enabled` so bandwidth changes
            // occur afterwards and this won't overwrite ImageState.DATA
            torNetworkState = networkState
        }

        appEventBroadcaster?.broadcastTorState(state, networkState)
    }
}