package io.matthewnelson.topl_android_service

import android.content.Context
import android.content.Intent
import io.matthewnelson.topl_android.OnionProxyContext
import io.matthewnelson.topl_android.OnionProxyManager
import io.matthewnelson.topl_android_service.onionproxy.OnionProxyInstaller
import io.matthewnelson.topl_android_settings.TorConfigFiles
import io.matthewnelson.topl_android_settings.TorSettings

internal class TorServiceManager private constructor(private val context: Context) {

    companion object {
        private lateinit var torServiceManagerInstance: TorServiceManager

        fun getInstance(context: Context): TorServiceManager {
            if (!::torServiceManagerInstance.isInitialized)
                torServiceManagerInstance = TorServiceManager(context)
            return torServiceManagerInstance
        }

        /**
         * Ensures that the [TorServiceController.Companion] method calls won't
         * execute anything prior to initialization.
         * */
        fun getInstance(): TorServiceManager? =
            if (::torServiceManagerInstance.isInitialized)
                torServiceManagerInstance
            else
                null
    }

    private lateinit var torSettings: TorSettings
    private lateinit var torConfig: TorConfigFiles
    private lateinit var torInstaller: OnionProxyInstaller

    // Will never throw an exception b/c it only gets called from TorService, which
    // can't be started w/o initializing this class via the TorServiceController Builder.
    fun getTorSettings(): TorSettings =
        torSettings

    fun getTorConfig(): TorConfigFiles =
        torConfig

    fun getTorInstaller(): OnionProxyInstaller =
        torInstaller

    fun initialize(
        settings: TorSettings,
        appVersionCode: Int,
        geoipAssetPath: String,
        geoip6AssetPath: String,
        torConfigFiles: TorConfigFiles?,
        startServiceAsap: Boolean,
        stopServiceOnTermination: Boolean
    ) {
        torSettings = settings
        torConfig = torConfigFiles ?: TorConfigFiles.createConfig(context)
        torInstaller =
            OnionProxyInstaller(
                context,
                torConfig,
                appVersionCode,
                geoipAssetPath,
                geoip6AssetPath
            )
        // TODO: Coroutine for installing files
        //  then starting services if startServiceAsap is true
    }

    private fun sendBroadcast(receiverAction: String) {

    }

    fun startTor() =
        sendBroadcast(TorServiceReceiver.TOR_SERVICE_START)

    fun stopTor() =
        sendBroadcast(TorServiceReceiver.TOR_SERVICE_STOP)

    fun restartTor() =
        sendBroadcast(TorServiceReceiver.TOR_SERVICE_RESTART)

    fun newIdentity() =
        sendBroadcast(TorServiceReceiver.TOR_SERVICE_RENEW)

}