package io.matthewnelson.sampleapp.ui.fragments.settings.library

import android.content.Context
import android.text.InputFilter
import android.view.View
import android.widget.*
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.R

class BackgroundManagerOptions(view: View, prefs: Prefs) {

    private companion object {
        // Spinner BackgroundManager Policy
        const val RESPECT_RESOURCES = "Stop Service After"
        const val FOREGROUND = "Run In Foreground"

        const val KILL_APP = "Kill App If Task Removed"
        const val NO_KILL_APP = "Don't Kill Application"
    }

    private var initialPolicy: String = LibraryPrefs.getBackgroundManagerPolicySetting(prefs)
    var policy: String = initialPolicy
        private set

    private var initialExecutionDelay: Int = LibraryPrefs.getBackgroundManagerExecuteDelaySetting(prefs)

    private var initialKillApp: Boolean = LibraryPrefs.getBackgroundManagerKillAppSetting(prefs)
    var killApp: Boolean = initialKillApp
        private set

    fun saveSettings(context: Context, prefs: Prefs): Boolean? {
        var somethingChanged = false

        when (policy) {
            LibraryPrefs.BACKGROUND_MANAGER_POLICY_RESPECT -> {
                val executionDelay = getExecutionDelay(context) ?: return null
                if (executionDelay != initialExecutionDelay) {
                    prefs.write(LibraryPrefs.BACKGROUND_MANAGER_EXECUTE_DELAY, executionDelay)
                    initialExecutionDelay = executionDelay
                    somethingChanged = true
                }
            }
            LibraryPrefs.BACKGROUND_MANAGER_POLICY_FOREGROUND -> {
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

    fun getExecutionDelay(context: Context): Int? {
        var executionDelay: Int? = null
        try {
            val value = editTextDelay.text.toString().toInt()
            if (value in 5..45)
                executionDelay = value
        } catch (e: Exception) {}

        if (executionDelay == null)
            Toast.makeText(
                context, "Execution Delay must be between 5 and 45 seconds", Toast.LENGTH_SHORT
            ).show()

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
            LibraryPrefs.BACKGROUND_MANAGER_POLICY_RESPECT -> {
                executionDelayOptionVisibility(true)
                spinnerPolicy.setSelection(0)
            }
            LibraryPrefs.BACKGROUND_MANAGER_POLICY_FOREGROUND -> {
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
                            LibraryPrefs.BACKGROUND_MANAGER_POLICY_RESPECT
                        }
                        FOREGROUND -> {
                            executionDelayOptionVisibility(false)
                            LibraryPrefs.BACKGROUND_MANAGER_POLICY_FOREGROUND
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