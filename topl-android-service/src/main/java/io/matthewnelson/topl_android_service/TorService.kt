package io.matthewnelson.topl_android_service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.matthewnelson.topl_android.OnionProxyContext
import io.matthewnelson.topl_android.util.TorInstaller
import io.matthewnelson.topl_android_service.receiver.ServiceAction
import io.matthewnelson.topl_android_settings.TorConfigFiles
import io.matthewnelson.topl_android_settings.TorSettings

internal class TorService: Service() {

    companion object {
        private lateinit var onionProxyContext: OnionProxyContext
        fun setOnionProxyContext(
            torConfigFiles: TorConfigFiles, torInstaller: TorInstaller, torSettings: TorSettings
        ) {
            if (!::onionProxyContext.isInitialized)
                onionProxyContext = OnionProxyContext(torConfigFiles, torInstaller, torSettings)
        }

        // Intents/LocalBroadcastManager
        const val FILTER = "io.matthewnelson.topl_android_service.TorService"
        const val ACTION_EXTRAS_KEY = "SERVICE_ACTION_EXTRA"
    }

    override fun onBind(intent: Intent?): IBinder? {
        // TODO: Implement
        return null
    }

    override fun onCreate() {
        super.onCreate()
        registerBroadcastReceiver(this)
        // TODO: Implement
    }

    override fun onDestroy() {
        unregisterBroadcastReceiver()
        super.onDestroy()
        // TODO: Implement
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // TODO: Implement
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        // TODO: Implement
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        // TODO: Implement
        stopSelf()
    }

    private val actionBR = ActionBroadcastReceiver()
    private lateinit var localBM: LocalBroadcastManager

    private fun registerBroadcastReceiver(torService: TorService) {
        if (!::localBM.isInitialized)
            localBM = LocalBroadcastManager.getInstance(torService)
        localBM.registerReceiver(actionBR, IntentFilter(FILTER))
    }

    private fun unregisterBroadcastReceiver() =
        localBM.unregisterReceiver(actionBR)

    private inner class ActionBroadcastReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {

                when (intent.getStringExtra(ACTION_EXTRAS_KEY)) {
                    ServiceAction.ACTION_STOP -> {
                        // TODO: Implement
                    }
                    ServiceAction.ACTION_RESTART -> {
                        // TODO: Implement
                    }
                    ServiceAction.ACTION_RENEW -> {
                        // TODO: Implement
                    }
                    else -> {}
                }
            }
        }
    }
}