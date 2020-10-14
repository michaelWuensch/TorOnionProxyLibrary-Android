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
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashMessage
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashboardFragment
import io.matthewnelson.sampleapp.ui.fragments.settings.library.components.LibraryPrefs
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.lifecycle.BackgroundManager
import io.matthewnelson.topl_service_base.BaseServiceConsts.BackgroundPolicy

/**
 * @suppress
 *
 * See [io.matthewnelson.sampleapp.topl_android.CodeSamples] for code samples
 * */
class App: Application() {

    companion object {
        const val PREFS_NAME = "TOPL-Android_SampleApp"
        lateinit var stopTorDelaySettingAtAppStartup: String
            private set

        /**
         * See [io.matthewnelson.sampleapp.topl_android.CodeSamples.generateTorServiceNotificationBuilder]
         * for a cleaner sample
         * */
        fun generateTorServiceNotificationBuilder(
            visibility: Int,
            iconColorRes: Int,
            enableRestart: Boolean,
            enableStop: Boolean,
            show: Boolean
        ): ServiceNotification.Builder {
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

                .setVisibility(visibility)
                .setCustomColor(iconColorRes)
                .enableTorRestartButton(enableRestart)
                .enableTorStopButton(enableStop)
                .showNotification(show)
        }

        /**
         * See [io.matthewnelson.sampleapp.topl_android.CodeSamples.generateBackgroundManagerPolicy]
         * for a cleaner sample
         * */
        fun generateBackgroundManagerPolicy(
            prefs: Prefs,
            @BackgroundPolicy policy: String? = null,
            killApp: Boolean? = null,
            executionDelay: Int? = null
        ): BackgroundManager.Builder.Policy {
            val builder = BackgroundManager.Builder()
            return when (policy ?: LibraryPrefs.getBackgroundManagerPolicySetting(prefs)) {
                BackgroundPolicy.RUN_IN_FOREGROUND -> {
                    builder.runServiceInForeground(
                        killApp ?: LibraryPrefs.getBackgroundManagerKillAppSetting(prefs)
                    )
                }
                BackgroundPolicy.RESPECT_RESOURCES -> {
                    builder.respectResourcesWhileInBackground(
                        executionDelay ?: LibraryPrefs.getBackgroundManagerExecuteDelaySetting(prefs)
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
        fun setupTorServices(
            application: Application,
            serviceNotificationBuilder: ServiceNotification.Builder,
            backgroundManagerPolicy: BackgroundManager.Builder.Policy,
            disableNetworkDelay: Long,
            restartTimeDelay: Long,
            stopServiceTimeDelay: Long,
            stopServiceOnTaskRemoved: Boolean,
            buildConfigDebug: Boolean
        ): TorServiceController.Builder {
            return TorServiceController.Builder(
                application = application,
                torServiceNotificationBuilder = serviceNotificationBuilder,
                backgroundManagerPolicy = backgroundManagerPolicy,
                buildConfigVersionCode = BuildConfig.VERSION_CODE,
                defaultTorSettings = MyTorSettings(),
                geoipAssetPath = "common/geoip",
                geoip6AssetPath = "common/geoip6"
            )
                .addTimeToDisableNetworkDelay(disableNetworkDelay)
                .addTimeToRestartTorDelay(restartTimeDelay)
                .addTimeToStopServiceDelay(stopServiceTimeDelay)
                .disableStopServiceOnTaskRemoved(stopServiceOnTaskRemoved)
                .setBuildConfigDebug(buildConfigDebug)
                .setEventBroadcaster(eventBroadcaster = MyEventBroadcaster())
        }
    }

    override fun onCreate() {
        super.onCreate()
        val prefs = Prefs.createUnencrypted(PREFS_NAME, this)

        val serviceNotificationBuilder = generateTorServiceNotificationBuilder(
            LibraryPrefs.getNotificationVisibilitySetting(prefs),
            LibraryPrefs.getNotificationColorSetting(prefs),
            LibraryPrefs.getNotificationRestartEnableSetting(prefs),
            LibraryPrefs.getNotificationStopEnableSetting(prefs),
            LibraryPrefs.getNotificationShowSetting(prefs)
        )

        stopTorDelaySettingAtAppStartup = LibraryPrefs.getControllerStopDelaySetting(prefs).toString()


        val builder = setupTorServices(
                this,
                serviceNotificationBuilder,
                generateBackgroundManagerPolicy(prefs),
                LibraryPrefs.getControllerDisableNetworkDelay(prefs),
                LibraryPrefs.getControllerRestartDelaySetting(prefs),
                stopTorDelaySettingAtAppStartup.toLong(),
                LibraryPrefs.getControllerDisableStopServiceOnTaskRemovedSetting(prefs),
                LibraryPrefs.getControllerBuildConfigDebugSetting(prefs)
            )
        try {
            builder.build()
        } catch (e: Exception) {
            e.message?.let {
                DashboardFragment.showMessage(
                    DashMessage(
                        "${DashMessage.EXCEPTION}$it",
                        R.drawable.dash_message_color_red,
                        5_000
                    )
                )
            }

            // Would not normally need any of this, but b/c the sample application allows for
            // modifying these settings to show off capabilities, it's necessary.
            setupTorServices(
                this,
                serviceNotificationBuilder,
                generateBackgroundManagerPolicy(prefs, BackgroundPolicy.RUN_IN_FOREGROUND, true),
                LibraryPrefs.getControllerDisableNetworkDelay(prefs),
                LibraryPrefs.getControllerRestartDelaySetting(prefs),
                stopTorDelaySettingAtAppStartup.toLong(),
                LibraryPrefs.getControllerDisableStopServiceOnTaskRemovedSetting(prefs),
                LibraryPrefs.getControllerBuildConfigDebugSetting(prefs)
            ).build()
        }

        TorServiceController.appEventBroadcaster?.let {
            (it as MyEventBroadcaster).broadcastLogMessage(
                "SampleApp|Application|Process ID: ${Process.myPid()}"
            )
        }
    }
}