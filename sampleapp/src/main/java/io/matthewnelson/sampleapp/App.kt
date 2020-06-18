package io.matthewnelson.sampleapp

import android.app.Application
import android.content.Context
import io.matthewnelson.topl_android_service.TorServiceController

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        setupTorServices(this)
    }

    private fun setupTorServices(context: Context) {
        TorServiceController.Builder(
            context.applicationContext,
            BuildConfig.VERSION_CODE,
            MyTorSettings(),
            "common/geoip",
            "common/geoip6"
        )
            .customizeNotification(
                "TorService Channel",
                "Tor Channel",
                "My Sample Application",
                6156)
            .setActivityToBeOpenedOnTap(MainActivity::class.java, null, null)
            .applyNotificationSettings()
            .build()
    }
}