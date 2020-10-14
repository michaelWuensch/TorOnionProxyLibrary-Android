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

import android.content.Context
import android.text.InputFilter
import android.view.View
import android.widget.*
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.R
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashMessage
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashboardFragment
import io.matthewnelson.topl_service_base.BaseServiceConsts.BackgroundPolicy

class BackgroundManagerOptions(view: View, prefs: Prefs) {

    companion object {
        // Spinner BackgroundManager Policy
        const val RESPECT_RESOURCES = "Stop Service After"
        const val FOREGROUND = "Run In Foreground"

        const val KILL_APP = "Kill App If Task Removed"
        const val NO_KILL_APP = "Don't Kill Application"
    }

    private var initialPolicy: String =
        LibraryPrefs.getBackgroundManagerPolicySetting(
            prefs
        )
    var policy: String = initialPolicy
        private set

    private var initialExecutionDelay: Int =
        LibraryPrefs.getBackgroundManagerExecuteDelaySetting(
            prefs
        )

    private var initialKillApp: Boolean =
        LibraryPrefs.getBackgroundManagerKillAppSetting(
            prefs
        )
    var killApp: Boolean = initialKillApp
        private set

    fun saveSettings(prefs: Prefs): Boolean? {
        var somethingChanged = false

        when (policy) {
            BackgroundPolicy.RESPECT_RESOURCES -> {
                val executionDelay = getExecutionDelay() ?: return null
                if (executionDelay != initialExecutionDelay) {
                    prefs.write(LibraryPrefs.BACKGROUND_MANAGER_EXECUTE_DELAY, executionDelay)
                    initialExecutionDelay = executionDelay
                    somethingChanged = true
                }
            }
            BackgroundPolicy.RUN_IN_FOREGROUND -> {
                if (killApp != initialKillApp) {
                    prefs.write(LibraryPrefs.BACKGROUND_MANAGER_KILL_APP, killApp)
                    initialKillApp = killApp
                    somethingChanged = true
                }
            }
        }

        if (policy != initialPolicy) {
            prefs.write(LibraryPrefs.BACKGROUND_MANAGER_POLICY, policy)
            initialPolicy = policy
            somethingChanged = true
        }

        return somethingChanged
    }

    fun getExecutionDelay(): Int? {
        var executionDelay: Int? = null
        try {
            val value = editTextDelay.text.toString().toInt()
            if (value in 5..45)
                executionDelay = value
        } catch (e: Exception) {}

        if (executionDelay == null)
            DashboardFragment.showMessage(
                DashMessage(
                    "${DashMessage.EXCEPTION}Execution Delay must be between 5 and 45 seconds",
                    R.drawable.dash_message_color_red,
                    5_000
                )
            )

        return executionDelay
    }

    private lateinit var spinnerPolicy: Spinner
    private lateinit var adapterPolicy: ArrayAdapter<String>
    private lateinit var textViewDelay: TextView
    private lateinit var editTextDelay: EditText
    private lateinit var textViewKillApp: TextView
    private lateinit var spinnerKillApp: Spinner
    private lateinit var adapterKillApp: ArrayAdapter<String>

    init {
        findViews(view)
        editTextDelay.filters = arrayOf(InputFilter.LengthFilter(2))
        editTextDelay.setText(initialExecutionDelay.toString())
        initSpinnerBackgroundManagerPolicy(view.context)
        initSpinnerBackgroundManagerKillApp(view.context)
    }

    private fun findViews(view: View) {
        spinnerPolicy = view.findViewById(R.id.settings_library_spinner_background_manager_policy)
        textViewDelay = view.findViewById(R.id.settings_library_text_view_background_manager_delay)
        editTextDelay = view.findViewById(R.id.settings_library_edit_text_background_manager_execution_delay)
        textViewKillApp = view.findViewById(R.id.settings_library_text_view_background_manager_kill_app)
        spinnerKillApp = view.findViewById(R.id.settings_library_spinner_background_manager_kill_app)
    }

    private fun initSpinnerBackgroundManagerPolicy(context: Context) {
        val categoryPolicy = arrayOf(
            RESPECT_RESOURCES,
            FOREGROUND
        )
        adapterPolicy = ArrayAdapter(context, R.layout.spinner_list_item, categoryPolicy)
        adapterPolicy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPolicy.adapter = adapterPolicy
        setBackgroundManagerPolicySpinnerValue()
        spinnerPolicy.onItemSelectedListener = BackgroundManagerOptionSelectionListener()
    }

    private fun setBackgroundManagerPolicySpinnerValue() {
        when (initialPolicy) {
            BackgroundPolicy.RESPECT_RESOURCES -> {
                executionDelayOptionVisibility(true)
                spinnerPolicy.setSelection(0)
            }
            BackgroundPolicy.RUN_IN_FOREGROUND -> {
                executionDelayOptionVisibility(false)
                spinnerPolicy.setSelection(1)
            }
        }
    }

    private fun initSpinnerBackgroundManagerKillApp(context: Context) {
        val categoryKillApp = arrayOf(
            KILL_APP,
            NO_KILL_APP
        )
        adapterKillApp = ArrayAdapter(context, R.layout.spinner_list_item, categoryKillApp)
        adapterKillApp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKillApp.adapter = adapterKillApp
        setBackgroundManagerKillAppSpinnerValue()
        spinnerKillApp.onItemSelectedListener = BackgroundManagerOptionSelectionListener()
    }

    private fun setBackgroundManagerKillAppSpinnerValue() {
        when (initialKillApp) {
            true -> {
                spinnerKillApp.setSelection(0)
            }
            false -> {
                spinnerKillApp.setSelection(1)
            }
        }
    }

    private fun executionDelayOptionVisibility(show: Boolean) {
        textViewDelay.visibility = if (show) View.VISIBLE else View.GONE
        editTextDelay.visibility = if (show) View.VISIBLE else View.GONE

        textViewKillApp.visibility = if (show) View.GONE else View.VISIBLE
        spinnerKillApp.visibility = if (show) View.GONE else View.VISIBLE
    }

    private inner class BackgroundManagerOptionSelectionListener: AdapterView.OnItemSelectedListener {
        var count = 0
            private set

        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (count == 0) {
                count ++
                return
            }

            val item = parent?.getItemAtPosition(position) ?: return

            when (parent.adapter) {
                adapterPolicy -> {
                    policy = when (item.toString()) {
                        RESPECT_RESOURCES -> {
                            executionDelayOptionVisibility(true)
                            BackgroundPolicy.RESPECT_RESOURCES
                        }
                        FOREGROUND -> {
                            executionDelayOptionVisibility(false)
                            BackgroundPolicy.RUN_IN_FOREGROUND
                        }
                        else -> {
                            return
                        }
                    }
                }
                adapterKillApp -> {
                    killApp = when (item.toString()) {
                        KILL_APP -> {
                            true
                        }
                        NO_KILL_APP -> {
                            false
                        }
                        else -> {
                            return
                        }
                    }
                }
            }
        }

    }
}