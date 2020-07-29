package io.matthewnelson.test_helpers.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.onionproxy.ServiceEventBroadcaster
import io.matthewnelson.topl_service.onionproxy.ServiceEventListener
import io.matthewnelson.topl_service.onionproxy.ServiceTorInstaller
import io.matthewnelson.topl_service.onionproxy.ServiceTorSettings
import io.matthewnelson.topl_service.prefs.TorServicePrefsListener
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.service.ServiceActionProcessor
import io.matthewnelson.topl_service.service.TorServiceBinder
import io.matthewnelson.topl_service.util.ServiceConsts.NotificationImage
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class TestTorService(
    override val context: Context
): BaseService() {

    //////////////////
    /// Coroutines ///
    //////////////////
    val supervisorJob = SupervisorJob()
    val testScopeMain = CoroutineScope(Dispatchers.Main + supervisorJob)
    override fun cancelSupervisorJob() {
        supervisorJob.cancel()
    }
    override fun getScopeMain(): CoroutineScope {
        return testScopeMain
    }


    //////////////////////////////
    /// ServiceActionProcessor ///
    //////////////////////////////
    val serviceActionProcessor by lazy { ServiceActionProcessor(this) }

    override fun processIntent(serviceActionIntent: Intent) {
        serviceActionProcessor.processIntent(serviceActionIntent)
    }

    @Volatile
    var stopSelfCalled = false

    override fun stopService() {
        stopSelfCalled = true
        testScopeMain.launch { onDestroy() }
    }


    ///////////////////////////////
    /// TorServicePrefsListener ///
    ///////////////////////////////
    val torServicePrefsListener by lazy { TorServicePrefsListener(this) }

    @Volatile
    var torServicePrefsListenerIsRegistered = false

    override fun unregisterPrefsListener() {
        torServicePrefsListener.unregister()
        torServicePrefsListenerIsRegistered = false
    }
    fun registerPrefsListener() {
        torServicePrefsListener.register()
        torServiceReceiverIsRegistered = true
    }


    /////////////////////////
    /// BroadcastReceiver ///
    /////////////////////////
    @Volatile
    var torServiceReceiverIsRegistered = false

    override fun registerReceiver() {
        torServiceReceiverIsRegistered = true
    }
    override fun unregisterReceiver() {
        torServiceReceiverIsRegistered = false
    }


    ///////////////////////////
    /// ServiceNotification ///
    ///////////////////////////
    val serviceNotification = ServiceNotification.get()

    override fun removeNotification() {
        serviceNotification.remove()
    }
    override fun stopForegroundService(): ServiceNotification {
        return serviceNotification.stopForeground(this)
    }
    override fun addNotificationActions() {
        serviceNotification.addActions(this)
    }
    override fun removeNotificationActions() {
        serviceNotification.removeActions(this)
    }
    override fun updateNotificationContentText(string: String) {
        serviceNotification.updateContentText(string)
    }
    override fun updateNotificationContentTitle(title: String) {
        serviceNotification.updateContentTitle(title)
    }
    override fun updateNotificationIcon(@NotificationImage notificationImage: Int) {
        serviceNotification.updateIcon(this, notificationImage)
    }
    override fun updateNotificationProgress(show: Boolean, progress: Int?) {
        serviceNotification.updateProgress(show, progress)
    }



    /////////////////
    /// TOPL-Core ///
    /////////////////
    override val onionProxyManager: OnionProxyManager by lazy {
        OnionProxyManager(
            context,
            TorServiceController.getTorConfigFiles(),
            ServiceTorInstaller(this),
            ServiceTorSettings(this, TorServiceController.getTorSettings()),
            ServiceEventListener(),
            ServiceEventBroadcaster(this),
            buildConfigDebug
        )
    }


    ///////////////
    /// Binding ///
    ///////////////
    val torServiceBinder: TorServiceBinder by lazy { TorServiceBinder(this) }

    @Volatile
    var serviceIsBound = false

    override fun unbindService() {
        serviceIsBound = false
    }
    override fun onBind(intent: Intent?): IBinder? {
        serviceIsBound = true
        return torServiceBinder
    }


    override fun onCreate() {
        serviceNotification.buildNotification(this)
        registerPrefsListener()
    }

    override fun onDestroy() {
        processIntent(Intent(ServiceAction.DESTROY))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null)
            processIntent(intent)
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        serviceNotification.startForeground(this)
        processIntent(Intent(ServiceAction.STOP))
    }
}