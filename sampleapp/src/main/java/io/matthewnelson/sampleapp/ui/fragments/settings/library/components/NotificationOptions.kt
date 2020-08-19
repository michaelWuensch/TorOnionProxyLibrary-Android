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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.NotificationCompat
import io.matthewnelson.encrypted_storage.Prefs
import io.matthewnelson.sampleapp.R

class NotificationOptions(view: View, prefs: Prefs) {

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

    private var initialVisibility: Int = LibraryPrefs.getNotificationVisibilitySetting(prefs)
    var visibility: Int = initialVisibility
        private set

    private var initialIconColor: Int = LibraryPrefs.getNotificationColorSetting(prefs)
    var iconColor: Int = initialIconColor
        private set

    private var initialEnableRestart: Boolean = LibraryPrefs.getNotificationRestartEnableSetting(prefs)
    var enableRestart: Boolean = initialEnableRestart
        private set

    private var initialEnableStop: Boolean = LibraryPrefs.getNotificationStopEnableSetting(prefs)
    var enableStop: Boolean = initialEnableStop
        private set

    private var initialShow: Boolean = LibraryPrefs.getNotificationShowSetting(prefs)
    var show: Boolean = initialShow
        private set

    fun saveSettings(prefs: Prefs): Boolean {
        var somethingChanged = false
        if (visibility != initialVisibility) {
            prefs.write(LibraryPrefs.NOTIFICATION_VISIBILITY, visibility)
            initialVisibility = visibility
            somethingChanged = true
        }
        if (iconColor != initialIconColor) {
            prefs.write(LibraryPrefs.NOTIFICATION_COLOR_RESOURCE, iconColor)
            initialIconColor = iconColor
            somethingChanged = true
        }
        if (enableRestart != initialEnableRestart) {
            prefs.write(LibraryPrefs.NOTIFICATION_ENABLE_RESTART, enableRestart)
            initialEnableRestart = enableRestart
            somethingChanged = true
        }
        if (enableStop != initialEnableStop) {
            prefs.write(LibraryPrefs.NOTIFICATION_ENABLE_STOP, enableStop)
            initialEnableStop = enableStop
            somethingChanged = true
        }
        if (show != initialShow) {
            prefs.write(LibraryPrefs.NOTIFICATION_SHOW, show)
            initialShow = show
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