package io.matthewnelson.sampleapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyBoolean
import io.matthewnelson.topl_service.util.TorServicePrefs

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

    private lateinit var recyclerView: RecyclerView
    private lateinit var logMessageAdapter: LogMessageAdapter
    private lateinit var logMessageManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        initPrefs(this)
        initButtons()
        initRecyclerView(this)
        initObservers(this)
    }

    private fun findViews() {
        buttonDebug = findViewById(R.id.button_debug)
        buttonNewId = findViewById(R.id.button_new_identity)
        buttonRestart = findViewById(R.id.button_restart)
        buttonStart = findViewById(R.id.button_start)
        buttonStop = findViewById(R.id.button_stop)

        recyclerView = findViewById(R.id.recycler_view_log_messages)

        textViewBandwidth = findViewById(R.id.text_view_bandwidth)
        textViewNetworkState = findViewById(R.id.text_view_tor_network_state)
        textViewState = findViewById(R.id.text_view_tor_state)
    }

    private fun initPrefs(context: Context) {
        torServicePrefs = TorServicePrefs(context)
        hasDebugLogs = torServicePrefs.getBoolean(
            PrefKeyBoolean.HAS_DEBUG_LOGS, App.myTorSettings.hasDebugLogs
        )
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

    private fun initRecyclerView(context: Context) {
        logMessageAdapter = LogMessageAdapter()
        logMessageManager = LinearLayoutManager(context)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = logMessageManager
            adapter = logMessageAdapter
        }
    }

    private fun initObservers(activity: MainActivity) {
        (TorServiceController.appEventBroadcaster as MyEventBroadcaster)
            .liveBandwidth.observe(activity, Observer { string ->
                if (!string.isNullOrEmpty()) {
                    textViewBandwidth.text = string
                }
            })
        (TorServiceController.appEventBroadcaster as MyEventBroadcaster)
            .liveLogMessage.observe(activity, Observer { msg ->
                if (!msg.isNullOrEmpty()) {
                    logMessageAdapter.addLogMessage(msg)
                }
            })
        (TorServiceController.appEventBroadcaster as MyEventBroadcaster)
            .liveTorState.observe(activity, Observer { data ->
                if (data != null) {
                    textViewState.text = data.state
                    textViewNetworkState.text = data.networkState
                }
            })
    }
}
