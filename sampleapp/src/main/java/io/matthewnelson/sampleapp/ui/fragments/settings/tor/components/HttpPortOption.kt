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
package io.matthewnelson.sampleapp.ui.fragments.settings.tor.components

import android.content.Context
import android.text.InputFilter
import android.view.View
import android.widget.*
import io.matthewnelson.sampleapp.R
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashMessage
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashboardFragment
import io.matthewnelson.sampleapp.ui.fragments.settings.tor.SettingsTorFragment
import io.matthewnelson.topl_core_base.BaseConsts.PortOption
import io.matthewnelson.topl_service_base.BaseServiceTorSettings

class HttpPortOption(view: View, private val serviceTorSettings: BaseServiceTorSettings) {

    // Views
    private lateinit var spinnerHttpPort: Spinner
    private lateinit var adapterHttpPort: ArrayAdapter<String>
    private lateinit var listenerHttpPort: HttpPortSelectionListener
    private lateinit var textViewHttpPortCustom: TextView
    private lateinit var editTextHttpPortCustom: EditText

    private var initialHttpPort = serviceTorSettings.httpTunnelPort

    fun saveHttpPort(): Any? {
        val httpPort = when (adapterHttpPort.getItem(listenerHttpPort.position)) {
            SettingsTorFragment.AUTO -> {
                PortOption.AUTO
            }
            SettingsTorFragment.DISABLED -> {
                PortOption.DISABLED
            }
            else -> {
                editTextHttpPortCustom.text.toString()
            }
        }

        try {
            serviceTorSettings.httpTunnelPortSave(httpPort)
            initialHttpPort = httpPort
        } catch (e: IllegalArgumentException) {
            DashboardFragment.showMessage(
                DashMessage("${DashMessage.EXCEPTION}${e.message}",
                    R.drawable.dash_message_color_red,
                    4_000
                )
            )
            return null
        }
        return Any()
    }

    init {
        findViews(view)
        setViewParameters()
        initSpinnerTorHttpPort(view.context)
    }

    private fun findViews(view: View) {
        spinnerHttpPort = view.findViewById(R.id.settings_tor_spinner_http_port)
        textViewHttpPortCustom = view.findViewById(R.id.settings_tor_text_view_http_port_custom)
        editTextHttpPortCustom = view.findViewById(R.id.settings_tor_edit_text_http_port_custom)
    }

    private fun setViewParameters() {
        editTextHttpPortCustom.filters = arrayOf(InputFilter.LengthFilter(5))
    }

    private fun initSpinnerTorHttpPort(context: Context) {
        val categoryHttpPort = arrayOf(
            SettingsTorFragment.AUTO,
            SettingsTorFragment.CUSTOM,
            SettingsTorFragment.DISABLED
        )
        adapterHttpPort = ArrayAdapter(context, R.layout.spinner_list_item, categoryHttpPort)
        adapterHttpPort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHttpPort.adapter = adapterHttpPort
        setHttpPortSpinnerValue()
        listenerHttpPort = HttpPortSelectionListener()
        spinnerHttpPort.onItemSelectedListener = listenerHttpPort
    }

    private fun setHttpPortSpinnerValue() {
        when (initialHttpPort) {
            PortOption.AUTO -> {
                httpPortCustomVisibility(false)
                spinnerHttpPort.setSelection(0)
            }
            PortOption.DISABLED -> {
                httpPortCustomVisibility(false)
                spinnerHttpPort.setSelection(2)
            }
            else -> {
                editTextHttpPortCustom.setText(initialHttpPort)
                spinnerHttpPort.setSelection(1)
                httpPortCustomVisibility(true)
            }
        }
    }

    private fun httpPortCustomVisibility(show: Boolean) {
        textViewHttpPortCustom.visibility = if (show) View.VISIBLE else View.GONE
        editTextHttpPortCustom.visibility = if (show) View.VISIBLE else View.GONE
    }

    private inner class HttpPortSelectionListener: AdapterView.OnItemSelectedListener {
        var position = 0
            private set
        var count = 0
            private set

        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            this.position = position

            if (count == 0) {
                count ++
                return
            }

            when (parent?.getItemAtPosition(position).toString()) {
                SettingsTorFragment.AUTO -> {
                    httpPortCustomVisibility(false)
                }
                SettingsTorFragment.CUSTOM -> {
                    when (initialHttpPort) {
                        PortOption.AUTO,
                        PortOption.DISABLED -> {
                            editTextHttpPortCustom.setText("")
                        }
                        else -> {
                            editTextHttpPortCustom.setText(initialHttpPort)
                        }
                    }
                    httpPortCustomVisibility(true)
                }
                SettingsTorFragment.DISABLED -> {
                    httpPortCustomVisibility(false)
                }
                else -> {
                    return
                }
            }
        }
    }
}