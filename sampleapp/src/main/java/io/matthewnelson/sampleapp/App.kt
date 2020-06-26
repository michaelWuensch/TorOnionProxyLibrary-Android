package io.matthewnelson.sampleapp

import android.app.Application
import android.content.Context
import io.matthewnelson.topl_service.TorServiceController

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        setupTorServices()
    }

    private fun setupTorServices() {
        TorServiceController.Builder(
            this,
            BuildConfig.VERSION_CODE,
            MyTorSettings(),
            "common/geoip",
            "common/geoip6"
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
            .setColorWhenTorOn(
                colorRes = R.color.tor_service_connected,
                colorizeBackground = false
            )
            .enableTorRestartButton()
            .enableTorStopButton()
            .applyNotificationSettings()
            .build()
    }
}