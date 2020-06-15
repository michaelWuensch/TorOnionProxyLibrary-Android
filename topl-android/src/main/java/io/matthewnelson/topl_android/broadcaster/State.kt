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

import io.matthewnelson.topl_android_settings.TorState

/**
 * Current Status of Tor
 */
class State(private val broadcaster: EventBroadcaster) {

    // TODO: Kotlin Coroutin StateFlow to emmit State

    var torState: @TorState.State String = TorState.OFF
        private set

    val isOff: Boolean
        get() = TorState.OFF == torState

    val isOn: Boolean
        get() = TorState.ON == torState

    val isStarting: Boolean
        get() = TorState.STARTING == torState

    val isStopping: Boolean
        get() = TorState.STOPPING == torState

    /**
     * Will set the state to that which is specified if it isn't already.
     *
     * @return Previous state.
     * */
    fun setTorState(state: @TorState.State String): @TorState.State String {
        val currentState = torState
        if (torState != state) {
            torState = state
            broadcaster.broadcastTorState(torState)
        } else {
            broadcaster.broadcastDebug("TorState was already set to $currentState")
        }
        return currentState
    }
}