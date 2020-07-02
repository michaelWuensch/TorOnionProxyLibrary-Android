package io.matthewnelson.topl_core.util

import androidx.annotation.StringDef
import io.matthewnelson.topl_core_base.BaseConsts

abstract class CoreConsts: BaseConsts() {

    @StringDef(
        ConfigFile.CONTROL_PORT_FILE,
        ConfigFile.COOKIE_AUTH_FILE,
        ConfigFile.HOSTNAME_FILE
    )
    @Retention(AnnotationRetention.SOURCE)
    internal annotation class ConfigFile {
        companion object {
            const val CONTROL_PORT_FILE = "ControlPort file"
            const val COOKIE_AUTH_FILE = "CookieAuth file"
            const val HOSTNAME_FILE = "Hostname file"
        }
    }
}