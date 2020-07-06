/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash 74407114cbfa8ea6f2ac51417dda8be98d8aba86. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     This program is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
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