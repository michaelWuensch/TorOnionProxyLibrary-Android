package io.matthewnelson.topl_android_service

import android.content.Context
import android.content.SharedPreferences
import io.matthewnelson.topl_android.OnionProxyManager
import io.matthewnelson.topl_android_service.model.NotificationSettings
import io.matthewnelson.topl_android_settings.TorConfigFiles
import io.matthewnelson.topl_android_settings.TorSettings

internal object TorServiceManager {

    // SharedPreferences Keys
    private const val PREFS_APP_VERSION = "PREFS_APP_VERSION"

    private lateinit var appContext: Context
    private lateinit var prefs: SharedPreferences

    private lateinit var notificationSettings: NotificationSettings
    fun isNotificationSettingsInitialized(): Boolean =
        ::notificationSettings.isInitialized
    fun setNotificationSettings(settings: NotificationSettings) {
        if (!isNotificationSettingsInitialized()) {
            notificationSettings = settings
        }
    }

    fun initialize(
        context: Context,
        torSettings: TorSettings,
        geoipAssetPath: String,
        geoip6AssetPath: String,
        torConfigFiles: TorConfigFiles?,
        startServiceAsap: Boolean,
        stopServiceOnTermination: Boolean
    ) {
        // TODO: Implement
    }

}