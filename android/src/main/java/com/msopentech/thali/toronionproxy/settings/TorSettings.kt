package com.msopentech.thali.toronionproxy.settings

interface TorSettings {

    val disableNetwork: Boolean

    val dnsPort: String

    val customTorrc: String?

    val entryNodes: String?

    val excludeNodes: String?

    val exitNodes: String?

    val httpTunnelPort: Int

    /**
     * Returns a list of supported bridges. The string value will include the name: meek_lite, obfs4
     */
    val listOfSupportedBridges: List<String>

    val proxyHost: String?

    val proxyPassword: String?

    val proxyPort: String?

    val proxySocks5Host: String?

    val proxySocks5ServerPort: String?

    val proxyType: String?

    val proxyUser: String?

    val reachableAddressPorts: String

    val relayNickname: String?

    val relayPort: Int

    val socksPort: String

    val virtualAddressNetwork: String?

    val hasBridges: Boolean

    val hasConnectionPadding: Boolean

    val hasCookieAuthentication: Boolean

    val hasDebugLogs: Boolean

    val hasDormantCanceledByStartup: Boolean

    val hasIsolationAddressFlagForTunnel: Boolean

    val hasOpenProxyOnAllInterfaces: Boolean

    val hasReachableAddress: Boolean

    val hasReducedConnectionPadding: Boolean

    val hasSafeSocks: Boolean

    val hasStrictNodes: Boolean

    val hasTestSocks: Boolean

    val isAutoMapHostsOnResolve: Boolean

    val isRelay: Boolean

    val runAsDaemon: Boolean

    val transPort: String

    val useSocks5: Boolean
}