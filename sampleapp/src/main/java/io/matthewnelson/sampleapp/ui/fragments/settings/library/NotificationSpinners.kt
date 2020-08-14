package io.matthewnelson.sampleapp.ui.fragments.settings.library

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.NotificationCompat
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.App
import io.matthewnelson.sampleapp.R

class NotificationSpinners(view: View) {

    private companion object {
        // Spinner Notification Visibility
        const val PRIVATE = "Private"
        const val PUBLIC = "Public"
        const val SECRET = "Secret"

        // Spinner Notification Color
        const val NONE = "None"
        const val TOR_PURPLE = "Tor Purple"
        const val YELLOW = "Yellow"
        const val GREEN = "Green"
        const val BLUE = "Blue"

        // Spinner Notification Buttons
        const val ENABLED = "Enabled"
        const val DISABLED = "Disabled"

        // Spinner Notification Show
        const val SHOW = "Show"
        const val HIDE = "Hide"
    }

    private val prefs: Prefs
        get() = App.prefs

    private val initialVisibility: Int = LibraryPrefs.getNotificationVisibilitySetting()
    var visibility: Int = initialVisibility
        private set

    private val initialIconColor: Int = LibraryPrefs.getNotificationColorSetting()
    var iconColor: Int = initialIconColor
        private set

    private val initialEnableRestart: Boolean = LibraryPrefs.getNotificationRestartEnableSetting()
    var enableRestart: Boolean = initialEnableRestart
        private set

    private val initialEnableStop: Boolean = LibraryPrefs.getNotificationStopEnableSetting()
    var enableStop: Boolean = initialEnableStop
        private set

    private val initialShow: Boolean = LibraryPrefs.getNotificationShowSetting()
    var show: Boolean = initialShow
        private set

    fun saveSettings(): Boolean {
        var somethingChanged = false
        if (visibility != initialVisibility) {
            prefs.write(LibraryPrefs.NOTIFICATION_VISIBILITY, visibility)
            somethingChanged = true
        }
        if (iconColor != initialIconColor) {
            prefs.write(LibraryPrefs.NOTIFICATION_COLOR_RESOURCE, iconColor)
            somethingChanged = true
        }
        if (enableRestart != initialEnableRestart) {
            prefs.write(LibraryPrefs.NOTIFICATION_ENABLE_RESTART, enableRestart)
            somethingChanged = true
        }
        if (enableStop != initialEnableStop) {
            prefs.write(LibraryPrefs.NOTIFICATION_ENABLE_STOP, enableStop)
            somethingChanged = true
        }
        if (show != initialShow) {
            prefs.write(LibraryPrefs.NOTIFICATION_SHOW, show)
            somethingChanged = true
        }
        return somethingChanged
    }

    private lateinit var spinnerVisibility: Spinner
    private lateinit var adapterVisibility: ArrayAdapter<String>
    private lateinit var spinnerColor: Spinner
    private lateinit var adapterColor: ArrayAdapter<String>
    private lateinit var spinnerRestart: Spinner
    private lateinit var adapterRestart: ArrayAdapter<String>
    private lateinit var spinnerStop: Spinner
    private lateinit var adapterStop: ArrayAdapter<String>
    private lateinit var spinnerShow: Spinner
    private lateinit var adapterShow: ArrayAdapter<String>

    init {
        findViews(view)
        initSpinnerNotificationVisibility(view.context)
        initSpinnerNotificationColor(view.context)
        initSpinnerNotificationRestartButton(view.context)
        initSpinnerNotificationStopButton(view.context)
        initSpinnerNotificationShow(view.context)
    }

    private fun findViews(view: View) {
        spinnerVisibility = view.findViewById(R.id.settings_library_spinner_notification_visibility)
        spinnerColor = view.findViewById(R.id.settings_library_spinner_notification_icon_color)
        spinnerRestart = view.findViewById(R.id.settings_library_spinner_notification_restart_button)
        spinnerStop = view.findViewById(R.id.settings_library_spinner_notification_stop_button)
        spinnerShow = view.findViewById(R.id.settings_library_spinner_notification_show)
    }

    private fun initSpinnerNotificationVisibility(context: Context) {
        val categoryVisibility = arrayOf(
            PRIVATE,
            PUBLIC,
            SECRET
        )
        adapterVisibility = ArrayAdapter(context, R.layout.spinner_list_item, categoryVisibility)
        adapterVisibility.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerVisibility.adapter = adapterVisibility
        setNotificationVisibilitySpinnerValue()
        spinnerVisibility.onItemSelectedListener = NotificationOptionSelectionListener()
    }

    private fun setNotificationVisibilitySpinnerValue() {
        when (initialVisibility) {
            NotificationCompat.VISIBILITY_PRIVATE -> {
                spinnerVisibility.setSelection(0)
            }
            NotificationCompat.VISIBILITY_PUBLIC -> {
                spinnerVisibility.setSelection(1)
            }
            NotificationCompat.VISIBILITY_SECRET -> {
                spinnerVisibility.setSelection(2)
            }
        }
    }

