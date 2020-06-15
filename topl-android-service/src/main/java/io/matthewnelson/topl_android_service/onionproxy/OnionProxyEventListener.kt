package io.matthewnelson.topl_android_service.onionproxy

import io.matthewnelson.topl_android.listener.BaseEventListener
import io.matthewnelson.topl_android_service.TorService
import net.freehaven.tor.control.TorControlCommands

internal class OnionProxyEventListener(
    private val torService: TorService,
    private val eventBroadcaster: OnionProxyEventBroadcaster
): BaseEventListener() {

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
        TODO("Not yet implemented")
    }

    override fun unrecognized(data: String?) {
        TODO("Not yet implemented")
    }

    override fun newConsensus(data: String?) {
        TODO("Not yet implemented")
    }

    override fun connBw(data: String?) {
        TODO("Not yet implemented")
    }

    override fun circBandwidthUsed(data: String?) {
        TODO("Not yet implemented")
    }

    override fun networkLiveness(data: String?) {
        TODO("Not yet implemented")
    }

    override fun onEvent(keyword: String?, data: String?) {
        super.onEvent(keyword, data)
    }

    override fun newDesc(data: String?) {
        TODO("Not yet implemented")
    }

    override fun ns(data: String?) {
        TODO("Not yet implemented")
    }

    override fun guard(data: String?) {
        TODO("Not yet implemented")
    }

    override fun clientsSeen(data: String?) {
        TODO("Not yet implemented")
    }

    override fun gotSignal(data: String?) {
        TODO("Not yet implemented")
    }

    override fun hsDescContent(data: String?) {
        TODO("Not yet implemented")
    }

    override fun transportLaunched(data: String?) {
        TODO("Not yet implemented")
    }

    override fun bandwidthUsed(data: String?) {
        TODO("Not yet implemented")
    }

    override fun addrMap(data: String?) {
        TODO("Not yet implemented")
    }

    override fun warnMsg(data: String?) {
        TODO("Not yet implemented")
    }

    override fun statusGeneral(data: String?) {
        TODO("Not yet implemented")
    }

    override fun circuitStatusMinor(data: String?) {
        TODO("Not yet implemented")
    }

    override fun errMsg(data: String?) {
        TODO("Not yet implemented")
    }

    override fun streamStatus(data: String?) {
        TODO("Not yet implemented")
    }

    override fun descChanged(data: String?) {
        TODO("Not yet implemented")
    }

    override fun orConnStatus(data: String?) {
        TODO("Not yet implemented")
    }

    override fun infoMsg(data: String?) {
        TODO("Not yet implemented")
    }

    override fun hsDesc(data: String?) {
        TODO("Not yet implemented")
    }

    override fun statusClient(data: String?) {
        TODO("Not yet implemented")
    }

    override fun debugMsg(data: String?) {
        TODO("Not yet implemented")
    }

    override fun streamBandwidthUsed(data: String?) {
        TODO("Not yet implemented")
    }

    override fun confChanged(data: String?) {
        TODO("Not yet implemented")
    }

    override fun cellStats(data: String?) {
        TODO("Not yet implemented")
    }

    override fun circuitStatus(data: String?) {
        TODO("Not yet implemented")
    }

    override fun buildTimeoutSet(data: String?) {
        TODO("Not yet implemented")
    }

    override fun statusServer(data: String?) {
        TODO("Not yet implemented")
    }
}