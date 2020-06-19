package io.matthewnelson.topl_android_settings

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
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class TorState {
        companion object {
            const val OFF = "TOR_STATE_OFF"
            const val ON = "TOR_STATE_ON"
            const val STARTING = "TOR_STATE_STARTING"
            const val STOPPING = "TOR_STATE_STOPPING"
        }
    }
}