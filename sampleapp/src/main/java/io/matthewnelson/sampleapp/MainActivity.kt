/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.sampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyBoolean
import io.matthewnelson.topl_service.prefs.TorServicePrefs

/**
 * @suppress
 * */
class MainActivity : AppCompatActivity() {

    private companion object {
        var hasDebugLogs = false
    }

    private lateinit var buttonDebug: Button
    private lateinit var buttonNewId: Button
    private lateinit var buttonRestart: Button
    private lateinit var buttonStart: Button
    private lateinit var buttonStop: Button

    private lateinit var textViewBandwidth: TextView
    private lateinit var textViewNetworkState: TextView
    private lateinit var textViewState: TextView

    private lateinit var torServicePrefs: TorServicePrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        torServicePrefs = TorServicePrefs(this)
        hasDebugLogs = torServicePrefs.getBoolean(
            PrefKeyBoolean.HAS_DEBUG_LOGS, App.myTorSettings.hasDebugLogs
        )
        findViews()
        initButtons()
        LogMessageAdapter(this)
        initObservers(this)
    }

    private fun findViews() {
        buttonDebug = findViewById(R.id.button_debug)
        buttonNewId = findViewById(R.id.button_new_identity)
        buttonRestart = findViewById(R.id.button_restart)
        buttonStart = findViewById(R.id.button_start)
        buttonStop = findViewById(R.id.button_stop)

        textViewBandwidth = findViewById(R.id.text_view_bandwidth)
        textViewNetworkState = findViewById(R.id.text_view_tor_network_state)
        textViewState = findViewById(R.id.text_view_tor_state)
    }

    private fun initButtons() {
        setButtonDebugText()
        buttonDebug.setOnClickListener {
            hasDebugLogs = !hasDebugLogs
            setButtonDebugText()
            torServicePrefs.putBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, hasDebugLogs)
        }
        buttonStart.setOnClickListener {
            TorServiceController.startTor()
        }
        buttonStop.setOnClickListener {
            TorServiceController.stopTor()
        }
        buttonRestart.setOnClickListener {
            TorServiceController.restartTor()
        }
        buttonNewId.setOnClickListener {
            TorServiceController.newIdentity()
        }
    }

    private fun setButtonDebugText() {
        buttonDebug.text = if (hasDebugLogs) {
            getString(R.string.button_debugging_disable)
        } else {
            getString(R.string.button_debugging_enable)
        }
    }

    private fun initObservers(activity: MainActivity) {
        TorServiceController.appEventBroadcaster?.let {
            (it as MyEventBroadcaster).liveBandwidth.observe(activity, Observer { string ->
                if (string.isNullOrEmpty()) return@Observer
                textViewBandwidth.text = string
            })
        }
        TorServiceController.appEventBroadcaster?.let {
            (it as MyEventBroadcaster).liveTorState.observe(activity, Observer { data ->
                if (data == null) return@Observer
                textViewState.text = data.state
                textViewNetworkState.text = data.networkState
            })
        }
    }
}
