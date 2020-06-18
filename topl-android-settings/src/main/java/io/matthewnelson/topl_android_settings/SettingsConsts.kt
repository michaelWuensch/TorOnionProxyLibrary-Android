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

}