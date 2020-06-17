package io.matthewnelson.sampleapp

import android.app.Application
import io.matthewnelson.topl_android_service.TorServiceController
import io.matthewnelson.topl_android_settings.DefaultTorSettings

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        TorServiceController.Builder(
            this,
            BuildConfig.VERSION_CODE,
            DefaultTorSettings(),
            "common/geoip",
            "common/geoip6"
        )
            .customizeNotification("My Sample Application", 6156, MainActivity::class.java)
            .applyNotificationSettings()
            .build()

        TorServiceController.startTor()
    }
}