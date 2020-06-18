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

    override fun broadcastDebug(msg: String) {
        super.broadcastDebug(msg)
    }

    override fun broadcastException(msg: String?, e: Exception) {
        super.broadcastException(msg, e)
    }

    override fun broadcastLogMessage(logMessage: String?) {
        TODO("Not yet implemented")
    }

    override fun broadcastNotice(msg: String) {
        super.broadcastNotice(msg)
    }

    override fun broadcastTorState(@TorState state: String) {
        TODO("Not yet implemented")
    }
}