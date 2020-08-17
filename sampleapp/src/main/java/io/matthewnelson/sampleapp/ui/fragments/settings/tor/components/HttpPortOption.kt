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

class HttpPortOption(view: View, private val serviceTorSettings: ServiceTorSettings) {

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
        val categorySocksPort = arrayOf(
            SettingsTorFragment.AUTO,
            SettingsTorFragment.CUSTOM,
            SettingsTorFragment.DISABLED
        )
        adapterHttpPort = ArrayAdapter(context, R.layout.spinner_list_item, categorySocksPort)
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