package io.matthewnelson.topl_core.listener

import net.freehaven.tor.control.EventListener
import net.freehaven.tor.control.TorControlCommands

/**
 * Extend this class to customize implementation of the member overrides.
 * */
abstract class BaseEventListener: EventListener() {

    /**
     * See [TorControlCommands.EVENT_NAMES] values. These are *REQUIRED*
     * for registering them in [io.matthewnelson.topl_core.OnionProxyManager.start]
     * which allows you full control over what you wish to listen for.
     * */
    abstract val CONTROL_COMMAND_EVENTS: Array<String>
}