package io.matthewnelson.topl_android.util

import androidx.annotation.StringDef

internal abstract class OnionProxyConsts {

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