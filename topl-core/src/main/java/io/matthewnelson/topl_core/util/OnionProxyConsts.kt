package io.matthewnelson.topl_core.util

import androidx.annotation.StringDef
import io.matthewnelson.topl_core_base.TorStates

abstract class OnionProxyConsts: TorStates() {

    @StringDef(
        ConfigFile.CONTROL_PORT_FILE,
        ConfigFile.COOKIE_AUTH_FILE,
        ConfigFile.HOSTNAME_FILE
    )
    @Retention(AnnotationRetention.SOURCE)
    internal annotation class ConfigFile {
        companion object {
            const val CONTROL_PORT_FILE = "control_port_file"
            const val COOKIE_AUTH_FILE = "cookie_auth_file"
            const val HOSTNAME_FILE = "hostname_file"
        }
    }
}