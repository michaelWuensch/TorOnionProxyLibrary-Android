package io.matthewnelson.topl_core.listener

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
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

    @Volatile
    private var noticeMsgBuffer: StringBuffer? = null

    /**
     * Will set [noticeMsgBuffer] such that [noticeMsg] will begin appending to it.
     *
     * Be sure to call [doesNoticeMsgBufferContain] to set it back to null so it doesn't
     * continue to append notice messages.
     *
     * // TODO: find a less hacky way to do this...
     * */
    suspend fun beginWatchingNoticeMsgs() {
        noticeMsgBuffer = StringBuffer()
        delay(50)
    }

    suspend fun doesNoticeMsgBufferContain(string: String): Boolean {
        delay(50)
        val boolean = noticeMsgBuffer.toString().contains(string)
        noticeMsgBuffer = null
        return boolean
    }

    /**
     * Requires that when you extend this class and override this method, you **must**
     * use `super.noticeMsg(data)` within your class; otherwise, it will never get
     * messages to watch for the string value.
     * */
    override fun noticeMsg(data: String?) {
        noticeMsgBuffer?.append("${data}\n")
    }
}