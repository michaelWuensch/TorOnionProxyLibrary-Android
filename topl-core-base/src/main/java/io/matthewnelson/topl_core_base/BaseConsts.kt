package io.matthewnelson.topl_core_base

import androidx.annotation.StringDef

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
        BroadcastType.EXCEPTION,
        BroadcastType.NOTICE,
        BroadcastType.WARN
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class BroadcastType {
        companion object {
            const val DEBUG = "DEBUG"
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