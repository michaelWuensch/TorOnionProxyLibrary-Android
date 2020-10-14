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
package io.matthewnelson.sampleapp.ui.fragments.settings.library.components

import android.content.Context
import android.text.InputFilter
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.R
import kotlin.math.absoluteValue

class ControllerOptions(view: View, prefs: Prefs) {

    companion object {
        const val DO_NOT_STOP_SERVICE = "Don't Stop Service"
        const val STOP_SERVICE = "Stop Service"

        const val DEBUG = "Debug"
        const val RELEASE = "Release"
    }

    private var initialDisableNetworkDelayTime: Long =
        LibraryPrefs.getControllerDisableNetworkDelay(prefs)

    private var initialRestartDelayTime: Long =
        LibraryPrefs.getControllerRestartDelaySetting(prefs)

    private var initialStopDelayTime: Long =
        LibraryPrefs.getControllerStopDelaySetting(prefs)

    private var initialDisableStopServiceOnTaskRemoved: Boolean =
        LibraryPrefs.getControllerDisableStopServiceOnTaskRemovedSetting(prefs)

    var disableStopServiceOnTaskRemoved: Boolean = initialDisableStopServiceOnTaskRemoved
        private set

    private var initialBuildConfigDebug: Boolean =
        LibraryPrefs.getControllerBuildConfigDebugSetting(prefs)

    var buildConfigDebug: Boolean = initialBuildConfigDebug
        private set

    fun saveSettings(prefs: Prefs): Boolean {
        var somethingChanged = false
        if (getDisableNetworkDelayValue() != initialDisableNetworkDelayTime) {
            prefs.write(LibraryPrefs.CONTROLLER_DISABLE_NETWORK_DELAY, getDisableNetworkDelayValue())
            initialDisableNetworkDelayTime = getDisableNetworkDelayValue()
            somethingChanged = true
        }
        if (getRestartDelayValue() != initialRestartDelayTime) {
            prefs.write(LibraryPrefs.CONTROLLER_RESTART_DELAY, getRestartDelayValue())
            initialRestartDelayTime = getRestartDelayValue()
            somethingChanged = true
        }
        if (getStopDelayTime() != initialStopDelayTime) {
            prefs.write(LibraryPrefs.CONTROLLER_STOP_DELAY, getStopDelayTime())
            initialStopDelayTime = getStopDelayTime()
            somethingChanged = true
        }
        if (disableStopServiceOnTaskRemoved != initialDisableStopServiceOnTaskRemoved) {
            prefs.write(LibraryPrefs.CONTROLLER_DISABLE_STOP_SERVICE_TASK_REMOVED, disableStopServiceOnTaskRemoved)
            initialDisableStopServiceOnTaskRemoved = disableStopServiceOnTaskRemoved
            somethingChanged = true
        }
        if (buildConfigDebug != initialBuildConfigDebug) {
            prefs.write(LibraryPrefs.CONTROLLER_BUILD_CONFIG_DEBUG, buildConfigDebug)
            initialBuildConfigDebug = buildConfigDebug
            somethingChanged = true
        }
        return somethingChanged
    }

    fun getDisableNetworkDelayValue(): Long =
        getEditTextValueLong(editTextDisableNetworkDelay)

    fun getRestartDelayValue(): Long =
        getEditTextValueLong(editTextRestartDelay)

    fun getStopDelayTime(): Long =
        getEditTextValueLong(editTextStopDelay)

    private fun getEditTextValueLong(editText: EditText): Long {
        return try {
            editText.text.toString().toLong().absoluteValue
        } catch (e: Exception) {
            0L
        }
    }

    private lateinit var editTextDisableNetworkDelay: EditText
    private lateinit var editTextRestartDelay: EditText
    private lateinit var editTextStopDelay: EditText
    private lateinit var spinnerDisableStopOnTaskRemoved: Spinner
    private lateinit var adapterDisableStopOnTaskRemoved: ArrayAdapter<String>
    private lateinit var spinnerBuildConfig: Spinner
    private lateinit var adapterBuildConfig: ArrayAdapter<String>

