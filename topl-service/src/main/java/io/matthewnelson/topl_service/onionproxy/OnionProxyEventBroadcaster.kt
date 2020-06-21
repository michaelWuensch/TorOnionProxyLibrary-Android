package io.matthewnelson.topl_service.onionproxy

import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.matthewnelson.topl_android.broadcaster.DefaultEventBroadcaster
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service_settings.TorServiceSettings

/**
 * [io.matthewnelson.topl_android.OnionProxyManager] utilizes this customized class for
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

    private val broadcastManager = LocalBroadcastManager.getInstance(torService.applicationContext)

    override fun broadcastBandwidth(upload: Long, download: Long, written: Long, read: Long) {
        Log.d(TAG, "BANDWIDTH__upload: $upload, download: $download, written: $written, read: $read")
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

    override fun broadcastNotice(msg: String) {
        super.broadcastNotice(msg)
    }

    override fun broadcastTorState(@TorState state: String) {
        Log.d(TAG, "TOR_STATE__$state")
    }
}