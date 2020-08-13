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
*     that are publicly available classes/functions/etc (ie: do not contain the
*     visibility modifiers `internal`, `private`, `protected`, or are within
*     classes/functions/etc that contain the aforementioned visibility modifiers)
*     to TorOnionProxyLibrary-Android users that are needed to implement
*     TorOnionProxyLibrary-Android and reside in ONLY the following modules:
*
*      - topl-core-base
*      - topl-service
*
*     The following are excluded from "The Interfaces":
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
package io.matthewnelson.sampleapp

import android.app.Application
import android.os.Process
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.topl_android.MyEventBroadcaster
import io.matthewnelson.sampleapp.topl_android.MyTorSettings
import io.matthewnelson.sampleapp.ui.MainActivity
import io.matthewnelson.sampleapp.ui.fragments.settings.library.LibraryPrefs
import io.matthewnelson.sampleapp.ui.fragments.settings.library.SettingsLibraryFragment
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.lifecycle.BackgroundManager

/**
 * @suppress
 *
 * See [io.matthewnelson.sampleapp.topl_android.CodeSamples] for code samples
 * */
class App: Application() {

    companion object {
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs.createUnencrypted("TOPL-Android_SampleApp", this)

        setupTorServices(this)

        TorServiceController.appEventBroadcaster?.let {
            (it as MyEventBroadcaster).broadcastLogMessage(
                "SampleApp|Application|Process ID: ${Process.myPid()}"
            )
        }
    }

    /**
     * See [io.matthewnelson.sampleapp.topl_android.CodeSamples.generateTorServiceNotificationBuilder]
     * for a cleaner sample
     * */
    private fun generateTorServiceNotificationBuilder(): ServiceNotification.Builder {
        return ServiceNotification.Builder(
            channelName = "TOPL-Android Demo",
            channelDescription = "TorOnionProxyLibrary-Android Demo",
            channelID = "TOPL-Android Demo",
            notificationID = 615
        )
            .setActivityToBeOpenedOnTap(
                clazz = MainActivity::class.java,
                intentExtrasKey = null,
                intentExtras = null,
                intentRequestCode = null
            )

            .setVisibility(LibraryPrefs.getNotificationVisibilitySetting())
            .setCustomColor(LibraryPrefs.getNotificationColorSetting())
            .enableTorRestartButton(LibraryPrefs.getNotificationRestartEnableSetting())
            .enableTorStopButton(LibraryPrefs.getNotificationStopEnableSetting())
            .showNotification(LibraryPrefs.getNotificationShowSetting())
    }

    /**
     * See [io.matthewnelson.sampleapp.topl_android.CodeSamples.generateBackgroundManagerPolicy]
     * for a cleaner sample
     * */
    private fun generateBackgroundManagerPolicy(): BackgroundManager.Builder.Policy {
        val builder = BackgroundManager.Builder()
        return when (LibraryPrefs.getBackgroundManagerPolicySetting()) {
            LibraryPrefs.BACKGROUND_MANAGER_POLICY_FOREGROUND -> {
                builder.runServiceInForeground(
                    LibraryPrefs.getBackgroundManagerKillAppSetting()
                )
            }
            LibraryPrefs.BACKGROUND_MANAGER_POLICY_RESPECT -> {
                builder.respectResourcesWhileInBackground(
                    LibraryPrefs.getBackgroundManagerExecuteDelaySetting()
                )
            }
            else -> {
                builder.respectResourcesWhileInBackground(20)
            }
        }
    }

    /**
     * See [io.matthewnelson.sampleapp.topl_android.CodeSamples.setupTorServices]
     * for a cleaner sample
     * */
    private fun setupTorServices(application: Application) {
        TorServiceController.Builder(
            application = application,
            torServiceNotificationBuilder = generateTorServiceNotificationBuilder(),
            backgroundManagerPolicy = generateBackgroundManagerPolicy(),
            buildConfigVersionCode = BuildConfig.VERSION_CODE,
            torSettings = MyTorSettings(),
            geoipAssetPath = "common/geoip",
            geoip6AssetPath = "common/geoip6"
        )
            .addTimeToRestartTorDelay(LibraryPrefs.getControllerRestartDelaySetting())
            .addTimeToStopServiceDelay(LibraryPrefs.getControllerStopDelaySetting())
            .disableStopServiceOnTaskRemoved(LibraryPrefs.getControllerDisableStopServiceOnTaskRemovedSetting())
            .setBuildConfigDebug(LibraryPrefs.getControllerBuildConfigDebugSetting())
            .setEventBroadcaster(eventBroadcaster = MyEventBroadcaster())
            .build()
    }
}