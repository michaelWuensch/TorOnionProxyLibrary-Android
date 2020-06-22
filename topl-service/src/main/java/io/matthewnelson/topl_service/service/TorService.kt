package io.matthewnelson.topl_service.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.matthewnelson.topl_core.OnionProxyContext
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_service.model.ServiceNotification
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.onionproxy.OnionProxyEventBroadcaster
import io.matthewnelson.topl_service.onionproxy.OnionProxyEventListener
import io.matthewnelson.topl_service.onionproxy.OnionProxyInstaller
import io.matthewnelson.topl_service.service.ActionConsts.ServiceAction

internal class TorService: Service() {

    companion object {
        private lateinit var torConfigFiles: TorConfigFiles
        private lateinit var torSettings: TorSettings
        private var buildConfigVersion: Int = -1
        private lateinit var geoipAssetPath: String
        private lateinit var geoip6AssetPath: String

        fun initialize(
            config: TorConfigFiles,
            settings: TorSettings,
            buildVersion: Int,
            geoipPath: String,
            geoip6Path: String
        ) {
            torConfigFiles = config
            torSettings = settings
            buildConfigVersion = buildVersion
            geoipAssetPath = geoipPath
            geoip6AssetPath = geoip6Path
        }

        // Intents/LocalBroadcastManager
        const val FILTER = "io.matthewnelson.topl_service.service.TorService"
        const val ACTION_EXTRAS_KEY = "SERVICE_ACTION_EXTRA"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        registerBroadcastReceiver(this)
        ServiceNotification.get().startForegroundNotification(this)

        // Setup TOPL
        val torServiceSettings = TorServiceSettings(torSettings, this)
        val onionProxyInstaller = OnionProxyInstaller(
            this,
            torConfigFiles,
            buildConfigVersion,
            geoipAssetPath,
            geoip6AssetPath
        )
        val onionProxyContext = OnionProxyContext(
            torConfigFiles,
            onionProxyInstaller,
            torServiceSettings
        )
        val onionProxyEventBroadcaster = OnionProxyEventBroadcaster(this, torServiceSettings)
        onionProxyManager = OnionProxyManager(
            this,
            onionProxyContext,
            onionProxyEventBroadcaster,
            OnionProxyEventListener(this, onionProxyEventBroadcaster)
        )
        onionProxyManager.setup()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcastReceiver()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        // TODO: Implement
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (onionProxyManager.eventBroadcaster.torStateMachine.isOff)
            Thread {
                try {
                    onionProxyManager.getNewSettingsBuilder()
                        .updateTorSettings()
                        .setGeoIpFiles()
                        .finishAndWriteToTorrcFile()

                    onionProxyManager.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopTor()
    }

    private fun stopTor() {
        Thread {
            try {
                onionProxyManager.stop()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            stopSelf()
        }.start()
    }

    ////////////
    /// TOPL ///
    ////////////
    private lateinit var onionProxyManager: OnionProxyManager

    /////////////////////////
    /// BroadcastReceiver ///
    /////////////////////////
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
                        stopTor()
                    }
                    ServiceAction.ACTION_RESTART -> {
                        // TODO: Implement
                    }
                    ServiceAction.ACTION_NEW_ID -> {
                        // TODO: Implement
                    }
                    else -> {}
                }
            }
        }
    }
}