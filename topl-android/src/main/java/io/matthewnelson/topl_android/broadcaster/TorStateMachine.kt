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

import io.matthewnelson.topl_android_settings.TorStates

/**
 * Current Status of Tor
 */
class TorStateMachine(private val broadcaster: EventBroadcaster): TorStates() {

    var currentTorState: @TorState String = TorState.OFF
        private set

    val isOff: Boolean
        get() = TorState.OFF == currentTorState

    val isOn: Boolean
        get() = TorState.ON == currentTorState

    val isStarting: Boolean
        get() = TorState.STARTING == currentTorState

    val isStopping: Boolean
        get() = TorState.STOPPING == currentTorState

    /**
     * Will set the state to that which is specified if it isn't already.
     *
     * @return Previous [TorStates.TorState]
     * */
    internal fun setTorState(@TorState state: String): @TorState String {
        val currentState = currentTorState
        if (currentTorState != state) {
            currentTorState = state
            broadcaster.broadcastTorState(currentTorState)
        } else {
            broadcaster.broadcastDebug("TorState was already set to $currentState")
        }
        return currentState
    }
}