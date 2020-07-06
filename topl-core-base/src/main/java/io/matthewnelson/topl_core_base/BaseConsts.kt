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
package io.matthewnelson.topl_core_base

import androidx.annotation.StringDef
import java.lang.Error

abstract class BaseConsts {


    ////////////////////////////
    /// EventBroadcast Types ///
    ////////////////////////////
    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        BroadcastType.DEBUG,
        BroadcastType.ERROR,
        BroadcastType.EXCEPTION,
        BroadcastType.NOTICE,
        BroadcastType.WARN
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class BroadcastType {
        companion object {
            const val DEBUG = "DEBUG"
            const val ERROR = "ERROR"
            const val EXCEPTION = "EXCEPTION"
            const val NOTICE = "NOTICE"
            const val WARN = "WARN"
        }
    }


    ///////////////////
    /// Tor's State ///
    ///////////////////
    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        TorState.OFF,
        TorState.ON,
        TorState.STARTING,
        TorState.STOPPING
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class TorState {
        companion object {
            const val OFF = "Tor: Off"
            const val ON = "Tor: On"
            const val STARTING = "Tor: Starting"
            const val STOPPING = "Tor: Stopping"
        }
    }

    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        TorNetworkState.DISABLED,
        TorNetworkState.ENABLED
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class TorNetworkState {
        companion object {
            const val DISABLED = "Network: disabled"
            const val ENABLED = "Network: enabled"
        }
    }


    ///////////////////
    /// TorSettings ///
    ///////////////////
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
        ConnectionPadding.OFF,
        ConnectionPadding.ON
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ConnectionPadding {
        companion object {
            const val AUTO = "auto"
            const val OFF = "0"
            const val ON = "1"
        }
    }


    ///////////////////////////
    /// TorConfigFile Names ///
    ///////////////////////////
    @Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
    @StringDef(
        ConfigFileName.CONTROL_PORT,
        ConfigFileName.COOKIE_AUTH,
        ConfigFileName.DATA_DIR,
        ConfigFileName.GEO_IP,
        ConfigFileName.GEO_IPV_6,
        ConfigFileName.HOST,
        ConfigFileName.HIDDEN_SERVICE,
        ConfigFileName.RESOLVE_CONF,
        ConfigFileName.TOR_EXECUTABLE,
        ConfigFileName.TORRC
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ConfigFileName {
        companion object {
            const val CONTROL_PORT = "control.txt"
            const val COOKIE_AUTH = "control_auth_cookie"
            const val DATA_DIR = "lib/tor"
            const val GEO_IP = "geoip"
            const val GEO_IPV_6 = "geoip6"
            const val HOST = "hostname"
            const val HIDDEN_SERVICE = "hiddenservice"
            const val RESOLVE_CONF = "resolv.conf"
            const val TOR_EXECUTABLE = "libTor.so"
            const val TORRC = "torrc"
        }
    }
}