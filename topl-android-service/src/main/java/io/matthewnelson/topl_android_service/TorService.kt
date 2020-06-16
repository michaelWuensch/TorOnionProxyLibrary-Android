package io.matthewnelson.topl_android_service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.matthewnelson.topl_android.OnionProxyContext
import io.matthewnelson.topl_android.util.TorInstaller
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
    }

    override fun onBind(intent: Intent?): IBinder? {
        // TODO: Implement
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // TODO: Implement
    }

    override fun onDestroy() {
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
        onDestroy()
    }
}