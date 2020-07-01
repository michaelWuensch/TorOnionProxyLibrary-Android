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
package io.matthewnelson.topl_core.broadcaster

import io.matthewnelson.topl_core.util.CoreConsts

/**
 * Current State of Tor
 */
class TorStateMachine(private val broadcastLogger: BroadcastLogger): CoreConsts() {

    private var currentTorState: @TorState String = TorState.OFF
    private var currentTorNetworkState: @TorNetworkState String = TorNetworkState.DISABLED

    val isOff: Boolean
        get() = TorState.OFF == currentTorState

    val isOn: Boolean
        get() = TorState.ON == currentTorState

    val isStarting: Boolean
        get() = TorState.STARTING == currentTorState

    val isStopping: Boolean
        get() = TorState.STOPPING == currentTorState

    val isNetworkDisabled: Boolean
        get() = TorNetworkState.DISABLED == currentTorNetworkState

    /**
     * Will set the state to that which is specified if it isn't already.
     *
     * @param [state] [io.matthewnelson.topl_core_base.BaseConsts.TorState]
     * @return Previous [io.matthewnelson.topl_core_base.BaseConsts.TorState]
     * */
    @Synchronized
    internal fun setTorState(@TorState state: String): @TorState String {
        val currentState = currentTorState
        if (currentTorState != state) {
            currentTorState = state
            broadcastLogger.torState(currentTorState, currentTorNetworkState)
        } else {
            broadcastLogger.debug("TorState was already set to $currentState")
        }
        return currentState
    }

    /**
     * Will set the network state to that which is specified if it isn't already.
     *
     * @param [networkState] [io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState]
     * @return Previous [io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState]
     * */
    @Synchronized
    internal fun setTorNetworkState(@TorNetworkState networkState: String): @TorNetworkState String {
        val currentNetworkState = currentTorNetworkState
        if (currentTorNetworkState != networkState) {
            currentTorNetworkState = networkState
            broadcastLogger.torState(currentTorState, currentTorNetworkState)
        } else {
            broadcastLogger.debug("TorNetworkState was already set to $currentNetworkState")
        }
        return currentNetworkState
    }
}