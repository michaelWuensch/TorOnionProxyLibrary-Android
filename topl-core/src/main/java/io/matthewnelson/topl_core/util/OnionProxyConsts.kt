package io.matthewnelson.topl_core.util

import androidx.annotation.StringDef
import io.matthewnelson.topl_core_base.TorStates

internal abstract class OnionProxyConsts: TorStates() {

    @StringDef(
        ConfigFile.CONTROL_PORT_FILE,
        ConfigFile.COOKIE_AUTH_FILE,
        ConfigFile.HOSTNAME_FILE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ConfigFile {
        companion object {
            const val CONTROL_PORT_FILE = "CONTROL_PORT_FILE"
            const val COOKIE_AUTH_FILE = "COOKIE_AUTH_FILE"
            const val HOSTNAME_FILE = "HOSTNAME_FILE"
        }
    }
}