package io.matthewnelson.topl_core.listener

import net.freehaven.tor.control.TorControlCommands
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Some basic defaults. Extend this class and implement them how you wish
 * by overriding things, or start from scratch by extending [BaseEventListener]
 * */
open class DefaultEventListener: BaseEventListener() {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(DefaultEventListener::class.java)
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

    override fun circuitStatus(data: String?) =
        LOG.info("CirctuiStatus: ${data ?: "null"}")

    override fun buildTimeoutSet(data: String?) {}

    override fun statusServer(data: String?) {}

    override fun streamStatus(data: String?) =
        LOG.info("StreamStatus: ${data ?: "null"}")

    override fun descChanged(data: String?) {}

    override fun orConnStatus(data: String?) =
        LOG.info("OR connection: ${data ?: "null"}")

    override fun infoMsg(data: String?) {}

    override fun hsDesc(data: String?) {}

    override fun statusClient(data: String?) {}

    override fun debugMsg(data: String?) {}

    override fun streamBandwidthUsed(data: String?) {}

    override fun confChanged(data: String?) {}

    override fun cellStats(data: String?) {}

    override fun bandwidthUsed(data: String?) =
        LOG.info("BandwidthUsed: ${data ?: "null"}")

    override fun addrMap(data: String?) {}

    override fun newDesc(data: String?) =
        LOG.info("NewDescriptors: ${data ?: "null"}")

    override fun ns(data: String?) {}

    override fun guard(data: String?) {}

    override fun clientsSeen(data: String?) {}

    override fun gotSignal(data: String?) {}

    override fun hsDescContent(data: String?) {}

    override fun transportLaunched(data: String?) {}

    override fun noticeMsg(data: String?) =
        LOG.info("NOTICE: ${data ?: "null"}")

    override fun warnMsg(data: String?) =
        LOG.warn("WARNING: ${data ?: ""}")

    override fun statusGeneral(data: String?) {}

    override fun circuitStatusMinor(data: String?) {}

    override fun errMsg(data: String?) =
        LOG.error("ERROR: ${data ?: ""}")

    override fun unrecognized(data: String?) =
        LOG.info("Unrecognized: ${data ?: "null"}")

    override fun newConsensus(data: String?) {}

    override fun connBw(data: String?) {}

    override fun circBandwidthUsed(data: String?) {}

    override fun networkLiveness(data: String?) {}

}