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
        super.noticeMsg(data)
    }

    override fun unrecognized(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("UNRECOGNIZED $data")
    }

    override fun newConsensus(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_NEWCONSENSUS} $data")
    }

    override fun connBw(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_CONN_BW} $data")
    }

    override fun circBandwidthUsed(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_CIRC_BANDWIDTH_USED} $data")
    }

    override fun networkLiveness(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_NETWORK_LIVENESS} $data")
    }

    override fun onEvent(keyword: String?, data: String?) {
        super.onEvent(keyword, data)
    }

    override fun newDesc(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_NEW_DESC} $data")
    }

    override fun ns(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_NS} $data")
    }

    override fun guard(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_GUARD} $data")
    }

    override fun clientsSeen(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_CLIENTS_SEEN} $data")
    }

    override fun gotSignal(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_GOT_SIGNAL} $data")
    }

    override fun hsDescContent(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_HS_DESC_CONTENT} $data")
    }

    override fun transportLaunched(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_TRANSPORT_LAUNCHED} $data")
    }

    // https://torproject.gitlab.io/torspec/control-spec/#bandwidth-used-in-the-last-second
    override fun bandwidthUsed(data: String?) {
        if (data.isNullOrEmpty()) return

        val dataList = data.split(" ")
        if (dataList.size != 2) return

        broadcastLogger?.eventBroadcaster?.broadcastBandwidth(dataList[0], dataList[1])

    }

    override fun addrMap(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_ADDRMAP} $data")
    }

    override fun warnMsg(data: String?) {
        if (data.isNullOrEmpty()) return
        broadcastLogger?.warn(data)
    }

    override fun statusGeneral(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_STATUS_GENERAL} $data")
    }

    override fun circuitStatusMinor(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_CIRCUIT_STATUS_MINOR} $data")
    }

    override fun errMsg(data: String?) {
        if (data.isNullOrEmpty()) return
        broadcastLogger?.error(data)
    }

    override fun streamStatus(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_STREAM_STATUS} $data")
    }

    override fun descChanged(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_DESCCHANGED} $data")
    }

    override fun orConnStatus(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_OR_CONN_STATUS} $data")
    }

    override fun infoMsg(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_INFO_MSG} $data")
    }

    override fun hsDesc(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_HS_DESC} $data")
    }

    override fun statusClient(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_STATUS_CLIENT} $data")
    }

    override fun debugMsg(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_DEBUG_MSG} $data")
    }

    override fun streamBandwidthUsed(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_STREAM_BANDWIDTH_USED} $data")
    }

    override fun confChanged(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_CONF_CHANGED} $data")
    }

    override fun cellStats(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_CELL_STATS} $data")
    }

    override fun circuitStatus(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_CIRCUIT_STATUS} $data")
    }

    override fun buildTimeoutSet(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_BUILDTIMEOUT_SET} $data")
    }

    override fun statusServer(data: String?) {
        if (data.isNullOrEmpty()) return
        debug("${TorControlCommands.EVENT_STATUS_SERVER} $data")
    }
}