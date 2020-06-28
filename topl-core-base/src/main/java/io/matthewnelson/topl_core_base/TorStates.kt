package io.matthewnelson.topl_core_base

import androidx.annotation.StringDef

abstract class TorStates {

    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        TorState.OFF,
        TorState.ON,
        TorState.STARTING,
        TorState.STOPPING
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class TorState {
        companion object {
            const val OFF = "Tor: Off"
            const val ON = "Tor: On"
            const val STARTING = "Tor: Starting"
            const val STOPPING = "Tor: Stopping"
        }
    }

    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        TorNetworkState.ENABLED,
        TorNetworkState.DISABLED
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class TorNetworkState {
        companion object {
            const val ENABLED = "Network: enabled"
            const val DISABLED = "Network: disabled"
        }
    }
}