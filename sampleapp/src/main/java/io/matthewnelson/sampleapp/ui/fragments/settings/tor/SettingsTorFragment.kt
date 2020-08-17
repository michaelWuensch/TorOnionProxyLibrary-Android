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
package io.matthewnelson.sampleapp.ui.fragments.settings.tor

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.matthewnelson.sampleapp.R
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashMessage
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashboardFragment
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings
import io.matthewnelson.topl_core_base.BaseConsts.PortOption

class SettingsTorFragment : Fragment() {

    private companion object {
        const val AUTO = "Auto"
        const val DISABLED = "Disabled"
        const val CUSTOM = "Custom"
    }

    private lateinit var serviceTorSettings: ServiceTorSettings

    // Views
    private lateinit var spinnerSocksPort: Spinner
    private lateinit var adapterSocksPort: ArrayAdapter<String>
    private lateinit var listenerSocksPort: TorOptionSelectionListener
    private lateinit var textViewSocksPortCustom: TextView
    private lateinit var editTextSocksPortCustom: EditText
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        serviceTorSettings = TorServiceController.getServiceTorSettings(inflater.context)
        return inflater.inflate(R.layout.fragment_settings_tor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialValues()
        findViews(view)
        setViewParameters()
        initSpinnerTorSocksPort(view.context)
        setButtonClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setInitialValues() {
        initialSocksPort = serviceTorSettings.socksPort
    }

    private fun findViews(view: View) {
        spinnerSocksPort = view.findViewById(R.id.settings_tor_spinner_socks_port)
        textViewSocksPortCustom = view.findViewById(R.id.settings_tor_text_view_socks_port_custom)
        editTextSocksPortCustom = view.findViewById(R.id.settings_tor_edit_text_socks_port_custom)
        buttonSave = view.findViewById(R.id.settings_tor_button_save)
    }

    private fun setViewParameters() {
        editTextSocksPortCustom.filters = arrayOf(InputFilter.LengthFilter(5))
    }

    private fun setButtonClickListener() {
        buttonSave.setOnClickListener {
            saveSocksPort() ?: return@setOnClickListener

            DashboardFragment.showMessage(
                DashMessage("Settings Saved", R.drawable.dash_message_color_green, 3_000L)
            )
        }
    }

    //////////////////
    /// Socks Port ///
    //////////////////
    private lateinit var initialSocksPort: String

    private fun initSpinnerTorSocksPort(context: Context) {
        val categorySocksPort = arrayOf(
            AUTO,
            CUSTOM,
            DISABLED
        )
        adapterSocksPort = ArrayAdapter(context, R.layout.spinner_list_item, categorySocksPort)
        adapterSocksPort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSocksPort.adapter = adapterSocksPort
        setSocksPortSpinnerValue()
        listenerSocksPort = TorOptionSelectionListener()
        spinnerSocksPort.onItemSelectedListener = listenerSocksPort
    }

    private fun setSocksPortSpinnerValue() {
        when (initialSocksPort) {
            PortOption.AUTO -> {
                socksPortCustomVisibility(false)
                spinnerSocksPort.setSelection(0)
            }
            PortOption.DISABLED -> {
                socksPortCustomVisibility(false)
                spinnerSocksPort.setSelection(2)
            }
            else -> {
                editTextSocksPortCustom.setText(initialSocksPort)
                spinnerSocksPort.setSelection(1)
                socksPortCustomVisibility(true)
            }
        }
    }

    private fun socksPortCustomVisibility(show: Boolean) {
        textViewSocksPortCustom.visibility = if (show) View.VISIBLE else View.GONE
        editTextSocksPortCustom.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun saveSocksPort(): Any? {
        val socksPort = when (adapterSocksPort.getItem(listenerSocksPort.position)) {
            AUTO -> {
                PortOption.AUTO
            }
            DISABLED -> {
                PortOption.DISABLED
            }
            else -> {
                editTextSocksPortCustom.text.toString()
            }
        }

        try {
            serviceTorSettings.socksPortSave(socksPort)
            initialSocksPort = socksPort
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

    /////////////////
    /// Http Port ///
    /////////////////

    private inner class TorOptionSelectionListener: AdapterView.OnItemSelectedListener {
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

            val item = parent?.getItemAtPosition(position) ?: return

            when (parent.adapter) {
                adapterSocksPort -> {
                    when (item.toString()) {
                        AUTO -> {
                            socksPortCustomVisibility(false)
                        }
                        DISABLED -> {
                            socksPortCustomVisibility(false)
                        }
                        else -> {
                            when (initialSocksPort) {
                                PortOption.AUTO, PortOption.DISABLED -> {
                                    editTextSocksPortCustom.setText("")
                                }
                                else -> {
                                    editTextSocksPortCustom.setText(initialSocksPort)
                                }
                            }
                            socksPortCustomVisibility(true)
                        }
                    }
                }
            }
        }
    }
}