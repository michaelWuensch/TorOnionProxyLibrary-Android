/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash `74407114cbfa8ea6f2ac51417dda8be98d8aba86`. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify it
*     under the terms of the GNU General Public License as published by the
*     Free Software Foundation, either version 3 of the License, or (at your
*     option) any later version.
*
*     This program is distributed in the hope that it will be useful, but
*     WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*     or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
*     for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program. If not, see <https://www.gnu.org/licenses/>.
*
* `===========================================================================`
* `+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++`
* `===========================================================================`
*
* The following exception is an additional permission under section 7 of the
* GNU General Public License, version 3 (“GPLv3”).
*
*     "The Interfaces" is henceforth defined as Application Programming Interfaces
*     needed to implement TorOnionProxyLibrary-Android, as listed below:
*
*      - From the `topl-core-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service` module:
*          - The TorServiceController class and it's contained classes/methods/variables
*          - The ServiceNotification.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Companion class and it's contained methods/variables
*
*     The following code is excluded from "The Interfaces":
*
*       - All other code
*
*     Linking TorOnionProxyLibrary-Android statically or dynamically with other
*     modules is making a combined work based on TorOnionProxyLibrary-Android.
*     Thus, the terms and conditions of the GNU General Public License cover the
*     whole combination.
*
*     As a special exception, the copyright holder of TorOnionProxyLibrary-Android
*     gives you permission to combine TorOnionProxyLibrary-Android program with free
*     software programs or libraries that are released under the GNU LGPL and with
*     independent modules that communicate with TorOnionProxyLibrary-Android solely
*     through "The Interfaces". You may copy and distribute such a system following
*     the terms of the GNU GPL for TorOnionProxyLibrary-Android and the licenses of
*     the other code concerned, provided that you include the source code of that
*     other code when and as the GNU GPL requires distribution of source code and
*     provided that you do not modify "The Interfaces".
*
*     Note that people who make modified versions of TorOnionProxyLibrary-Android
*     are not obligated to grant this special exception for their modified versions;
*     it is their choice whether to do so. The GNU General Public License gives
*     permission to release a modified version without this exception; this exception
*     also makes it possible to release a modified version which carries forward this
*     exception. If you modify "The Interfaces", this exception does not apply to your
*     modified version of TorOnionProxyLibrary-Android, and you must remove this
*     exception when you distribute your modified version.
* */
package io.matthewnelson.sampleapp.topl_android

import android.app.Application
import android.content.Context
import androidx.core.app.NotificationCompat
import io.matthewnelson.sampleapp.BuildConfig
import io.matthewnelson.sampleapp.R
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.lifecycle.BackgroundManager
import io.matthewnelson.topl_service.notification.ServiceNotification
import java.io.File

/**
 * @suppress
 * Just samples for the other modules. Does not get instantiated.
 * */
class CodeSamples {

    private fun generateTorServiceNotificationBuilder(): ServiceNotification.Builder {
//  private fun generateTorServiceNotificationBuilder(): ServiceNotification.Builder {
        return ServiceNotification.Builder(
            channelName = "TOPL-Android Demo",
            channelDescription = "TorOnionProxyLibrary-Android Demo",
            channelID = "TOPL-Android Demo",
            notificationID = 615
        )
            // Only needed if you are passing a bundle, or changing request code to something other than 0
            .setContentIntentData(bundle = null, requestCode = 21)

            .setImageTorNetworkingEnabled(drawableRes = R.drawable.tor_stat_network_enabled)
            .setImageTorNetworkingDisabled(drawableRes = R.drawable.tor_stat_network_disabled)
            .setImageTorDataTransfer(drawableRes = R.drawable.tor_stat_network_dataxfer)
            .setImageTorErrors(drawableRes = R.drawable.tor_stat_notifyerr)
            .setVisibility(visibility = NotificationCompat.VISIBILITY_PRIVATE)
            .setCustomColor(colorRes = R.color.primaryColor)
            .enableTorRestartButton(enable = true)
            .enableTorStopButton(enable = true)
            .showNotification(show = true)
//  }
    }

    private fun generateBackgroundManagerPolicy(): BackgroundManager.Builder.Policy {
//  private fun generateBackgroundManagerPolicy(): BackgroundManager.Builder.Policy {
        return BackgroundManager.Builder()

            // All available options present. Only 1 is able to be chosen.
            .respectResourcesWhileInBackground(secondsFrom5To45 = 20)
            //.runServiceInForeground(killAppIfTaskIsRemoved = true)
//  }
    }

    private fun setupTorServices(application: Application, torConfigFiles: TorConfigFiles) {
//  private fun setupTorServices(application: Application, torConfigFiles: TorConfigFiles ) {
        TorServiceController.Builder(
            application = application,
            torServiceNotificationBuilder = generateTorServiceNotificationBuilder(),
            backgroundManagerPolicy = generateBackgroundManagerPolicy(),
            buildConfigVersionCode = BuildConfig.VERSION_CODE,

            // Can instantiate directly here then access it from
            // TorServiceController.Companion.getTorSettings() and cast what's returned
            // as MyTorSettings
            defaultTorSettings = MyTorSettings(),

            // These should live somewhere in your module's assets directory,
            // ex: my-project/my-application-module/src/main/assets/common/geoip
            // ex: my-project/my-application-module/src/main/assets/common/geoip6
            geoipAssetPath = "common/geoip",
            geoip6AssetPath = "common/geoip6"
        )
            .addTimeToDisableNetworkDelay(milliseconds = 1_000L)
            .addTimeToRestartTorDelay(milliseconds = 100L)
            .addTimeToStopServiceDelay(milliseconds = 100L)
            .disableStopServiceOnTaskRemoved(disable = false)
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