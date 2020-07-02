package io.matthewnelson.topl_service.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.BuildConfig
import io.matthewnelson.topl_service.onionproxy.ServiceEventBroadcaster
import io.matthewnelson.topl_service.onionproxy.ServiceEventListener
import io.matthewnelson.topl_service.onionproxy.ServiceTorInstaller
import io.matthewnelson.topl_service.onionproxy.ServiceTorSettings
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyBoolean
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceAction
import io.matthewnelson.topl_service.util.TorServicePrefs
import kotlinx.coroutines.*

internal class TorService: Service() {

    companion object {
        private lateinit var torConfigFiles: TorConfigFiles
        private lateinit var torSettings: TorSettings
        private var buildConfigVersionCode: Int = -1
        private var buildConfigDebug: Boolean? = null
        private lateinit var geoipAssetPath: String
        private lateinit var geoip6AssetPath: String

        fun initialize(
            torConfigFiles: TorConfigFiles,
            torSettings: TorSettings,
            buildConfigVersionCode: Int,
            buildConfigDebug: Boolean,
            geoipAssetPath: String,
            geoip6AssetPath: String
        ) {
            this.torConfigFiles = torConfigFiles
            this.torSettings = torSettings
            this.buildConfigVersionCode = buildConfigVersionCode
            this.buildConfigDebug = buildConfigDebug
            this.geoipAssetPath = geoipAssetPath
            this.geoip6AssetPath = geoip6AssetPath
        }

        // Needed to inhibit all TorServiceController methods except for startTor()
        // from sending working such that startService isn't called and Tor doesn't
        // properly start up.
        var isTorStarted = false
            private set

        fun getLocalPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences("TorServiceLocalPrefs", Context.MODE_PRIVATE)
    }