    init {
        findViews(view)
        initEditTextViews()
        initSpinnerControllerDisableStopOnTaskRemoved(view.context)
        initSpinnerControllerBuildConfig(view.context)
    }

    private fun findViews(view: View) {
        editTextDisableNetworkDelay = view.findViewById(R.id.settings_library_edit_text_controller_disable_network_delay)
        editTextRestartDelay = view.findViewById(R.id.settings_library_edit_text_controller_restart_delay)
        editTextStopDelay = view.findViewById(R.id.settings_library_edit_text_controller_stop_delay)
        spinnerDisableStopOnTaskRemoved = view.findViewById(R.id.settings_library_spinner_controller_stop_on_task_removed)
        spinnerBuildConfig = view.findViewById(R.id.settings_library_spinner_controller_build_config)
    }

    private fun initEditTextViews() {
        editTextDisableNetworkDelay.filters = arrayOf(InputFilter.LengthFilter(4))
        if (initialDisableNetworkDelayTime > 0L)
            editTextDisableNetworkDelay.setText(initialDisableNetworkDelayTime.toString())

        editTextRestartDelay.filters = arrayOf(InputFilter.LengthFilter(4))
        if (initialRestartDelayTime > 0L)
            editTextRestartDelay.setText(initialRestartDelayTime.toString())

        editTextStopDelay.filters = arrayOf(InputFilter.LengthFilter(4))
        if (initialStopDelayTime > 0L)
            editTextStopDelay.setText(initialStopDelayTime.toString())
    }

    private fun initSpinnerControllerDisableStopOnTaskRemoved(context: Context) {
        val categoryDisable = arrayOf(
            DO_NOT_STOP_SERVICE,
            STOP_SERVICE
        )
        adapterDisableStopOnTaskRemoved = ArrayAdapter(context, R.layout.spinner_list_item, categoryDisable)
        adapterDisableStopOnTaskRemoved.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDisableStopOnTaskRemoved.adapter = adapterDisableStopOnTaskRemoved
        setControllerDisableStopOnTaskRemovedSpinnerValue()
        spinnerDisableStopOnTaskRemoved.onItemSelectedListener = ControllerOptionSelectionListener()
    }

    private fun setControllerDisableStopOnTaskRemovedSpinnerValue() {
        when (initialDisableStopServiceOnTaskRemoved) {
            true -> {
                spinnerDisableStopOnTaskRemoved.setSelection(0)
            }
            false -> {
                spinnerDisableStopOnTaskRemoved.setSelection(1)
            }
        }
    }

    private fun initSpinnerControllerBuildConfig(context: Context) {
        val categoryDebug = arrayOf(
            DEBUG,
            RELEASE
        )
        adapterBuildConfig = ArrayAdapter(context, R.layout.spinner_list_item, categoryDebug)
        adapterBuildConfig.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBuildConfig.adapter = adapterBuildConfig
        setControllerBuildConfigSpinnerValue()
        spinnerBuildConfig.onItemSelectedListener = ControllerOptionSelectionListener()
    }

    private fun setControllerBuildConfigSpinnerValue() {
        when (initialBuildConfigDebug) {
            true -> {
                spinnerBuildConfig.setSelection(0)
            }
            false -> {
                spinnerBuildConfig.setSelection(1)
            }
        }
    }

    private inner class ControllerOptionSelectionListener: AdapterView.OnItemSelectedListener {
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
                adapterDisableStopOnTaskRemoved -> {
                    disableStopServiceOnTaskRemoved = when (item.toString()) {
                        DO_NOT_STOP_SERVICE -> {
                            true
                        }
                        STOP_SERVICE -> {
                            false
                        }
                        else -> {
                            return
                        }
                    }
                }
                adapterBuildConfig -> {
                    buildConfigDebug = when (item.toString()) {
                        DEBUG -> {
                            true
                        }
                        RELEASE -> {
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