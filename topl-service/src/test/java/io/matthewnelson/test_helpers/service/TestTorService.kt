package io.matthewnelson.test_helpers.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.annotation.WorkerThread
import io.matthewnelson.test_helpers.application_provided_classes.TestEventBroadcaster
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState
import io.matthewnelson.topl_core_base.BaseConsts.TorState
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.onionproxy.ServiceEventBroadcaster
import io.matthewnelson.topl_service.onionproxy.ServiceEventListener
import io.matthewnelson.topl_service.onionproxy.ServiceTorInstaller
import io.matthewnelson.topl_service.onionproxy.ServiceTorSettings
import io.matthewnelson.topl_service.prefs.TorServicePrefsListener
import io.matthewnelson.topl_service.receiver.TorServiceReceiver
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.service.components.actions.ServiceActionProcessor
import io.matthewnelson.topl_service.service.components.TorServiceBinder
import io.matthewnelson.topl_service.util.ServiceConsts.NotificationImage
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceAction
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import net.freehaven.tor.control.TorControlCommands
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException

internal class TestTorService(
    override val context: Context,
    dispatcher: CoroutineDispatcher
): BaseService() {


    ///////////////
    /// Binding ///
    ///////////////
    val torServiceBinder: TorServiceBinder by lazy { TorServiceBinder(this) }

    @Volatile
    var serviceIsBound = false
        private set

    override fun unbindService() {
        serviceIsBound = false
    }
    override fun onBind(intent: Intent?): IBinder? {
        serviceIsBound = true
        return torServiceBinder
    }


    /////////////////////////
    /// BroadcastReceiver ///
    /////////////////////////
    val torServiceReceiver by lazy { TorServiceReceiver(this) }

    override fun registerReceiver() {
        torServiceReceiver.register()
    }
    override fun unregisterReceiver() {
        torServiceReceiver.unregister()
    }


    //////////////////
    /// Coroutines ///
    //////////////////
    val supervisorJob = SupervisorJob()
    @ExperimentalCoroutinesApi
    val testCoroutineScope = TestCoroutineScope(dispatcher + supervisorJob)

    @ExperimentalCoroutinesApi
    override fun getScopeIO(): CoroutineScope {
        return testCoroutineScope
    }
    @ExperimentalCoroutinesApi
    override fun getScopeMain(): CoroutineScope {
        return testCoroutineScope
    }


    //////////////////////////////
    /// ServiceActionProcessor ///
    //////////////////////////////
    val serviceActionProcessor by lazy {
        ServiceActionProcessor(
            this
        )
    }

    override fun processIntent(serviceActionIntent: Intent) {
        serviceActionProcessor.processIntent(serviceActionIntent)
    }

    @Volatile
    var stopSelfCalled = false
        private set

    /**
     * In production, this is where `stopSelf()` is called. Need to decouple from
     * the thread it is called on so that functionality of the [ServiceActionProcessor]
     * is simulated properly in that it will stop processing the queue and the Coroutine
     * Job will move to `complete`.
     * */
    @ExperimentalCoroutinesApi
    override fun stopService() {
        stopSelfCalled = true
        getScopeMain().launch { onDestroy() }
    }


    ///////////////////////////
    /// ServiceNotification ///
    ///////////////////////////
    val serviceNotification = ServiceNotification.get()

    override fun addNotificationActions() {
        serviceNotification.addActions(this)
    }
    override fun removeNotification() {
        serviceNotification.remove()
    }
    override fun removeNotificationActions() {
        serviceNotification.removeActions(this)
    }
    override fun startForegroundService(): ServiceNotification {
        return serviceNotification.startForeground(this)
    }
    override fun stopForegroundService(): ServiceNotification {
        return serviceNotification.stopForeground(this)
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
    private val broadcastLogger: BroadcastLogger by lazy {
        getBroadcastLogger(TestTorService::class.java)
    }
    val serviceEventBroadcaster: ServiceEventBroadcaster by lazy {
        ServiceEventBroadcaster(this)
    }
    val serviceTorSettings: ServiceTorSettings by lazy {
        ServiceTorSettings(this, TorServiceController.getTorSettings())
    }
    val onionProxyManager: OnionProxyManager by lazy {
        OnionProxyManager(
            context,
            TorServiceController.getTorConfigFiles(),
            ServiceTorInstaller(this),
            serviceTorSettings,
            ServiceEventListener(),
            serviceEventBroadcaster,
            buildConfigDebug
        )
    }

    /**
     * Requires the use of [TorServiceController.Builder.setEventBroadcaster] to set
     * [TestEventBroadcaster] so that [TorServiceController.appEventBroadcaster] is
     * assuredly not null.
     * */
    fun getSimulatedTorStates(): Pair<@TorState String, @TorNetworkState String> {
        val appEventBroadcaster = TorServiceController.appEventBroadcaster!! as TestEventBroadcaster
        return Pair(appEventBroadcaster.torState, appEventBroadcaster.torNetworkState)
    }

    @WorkerThread
    @Throws(IOException::class)
    override fun copyAsset(assetPath: String, file: File) {
        try {
//            FileUtilities.copy(context.assets.open(assetPath), file.outputStream())
        } catch (e: Exception) {
            throw IOException("Failed copying asset from $assetPath", e)
        }
    }
    override fun getBroadcastLogger(clazz: Class<*>): BroadcastLogger {
        return onionProxyManager.getBroadcastLogger(clazz)
    }
    override fun hasControlConnection(): Boolean {
        return getSimulatedTorStates().first == TorState.ON
    }
    override fun isTorOff(): Boolean {
        return getSimulatedTorStates().first == TorState.OFF
    }

    var refreshBroadcastLoggerWasCalled = false
    override fun refreshBroadcastLoggersHasDebugLogsVar() {
        onionProxyManager.refreshBroadcastLoggersHasDebugLogsVar()
        refreshBroadcastLoggerWasCalled = true
    }


    override suspend fun signalNewNym() {
        serviceEventBroadcaster.broadcastNotice(
            "${TorControlCommands.SIGNAL_NEWNYM}: ${OnionProxyManager.NEWNYM_SUCCESS_MESSAGE}"
        )
        delay(1000L)
    }
    override fun signalControlConnection(torControlCommand: String): Boolean {
        return true
    }
    @WorkerThread
    override fun startTor() {
        simulateStart()
    }
    @ExperimentalCoroutinesApi
    private fun simulateStart() = getScopeIO().launch {
        try {
            onionProxyManager.setup()
            generateTorrcFile()

            serviceEventBroadcaster.broadcastTorState(TorState.STARTING, TorNetworkState.DISABLED)
            delay(1000)
            serviceEventBroadcaster.broadcastTorState(TorState.ON, TorNetworkState.DISABLED)
            delay(1000)
            serviceEventBroadcaster.broadcastTorState(TorState.ON, TorNetworkState.ENABLED)
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }
    @WorkerThread
    override fun stopTor() {
        try {
            serviceEventBroadcaster.broadcastTorState(TorState.STOPPING, TorNetworkState.ENABLED)
            serviceEventBroadcaster.broadcastTorState(TorState.STOPPING, TorNetworkState.DISABLED)
            serviceEventBroadcaster.broadcastTorState(TorState.OFF, TorNetworkState.DISABLED)
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }
    @WorkerThread
    @Throws(
        SecurityException::class,
        IllegalAccessException::class,
        IllegalArgumentException::class,
        InvocationTargetException::class,
        NullPointerException::class,
        ExceptionInInitializerError::class,
        IOException::class
    )
    private fun generateTorrcFile() {
        onionProxyManager.getNewSettingsBuilder()
            .updateTorSettings()
//            .setGeoIpFiles()
            .finishAndWriteToTorrcFile()
    }


    ///////////////////////////////
    /// TorServicePrefsListener ///
    ///////////////////////////////
    var torServicePrefsListener: TorServicePrefsListener? = null
        private set

    @Volatile
    var torServicePrefsListenerIsRegistered = false
        private set

    override fun registerPrefsListener() {
        torServicePrefsListener?.unregister()
        torServicePrefsListener = TorServicePrefsListener(this)
        torServicePrefsListenerIsRegistered = true
    }
    override fun unregisterPrefsListener() {
        torServicePrefsListener?.unregister()
        torServicePrefsListener = null
        torServicePrefsListenerIsRegistered = false
    }


    override fun onCreate() {
        serviceNotification.buildNotification(this)
        broadcastLogger.notice("BuildConfig.DEBUG set to: $buildConfigDebug")
        registerPrefsListener()
    }

    override fun onDestroy() {
//        processIntent(Intent(ServiceAction.DESTROY))
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