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

class DnsPortOption(view: View, private val serviceTorSettings: ServiceTorSettings) {

    // Views
    private lateinit var spinnerDnsPort: Spinner
    private lateinit var adapterDnsPort: ArrayAdapter<String>
    private lateinit var listenerDnsPort: DnsPortSelectionListener
    private lateinit var textViewDnsPortCustom: TextView
    private lateinit var editTextDnsPortCustom: EditText

    private var initialDnsPort = serviceTorSettings.dnsPort

    fun saveDnsPort(): Any? {
        val dnsPort = when (adapterDnsPort.getItem(listenerDnsPort.position)) {
            SettingsTorFragment.AUTO -> {
                BaseConsts.PortOption.AUTO
            }
            SettingsTorFragment.DISABLED -> {
                BaseConsts.PortOption.DISABLED
            }
            else -> {
                editTextDnsPortCustom.text.toString()
            }
        }

        try {
            serviceTorSettings.dnsPortSave(dnsPort)
            initialDnsPort = dnsPort
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
        initSpinnerTorDnsPort(view.context)
    }

    private fun findViews(view: View) {
        spinnerDnsPort = view.findViewById(R.id.settings_tor_spinner_dns_port)
        textViewDnsPortCustom = view.findViewById(R.id.settings_tor_text_view_dns_port_custom)
        editTextDnsPortCustom = view.findViewById(R.id.settings_tor_edit_text_dns_port_custom)
    }

    private fun setViewParameters() {
        editTextDnsPortCustom.filters = arrayOf(InputFilter.LengthFilter(5))
    }

    private fun initSpinnerTorDnsPort(context: Context) {
        val categorySocksPort = arrayOf(
            SettingsTorFragment.AUTO,
            SettingsTorFragment.CUSTOM,
            SettingsTorFragment.DISABLED
        )
        adapterDnsPort = ArrayAdapter(context, R.layout.spinner_list_item, categorySocksPort)
        adapterDnsPort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDnsPort.adapter = adapterDnsPort
        setDnsPortSpinnerValue()
        listenerDnsPort = DnsPortSelectionListener()
        spinnerDnsPort.onItemSelectedListener = listenerDnsPort
    }

    private fun setDnsPortSpinnerValue() {
        when (initialDnsPort) {
            BaseConsts.PortOption.AUTO -> {
                dnsPortCustomVisibility(false)
                spinnerDnsPort.setSelection(0)
            }
            BaseConsts.PortOption.DISABLED -> {
                dnsPortCustomVisibility(false)
                spinnerDnsPort.setSelection(2)
            }
            else -> {
                editTextDnsPortCustom.setText(initialDnsPort)
                spinnerDnsPort.setSelection(1)
                dnsPortCustomVisibility(true)
            }
        }
    }

    private fun dnsPortCustomVisibility(show: Boolean) {
        textViewDnsPortCustom.visibility = if (show) View.VISIBLE else View.GONE
        editTextDnsPortCustom.visibility = if (show) View.VISIBLE else View.GONE
    }

    private inner class DnsPortSelectionListener: AdapterView.OnItemSelectedListener {
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
                    dnsPortCustomVisibility(false)
                }
                SettingsTorFragment.CUSTOM -> {
                    when (initialDnsPort) {
                        BaseConsts.PortOption.AUTO,
                        BaseConsts.PortOption.DISABLED -> {
                            editTextDnsPortCustom.setText("")
                        }
                        else -> {
                            editTextDnsPortCustom.setText(initialDnsPort)
                        }
                    }
                    dnsPortCustomVisibility(true)
                }
                SettingsTorFragment.DISABLED -> {
                    dnsPortCustomVisibility(false)
                }
                else -> {
                    return
                }
            }
        }
    }
}