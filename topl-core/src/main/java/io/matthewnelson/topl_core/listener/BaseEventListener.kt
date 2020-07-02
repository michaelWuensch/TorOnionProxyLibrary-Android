package io.matthewnelson.topl_core.listener

import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_core.util.TorInstaller
import kotlinx.coroutines.delay
import net.freehaven.tor.control.EventListener
import net.freehaven.tor.control.TorControlCommands

/**
 * Extend this class to customize implementation of the member overrides.
 * */
abstract class BaseEventListener: EventListener() {

    /**
     * This gets set as soon as [io.matthewnelson.topl_core.OnionProxyManager] is instantiated,
     * and can be used to broadcast messages in your class which extends [TorInstaller].
     * */
    var broadcastLogger: BroadcastLogger? = null
        private set
    internal fun initBroadcastLogger(torInstallerBroadcastLogger: BroadcastLogger) {
        if (broadcastLogger == null)
            broadcastLogger = torInstallerBroadcastLogger
    }

    /**
     * See [TorControlCommands.EVENT_NAMES] values. These are **REQUIRED**
     * for registering them in [io.matthewnelson.topl_core.OnionProxyManager.start]
     * which allows you full control over what you wish to listen for.
     *
     * WARNING: Be careful as to what events your are attempting to listen for. If
     * jtorctl has not implemented that event yet it will crash.
     *
     * See <a href="https://github.com/05nelsonm/TorOnionProxyLibrary-Android/issues/14" target="_blank">this issue</a>
     * for more details.
     * */
    abstract val CONTROL_COMMAND_EVENTS: Array<String>

    @Volatile
    private var noticeMsgBuffer: StringBuffer? = null

    /**
     * Will set [noticeMsgBuffer] such that [noticeMsg] will begin appending to it.
     *
     * Be sure to call [doesNoticeMsgBufferContain] to set it back to null so it doesn't
     * continue to append notice messages.
     *
     * TODO: find a less hacky way to do this...
     * */
    internal suspend fun beginWatchingNoticeMsgs() {
        noticeMsgBuffer = StringBuffer()
        delay(50)
    }

    internal suspend fun doesNoticeMsgBufferContain(string: String): Boolean {
        delay(50)
        val boolean = noticeMsgBuffer.toString().contains(string)
        noticeMsgBuffer = null
        return boolean
    }

    /**
     * Requires that when you extend this class and override [noticeMsg], you **must**
     * use `super.noticeMsg(data)` within your override; otherwise, it will never get
     * messages to watch for the string value.
     * */
    override fun noticeMsg(data: String?) {
        noticeMsgBuffer?.append("${data}\n")
    }
}