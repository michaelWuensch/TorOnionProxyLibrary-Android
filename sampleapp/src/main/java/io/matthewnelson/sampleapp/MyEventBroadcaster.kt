package io.matthewnelson.sampleapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.matthewnelson.topl_core_base.EventBroadcaster
import io.matthewnelson.topl_service.util.ServiceUtilities

class MyEventBroadcaster: EventBroadcaster() {


    /////////////////
    /// Bandwidth ///
    /////////////////
    private var lastDownload = "0"
    private var lastUpload = "0"

    private val _liveBandwidth = MutableLiveData<String>(
        ServiceUtilities.getFormattedBandwidthString(0L, 0L)
    )
    val liveBandwidth: LiveData<String> = _liveBandwidth

    override fun broadcastBandwidth(bytesRead: String, bytesWritten: String) {
        if (bytesRead != lastDownload || bytesWritten != lastUpload) {
            lastDownload = bytesRead
            lastUpload = bytesWritten
            if (liveBandwidth.hasActiveObservers()) {
                _liveBandwidth.value = ServiceUtilities.getFormattedBandwidthString(
                    bytesRead.toLong(), bytesWritten.toLong()
                )
            }
        }
    }

    override fun broadcastDebug(msg: String) {
        broadcastLogMessage(msg)
    }

    override fun broadcastException(msg: String?, e: Exception) {
        if (!msg.isNullOrEmpty()) {
            broadcastLogMessage(msg)
            e.printStackTrace()
        }
    }


    ////////////////////
    /// Log Messages ///
    ////////////////////
    override fun broadcastLogMessage(logMessage: String?) {
        if (!logMessage.isNullOrEmpty()) {
            val splitMsg = logMessage.split("|")
            if (splitMsg.size > 2) {
                LogMessageAdapter.addLogMessage("${splitMsg[0]} | ${splitMsg[1]} | ${splitMsg[2]}")
            }
        }
    }

    override fun broadcastNotice(msg: String) {
        broadcastLogMessage(msg)
    }


    ///////////////////
    /// Tor's State ///
    ///////////////////
    inner class TorStateData(val state: String, val networkState: String)

    private var lastState = TorState.OFF
    private var lastNetworkState = TorNetworkState.DISABLED

    private val _liveTorState = MutableLiveData<TorStateData>(
        TorStateData(lastState, lastNetworkState)
    )
    val liveTorState: LiveData<TorStateData> = _liveTorState

    override fun broadcastTorState(@TorState state: String, @TorNetworkState networkState: String) {
        if (state != lastState || networkState != lastNetworkState) {
            lastState = state
            lastNetworkState = networkState
            _liveTorState.value = TorStateData(state, networkState)
        }
    }
}