/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.sampleapp

import android.app.Application
import io.matthewnelson.topl_service.TorServiceController

/**
 * @suppress
 * */
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
            buildConfigVersionCode = BuildConfig.VERSION_CODE,
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