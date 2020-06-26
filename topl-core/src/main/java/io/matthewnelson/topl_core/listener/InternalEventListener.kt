package io.matthewnelson.topl_core.listener

import net.freehaven.tor.control.EventListener

/**
 * See [io.matthewnelson.topl_core.OnionProxyManager.signalNewNym] for details on how
 * this is used.
 * */
internal class InternalEventListener: EventListener() {

    private val buffer = StringBuffer()

    fun doesBufferContainString(string: String): Boolean =
        buffer.toString().contains(string)

    override fun noticeMsg(data: String?) {
        buffer.append("${data}\n")
    }

    override fun unrecognized(data: String?) {}

    override fun newConsensus(data: String?) {}

    override fun connBw(data: String?) {}

    override fun circBandwidthUsed(data: String?) {}

    override fun networkLiveness(data: String?) {}

    override fun newDesc(data: String?) {}

    override fun ns(data: String?) {}

    override fun guard(data: String?) {}

    override fun clientsSeen(data: String?) {}

    override fun gotSignal(data: String?) {}

    override fun hsDescContent(data: String?) {}

    override fun transportLaunched(data: String?) {}

    override fun bandwidthUsed(data: String?) {}

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