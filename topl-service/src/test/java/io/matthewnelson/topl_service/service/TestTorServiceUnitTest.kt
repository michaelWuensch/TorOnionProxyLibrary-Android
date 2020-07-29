package io.matthewnelson.topl_service.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_service.notification.ServiceNotification
import kotlinx.coroutines.CoroutineScope

internal class TestTorServiceUnitTest(
    override val context: Context,
    override val onionProxyManager: OnionProxyManager
): BaseService() {

    //////////////////
    /// Coroutines ///
    //////////////////
    override fun cancelSupervisorJob() {
        TODO("Not yet implemented")
    }
    override fun getScopeMain(): CoroutineScope {
        TODO("Not yet implemented")
    }


    //////////////////////////////
    /// ServiceActionProcessor ///
    //////////////////////////////
    override fun processIntent(serviceActionIntent: Intent) {
        TODO("Not yet implemented")
    }
    override fun stopService() {
        TODO("Not yet implemented")
    }


    ///////////////////////////////
    /// TorServicePrefsListener ///
    ///////////////////////////////
    override fun unregisterPrefsListener() {
        TODO("Not yet implemented")
    }


    /////////////////////////
    /// BroadcastReceiver ///
    /////////////////////////
    override fun registerReceiver() {
        TODO("Not yet implemented")
    }

    override fun unregisterReceiver() {
        TODO("Not yet implemented")
    }


    ///////////////////////////
    /// ServiceNotification ///
    ///////////////////////////
    override fun removeNotification() {
        TODO("Not yet implemented")
    }

    override fun stopForegroundService(): ServiceNotification {
        TODO("Not yet implemented")
    }

    override fun addNotificationActions() {
        TODO("Not yet implemented")
    }

    override fun removeNotificationActions() {
        TODO("Not yet implemented")
    }

    override fun updateNotificationContentText(string: String) {
        TODO("Not yet implemented")
    }

    override fun updateNotificationContentTitle(title: String) {
        TODO("Not yet implemented")
    }

    override fun updateNotificationIcon(notificationImage: Int) {
        TODO("Not yet implemented")
    }

    override fun updateNotificationProgress(show: Boolean, progress: Int?) {
        TODO("Not yet implemented")
    }


    ///////////////
    /// Binding ///
    ///////////////
    override fun unbindService() {
        TODO("Not yet implemented")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


    override fun onCreate() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun onLowMemory() {
        TODO("Not yet implemented")
    }

    override fun onTrimMemory(level: Int) {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TODO("Not yet implemented")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        TODO("Not yet implemented")
    }
}