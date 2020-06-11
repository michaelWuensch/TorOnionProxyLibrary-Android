/*
Copyright (c) Microsoft Open Technologies, Inc.
All Rights Reserved
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED,
INCLUDING WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
MERCHANTABLITY OR NON-INFRINGEMENT.

See the Apache 2 License for the specific language governing permissions and limitations under the License.
*/
package com.msopentech.thali.toronionproxy.broadcaster

import com.msopentech.thali.toronionproxy.settings.DefaultSettings
import com.msopentech.thali.toronionproxy.settings.TorSettings
import io.matthewnelson.topl_settings.TorSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Override this class to implement [broadcastBandwidth], [broadcastLogMessage],
 * and [broadcastStatus].
 * */
open class DefaultEventBroadcaster(settings: TorSettings?) : EventBroadcaster() {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(DefaultEventBroadcaster::class.java)
    }

    private val mSettings: TorSettings = settings ?: DefaultSettings()

    override val status: Status
        get() = Status(this)

    override fun broadcastBandwidth(upload: Long, download: Long, written: Long, read: Long) {}

    override fun broadcastDebug(msg: String) {
        if (mSettings.hasDebugLogs) {
            LOG.debug(msg)
            broadcastLogMessage(msg)
        }
    }

    override fun broadcastException(msg: String, e: Exception) {
        if (mSettings.hasDebugLogs) {
            LOG.error(msg, e)
            val sw = StringWriter()
            e.printStackTrace(PrintWriter(sw))
            broadcastLogMessage("$msg\n$sw".trimIndent())
        } else {
            broadcastLogMessage(msg)
        }
    }

    override fun broadcastLogMessage(logMessage: String) {}

    override fun broadcastNotice(msg: String) {
        if (msg.isNotEmpty()) {
            if (mSettings.hasDebugLogs) {
                LOG.debug(msg)
            }
            broadcastLogMessage(msg)
        }
    }

    override fun broadcastStatus() {}
}