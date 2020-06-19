package io.matthewnelson.topl_service_prefs

import androidx.annotation.StringDef

abstract class PrefsKeys {

    @StringDef(
        BooleanKey.DISABLE_NETWORK,
        BooleanKey.HAS_BRIDGES,
        BooleanKey.HAS_COOKIE_AUTHENTICATION,
        BooleanKey.HAS_DEBUG_LOGS,
        BooleanKey.HAS_DORMANT_CANCELED_BY_STARTUP,
        BooleanKey.HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL,
        BooleanKey.HAS_OPEN_PROXY_ON_ALL_INTERFACES,
        BooleanKey.HAS_REACHABLE_ADDRESS,
        BooleanKey.HAS_REDUCED_CONNECTION_PADDING,
        BooleanKey.HAS_SAFE_SOCKS,
        BooleanKey.HAS_STRICT_NODES,
        BooleanKey.HAS_TEST_SOCKS,
        BooleanKey.IS_AUTO_MAP_HOSTS_ON_RESOLVE,
        BooleanKey.IS_RELAY,
        BooleanKey.RUN_AS_DAEMON,
        BooleanKey.USE_SOCKS5
        )
    @Retention(AnnotationRetention.SOURCE)
    annotation class BooleanKey {
        companion object {
            // Keys for returning Booleans
            const val DISABLE_NETWORK = "DISABLE_NETWORK"
            const val HAS_BRIDGES = "HAS_BRIDGES"
            const val HAS_COOKIE_AUTHENTICATION = "HAS_COOKIE_AUTHENTICATION"
            const val HAS_DEBUG_LOGS = "HAS_DEBUG_LOGS"
            const val HAS_DORMANT_CANCELED_BY_STARTUP = "HAS_DORMANT_CANCELED_BY_STARTUP"
            const val HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL = "HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL"
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
        IntKey.PROXY_PORT,
        IntKey.PROXY_SOCKS5_SERVER_PORT,
        IntKey.RELAY_PORT
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class IntKey {
        companion object {
            // Keys for returning Ints
            const val PROXY_PORT = "PROXY_PORT"
            const val PROXY_SOCKS5_SERVER_PORT = "PROXY_SOCKS5_SERVER_PORT"
            const val RELAY_PORT = "RELAY_PORT"
        }
    }

    @StringDef(
        ListKey.LIST_OF_SUPPORTED_BRIDGES
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ListKey {
        companion object {
            // Keys for returning Lists
            const val LIST_OF_SUPPORTED_BRIDGES = "LIST_OF_SUPPORTED_BRIDGES"
        }
    }

    @StringDef(
        StringKey.DNS_PORT,
        StringKey.CUSTOM_TORRC,
        StringKey.ENTRY_NODES,
        StringKey.EXCLUDED_NODES,
        StringKey.EXIT_NODES,
        StringKey.HTTP_TUNNEL_PORT,
        StringKey.PROXY_HOST,
        StringKey.PROXY_PASSWORD,
        StringKey.PROXY_SOCKS5_HOST,
        StringKey.PROXY_TYPE,
        StringKey.PROXY_USER,
        StringKey.REACHABLE_ADDRESS_PORTS,
        StringKey.RELAY_NICKNAME,
        StringKey.SOCKS_PORT,
        StringKey.VIRTUAL_ADDRESS_NETWORK,
        StringKey.HAS_CONNECTION_PADDING,
        StringKey.TRANS_PORT
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class StringKey {
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
            const val SOCKS_PORT = "SOCKS_PORT"
            const val VIRTUAL_ADDRESS_NETWORK = "VIRTUAL_ADDRESS_NETWORK"
            const val HAS_CONNECTION_PADDING = "HAS_CONNECTION_PADDING"
            const val TRANS_PORT = "TRANS_PORT"
        }
    }
}