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
import io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings

class TransPortOption(view: View, private val serviceTorSettings: ServiceTorSettings) {

    // Views
    private lateinit var spinnerTransPort: Spinner
    private lateinit var adapterTransPort: ArrayAdapter<String>
    private lateinit var listenerTransPort: TransPortSelectionListener
    private lateinit var textViewTransPortCustom: TextView
    private lateinit var editTextTransPortCustom: EditText

    private var initialTransPort = serviceTorSettings.transPort

    fun saveTransPort(): Any? {
        val transPort = when (adapterTransPort.getItem(listenerTransPort.position)) {
            SettingsTorFragment.AUTO -> {
                PortOption.AUTO
            }
            SettingsTorFragment.DISABLED -> {
                PortOption.DISABLED
            }
            else -> {
                editTextTransPortCustom.text.toString()
            }
        }

        try {
            serviceTorSettings.transPortSave(transPort)
            initialTransPort = transPort
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
        initSpinnerTorTransPort(view.context)
    }

    private fun findViews(view: View) {
        spinnerTransPort = view.findViewById(R.id.settings_tor_spinner_trans_port)
        textViewTransPortCustom = view.findViewById(R.id.settings_tor_text_view_trans_port_custom)
        editTextTransPortCustom = view.findViewById(R.id.settings_tor_edit_text_trans_port_custom)
    }

    private fun setViewParameters() {
        editTextTransPortCustom.filters = arrayOf(InputFilter.LengthFilter(5))
    }

    private fun initSpinnerTorTransPort(context: Context) {
        val categoryTransPort = arrayOf(
            SettingsTorFragment.AUTO,
            SettingsTorFragment.CUSTOM,
            SettingsTorFragment.DISABLED
        )
        adapterTransPort = ArrayAdapter(context, R.layout.spinner_list_item, categoryTransPort)
        adapterTransPort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTransPort.adapter = adapterTransPort
        setTransPortSpinnerValue()
        listenerTransPort = TransPortSelectionListener()
        spinnerTransPort.onItemSelectedListener = listenerTransPort
    }

    private fun setTransPortSpinnerValue() {
        when (initialTransPort) {
            PortOption.AUTO -> {
                transPortCustomVisibility(false)
                spinnerTransPort.setSelection(0)
            }
            PortOption.DISABLED -> {
                transPortCustomVisibility(false)
                spinnerTransPort.setSelection(2)
            }
            else -> {
                editTextTransPortCustom.setText(initialTransPort)
                spinnerTransPort.setSelection(1)
                transPortCustomVisibility(true)
            }
        }
    }

    private fun transPortCustomVisibility(show: Boolean) {
        textViewTransPortCustom.visibility = if (show) View.VISIBLE else View.GONE
        editTextTransPortCustom.visibility = if (show) View.VISIBLE else View.GONE
    }

    private inner class TransPortSelectionListener: AdapterView.OnItemSelectedListener {
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
                    transPortCustomVisibility(false)
                }
                SettingsTorFragment.CUSTOM -> {
                    when (initialTransPort) {
                        PortOption.AUTO,
                        PortOption.DISABLED -> {
                            editTextTransPortCustom.setText("")
                        }
                        else -> {
                            editTextTransPortCustom.setText(initialTransPort)
                        }
                    }
                    transPortCustomVisibility(true)
                }
                SettingsTorFragment.DISABLED -> {
                    transPortCustomVisibility(false)
                }
                else -> {
                    return
                }
            }
        }
    }
}