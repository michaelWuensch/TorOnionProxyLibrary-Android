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
        ).showNotification("My Sample Application for demoing TorServices", 6156)
            .applyNotificationSettings().build()

        TorServiceController.startTor()
    }
}