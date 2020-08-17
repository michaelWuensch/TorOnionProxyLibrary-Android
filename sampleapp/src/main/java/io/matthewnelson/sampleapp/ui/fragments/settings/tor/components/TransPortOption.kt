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