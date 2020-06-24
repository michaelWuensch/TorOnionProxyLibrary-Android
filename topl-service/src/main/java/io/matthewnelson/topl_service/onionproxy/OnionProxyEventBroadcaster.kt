package io.matthewnelson.topl_service.onionproxy

import android.os.Build
import android.util.Log
import io.matthewnelson.topl_core.broadcaster.DefaultEventBroadcaster
import io.matthewnelson.topl_service.notification.NotificationConsts.ImageState
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.service.TorServiceSettings

/**
 * [io.matthewnelson.topl_core.OnionProxyManager] utilizes this customized class for
 * broadcasting things while it is operating (such as Tor's State, operation errors,
 * debugging, etc).
 *
 * [OnionProxyEventListener] utilizes this class by sending it what Tor is spitting out
 * (selectively curated, ofc).
 *
 * @param [torService] [TorService] for context.
 * @param [torSettings] [TorServiceSettings]
 * */
internal class OnionProxyEventBroadcaster(
    private val torService: TorService,
    private val torSettings: TorServiceSettings
): DefaultEventBroadcaster(torSettings) {

    companion object {
        private const val TAG = "EventBroadcaster"
    }

    private val serviceNotification = ServiceNotification.get()

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

        // Only update the notification if proper State is had.
        if (torStateMachine.isOn &&
            torStateMachine.isConnected &&
            bootstrapProgress == "Bootstrapped 100%"
        ) {
            if (read != this.bytesRead || written != this.bytesWritten) {
                this.bytesRead = read
                this.bytesWritten = written
                serviceNotification.updateBandwidth(read, written)

                if (read == 0L && written == 0L)
                    serviceNotification.updateIcon(torService, ImageState.ENABLED)
                else
                    serviceNotification.updateIcon(torService, ImageState.DATA)
            }
        }

    }

    override fun broadcastDebug(msg: String) {
        super.broadcastDebug(msg)
    }

    override fun broadcastException(msg: String?, e: Exception) {
        super.broadcastException(msg, e)
    }

    override fun broadcastLogMessage(logMessage: String?) {
        Log.d(TAG, "LOG_MESSAGE__$logMessage")
    }

    private var bootstrapProgress = ""
    override fun broadcastNotice(msg: String) {
        if (msg.contains("Bootstrapped")) {
            val msgSplit = msg.split(" ")
            val bootstrapped = "${msgSplit[0]} ${msgSplit[1]}"

            if (bootstrapped != bootstrapProgress) {
                serviceNotification.updateBootstrap(bootstrapped)

                if (bootstrapped == "Bootstrapped 100%") {
                    serviceNotification.updateIcon(torService, ImageState.ENABLED)
                    serviceNotification.addActions(torService)
                }

                bootstrapProgress = bootstrapped
            }
        } else if (msg.contains("Rate limiting NEWYM")) {
            val msgSplit = msg.split(":")
        }
        super.broadcastNotice(msg)
    }

    private var lastState = TorState.OFF
    override fun broadcastTorState(@TorState state: String, @TorNetworkState networkState: String) {
        // Need just a moment here for bandwidth's notification updates to clear up so the
        // notification builder containing the state change isn't overwritten.
        Thread.sleep(75)
        if (lastState == TorState.ON && state != lastState) {
            serviceNotification.removeActions(torService, state)
        }
        lastState = state
        serviceNotification.updateContentTitle(state)

        if (!torStateMachine.isConnected)
            serviceNotification.updateIcon(torService, ImageState.DISABLED)

        super.broadcastTorState(state, networkState)
    }
}