    private lateinit var broadcastLogger: BroadcastLogger

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        ServiceNotification.get().startForegroundNotification(this)
        initTorServicePrefsListener(this)
        initTOPLCore(this)
        broadcastLogger = onionProxyManager.createBroadcastLogger(TorService::class.java)
        torServicePrefsListener.initBroadcastLogger(onionProxyManager)
    }

    override fun onDestroy() {
        broadcastLogger.debug("onDestroy called. Cleaning up.")
        torServicePrefsListener.unregister()
        supervisorJob.cancel()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        broadcastLogger.warn("Low memory!!!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let {
            broadcastLogger.debug("Executing ServiceAction: $it")
            executeAction(it)
        }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        // TODO: add to debug message if Controller option for disabling stop on task removed
        //  when the feature gets implemented.
        broadcastLogger.debug("Task has been removed")
        executeAction(ServiceAction.ACTION_STOP)
    }

    /////////////////
    /// TOPL-Core ///
    /////////////////
    private lateinit var onionProxyManager: OnionProxyManager

    private fun initTOPLCore(torService: TorService) {
        val serviceTorInstaller = ServiceTorInstaller(
            torService,
            torConfigFiles,
            buildConfigVersionCode,
            buildConfigDebug ?: BuildConfig.DEBUG,
            geoipAssetPath,
            geoip6AssetPath
        )
        onionProxyManager = OnionProxyManager(
            torService,
            torConfigFiles,
            serviceTorInstaller,
            ServiceTorSettings(torSettings, torService),
            ServiceEventListener(),
            ServiceEventBroadcaster(torService),
            buildConfigDebug
        )
    }

    /**
     * Do not call directly, use [executeAction].
     * */
    @WorkerThread
    private fun startTor() {
        if (!onionProxyManager.torStateMachine.isOn) {
            try {
                onionProxyManager.setup()
                onionProxyManager.getNewSettingsBuilder()
                    .updateTorSettings()
                    .setGeoIpFiles()
                    .finishAndWriteToTorrcFile()

                onionProxyManager.start()
            } catch (e: Exception) {
                broadcastLogger.exception(e)
            }
        }
    }

    /**
     * Do not call directly, use [executeAction].
     * */
    @WorkerThread
    private fun stopTor() {
        try {
            onionProxyManager.stop()
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }


    ///////////////
    /// Actions ///
    ///////////////
    private val supervisorJob = SupervisorJob()
    val scopeMain = CoroutineScope(Dispatchers.Main + supervisorJob)
    private lateinit var executeActionJob: Job

    /**
     * Route all [ServiceAction]s here for execution.
     *
     * @param [action] A [ServiceAction]
     * */
    private fun executeAction(@ServiceAction action: String) {
        scopeMain.launch {
            if (::executeActionJob.isInitialized && executeActionJob.isActive) {
                executeActionJob.join()
                delay(100L)
            }

            executeActionJob = launch(Dispatchers.IO) {
                when (action) {
                    ServiceAction.ACTION_START -> {
                        startTor()
                        isTorStarted = true
                    }
                    ServiceAction.ACTION_STOP -> {
                        isTorStarted = false
                        stopTor()

                        // Need a delay before calling stopSelf so that the coroutine which
                        // removes notification actions isn't cancelled via the supervisorJob
                        // being cancelled in onDestroy.
                        delay(200L)
                        stopSelf()
                    }
                    ServiceAction.ACTION_RESTART -> {
                        stopTor()
                        delay(1000L)
                        startTor()
                    }
                    ServiceAction.ACTION_NEW_ID -> {
                        onionProxyManager.signalNewNym()
                    }
                }
            }
        }
    }


    ///////////////////////
    /// TorServicePrefs ///
    ///////////////////////
    private lateinit var torServicePrefsListener: TorServicePrefsListener

    private fun initTorServicePrefsListener(torService: TorService) {
        torServicePrefsListener = TorServicePrefsListener(TorServicePrefs(torService))
    }

    /**
     * Gets initialized in [onCreate] **before** [initTOPLCore] is called so that the
     * initial [onSharedPreferenceChanged] proc won't do anything.
     *
     * Listens to [TorServicePrefs] for changes such that while Tor is running, it can
     * query [onionProxyManager] to have it updated immediately (if the setting doesn't
     * require a restart).
     *
     * @param [torServicePrefs] Our target to listen to for any changes
     * */
    private inner class TorServicePrefsListener(
        val torServicePrefs: TorServicePrefs
    ): SharedPreferences.OnSharedPreferenceChangeListener {

        private var broadcastLogger: BroadcastLogger? = null
        fun initBroadcastLogger(onionProxyManager: OnionProxyManager) {
            if (broadcastLogger == null)
                broadcastLogger =
                    onionProxyManager.createBroadcastLogger(TorServicePrefsListener::class.java)
        }

        // Register itself immediately upon instantiation.
        init {
            torServicePrefs.registerListener(this)
        }

        /**
         * Called from [onDestroy] to prevent memory leaks.
         * */
        fun unregister() {
            broadcastLogger?.debug("Unregistering self")
            torServicePrefs.unregisterListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (::onionProxyManager.isInitialized && !key.isNullOrEmpty()) {
                broadcastLogger?.debug("$key was modified")
                when (key) {
                    PrefKeyBoolean.HAS_DEBUG_LOGS -> {
                        // TODO: Think about doing something with local prefs such that it
                        //  turns Debugging off at every application start automatically?
                        //  .
                        //  Especially necessary if I switch Tor's debug output location from
                        //  SystemOut to log to a file (more secure).
                        //  .
                        //  Will need to create another class available to Library user
                        //  strictly for Tor logs if logging to a file, such that they can
                        //  easily query, read, and load them to views.
                        //  Maybe a `TorDebugLogHelper` class?
                        //  .
                        //  Will need some way of automatically clearing old log files, too.
                        if (!onionProxyManager.torStateMachine.isOff)
                            onionProxyManager.refreshBroadcastLoggersHasDebugLogsVar()
                    }
                }
            }
        }

    }
}