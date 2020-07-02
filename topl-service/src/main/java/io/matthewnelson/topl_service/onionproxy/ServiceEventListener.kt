package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core.listener.BaseEventListener
import net.freehaven.tor.control.TorControlCommands

/**
 * [io.matthewnelson.topl_core.OnionProxyManager] registers this class in it's
 * [io.matthewnelson.topl_core.OnionProxyManager.start] method such that messages from
 * Tor get funneled here. They get modify as needed, then shipped to it's final destination.
 * */
internal class ServiceEventListener: BaseEventListener() {

    // broadcastLogger is available from BaseEventListener and is instantiated as soon as
    // OnionProxyManager gets initialized.
//    private lateinit var broadcastLogger: BroadcastLogger

    override val CONTROL_COMMAND_EVENTS: Array<String>
        get() = arrayOf(
            TorControlCommands.EVENT_CIRCUIT_STATUS,
            TorControlCommands.EVENT_CIRCUIT_STATUS_MINOR,
            TorControlCommands.EVENT_STREAM_STATUS,
            TorControlCommands.EVENT_OR_CONN_STATUS,
            TorControlCommands.EVENT_BANDWIDTH_USED,
            TorControlCommands.EVENT_NOTICE_MSG,
            TorControlCommands.EVENT_WARN_MSG,
            TorControlCommands.EVENT_ERR_MSG,
            TorControlCommands.EVENT_NEW_DESC,
            TorControlCommands.EVENT_STATUS_GENERAL,
            TorControlCommands.EVENT_STATUS_CLIENT,
            TorControlCommands.EVENT_TRANSPORT_LAUNCHED
        )

    private fun debug(data: String) {
        broadcastLogger?.debug(data)
    }

    override fun noticeMsg(data: String?) {
        if (!data.isNullOrEmpty()) {
            broadcastLogger?.notice(data)
        }
        debug("${TorControlCommands.EVENT_NOTICE_MSG} $data")
        super.noticeMsg(data)
    }

    override fun unrecognized(data: String?) {
        debug("UNRECOGNIZED $data")
    }

    override fun newConsensus(data: String?) {
        debug("${TorControlCommands.EVENT_NEWCONSENSUS} $data")
    }

    override fun connBw(data: String?) {
        debug("${TorControlCommands.EVENT_CONN_BW} $data")
    }

    override fun circBandwidthUsed(data: String?) {
        debug("${TorControlCommands.EVENT_CIRC_BANDWIDTH_USED} $data")
    }

    override fun networkLiveness(data: String?) {
        debug("${TorControlCommands.EVENT_NETWORK_LIVENESS} $data")
    }

    override fun onEvent(keyword: String?, data: String?) {
        super.onEvent(keyword, data)
    }

    override fun newDesc(data: String?) {
        debug("${TorControlCommands.EVENT_NEW_DESC} $data")
    }

    override fun ns(data: String?) {
        debug("${TorControlCommands.EVENT_NS} $data")
    }

    override fun guard(data: String?) {
        debug("${TorControlCommands.EVENT_GUARD} $data")
    }

    override fun clientsSeen(data: String?) {
        debug("${TorControlCommands.EVENT_CLIENTS_SEEN} $data")
    }

    override fun gotSignal(data: String?) {
        debug("${TorControlCommands.EVENT_GOT_SIGNAL} $data")
    }

    override fun hsDescContent(data: String?) {
        debug("${TorControlCommands.EVENT_HS_DESC_CONTENT} $data")
    }

    override fun transportLaunched(data: String?) {
        debug("${TorControlCommands.EVENT_TRANSPORT_LAUNCHED} $data")
    }

    // https://torproject.gitlab.io/torspec/control-spec/#bandwidth-used-in-the-last-second
    override fun bandwidthUsed(data: String?) {
        if (!data.isNullOrEmpty()) {
            val dataList = data.split(" ")
            if (dataList.size == 2)
                broadcastLogger?.eventBroadcaster?.broadcastBandwidth(dataList[0], dataList[1])
        }
    }

    override fun addrMap(data: String?) {
        debug("${TorControlCommands.EVENT_ADDRMAP} $data")
    }

    override fun warnMsg(data: String?) {
        if (!data.isNullOrEmpty())
            broadcastLogger?.warn(data)
        debug("${TorControlCommands.EVENT_WARN_MSG} $data")
    }

    override fun statusGeneral(data: String?) {
        debug("${TorControlCommands.EVENT_STATUS_GENERAL} $data")
    }

    override fun circuitStatusMinor(data: String?) {
        debug("${TorControlCommands.EVENT_CIRCUIT_STATUS_MINOR} $data")
    }

    override fun errMsg(data: String?) {
        if (!data.isNullOrEmpty())
            broadcastLogger?.error(data)
        debug("${TorControlCommands.EVENT_ERR_MSG} $data")
    }

    override fun streamStatus(data: String?) {
        debug("${TorControlCommands.EVENT_STREAM_STATUS} $data")
    }

    override fun descChanged(data: String?) {
        debug("${TorControlCommands.EVENT_DESCCHANGED} $data")
    }

    override fun orConnStatus(data: String?) {
        debug("${TorControlCommands.EVENT_OR_CONN_STATUS} $data")
    }

    override fun infoMsg(data: String?) {
        debug("${TorControlCommands.EVENT_INFO_MSG} $data")
    }

    override fun hsDesc(data: String?) {
        debug("${TorControlCommands.EVENT_HS_DESC} $data")
    }

    override fun statusClient(data: String?) {
        debug("${TorControlCommands.EVENT_STATUS_CLIENT} $data")
    }

    override fun debugMsg(data: String?) {
        debug("${TorControlCommands.EVENT_DEBUG_MSG} $data")
    }

    override fun streamBandwidthUsed(data: String?) {
        debug("${TorControlCommands.EVENT_STREAM_BANDWIDTH_USED} $data")
    }

    override fun confChanged(data: String?) {
        debug("${TorControlCommands.EVENT_CONF_CHANGED} $data")
    }

    override fun cellStats(data: String?) {
        debug("${TorControlCommands.EVENT_CELL_STATS} $data")
    }

    override fun circuitStatus(data: String?) {
        debug("${TorControlCommands.EVENT_CIRCUIT_STATUS} $data")
    }

    override fun buildTimeoutSet(data: String?) {
        debug("${TorControlCommands.EVENT_BUILDTIMEOUT_SET} $data")
    }

    override fun statusServer(data: String?) {
        debug("${TorControlCommands.EVENT_STATUS_SERVER} $data")
    }
}