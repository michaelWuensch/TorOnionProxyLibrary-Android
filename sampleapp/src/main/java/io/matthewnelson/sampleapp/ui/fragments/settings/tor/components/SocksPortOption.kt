package io.matthewnelson.sampleapp.ui.fragments.settings.tor.components

import android.content.Context
import android.text.InputFilter
import android.view.View
import android.widget.*
import io.matthewnelson.sampleapp.R
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashMessage
import io.matthewnelson.sampleapp.ui.fragments.dashboard.DashboardFragment
import io.matthewnelson.sampleapp.ui.fragments.settings.tor.SettingsTorFragment
import io.matthewnelson.topl_core_base.BaseConsts
import io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings

class SocksPortOption(view: View, private val serviceTorSettings: ServiceTorSettings) {

    // Views
    private lateinit var spinnerSocksPort: Spinner
    private lateinit var adapterSocksPort: ArrayAdapter<String>
    private lateinit var listenerSocksPort: SocksPortSelectionListener
    private lateinit var textViewSocksPortCustom: TextView
    private lateinit var editTextSocksPortCustom: EditText

    private var initialSocksPort = serviceTorSettings.socksPort

    fun saveSocksPort(): Any? {
        val socksPort = when (adapterSocksPort.getItem(listenerSocksPort.position)) {
            SettingsTorFragment.AUTO -> {
                BaseConsts.PortOption.AUTO
            }
            SettingsTorFragment.DISABLED -> {
                BaseConsts.PortOption.DISABLED
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

    init {
        findViews(view)
        setViewParameters()
        initSpinnerTorSocksPort(view.context)
    }

    private fun findViews(view: View) {
        spinnerSocksPort = view.findViewById(R.id.settings_tor_spinner_socks_port)
        textViewSocksPortCustom = view.findViewById(R.id.settings_tor_text_view_socks_port_custom)
        editTextSocksPortCustom = view.findViewById(R.id.settings_tor_edit_text_socks_port_custom)
    }

    private fun setViewParameters() {
        editTextSocksPortCustom.filters = arrayOf(InputFilter.LengthFilter(5))
    }

    private fun initSpinnerTorSocksPort(context: Context) {
        val categorySocksPort = arrayOf(
            SettingsTorFragment.AUTO,
            SettingsTorFragment.CUSTOM,
            SettingsTorFragment.DISABLED
        )
        adapterSocksPort = ArrayAdapter(context, R.layout.spinner_list_item, categorySocksPort)
        adapterSocksPort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSocksPort.adapter = adapterSocksPort
        setSocksPortSpinnerValue()
        listenerSocksPort = SocksPortSelectionListener()
        spinnerSocksPort.onItemSelectedListener = listenerSocksPort
    }

    private fun setSocksPortSpinnerValue() {
        when (initialSocksPort) {
            BaseConsts.PortOption.AUTO -> {
                socksPortCustomVisibility(false)
                spinnerSocksPort.setSelection(0)
            }
            BaseConsts.PortOption.DISABLED -> {
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

    private inner class SocksPortSelectionListener: AdapterView.OnItemSelectedListener {
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
                    socksPortCustomVisibility(false)
                }
                SettingsTorFragment.CUSTOM -> {
                    when (initialSocksPort) {
                        BaseConsts.PortOption.AUTO,
                        BaseConsts.PortOption.DISABLED -> {
                            editTextSocksPortCustom.setText("")
                        }
                        else -> {
                            editTextSocksPortCustom.setText(initialSocksPort)
                        }
                    }
                    socksPortCustomVisibility(true)
                }
                SettingsTorFragment.DISABLED -> {
                    socksPortCustomVisibility(false)
                }
                else -> {
                    return
                }
            }
        }
    }
}