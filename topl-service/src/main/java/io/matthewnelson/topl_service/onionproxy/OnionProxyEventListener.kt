package io.matthewnelson.topl_service.onionproxy

import android.util.Log
import io.matthewnelson.topl_android.listener.BaseEventListener
import io.matthewnelson.topl_service.service.TorService
import net.freehaven.tor.control.TorControlCommands

internal class OnionProxyEventListener(
    private val torService: TorService,
    private val eventBroadcaster: OnionProxyEventBroadcaster
): BaseEventListener() {

    private companion object {
        const val TAG = "EventListener"
    }

    override val CONTROL_COMMAND_EVENTS: Array<String>
        get() = arrayOf(
            TorControlCommands.EVENT_CIRCUIT_STATUS,
            TorControlCommands.EVENT_OR_CONN_STATUS,
            TorControlCommands.EVENT_NOTICE_MSG,
            TorControlCommands.EVENT_WARN_MSG,
            TorControlCommands.EVENT_ERR_MSG,
            TorControlCommands.EVENT_BANDWIDTH_USED,
            TorControlCommands.EVENT_STATUS_CLIENT
        )

    override fun noticeMsg(data: String?) {
        Log.d(TAG, "NOTICE_MSG__$data")
    }

    override fun unrecognized(data: String?) {
        Log.d(TAG, "UNRECOGNIZED__$data")
    }

    override fun newConsensus(data: String?) {
        Log.d(TAG, "NEW_CONSENSUS__$data")
    }

    override fun connBw(data: String?) {
        Log.d(TAG, "CONN_BW__$data")
    }

    override fun circBandwidthUsed(data: String?) {
        Log.d(TAG, "CIR_BANDWIDTH_USED__$data")
    }

    override fun networkLiveness(data: String?) {
        Log.d(TAG, "NETWORK_LIVENESS__$data")
    }

    override fun onEvent(keyword: String?, data: String?) {
        super.onEvent(keyword, data)
    }

    override fun newDesc(data: String?) {
        Log.d(TAG, "NEW_DESC__$data")
    }

    override fun ns(data: String?) {
        Log.d(TAG, "NS__$data")
    }

    override fun guard(data: String?) {
        Log.d(TAG, "GUARD__$data")
    }

    override fun clientsSeen(data: String?) {
        Log.d(TAG, "CLIENTS_SEEN__$data")
    }

    override fun gotSignal(data: String?) {
        Log.d(TAG, "GOT_SIGNAL__$data")
    }

    override fun hsDescContent(data: String?) {
        Log.d(TAG, "HS_DESC_CONTENT__$data")
    }

    override fun transportLaunched(data: String?) {
        Log.d(TAG, "TRANSPORT_LAUNCHED__$data")
    }

    override fun bandwidthUsed(data: String?) {
        Log.d(TAG, "BANDWIDTH_USED__$data")
    }

    override fun addrMap(data: String?) {
        Log.d(TAG, "ADDR_MAP__$data")
    }

    override fun warnMsg(data: String?) {
        Log.d(TAG, "WARN_MSG__$data")
    }

    override fun statusGeneral(data: String?) {
        Log.d(TAG, "STATUS_GENERAL__$data")
    }

    override fun circuitStatusMinor(data: String?) {
        Log.d(TAG, "CIRCUIT_STATUS_MINOR__$data")
    }

    override fun errMsg(data: String?) {
        Log.d(TAG, "ERR_MSG__$data")
    }

    override fun streamStatus(data: String?) {
        Log.d(TAG, "STREAM_STATUS__$data")
    }

    override fun descChanged(data: String?) {
        Log.d(TAG, "DESC_CHANGED__$data")
    }

    override fun orConnStatus(data: String?) {
        Log.d(TAG, "OR_CONN_STATUS__$data")
    }

    override fun infoMsg(data: String?) {
        Log.d(TAG, "INFO_MSG__$data")
    }

    override fun hsDesc(data: String?) {
        Log.d(TAG, "HS_DESC__$data")
    }

    override fun statusClient(data: String?) {
        Log.d(TAG, "STATUS_CLIENT__$data")
    }

    override fun debugMsg(data: String?) {
        Log.d(TAG, "DEBUG_MSG__$data")
    }

    override fun streamBandwidthUsed(data: String?) {
        Log.d(TAG, "STREAM_BANDWIDTH_USED__$data")
    }

    override fun confChanged(data: String?) {
        Log.d(TAG, "CONF_CHANGED__$data")
    }

    override fun cellStats(data: String?) {
        Log.d(TAG, "CELL_STATS__$data")
    }

    override fun circuitStatus(data: String?) {
        Log.d(TAG, "CIRCUIT_STATUS__$data")
    }

    override fun buildTimeoutSet(data: String?) {
        Log.d(TAG, "BUILD_TIMEOUT_SET__$data")
    }

    override fun statusServer(data: String?) {
        Log.d(TAG, "STATUS_SERVER__$data")
    }
}