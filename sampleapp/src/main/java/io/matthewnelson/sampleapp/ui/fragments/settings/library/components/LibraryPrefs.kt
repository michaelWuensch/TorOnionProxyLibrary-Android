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
package io.matthewnelson.sampleapp.ui.fragments.settings.library.components

import androidx.core.app.NotificationCompat
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.BuildConfig
import io.matthewnelson.sampleapp.R
import io.matthewnelson.topl_service_base.BaseServiceConsts.BackgroundPolicy

object LibraryPrefs {

    // Notification Keys
    const val NOTIFICATION_VISIBILITY = "NOTIFICATION_VISIBILITY"
    const val NOTIFICATION_COLOR_RESOURCE = "NOTIFICATION_COLOR_RESOURCE"
    const val NOTIFICATION_ENABLE_RESTART = "NOTIFICATION_ENABLE_RESTART"
    const val NOTIFICATION_ENABLE_STOP = "NOTIFICATION_ENABLE_STOP"
    const val NOTIFICATION_SHOW = "NOTIFICATION_SHOW"

    // BackgroundManager Keys
    const val BACKGROUND_MANAGER_POLICY = "BACKGROUND_MANAGER_POLICY"
    const val BACKGROUND_MANAGER_EXECUTE_DELAY = "BACKGROUND_MANAGER_EXECUTE_DELAY"
    const val BACKGROUND_MANAGER_KILL_APP = "BACKGROUND_MANAGER_KILL_APP"

    // Controller Keys
    const val CONTROLLER_BUILD_CONFIG_DEBUG = "CONTROLLER_BUILD_CONFIG_DEBUG"
    const val CONTROLLER_DISABLE_NETWORK_DELAY = "CONTROLLER_DISABLE_NETWORK_DELAY"
    const val CONTROLLER_RESTART_DELAY = "CONTROLLER_RESTART_DELAY"
    const val CONTROLLER_STOP_DELAY = "CONTROLLER_STOP_DELAY"
    const val CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED = "CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED"

    // Notification Settings
    fun getNotificationVisibilitySetting(prefs: Prefs): Int =
        prefs.read(NOTIFICATION_VISIBILITY, NotificationCompat.VISIBILITY_PRIVATE)

    fun getNotificationColorSetting(prefs: Prefs): Int =
        prefs.read(NOTIFICATION_COLOR_RESOURCE, R.color.primaryColor)

    fun getNotificationRestartEnableSetting(prefs: Prefs): Boolean =
        prefs.read(NOTIFICATION_ENABLE_RESTART, true)

    fun getNotificationStopEnableSetting(prefs: Prefs): Boolean =
        prefs.read(NOTIFICATION_ENABLE_STOP, true)

    fun getNotificationShowSetting(prefs: Prefs): Boolean =
        prefs.read(NOTIFICATION_SHOW, true)

    // Background Manager Settings
    fun getBackgroundManagerPolicySetting(prefs: Prefs): String =
        prefs.read(BACKGROUND_MANAGER_POLICY, BackgroundPolicy.RESPECT_RESOURCES) ?: BackgroundPolicy.RESPECT_RESOURCES

    fun getBackgroundManagerExecuteDelaySetting(prefs: Prefs): Int =
        prefs.read(BACKGROUND_MANAGER_EXECUTE_DELAY, 20)

    fun getBackgroundManagerKillAppSetting(prefs: Prefs): Boolean =
        prefs.read(BACKGROUND_MANAGER_KILL_APP, true)

    // Controller Settings
    fun getControllerDisableNetworkDelay(prefs: Prefs): Long =
        prefs.read(CONTROLLER_DISABLE_NETWORK_DELAY, 0L)

    fun getControllerRestartDelaySetting(prefs: Prefs): Long =
        prefs.read(CONTROLLER_RESTART_DELAY, 100L)

    fun getControllerStopDelaySetting(prefs: Prefs): Long =
        prefs.read(CONTROLLER_STOP_DELAY, 100L)

    fun getControllerDisableStopServiceOnTaskRemovedSetting(prefs: Prefs): Boolean =
        prefs.read(CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED, false)

    fun getControllerBuildConfigDebugSetting(prefs: Prefs): Boolean =
        prefs.read(CONTROLLER_BUILD_CONFIG_DEBUG, BuildConfig.DEBUG)
}