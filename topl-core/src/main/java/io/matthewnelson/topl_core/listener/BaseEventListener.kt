/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash 74407114cbfa8ea6f2ac51417dda8be98d8aba86. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     This program is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
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

    /**
     * Checks [noticeMsgBuffer] for the declared [string] and resets [noticeMsgBuffer] to null.
     *
     * @param [string] The string you wish to check for in [noticeMsgBuffer]
     * @param [delayMilliseconds] Length of time you wish to delay the coroutine for before executing
     * @return True if it contains the string, false if not.
     * */
    internal suspend fun doesNoticeMsgBufferContain(string: String, delayMilliseconds: Long): Boolean {
        delay(delayMilliseconds)
        val boolean = noticeMsgBuffer.toString().contains(string)
        noticeMsgBuffer = null
        return boolean
    }

    /**
     * Requires that when you extend this class and override [noticeMsg], you **must**
     * use `super.noticeMsg(data)` within your overridden method; otherwise, [noticeMsgBuffer]
     * [beginWatchingNoticeMsgs] and [doesNoticeMsgBufferContain] will not work correctly.
     * */
    override fun noticeMsg(data: String?) {
        noticeMsgBuffer?.append("${data}\n")
    }
}