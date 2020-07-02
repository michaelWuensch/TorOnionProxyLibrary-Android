package io.matthewnelson.sampleapp

import android.app.Application
import io.matthewnelson.topl_service.TorServiceController

class App: Application() {

    companion object {
        val myTorSettings = MyTorSettings()
    }

    override fun onCreate() {
        super.onCreate()
        setupTorServices()
    }

    private fun setupTorServices() {
        TorServiceController.Builder(
            application = this,
            buildConfigVersion = BuildConfig.VERSION_CODE,
            torSettings = myTorSettings,
            geoipAssetPath = "common/geoip",
            geoip6AssetPath = "common/geoip6"
        )

            .customizeNotification(
                channelName = "TorService Channel",
                channelDescription = "Tor Channel",
                channelID = "My Sample Application",
                notificationID = 615)
            .setActivityToBeOpenedOnTap(
                clazz = MainActivity::class.java,
                intentExtrasKey = null,
                intentExtras = null,
                intentRequestCode = null
            )
            .setCustomColor(
                colorRes = R.color.primaryColor,
                colorizeBackground = true
            )
            .enableTorRestartButton()
            .enableTorStopButton()
            .applyNotificationSettings()

            .setBuildConfigDebug(buildConfigDebug = BuildConfig.DEBUG)
            .setEventBroadcaster(eventBroadcaster = MyEventBroadcaster())
            .build()
    }
}