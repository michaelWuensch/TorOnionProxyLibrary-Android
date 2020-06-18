package io.matthewnelson.topl_android_service.service

import androidx.annotation.StringDef

abstract class ServiceActions {

    @StringDef(
        ServiceAction.ACTION_START,
        ServiceAction.ACTION_STOP,
        ServiceAction.ACTION_RESTART,
        ServiceAction.ACTION_NEW_ID
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ServiceAction {
        companion object {
            const val ACTION_START = "ACTION_START"
            const val ACTION_STOP = "ACTION_STOP"
            const val ACTION_RESTART = "ACTION_RESTART"
            const val ACTION_NEW_ID = "ACTION_NEW_ID"
        }
    }
}