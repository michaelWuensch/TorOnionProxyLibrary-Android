package io.matthewnelson.topl_android_settings

/**
 * Extend this class and define your own settings.
 *
 * Keep in mind that Orbot and TorBrowser are the 2 most widely used applications
 * using Tor, and to use settings that won't conflict (those settings are annotated
 * as such, and with further details).
 *
 * Comments contain pretty standard default values. Would highly recommend reading up
 * on what's what in the spec -> https://gitweb.torproject.org/torspec.git/tree/
 *
 * @sample [io.matthewnelson.topl_service_settings.TestTorSettings]
 * */
abstract class TorSettings: SettingsConsts() {

    /**
     * Constants for some settings. Handy for declaring pre-defined values when you extend
     * this class to setup your own.
     *
     * The provided "default" values here are only a suggestion; see the spec for further detail.
     * */
    companion object {
        const val DEFAULT__DISABLE_NETWORK = true
        const val DEFAULT__CUSTOM_TORRC = ""
        const val DEFAULT__ENTRY_NODES = ""
        const val DEFAULT__EXCLUDED_NODES = ""
        const val DEFAULT__EXIT_NODES = ""
        const val DEFAULT__PROXY_HOST = ""
        const val DEFAULT__PROXY_PASSWORD = ""
        const val DEFAULT__PROXY_SOCKS5_HOST = "127.0.0.1"
        const val DEFAULT__PROXY_TYPE = ""
        const val DEFAULT__PROXY_USER = ""
        const val DEFAULT__REACHABLE_ADDRESS_PORTS = "*:80,*:443"
        const val DEFAULT__RELAY_NICKNAME = ""
        const val DEFAULT__HAS_BRIDGES = false
        const val DEFAULT__HAS_CONNECTION_PADDING = false
        const val DEFAULT__HAS_COOKIE_AUTHENTICATION = true
        const val DEFAULT__HAS_DEBUG_LOGS = false
        const val DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP = true
        const val DEFAULT__HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL = false
        const val DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES = false
        const val DEFAULT__HAS_REACHABLE_ADDRESS = false
        const val DEFAULT__HAS_REDUCED_CONNECTION_PADDING = true
        const val DEFAULT__HAS_SAFE_SOCKS = false
        const val DEFAULT__HAS_STRICT_NODES = false
        const val DEFAULT__HAS_TEST_SOCKS = false
        const val DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE = true
        const val DEFAULT__IS_RELAY = false
        const val DEFAULT__RUN_AS_DAEMON = true
        const val DEFAULT__USE_SOCKS5 = true
    }

    /**
     * See [DEFAULT__DISABLE_NETWORK]
     * */
    abstract val disableNetwork: Boolean

    /**
     * TorBrowser and Orbot use 5400 by default. It may be wise to pick something
     * that won't conflict.
     * */
    abstract val dnsPort: Int

    /**
     * See [DEFAULT__CUSTOM_TORRC]
     * */
    abstract val customTorrc: String?

    /**
     * See [DEFAULT__ENTRY_NODES]
     * */
    abstract val entryNodes: String?

    /**
     * See [DEFAULT__EXCLUDED_NODES]
     * */
    abstract val excludeNodes: String?

    /**
     * See [DEFAULT__EXIT_NODES]
     * */
    abstract val exitNodes: String?

    /**
     * TorBrowser and Orbot use 8218 by default. It may be wise to pick something
     * that won't conflict.
     * */
    abstract val httpTunnelPort: Int

    /**
     * Must have the transport binaries for obfs4 and snowflake, depending
     * on if you wish to include them in your bridges file to use.
     *
     * See [SettingsConsts.SupportedBridges] for options.
     */
    abstract val listOfSupportedBridges: List<@SupportedBridges String>

    /**
     * See [DEFAULT__PROXY_HOST]
     * */
    abstract val proxyHost: String?

    /**
     * See [DEFAULT__PROXY_PASSWORD]
     * */
    abstract val proxyPassword: String?

    /**
     * Default = [java.null]
     * */
    abstract val proxyPort: Int?

    /**
     * See [DEFAULT__PROXY_SOCKS5_HOST]
     *
     * For VPN things...
     * */
    abstract val proxySocks5Host: String?

    /**
     * Default = [java.null]
     *
     * For VPN things... Try ((Math.random() * 1000) + 10000).toInt()
     * */
    abstract val proxySocks5ServerPort: Int?

    /**
     * See [DEFAULT__PROXY_TYPE]
     * */
    abstract val proxyType: String?

    /**
     * See [DEFAULT__PROXY_USER]
     * */
    abstract val proxyUser: String?

    /**
     * See [DEFAULT__REACHABLE_ADDRESS_PORTS]
     * */
    abstract val reachableAddressPorts: String

    /**
     * See [DEFAULT__RELAY_NICKNAME]
     * */
    abstract val relayNickname: String?

    /**
     * TorBrowser and Orbot use 9001 by default. It may be wise to pick something
     * that won't conflict.
     * */
    abstract val relayPort: Int

    /**
     * Could be "auto" or a specified port, such as "9051".
     *
     * TorBrowser uses "9150", and Orbot uses "9050" by default. It may be wise
     * to pick something that won't conflict.
     * */
    abstract val socksPort: String

    /**
     * TorBrowser and Orbot use "10.192.0.1/10", it may be wise to pick something
     * that won't conflict if you are using this setting.
     * */
    abstract val virtualAddressNetwork: String?

