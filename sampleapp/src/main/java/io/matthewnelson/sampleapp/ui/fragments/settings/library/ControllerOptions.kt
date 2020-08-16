package io.matthewnelson.sampleapp.ui.fragments.settings.library

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

    private var initialRestartDelayTime: Long = LibraryPrefs.getControllerRestartDelaySetting(prefs)

    private var initialStopDelayTime: Long = LibraryPrefs.getControllerStopDelaySetting(prefs)

    private var initialDisableStopServiceOnTaskRemoved: Boolean = LibraryPrefs.getControllerDisableStopServiceOnTaskRemovedSetting(prefs)
    var disableStopServiceOnTaskRemoved: Boolean = initialDisableStopServiceOnTaskRemoved
        private set

    private var initialBuildConfigDebug: Boolean = LibraryPrefs.getControllerBuildConfigDebugSetting(prefs)
    var buildConfigDebug: Boolean = initialBuildConfigDebug
        private set

    fun saveSettings(prefs: Prefs): Boolean {
        var somethingChanged = false
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
        editTextRestartDelay = view.findViewById(R.id.settings_library_edit_text_controller_restart_delay)
        editTextStopDelay = view.findViewById(R.id.settings_library_edit_text_controller_stop_delay)
        spinnerDisableStopOnTaskRemoved = view.findViewById(R.id.settings_library_spinner_controller_stop_on_task_removed)
        spinnerBuildConfig = view.findViewById(R.id.settings_library_spinner_controller_build_config)
    }

    private fun initEditTextViews() {
        editTextRestartDelay.filters = arrayOf(InputFilter.LengthFilter(4))
        editTextRestartDelay.setText(initialRestartDelayTime.toString())

        editTextStopDelay.filters = arrayOf(InputFilter.LengthFilter(4))
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