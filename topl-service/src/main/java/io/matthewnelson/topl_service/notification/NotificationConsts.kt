package io.matthewnelson.topl_service.notification

import androidx.annotation.IntDef

internal abstract class NotificationConsts {
    @IntDef(
        ImageState.ENABLED,
        ImageState.DISABLED,
        ImageState.DATA,
        ImageState.ERROR
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ImageState {
        companion object {
            const val ENABLED = 0
            const val DISABLED = 1
            const val DATA = 2
            const val ERROR = 3
        }
    }
}