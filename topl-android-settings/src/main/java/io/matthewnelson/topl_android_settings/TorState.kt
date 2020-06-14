package io.matthewnelson.topl_android_settings

import androidx.annotation.StringDef

abstract class TorState {

    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @StringDef(
        OFF,
        ON,
        STARTING,
        STOPPING
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class State

    @get:State
    abstract var torState: String

    companion object {
        const val OFF = "OFF"
        const val ON = "ON"
        const val STARTING = "STARTING"
        const val STOPPING = "STOPPING"
    }
}