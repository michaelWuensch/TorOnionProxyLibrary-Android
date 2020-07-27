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
import android.content.Context
import androidx.core.app.NotificationCompat
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.notification.ServiceNotification
import java.io.File

/**
 * @suppress
 * */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        setupTorServices(this, TorConfigFiles.createConfig(this))
    }

    private fun generateTorServiceNotificationBuilder(): ServiceNotification.Builder {
//  private fun generateTorServiceNotificationBuilder(): ServiceNotification.Builder {
        return ServiceNotification.Builder(
            channelName = "TorService Channel",
            channelDescription = "Tor Channel",
            channelID = "My Sample Application",
            notificationID = 615
        )
            .setActivityToBeOpenedOnTap(
                clazz = MainActivity::class.java,
                intentExtrasKey = null,
                intentExtras = null,
                intentRequestCode = null
            )
            .setImageTorNetworkingEnabled(drawableRes = R.drawable.tor_stat_network_enabled)
            .setImageTorNetworkingDisabled(drawableRes = R.drawable.tor_stat_network_disabled)
            .setImageTorDataTransfer(drawableRes = R.drawable.tor_stat_network_dataxfer)
            .setImageTorErrors(drawableRes = R.drawable.tor_stat_notifyerr)
            .setCustomColor(colorRes = R.color.tor_service_white)
            .setVisibility(visibility = NotificationCompat.VISIBILITY_PRIVATE)
            .setCustomColor(colorRes = R.color.primaryColor)
            .enableTorRestartButton(enable = true)
            .enableTorStopButton(enable = true)
            .showNotification(show = true)
//  }
    }

    private fun setupTorServices(application: Application, torConfigFiles: TorConfigFiles) {
//  private fun setupTorServices(application: Application, torConfigFiles: TorConfigFiles ) {
        TorServiceController.Builder(
            application = application,
            torServiceNotificationBuilder = generateTorServiceNotificationBuilder(),
            buildConfigVersionCode = BuildConfig.VERSION_CODE,

            // Can instantiate directly here then access it from
            // TorServiceController.Companion.getTorSettings() and cast what's returned
            // as MyTorSettings
            torSettings = MyTorSettings(),

            // These should live somewhere in your module's assets directory,
            // ex: my-project/my-application-module/src/main/assets/common/geoip
            // ex: my-project/my-application-module/src/main/assets/common/geoip6
            geoipAssetPath = "common/geoip",
            geoip6AssetPath = "common/geoip6"
        )
            .setBuildConfigDebug(buildConfigDebug = BuildConfig.DEBUG)

            // Can instantiate directly here then access it from
            // TorServiceController.Companion?.appEventBroadcaster and cast what's returned
            // as MyEventBroadcaster
            .setEventBroadcaster(eventBroadcaster = MyEventBroadcaster())

            // Only needed if you wish to customize the directories/files used by Tor if
            // the defaults aren't to your liking.
            .useCustomTorConfigFiles(torConfigFiles = torConfigFiles)

            .build()
//  }
    }

    fun customTorConfigFilesSetup(context: Context): TorConfigFiles {
//  fun customTorConfigFilesSetup(context: Context): TorConfigFiles {

        // This is modifying the directory hierarchy from TorService's
        // default setup. For example, if you are using binaries for Tor that
        // are named differently that that expressed in TorConfigFiles.createConfig()

        // Post Android API 28 requires that executable files be contained in your
        // application's data/app directory, as they can no longer execute from data/data.
        val installDir = File(context.applicationInfo.nativeLibraryDir)

        // Will create a directory within your application's data/data dir
        val configDir = context.getDir("torservice", Context.MODE_PRIVATE)

        val builder = TorConfigFiles.Builder(installDir, configDir)

        // Customize the tor executable file name. Requires that the executable file
        // be in your module's src/main/jniLibs directory. If you are getting your
        // executable files via a dependency be sure to consult that Library's documentation.
        builder.torExecutable(File(installDir, "libtor.so"))

        // customize further via the builder methods...

        return builder.build()
//  }
    }
}