package io.matthewnelson.topl_service_base

import androidx.annotation.StringDef
import io.matthewnelson.topl_core_base.BaseConsts

abstract class BaseServiceConsts: BaseConsts() {


    ///////////////////////////////
    /// BackgroundManagerPolicy ///
    ///////////////////////////////
    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.PROPERTY,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.TYPE
    )
    @StringDef(
        BackgroundPolicy.RESPECT_RESOURCES,
        BackgroundPolicy.RUN_IN_FOREGROUND
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class BackgroundPolicy {
        companion object {
            private const val BACKGROUND_POLICY = "BackgroundPolicy_"
            const val RESPECT_RESOURCES = "${BACKGROUND_POLICY}RESPECT_RESOURCES"
            const val RUN_IN_FOREGROUND = "${BACKGROUND_POLICY}RUN_IN_FOREGROUND"
        }
    }


    ///////////////////////
    /// TorServicePrefs ///
    ///////////////////////
    @StringDef(
        PrefKeyBoolean.DISABLE_NETWORK,
        PrefKeyBoolean.HAS_BRIDGES,
        PrefKeyBoolean.HAS_COOKIE_AUTHENTICATION,
        PrefKeyBoolean.HAS_DEBUG_LOGS,
        PrefKeyBoolean.HAS_DORMANT_CANCELED_BY_STARTUP,
        PrefKeyBoolean.HAS_OPEN_PROXY_ON_ALL_INTERFACES,
        PrefKeyBoolean.HAS_REACHABLE_ADDRESS,
        PrefKeyBoolean.HAS_REDUCED_CONNECTION_PADDING,
        PrefKeyBoolean.HAS_SAFE_SOCKS,
        PrefKeyBoolean.HAS_STRICT_NODES,
        PrefKeyBoolean.HAS_TEST_SOCKS,
        PrefKeyBoolean.IS_AUTO_MAP_HOSTS_ON_RESOLVE,
        PrefKeyBoolean.IS_RELAY,
        PrefKeyBoolean.RUN_AS_DAEMON,
        PrefKeyBoolean.USE_SOCKS5
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class PrefKeyBoolean {
        companion object {
            // Keys for returning Booleans
            const val DISABLE_NETWORK = "DISABLE_NETWORK"
            const val HAS_BRIDGES = "HAS_BRIDGES"
            const val HAS_COOKIE_AUTHENTICATION = "HAS_COOKIE_AUTHENTICATION"
            const val HAS_DEBUG_LOGS = "HAS_DEBUG_LOGS"
            const val HAS_DORMANT_CANCELED_BY_STARTUP = "HAS_DORMANT_CANCELED_BY_STARTUP"
            const val HAS_OPEN_PROXY_ON_ALL_INTERFACES = "HAS_OPEN_PROXY_ON_ALL_INTERFACES"
            const val HAS_REACHABLE_ADDRESS = "HAS_REACHABLE_ADDRESS"
            const val HAS_REDUCED_CONNECTION_PADDING = "HAS_REDUCED_CONNECTION_PADDING"
            const val HAS_SAFE_SOCKS = "HAS_SAFE_SOCKS"
            const val HAS_STRICT_NODES = "HAS_STRICT_NODES"
            const val HAS_TEST_SOCKS = "HAS_TEST_SOCKS"
            const val IS_AUTO_MAP_HOSTS_ON_RESOLVE = "IS_AUTO_MAP_HOSTS_ON_RESOLVE"
            const val IS_RELAY = "IS_RELAY"
            const val RUN_AS_DAEMON = "RUN_AS_DAEMON"
            const val USE_SOCKS5 = "USE_SOCKS5"
        }
    }

    @StringDef(
        PrefKeyInt.DORMANT_CLIENT_TIMEOUT,
        PrefKeyInt.PROXY_PORT,
        PrefKeyInt.PROXY_SOCKS5_SERVER_PORT
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class PrefKeyInt {
        companion object {
            // Keys for returning Ints
            const val DORMANT_CLIENT_TIMEOUT = "DORMANT_CLIENT_TIMEOUT"
            const val PROXY_PORT = "PROXY_PORT"
            const val PROXY_SOCKS5_SERVER_PORT = "PROXY_SOCKS5_SERVER_PORT"
        }
    }

    @StringDef(
        PrefKeyList.DNS_PORT_ISOLATION_FLAGS,
        PrefKeyList.HTTP_TUNNEL_PORT_ISOLATION_FLAGS,
        PrefKeyList.SOCKS_PORT_ISOLATION_FLAGS,
        PrefKeyList.TRANS_PORT_ISOLATION_FLAGS,
        PrefKeyList.USER_DEFINED_BRIDGES
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class PrefKeyList {
        companion object {
            // Keys for returning Lists
            const val DNS_PORT_ISOLATION_FLAGS = "DNS_PORT_ISOLATION_FLAGS"
            const val HTTP_TUNNEL_PORT_ISOLATION_FLAGS = "HTTP_PORT_ISOLATION_FLAGS"
            const val SOCKS_PORT_ISOLATION_FLAGS = "SOCKS_PORT_ISOLATION_FLAGS"
            const val TRANS_PORT_ISOLATION_FLAGS = "TRANS_PORT_ISOLATION_FLAGS"
            const val USER_DEFINED_BRIDGES = "USER_DEFINED_BRIDGES"
        }
    }

    @StringDef(
        PrefKeyString.DNS_PORT,
        PrefKeyString.CUSTOM_TORRC,
        PrefKeyString.ENTRY_NODES,
        PrefKeyString.EXCLUDED_NODES,
        PrefKeyString.EXIT_NODES,
        PrefKeyString.HTTP_TUNNEL_PORT,
        PrefKeyString.PROXY_HOST,
        PrefKeyString.PROXY_PASSWORD,
        PrefKeyString.PROXY_SOCKS5_HOST,
        PrefKeyString.PROXY_TYPE,
        PrefKeyString.PROXY_USER,
        PrefKeyString.REACHABLE_ADDRESS_PORTS,
        PrefKeyString.RELAY_NICKNAME,
        PrefKeyString.RELAY_PORT,
        PrefKeyString.SOCKS_PORT,
        PrefKeyString.VIRTUAL_ADDRESS_NETWORK,
        PrefKeyString.HAS_CONNECTION_PADDING,
        PrefKeyString.TRANS_PORT
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class PrefKeyString {
        companion object {
            // Keys for returning Strings
            const val DNS_PORT = "DNS_PORT"
            const val CUSTOM_TORRC = "CUSTOM_TORRC"
            const val ENTRY_NODES = "ENTRY_NODES"
            const val EXCLUDED_NODES = "EXCLUDED_NODES"
            const val EXIT_NODES = "EXIT_NODES"
            const val HTTP_TUNNEL_PORT = "HTTP_TUNNEL_PORT"
            const val PROXY_HOST = "PROXY_HOST"
            const val PROXY_PASSWORD = "PROXY_PASSWORD"
            const val PROXY_SOCKS5_HOST = "PROXY_SOCKS5_HOST"
            const val PROXY_TYPE = "PROXY_TYPE"
            const val PROXY_USER = "PROXY_USER"
            const val REACHABLE_ADDRESS_PORTS = "REACHABLE_ADDRESS_PORTS"
            const val RELAY_NICKNAME = "RELAY_NICKNAME"
            const val RELAY_PORT = "RELAY_PORT"
            const val SOCKS_PORT = "SOCKS_PORT"
            const val VIRTUAL_ADDRESS_NETWORK = "VIRTUAL_ADDRESS_NETWORK"
            const val HAS_CONNECTION_PADDING = "HAS_CONNECTION_PADDING"
            const val TRANS_PORT = "TRANS_PORT"
        }
    }
}