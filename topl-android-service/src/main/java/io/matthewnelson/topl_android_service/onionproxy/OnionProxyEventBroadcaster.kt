package io.matthewnelson.topl_android_service.onionproxy

import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.matthewnelson.topl_android.broadcaster.DefaultEventBroadcaster
import io.matthewnelson.topl_android_settings.TorSettings

class OnionProxyEventBroadcaster(
    private val context: Context,
    private val torSettings: TorSettings
): DefaultEventBroadcaster(torSettings) {

    private val broadcastManager = LocalBroadcastManager.getInstance(context)

    override fun broadcastBandwidth(upload: Long, download: Long, written: Long, read: Long) {
        TODO("Not yet implemented")
    }

    override fun broadcastLogMessage(logMessage: String) {
        TODO("Not yet implemented")
    }

    override fun broadcastNotice(msg: String) {
        TODO("Not yet implemented")
    }

    override fun broadcastStatus() {
        TODO("Not yet implemented")
    }
}