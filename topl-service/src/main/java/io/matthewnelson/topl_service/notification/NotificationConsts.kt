package io.matthewnelson.topl_service.notification

import androidx.annotation.IntDef

internal abstract class NotificationConsts {
    @IntDef(
        ImageState.ON,
        ImageState.OFF,
        ImageState.DATA,
        ImageState.ERROR
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ImageState {
        companion object {
            const val ON = 0
            const val OFF = 1
            const val DATA = 2
            const val ERROR = 3
        }
    }
}