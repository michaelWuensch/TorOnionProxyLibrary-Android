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
*
* ================================================================================
* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
* ================================================================================
*
* The original code, prior to commit hash 74407114cbfa8ea6f2ac51417dda8be98d8aba86,
* was:
*
*     Copyright (c) Microsoft Open Technologies, Inc.
*     All Rights Reserved
*
*     Licensed under the Apache License, Version 2.0 (the "License");
*     you may not use this file except in compliance with the License.
*     You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*
*
*     THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR
*     CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING
*     WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE,
*     FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABLITY OR NON-INFRINGEMENT.
*
*     See the Apache 2 License for the specific language governing permissions and
*     limitations under the License.
* */
package io.matthewnelson.topl_core_base

/**
 * Service for sending event logs to the system.
 *
 * Both `topl-core` and `topl-service` utilize this class to broadcast messages. This
 * allows for easier separation of messages based on the type, process or class.
 *
 * See [BaseConsts.BroadcastType]s
 */
abstract class EventBroadcaster: BaseConsts() {

    /**
     * [bytesRead] = bytes downloaded
     * [bytesWritten] = bytes uploaded
     * */
    abstract fun broadcastBandwidth(bytesRead: String, bytesWritten: String)

    /**
     * ("DEBUG|ClassName|msg")
     * */
    abstract fun broadcastDebug(msg: String)

    /**
     * ("EXCEPTION|ClassName|msg", e)
     * */
    abstract fun broadcastException(msg: String?, e: Exception)

    /**
     * Not yet implemented in either module.
     * */
    abstract fun broadcastLogMessage(logMessage: String?)

    /**
     * Will be one of:
     *  - ("ERROR|ClassName|msg")
     *  - ("NOTICE|ClassName|msg")
     *  - ("WARN|ClassName|msg")
     * */
    abstract fun broadcastNotice(msg: String)

    /**
     * See [BaseConsts.TorState] and [BaseConsts.TorNetworkState]
     * */
    abstract fun broadcastTorState(@TorState state: String, @TorNetworkState networkState: String)

}