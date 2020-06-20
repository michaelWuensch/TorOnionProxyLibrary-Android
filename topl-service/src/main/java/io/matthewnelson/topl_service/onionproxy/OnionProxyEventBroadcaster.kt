package io.matthewnelson.topl_service.onionproxy

import android.content.Context
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.matthewnelson.topl_android.broadcaster.DefaultEventBroadcaster
import io.matthewnelson.topl_android_settings.TorSettings

class OnionProxyEventBroadcaster(
    private val context: Context,
    private val torSettings: TorSettings
): DefaultEventBroadcaster(torSettings) {

    private companion object {
        const val TAG = "EventBroadcaster"
    }

    private val broadcastManager = LocalBroadcastManager.getInstance(context)

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