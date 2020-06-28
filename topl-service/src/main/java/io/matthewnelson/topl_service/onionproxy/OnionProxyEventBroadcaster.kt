package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core.broadcaster.DefaultEventBroadcaster
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
): DefaultEventBroadcaster(torServiceSettings) {

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
        if (torStateMachine.isOn &&
            !torStateMachine.isNetworkDisabled &&
            bootstrapProgress == "Bootstrapped 100%"
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
        super.broadcastDebug(msg)
    }


    //////////////////
    /// Exceptions ///
    //////////////////
    override fun broadcastException(msg: String?, e: Exception) {
        super.broadcastException(msg, e)
    }


    ///////////////////
    /// LogMessages ///
    ///////////////////
    override fun broadcastLogMessage(logMessage: String?) {}


    ///////////////
    /// Notices ///
    ///////////////
    private lateinit var noticeMsgToContentTextJob: Job
    private var bootstrapProgress = ""

    override fun broadcastNotice(msg: String) {

        // BOOTSTRAPPED
        if (msg.contains("Bootstrapped")) {
            val msgSplit = msg.split(" ")
            val bootstrapped = "${msgSplit[0]} ${msgSplit[1]}"

            if (bootstrapped != bootstrapProgress) {
                serviceNotification.updateContentText(bootstrapped)

                if (bootstrapped == "Bootstrapped 100%") {
                    serviceNotification.updateIcon(torService, ImageState.ENABLED)
                    serviceNotification.addActions(torService)
                }

                bootstrapProgress = bootstrapped
            }

        // NEWNYM
        } else if (msg.contains(TorControlCommands.SIGNAL_NEWNYM)) {
            val msgToShow =
                if (msg.contains(OnionProxyManager.NEWNYM_SUCCESS_MESSAGE))
                    OnionProxyManager.NEWNYM_SUCCESS_MESSAGE
                else
                    msg

            if (::noticeMsgToContentTextJob.isInitialized && noticeMsgToContentTextJob.isActive)
                noticeMsgToContentTextJob.cancel()

            displayMessageToContentText(msgToShow, 3500L)
        }
        super.broadcastNotice(msg)
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

            // If we're still connected, publish the last bandwidth
            // broadcast to overwrite the message.
            if (!torStateMachine.isNetworkDisabled)
                serviceNotification.updateContentText(
                    getFormattedBandwidthString(bytesRead, bytesWritten)
                )
        }
    }


    ////////////////
    /// TorState ///
    ////////////////
    private var lastState = TorState.OFF

    override fun broadcastTorState(@TorState state: String, @TorNetworkState networkState: String) {
        super.broadcastTorState(state, networkState)

        if (lastState == TorState.ON && state != lastState)
            serviceNotification.removeActions(torService, state)
        else
            serviceNotification.updateContentTitle(state)

        lastState = state

        if (torStateMachine.isNetworkDisabled)
            serviceNotification.updateIcon(torService, ImageState.DISABLED)
    }
}