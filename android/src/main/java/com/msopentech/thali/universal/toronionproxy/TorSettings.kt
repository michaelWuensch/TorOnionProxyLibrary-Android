package com.msopentech.thali.universal.toronionproxy

interface TorSettings {
    fun disableNetwork(): Boolean
    fun dnsPort(): String
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
    fun hasBridges(): Boolean
    fun hasConnectionPadding(): Boolean
    fun hasCookieAuthentication(): Boolean
    fun hasDebugLogs(): Boolean
    fun hasDormantCanceledByStartup(): Boolean
    fun hasIsolationAddressFlagForTunnel(): Boolean
    fun hasOpenProxyOnAllInterfaces(): Boolean
    fun hasReachableAddress(): Boolean
    fun hasReducedConnectionPadding(): Boolean
    fun hasSafeSocks(): Boolean
    fun hasStrictNodes(): Boolean
    fun hasTestSocks(): Boolean
    val isAutomapHostsOnResolve: Boolean
    val isRelay: Boolean
    fun runAsDaemon(): Boolean
    fun transPort(): String
    fun useSocks5(): Boolean
}