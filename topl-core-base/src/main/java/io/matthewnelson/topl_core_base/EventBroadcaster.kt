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
package io.matthewnelson.topl_core_base

/**
 * Service for sending event logs to the system.
 *
 * Both `topl-core` and `topl-service` utilize this class to broadcast messages. There are 2
 * formats to the messages that get broadcast.
 *
 *  - If a class within either module is broadcasting something, it will look like:
 *      - TYPE|ClassName|Message
 *  - If it is a broadcast from Tor, it will simply be a message with no format.
 *
 * See [BaseConsts.BroadcastType]s
 *
 * This allows for easier separation of messages based on the type, process or class
 */
abstract class EventBroadcaster: BaseConsts() {

    /**
     * Broadcast from Tor only:
     *  - (bytesRead as a string, bytesWritten as a string)
     *
     * [bytesRead] = bytes downloaded
     * [bytesWritten] = bytes uploaded
     * */
    abstract fun broadcastBandwidth(bytesRead: String, bytesWritten: String)

    /**
     * Broadcast from modules:
     *  - ("DEBUG|ClassName|msg")
     *
     * Broadcast from Tor:
     *  - ("msg")
     * */
    abstract fun broadcastDebug(msg: String)

    /**
     * Broadcast from modules:
     *  - (EXCEPTION|ClassName|msg, e)
     *
     * Broadcast from Tor:
     *  - (msg, e)
     * */
    abstract fun broadcastException(msg: String?, e: Exception)

    /**
     * Not yet implemented in either module.
     * */
    abstract fun broadcastLogMessage(logMessage: String?)

    /**
     * Broadcast from modules:
     *  - ("NOTICE|ClassName|msg")
     *  - ("WARN|ClassName|msg")
     *
     * Broadcast from Tor:
     *  - (msg)
     * */
    abstract fun broadcastNotice(msg: String)

    /**
     * Only used by modules:
     *  - (TorState as a string, TorNetworkState as a string)
     * */
    abstract fun broadcastTorState(@TorState state: String, @TorNetworkState networkState: String)

}