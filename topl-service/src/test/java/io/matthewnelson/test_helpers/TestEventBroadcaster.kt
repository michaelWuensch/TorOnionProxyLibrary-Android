package io.matthewnelson.test_helpers

import io.matthewnelson.topl_core_base.EventBroadcaster

class TestEventBroadcaster: EventBroadcaster() {

    override fun broadcastBandwidth(bytesRead: String, bytesWritten: String) {

    }

    override fun broadcastDebug(msg: String) {

    }

    override fun broadcastException(msg: String?, e: Exception) {

    }

    override fun broadcastLogMessage(logMessage: String?) {

    }

    override fun broadcastNotice(msg: String) {

    }

    override fun broadcastTorState(state: String, networkState: String) {

    }
}