    private fun initSpinnerNotificationColor(context: Context) {
        val categoryColor = arrayOf(
            NONE,
            TOR_PURPLE,
            YELLOW,
            GREEN,
            BLUE
        )
        adapterColor = ArrayAdapter(context, R.layout.spinner_list_item, categoryColor)
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerColor.adapter = adapterColor
        setNotificationColorSpinnerValue()
        spinnerColor.onItemSelectedListener = NotificationOptionSelectionListener()
    }

    private fun setNotificationColorSpinnerValue() {
        when (initialIconColor) {
            R.color.tor_service_white -> {
                spinnerColor.setSelection(0)
            }
            R.color.primaryColor -> {
                spinnerColor.setSelection(1)
            }
            R.color.yellow -> {
                spinnerColor.setSelection(2)
            }
            R.color.green -> {
                spinnerColor.setSelection(3)
            }
            R.color.blue -> {
                spinnerColor.setSelection(4)
            }
        }
    }

    private fun initSpinnerNotificationRestartButton(context: Context) {
        val categoryEnabledDisabled = arrayOf(
            ENABLED,
            DISABLED
        )
        adapterRestart = ArrayAdapter(context, R.layout.spinner_list_item, categoryEnabledDisabled)
        adapterRestart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRestart.adapter = adapterRestart
        setNotificationRestartButtonSpinnerValue()
        spinnerRestart.onItemSelectedListener = NotificationOptionSelectionListener()
    }

    private fun setNotificationRestartButtonSpinnerValue() {
        when (initialEnableRestart) {
            true -> {
                spinnerRestart.setSelection(0)
            }
            false -> {
                spinnerRestart.setSelection(1)
            }
        }
    }

    private fun initSpinnerNotificationStopButton(context: Context) {
        val categoryEnabledDisabled = arrayOf(
            ENABLED,
            DISABLED
        )
        adapterStop = ArrayAdapter(context, R.layout.spinner_list_item, categoryEnabledDisabled)
        adapterStop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStop.adapter = adapterStop
        setNotificationStopButtonSpinnerValue()
        spinnerStop.onItemSelectedListener = NotificationOptionSelectionListener()
    }

    private fun setNotificationStopButtonSpinnerValue() {
        when (initialEnableStop) {
            true -> {
                spinnerStop.setSelection(0)
            }
            false -> {
                spinnerStop.setSelection(1)
            }
        }
    }

    private fun initSpinnerNotificationShow(context: Context) {
        val categoryEnabledDisabled = arrayOf(
            SHOW,
            HIDE
        )
        adapterShow = ArrayAdapter(context, R.layout.spinner_list_item, categoryEnabledDisabled)
        adapterShow.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerShow.adapter = adapterShow
        setNotificationShowSpinnerValue()
        spinnerShow.onItemSelectedListener = NotificationOptionSelectionListener()
    }

    private fun setNotificationShowSpinnerValue() {
        when (initialShow) {
            true -> {
                spinnerShow.setSelection(0)
            }
            false -> {
                spinnerShow.setSelection(1)
            }
        }
    }

    private inner class NotificationOptionSelectionListener: AdapterView.OnItemSelectedListener {
        var count = 0
            private set

        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (count == 0) {
                count++
                return
            }

            val item = parent?.getItemAtPosition(position) ?: return

            when (parent.adapter) {
                adapterVisibility -> {
                    visibility = when (item.toString()) {
                        PRIVATE -> {
                            NotificationCompat.VISIBILITY_PRIVATE
                        }
                        PUBLIC -> {
                            NotificationCompat.VISIBILITY_PUBLIC
                        }
                        SECRET -> {
                            NotificationCompat.VISIBILITY_SECRET
                        }
                        else -> {
                            return
                        }
                    }
                }
                adapterColor -> {
                    iconColor = when (item.toString()) {
                        NONE -> {
                            R.color.tor_service_white
                        }
                        TOR_PURPLE -> {
                            R.color.primaryColor
                        }
                        YELLOW -> {
                            R.color.yellow
                        }
                        GREEN -> {
                            R.color.green
                        }
                        BLUE -> {
                            R.color.blue
                        }
                        else -> {
                            return
                        }
                    }
                }
                adapterRestart -> {
                    enableRestart = when (item.toString()) {
                        ENABLED -> {
                            true
                        }
                        DISABLED -> {
                            false
                        }
                        else -> {
                            return
                        }
                    }
                }
                adapterStop -> {
                    enableStop = when (item.toString()) {
                        ENABLED -> {
                            true
                        }
                        DISABLED -> {
                            false
                        }
                        else -> {
                            return
                        }
                    }
                }
                adapterShow -> {
                    show = when (item.toString()) {
                        SHOW -> {
                            true
                        }
                        HIDE -> {
                            false
                        }
                        else -> {
                            return
                        }
                    }
                }
            }
            // TODO: add restart app popup
        }
    }
}