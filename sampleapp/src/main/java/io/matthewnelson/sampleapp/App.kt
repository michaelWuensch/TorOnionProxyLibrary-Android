package io.matthewnelson.sampleapp

import android.app.Application
import io.matthewnelson.topl_android_service.TorServiceController

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        TorServiceController.Builder(
            this,
            BuildConfig.VERSION_CODE,
            MyTorSettings(),
            "common/geoip",
            "common/geoip6"
        )
            .customizeNotification("My Sample Application", 6156, MainActivity::class.java)
            .applyNotificationSettings()
            .build()

        TorServiceController.startTor()
    }
}