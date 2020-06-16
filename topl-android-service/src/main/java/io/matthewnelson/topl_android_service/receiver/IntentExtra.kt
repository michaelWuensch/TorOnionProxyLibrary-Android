package io.matthewnelson.topl_android_service.receiver

import androidx.annotation.StringDef

abstract class IntentExtra {
    @Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY_GETTER)
    @StringDef(
        ACTION_START,
        ACTION_STOP,
        ACTION_RESTART,
        ACTION_RENEW
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Extra

    @get:Extra
    abstract var intentExtra: String

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_RESTART = "ACTION_RESTART"
        const val ACTION_RENEW = "ACTION_RENEW"
    }
}