    /**
     * See [DEFAULT__HAS_BRIDGES]
     * */
    abstract val hasBridges: Boolean

    /**
     * See [DEFAULT__HAS_CONNECTION_PADDING]
     * */
    abstract val hasConnectionPadding: Boolean

    /**
     * See [DEFAULT__HAS_COOKIE_AUTHENTICATION]
     * */
    abstract val hasCookieAuthentication: Boolean

    /**
     * See [DEFAULT__HAS_DEBUG_LOGS]
     * */
    abstract val hasDebugLogs: Boolean

    /**
     * See [DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP]
     * */
    abstract val hasDormantCanceledByStartup: Boolean

    /**
     * See [DEFAULT__HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL]
     * */
    abstract val hasIsolationAddressFlagForTunnel: Boolean

    /**
     * See [DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES]
     * */
    abstract val hasOpenProxyOnAllInterfaces: Boolean

    /**
     * See [DEFAULT__HAS_REACHABLE_ADDRESS]
     * */
    abstract val hasReachableAddress: Boolean

    /**
     * See [DEFAULT__HAS_REDUCED_CONNECTION_PADDING]
     * */
    abstract val hasReducedConnectionPadding: Boolean

    /**
     * See [DEFAULT__HAS_SAFE_SOCKS]
     * */
    abstract val hasSafeSocks: Boolean

    /**
     * See [DEFAULT__HAS_STRICT_NODES]
     * */
    abstract val hasStrictNodes: Boolean

    /**
     * See [DEFAULT__HAS_TEST_SOCKS]
     * */
    abstract val hasTestSocks: Boolean

    /**
     * See [DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE]
     * */
    abstract val isAutoMapHostsOnResolve: Boolean

    /**
     * See [DEFAULT__IS_RELAY]
     * */
    abstract val isRelay: Boolean

    /**
     * See [DEFAULT__RUN_AS_DAEMON]
     * */
    abstract val runAsDaemon: Boolean

    /**
     * Orbot and TorBrowser default to 9140. It may be wise to pick something
     * that won't conflict.
     * */
    abstract val transPort: Int?

    /**
     * See [DEFAULT__USE_SOCKS5]
     * */
    abstract val useSocks5: Boolean

//    override fun toString(): String {
//        return "TorSettings{ " +
//                "disableNetwork=${if (disableNetwork) TRUE else FALSE}, " +
//                "dnsPort=${dnsPort.toString()}, " +
//                "customTorrc=${customTorrc ?: NULL}, " +
//                "entryNodes=${entryNodes ?: NULL}, " +
//                "excludeNodes=${excludeNodes ?: NULL}, " +
//                "exitNodes=${exitNodes ?: NULL}, " +
//                "httpTunnelPort=${httpTunnelPort.toString()}, " +
//                "listOfSupportedBridges=${listOfSupportedBridges.joinToString(", ", "[", "]")}, " +
//                "proxyHost=${proxyHost ?: NULL}, " +
//                "proxyPassword=${proxyPassword ?: NULL}, " +
//                "proxyPort=${proxyPort?.toString() ?: NULL}, " +
//                "proxySocks5Host=${proxySocks5Host ?: NULL}, " +
//                "proxySocks5ServerPort=${proxySocks5ServerPort?.toString() ?: NULL}, " +
//                "proxyType=${proxyType ?: NULL}, " +
//                "proxyUser=${proxyUser ?: NULL}, " +
//                "reachableAddressPorts=$reachableAddressPorts, " +
//                "relayNickname=${relayNickname ?: NULL}, " +
//                "relayPort=${relayPort.toString()}, " +
//                "socksPort=$socksPort, " +
//                "virtualAddressNetwork=${virtualAddressNetwork ?: NULL}, " +
//                "hasBridges=${if (hasBridges) TRUE else FALSE}, " +
//                "hasConnectionPadding=${if (hasConnectionPadding) TRUE else FALSE}, " +
//                "hasCookieAuthentication=${if (hasCookieAuthentication) TRUE else FALSE}, " +
//                "hasDebugLogs=${if (hasDebugLogs) TRUE else FALSE}, " +
//                "hasDormantCanceledByStartup=${if (hasDormantCanceledByStartup) TRUE else FALSE}, " +
//                "hasIsolationAddressFlagForTunnel=${if (hasIsolationAddressFlagForTunnel) TRUE else FALSE}, " +
//                "hasOpenProxyOnAllInterfaces=${if (hasOpenProxyOnAllInterfaces) TRUE else FALSE}, " +
//                "hasReachableAddress=${if (hasReachableAddress) TRUE else FALSE}, " +
//                "hasReducedConnectionPadding=${if (hasReducedConnectionPadding) TRUE else FALSE}, " +
//                "hasSafeSocks=${if (hasSafeSocks) TRUE else FALSE}, " +
//                "hasStrictNodes=${if (hasStrictNodes) TRUE else FALSE}, " +
//                "hasTestSocks=${if (hasTestSocks) TRUE else FALSE}, " +
//                "isAutoMapHostsOnResolve=${if (isAutoMapHostsOnResolve) TRUE else FALSE}, " +
//                "isRelay=${if (isRelay) TRUE else FALSE}, " +
//                "runAsDaemon=${if (runAsDaemon) TRUE else FALSE}, " +
//                "transPort=${transPort?.toString() ?: NULL}, " +
//                "useSocks5=${if (useSocks5) TRUE else FALSE} }"
//    }
}