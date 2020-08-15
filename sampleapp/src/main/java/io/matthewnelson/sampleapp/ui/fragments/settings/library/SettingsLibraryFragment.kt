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
package io.matthewnelson.sampleapp.ui.fragments.settings.library

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.App
import io.matthewnelson.sampleapp.R
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashboardFragment
import io.matthewnelson.topl_service.lifecycle.BackgroundManager

class SettingsLibraryFragment : Fragment() {

    private lateinit var notificationOptions: NotificationOptions
    private lateinit var backgroundManagerOptions: BackgroundManagerOptions
    private lateinit var controllerOptions: ControllerOptions
    private lateinit var prefs: Prefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs.createUnencrypted(App.PREFS_NAME, view.context)
        notificationOptions = NotificationOptions(view, prefs)
        backgroundManagerOptions = BackgroundManagerOptions(view, prefs)
        controllerOptions = ControllerOptions(view, prefs)

        view.findViewById<Button>(R.id.settings_library_button_save).setOnClickListener {
            saveSettings(view.context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun isBackgroundManagerPolicyRespectResources(): Boolean =
        backgroundManagerOptions.policy == LibraryPrefs.BACKGROUND_MANAGER_POLICY_RESPECT

    private fun saveSettings(context: Context) {

        // Will return null if outside of the range 5 to 45 and a toast is displayed
        if (isBackgroundManagerPolicyRespectResources())
            backgroundManagerOptions.getExecutionDelay(context) ?: return

        // Ensure settings chosen are compatible before saving to prefs
        val notificationBuilder = App.generateTorServiceNotificationBuilder(
            notificationOptions.visibility,
            notificationOptions.iconColor,
            notificationOptions.enableRestart,
            notificationOptions.enableStop,
            notificationOptions.show
        )

        val backgroundManagerPolicy = if (isBackgroundManagerPolicyRespectResources())
            BackgroundManager.Builder().respectResourcesWhileInBackground(
                backgroundManagerOptions.getExecutionDelay(context)
            )
        else
            BackgroundManager.Builder().runServiceInForeground(
                backgroundManagerOptions.killApp
            )

        try {
            App.setupTorServices(
                context.applicationContext as Application,
                notificationBuilder,
                backgroundManagerPolicy,
                controllerOptions.getRestartDelayValue(),
                controllerOptions.getStopDelayTime(),
                controllerOptions.disableStopServiceOnTaskRemoved,
                controllerOptions.buildConfigDebug
            )
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            return
        }

        val bmChanges = backgroundManagerOptions.saveSettings(context, prefs) ?: return
        val nChanges = notificationOptions.saveSettings(prefs)
        val cChanges = controllerOptions.saveSettings(prefs)

        if (bmChanges || nChanges || cChanges) {
            DashboardFragment.librarySettingsWereChanged()
            Toast.makeText(context, "Settings Saved", Toast.LENGTH_SHORT).show()
        }
    }
}