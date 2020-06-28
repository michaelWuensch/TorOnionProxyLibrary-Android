package io.matthewnelson.topl_core.broadcaster

import io.matthewnelson.topl_core_base.EventBroadcaster

/**
 * This class ensures that [io.matthewnelson.topl_core.OnionProxyManager] has access to
 * Tor's State so it can keep it in sync.
 * */
abstract class BaseEventBroadcaster: EventBroadcaster() {

    private lateinit var stateMachine: TorStateMachine

    val torStateMachine: TorStateMachine
        get() = if (!::stateMachine.isInitialized) {
            stateMachine = TorStateMachine(this)
            stateMachine
        } else {
            stateMachine
        }
}