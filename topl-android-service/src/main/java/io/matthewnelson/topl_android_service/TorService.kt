package io.matthewnelson.topl_android_service

import android.app.Service
import android.content.Intent
import android.os.IBinder

internal class TorService: Service() {

    companion object {

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
    }
}