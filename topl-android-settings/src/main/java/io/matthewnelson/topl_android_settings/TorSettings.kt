package io.matthewnelson.topl_android_settings

abstract class TorSettings {

    abstract val disableNetwork: Boolean

    abstract val dnsPort: Int

    abstract val customTorrc: String?

    abstract val entryNodes: String?

    abstract val excludeNodes: String?

    abstract val exitNodes: String?

    abstract val httpTunnelPort: Int

    /**
     * Returns a list of supported bridges. The string value will include the name: meek_lite, obfs4
     */
    abstract val listOfSupportedBridges: List<String>

    abstract val proxyHost: String?

    abstract val proxyPassword: String?

    abstract val proxyPort: Int?

    abstract val proxySocks5Host: String?

    abstract val proxySocks5ServerPort: String?

    abstract val proxyType: String?

    abstract val proxyUser: String?

    abstract val reachableAddressPorts: String

    abstract val relayNickname: String?

    abstract val relayPort: Int

    /**
     * Could be a port "9050", or "auto"
     * */
    abstract val socksPort: String

    abstract val virtualAddressNetwork: String?

    abstract val hasBridges: Boolean

    abstract val hasConnectionPadding: Boolean

    abstract val hasCookieAuthentication: Boolean

    abstract val hasDebugLogs: Boolean

    abstract val hasDormantCanceledByStartup: Boolean

    abstract val hasIsolationAddressFlagForTunnel: Boolean

    abstract val hasOpenProxyOnAllInterfaces: Boolean

    abstract val hasReachableAddress: Boolean

    abstract val hasReducedConnectionPadding: Boolean

    abstract val hasSafeSocks: Boolean

    abstract val hasStrictNodes: Boolean

    abstract val hasTestSocks: Boolean

    abstract val isAutoMapHostsOnResolve: Boolean

    abstract val isRelay: Boolean

    abstract val runAsDaemon: Boolean

    abstract val transPort: Int?

    abstract val useSocks5: Boolean
}