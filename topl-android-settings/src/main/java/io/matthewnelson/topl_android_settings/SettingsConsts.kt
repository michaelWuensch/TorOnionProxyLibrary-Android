package io.matthewnelson.topl_android_settings

import androidx.annotation.StringDef

/**
 * This class is extended by [TorSettings] and is for standardization of some of the
 * values used.
 * */
abstract class SettingsConsts {

    @Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
    @StringDef(
        SupportedBridges.MEEK,
        SupportedBridges.OBFS4,
        SupportedBridges.SNOWFLAKE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class SupportedBridges {
        companion object {
            const val MEEK = "meek"
            const val OBFS4 = "obfs4"
            const val SNOWFLAKE = "snowflake"
        }
    }

    @Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
    @StringDef(
        ConnectionPadding.AUTO,
        ConnectionPadding.ON,
        ConnectionPadding.OFF
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ConnectionPadding {
        companion object {
            const val AUTO = "auto"
            const val ON = "1"
            const val OFF = "0"
        }
    }

}