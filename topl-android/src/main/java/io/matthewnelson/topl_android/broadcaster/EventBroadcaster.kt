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

/**
 * Service for sending event logs to the system
 */
abstract class EventBroadcaster {

    abstract val status: Status

    abstract fun broadcastBandwidth(upload: Long, download: Long, written: Long, read: Long)

    abstract fun broadcastDebug(msg: String)

    abstract fun broadcastException(msg: String, e: Exception)

    abstract fun broadcastLogMessage(logMessage: String)

    abstract fun broadcastNotice(msg: String)

    abstract fun broadcastStatus()

}