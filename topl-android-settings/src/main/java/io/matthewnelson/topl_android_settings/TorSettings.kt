package io.matthewnelson.topl_android_settings

/**
 * Extend this class and create your own settings.
 *
 * Keep in mind that Orbot and TorBrowser are the 2 most used applications that
 * use Tor, and to use settings that won't conflict.
 *
 * Comments are pretty standard default values. Would highly recommend reading up
 * on what's what in the spec -> https://gitweb.torproject.org/torspec.git/tree/
 * */
abstract class TorSettings {

    /**
     * True
     * */
    abstract val disableNetwork: Boolean

    /**
     * TorBrowser and Orbot use 5400 by default. It may be wise to pick something
     * that won't conflict.
     * */
    abstract val dnsPort: Int

    abstract val customTorrc: String?

    abstract val entryNodes: String?

    abstract val excludeNodes: String?

    abstract val exitNodes: String?

    abstract val httpTunnelPort: Int

    /**
     * Returns a list of supported bridges. The string value will include
     * the name: meek_lite, obfs4, snowflake.
     *
     * Must have the transport binaries for obfs4/meek and snowflake, depending
     * one what protocol you wish to use in your bridges file.
     */
    abstract val listOfSupportedBridges: List<String>

    abstract val proxyHost: String?

    abstract val proxyPassword: String?

    abstract val proxyPort: Int?

    /**
     * For VPN things... Typically 127.0.0.1
     * */
    abstract val proxySocks5Host: String?

    /**
     * For VPN things... Try ((Math.random() * 1000) + 10000).toInt()
     * */
    abstract val proxySocks5ServerPort: Int?

    abstract val proxyType: String?

    abstract val proxyUser: String?

    /**
     * Typically "*:80,*:443"
     * */
    abstract val reachableAddressPorts: String

    abstract val relayNickname: String?

    /**
     * TorBrowser and Orbot use 9001 by default. It may be wise to pick something
     * that won't conflict.
     * */
    abstract val relayPort: Int

    /**
     * Could be a port "9051", or "auto".
     *
     * TorBrowser uses 9150, and Orbot uses 9050 by default. It may be wise
     * to pick something that won't conflict.
     * */
    abstract val socksPort: String

    abstract val virtualAddressNetwork: String?

    abstract val hasBridges: Boolean

    abstract val hasConnectionPadding: Boolean

    /**
     * True
     * */
    abstract val hasCookieAuthentication: Boolean

    abstract val hasDebugLogs: Boolean

    /**
     * True
     * */
    abstract val hasDormantCanceledByStartup: Boolean

    abstract val hasIsolationAddressFlagForTunnel: Boolean

    abstract val hasOpenProxyOnAllInterfaces: Boolean

    abstract val hasReachableAddress: Boolean

    abstract val hasReducedConnectionPadding: Boolean

    /**
     * False
     * */
    abstract val hasSafeSocks: Boolean

    /**
     * False
     * */
    abstract val hasStrictNodes: Boolean

    /**
     * False
     * */
    abstract val hasTestSocks: Boolean

    /**
     * True
     * */
    abstract val isAutoMapHostsOnResolve: Boolean

    abstract val isRelay: Boolean

    /**
     * True
     * */
    abstract val runAsDaemon: Boolean

    /**
     * Orbot and TorBrowser default to 9140. It may be wise to pick something
     * that won't conflict.
     * */
    abstract val transPort: Int?

    abstract val useSocks5: Boolean
}