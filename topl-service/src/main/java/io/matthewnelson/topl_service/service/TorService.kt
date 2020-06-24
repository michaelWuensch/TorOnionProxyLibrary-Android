package io.matthewnelson.topl_service.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.matthewnelson.topl_core.OnionProxyContext
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.onionproxy.OnionProxyEventBroadcaster
import io.matthewnelson.topl_service.onionproxy.OnionProxyEventListener
import io.matthewnelson.topl_service.onionproxy.OnionProxyInstaller
import io.matthewnelson.topl_service.service.ActionConsts.ServiceAction
import kotlinx.coroutines.*
import net.freehaven.tor.control.EventListener

internal class TorService: Service() {

    companion object {
        private lateinit var torConfigFiles: TorConfigFiles
        private lateinit var torSettings: TorSettings
        private var additionalEventListener: EventListener? = null
        private var buildConfigVersionCode: Int = -1
        private lateinit var geoipAssetPath: String
        private lateinit var geoip6AssetPath: String

        fun initialize(
            torConfigFiles: TorConfigFiles,
            torSettings: TorSettings,
            additionalEventListener: EventListener?,
            buildConfigVersionCode: Int,
            geoipAssetPath: String,
            geoip6AssetPath: String
        ) {
            this.torConfigFiles = torConfigFiles
            this.torSettings = torSettings
            this.additionalEventListener = additionalEventListener
            this.buildConfigVersionCode = buildConfigVersionCode
            this.geoipAssetPath = geoipAssetPath
            this.geoip6AssetPath = geoip6AssetPath
        }

        var isServiceStarted = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        isServiceStarted = true
        ServiceNotification.get().startForegroundNotification(this)

        // Instantiate TOPL
        val torServiceSettings = TorServiceSettings(torSettings, this)
        val onionProxyInstaller = OnionProxyInstaller(
            this,
            torConfigFiles,
            buildConfigVersionCode,
            geoipAssetPath,
            geoip6AssetPath
        )
        val onionProxyContext = OnionProxyContext(
            torConfigFiles,
            onionProxyInstaller,
            torServiceSettings
        )
        val onionProxyEventBroadcaster = OnionProxyEventBroadcaster(this, torServiceSettings)
        val onionProxyEventListener = OnionProxyEventListener(this, onionProxyEventBroadcaster)
        onionProxyManager = OnionProxyManager(
            this,
            onionProxyContext,
            onionProxyEventBroadcaster,
            onionProxyEventListener,
            arrayOf(additionalEventListener)
        )
    }

    override fun onDestroy() {
        supervisorJob.cancel()
        isServiceStarted = false
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { executeAction(it) }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        executeAction(ServiceAction.ACTION_STOP)
    }

    ////////////
    /// TOPL ///
    ////////////
    private lateinit var onionProxyManager: OnionProxyManager
    private val actionLock = Object()

    /**
     * Do not call directly. Use [executeAction].
     * */
    private fun startTor() =
        synchronized(actionLock) {
            if (!onionProxyManager.eventBroadcaster.torStateMachine.isOn) {
                try {
                    onionProxyManager.setup()
                    onionProxyManager.getNewSettingsBuilder()
                        .updateTorSettings()
                        .setGeoIpFiles()
                        .finishAndWriteToTorrcFile()

                    onionProxyManager.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    /**
     * Do not call directly. Use [executeAction].
     * */
    private fun stopTor(executeStopSelf: Boolean = true) =
        synchronized(actionLock) {
            try {
                onionProxyManager.stop()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (executeStopSelf)
                stopSelf()
        }

    private suspend fun restartTor() {
        stopTor(executeStopSelf = false)
        delay(1000)
        startTor()
    }

    /**
     * Do not call directly. Use [executeAction].
     * */
    private fun newIdentity() =
        synchronized(actionLock) {
            try {
                onionProxyManager.setNewIdentity()
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }


    ///////////////
    /// Actions ///
    ///////////////
    private val supervisorJob = SupervisorJob()
    private val scopeDefault = CoroutineScope(Dispatchers.Default + supervisorJob)
    private lateinit var startTorJob: Job
    private lateinit var stopTorJob: Job
    private lateinit var restartTorJob: Job
    private lateinit var newIdentityJob: Job

    /**
     * All actions must go through here to be executed using a coroutine.
     * */
    private fun executeAction(action: String) {
        when (action) {
            ServiceAction.ACTION_START -> {
                startTorJob = scopeDefault.launch {
                    startTor()
                }
            }
            ServiceAction.ACTION_STOP -> {
                if (::stopTorJob.isInitialized && stopTorJob.isActive) return
                if (::restartTorJob.isInitialized && restartTorJob.isActive) return
                stopTorJob = scopeDefault.launch {
                    stopTor()
                }
            }
            ServiceAction.ACTION_RESTART -> {
                if (::stopTorJob.isInitialized && stopTorJob.isActive) return
                if (::restartTorJob.isInitialized && restartTorJob.isActive) return
                restartTorJob = scopeDefault.launch {
                    restartTor()
                }
            }
            ServiceAction.ACTION_NEW_ID -> {
                if (::stopTorJob.isInitialized && stopTorJob.isActive) return
                if (::restartTorJob.isInitialized && restartTorJob.isActive) return
                newIdentityJob = scopeDefault.launch {
                    newIdentity()
                }
            }
        }
    }
}