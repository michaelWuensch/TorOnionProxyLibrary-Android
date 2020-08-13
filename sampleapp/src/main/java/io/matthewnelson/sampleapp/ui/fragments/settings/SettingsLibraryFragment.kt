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
package io.matthewnelson.sampleapp.ui.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.App
import io.matthewnelson.sampleapp.BuildConfig
import io.matthewnelson.sampleapp.R

class SettingsLibraryFragment : Fragment() {

    companion object {
        private val prefs: Prefs
            get() = App.prefs

        // Notification Keys
        private const val NOTIFICATION_VISIBILITY = "NOTIFICATION_VISIBILITY"
        private const val NOTIFICATION_COLOR_RESOURCE = "NOTIFICATION_COLOR_RESOURCE"
        private const val NOTIFICATION_ENABLE_RESTART = "NOTIFICATION_ENABLE_RESTART"
        private const val NOTIFICATION_ENABLE_STOP = "NOTIFICATION_ENABLE_STOP"
        private const val NOTIFICATION_SHOW = "NOTIFICATION_SHOW"

        fun getNotificationVisibilitySetting(): Int =
            prefs.read(NOTIFICATION_VISIBILITY, NotificationCompat.VISIBILITY_PRIVATE)

        fun getNotificationColorSetting(): Int =
            prefs.read(NOTIFICATION_COLOR_RESOURCE, R.color.primaryColor)

        fun getNotificationRestartEnableSetting(): Boolean =
            prefs.read(NOTIFICATION_ENABLE_RESTART, true)

        fun getNotificationStopEnableSetting(): Boolean =
            prefs.read(NOTIFICATION_ENABLE_STOP, true)

        fun getNotificationShowSetting(): Boolean =
            prefs.read(NOTIFICATION_SHOW, true)

        // BackgroundManager Keys
        private const val BACKGROUND_MANAGER_POLICY = "BACKGROUND_MANAGER_POLICY"
        private const val BACKGROUND_MANAGER_EXECUTE_DELAY = "BACKGROUND_MANAGER_EXECUTE_DELAY"
        private const val BACKGROUND_MANAGER_KILL_APP = "BACKGROUND_MANAGER_KILL_APP"

        // values
        const val BACKGROUND_MANAGER_POLICY_RESPECT = "BACKGROUND_MANAGER_POLICY_RESPECT"
        const val BACKGROUND_MANAGER_POLICY_FOREGROUND = "BACKGROUND_MANAGER_POLICY_FOREGROUND"

        fun getBackgroundManagerPolicySetting(): String? =
            prefs.read(BACKGROUND_MANAGER_POLICY, Prefs.INVALID_STRING)

        fun getBackgroundManagerExecuteDelaySetting(): Int =
            prefs.read(BACKGROUND_MANAGER_EXECUTE_DELAY, 20)

        fun getBackgroundManagerKillAppSetting(): Boolean =
            prefs.read(BACKGROUND_MANAGER_KILL_APP, true)

        // Controller Keys
        private const val CONTROLLER_BUILD_CONFIG_DEBUG = "CONTROLLER_BUILD_CONFIG_DEBUG"
        private const val CONTROLLER_RESTART_DELAY = "CONTROLLER_RESTART_DELAY"
        private const val CONTROLLER_STOP_DELAY = "CONTROLLER_STOP_DELAY"
        private const val CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED = "CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED"

        fun getControllerRestartDelaySetting(): Long =
            prefs.read(CONTROLLER_RESTART_DELAY, 100L)

        fun getControllerStopDelaySetting(): Long =
            prefs.read(CONTROLLER_STOP_DELAY, 100L)

        fun getControllerStopServiceOnTaskRemovedSetting(): Boolean =
            prefs.read(CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED, false)

        fun getControllerBuildConfigDebugSetting(): Boolean =
            prefs.read(CONTROLLER_BUILD_CONFIG_DEBUG, BuildConfig.DEBUG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}