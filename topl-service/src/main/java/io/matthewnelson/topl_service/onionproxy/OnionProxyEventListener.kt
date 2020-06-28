package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core.listener.BaseEventListener
import io.matthewnelson.topl_service.service.TorService
import net.freehaven.tor.control.TorControlCommands

/**
 * [io.matthewnelson.topl_core.OnionProxyManager] registers this class in it's
 * [io.matthewnelson.topl_core.OnionProxyManager.start] method such that messages from
 * Tor get funneled here. They get modify as needed, then shipped to it's final destination.
 *
 * TODO: Decide whether or not I wish to ship it directly to
 *  [io.matthewnelson.topl_service.TorServiceController.Companion] as a Flow so library
 *  users can easily hook in, or if the EventBroadcaster should... (Might be too confusing).
 *
 *  @param [torService] [TorService] for context.
 *  @param [eventBroadcaster] [OnionProxyEventBroadcaster] for broadcasting data.
 * */
internal class OnionProxyEventListener(
    private val torService: TorService,
    private val eventBroadcaster: OnionProxyEventBroadcaster
): BaseEventListener() {

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
            TorControlCommands.EVENT_NEWCONSENSUS,
            TorControlCommands.EVENT_TRANSPORT_LAUNCHED
        )

    override fun noticeMsg(data: String?) {
        if (!data.isNullOrEmpty())
            eventBroadcaster.broadcastNotice(data)
        super.noticeMsg(data)
    }

    override fun unrecognized(data: String?) {}

    override fun newConsensus(data: String?) {}

    override fun connBw(data: String?) {}

    override fun circBandwidthUsed(data: String?) {}

    override fun networkLiveness(data: String?) {}

    override fun onEvent(keyword: String?, data: String?) {
        super.onEvent(keyword, data)
    }

    override fun newDesc(data: String?) {}

    override fun ns(data: String?) {}

    override fun guard(data: String?) {}

    override fun clientsSeen(data: String?) {}

    override fun gotSignal(data: String?) {}

    override fun hsDescContent(data: String?) {}

    override fun transportLaunched(data: String?) {}

    // https://torproject.gitlab.io/torspec/control-spec/#bandwidth-used-in-the-last-second
    override fun bandwidthUsed(data: String?) {
        if (!data.isNullOrEmpty()) {
            val dataList = data.split(" ")
            if (dataList.size == 2)
                eventBroadcaster.broadcastBandwidth(dataList[0], dataList[1])
        }
    }

    override fun addrMap(data: String?) {}

    override fun warnMsg(data: String?) {}

    override fun statusGeneral(data: String?) {}

    override fun circuitStatusMinor(data: String?) {}

    override fun errMsg(data: String?) {}

    override fun streamStatus(data: String?) {}

    override fun descChanged(data: String?) {}

    override fun orConnStatus(data: String?) {}

    override fun infoMsg(data: String?) {}

    override fun hsDesc(data: String?) {}

    override fun statusClient(data: String?) {}

    override fun debugMsg(data: String?) {}

    override fun streamBandwidthUsed(data: String?) {}

    override fun confChanged(data: String?) {}

    override fun cellStats(data: String?) {}

    override fun circuitStatus(data: String?) {}

    override fun buildTimeoutSet(data: String?) {}

    override fun statusServer(data: String?) {}
}