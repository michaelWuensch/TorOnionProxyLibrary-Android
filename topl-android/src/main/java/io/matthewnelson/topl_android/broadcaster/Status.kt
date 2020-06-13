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
package io.matthewnelson.topl_android.broadcaster

/**
 * Current Status of Tor
 */
class Status(private val broadcaster: EventBroadcaster) {

    companion object {
        const val STATUS_OFF = "OFF"
        const val STATUS_ON = "ON"
        const val STATUS_STARTING = "STARTING"
        const val STATUS_STOPPING = "STOPPING"
    }

    var status: String = STATUS_OFF
        private set

    val isOff: Boolean
        get() = STATUS_OFF == status

    val isOn: Boolean
        get() = STATUS_ON == status

    val isStarting: Boolean
        get() = STATUS_STARTING == status

    val isStopping: Boolean
        get() = STATUS_STOPPING == status

    fun off() {
        status = STATUS_OFF
        broadcaster.broadcastStatus()
    }

    fun on() {
        status = STATUS_ON
        broadcaster.broadcastStatus()
    }

    fun starting() {
        status = STATUS_STARTING
        broadcaster.broadcastStatus()
    }

    fun stopping() {
        status = STATUS_STOPPING
        broadcaster.broadcastStatus()
    }
}