package com.msopentech.thali.toronionproxy.settings

import io.matthewnelson.topl_settings.TorSettings
import java.util.*

/**
 * Provides some reasonable default settings. Override this class or create a new implementation to
 * make changes.
 */
open class DefaultSettings : TorSettings() {

    override val disableNetwork: Boolean
        get() = true

    override val dnsPort: String
        get() = "5400"

    override val customTorrc: String?
        get() = null

    override val entryNodes: String?
        get() = null

    override val excludeNodes: String?
        get() = null

    override val exitNodes: String?
        get() = null

    override val httpTunnelPort: Int
        get() = 8118

    override val listOfSupportedBridges: List<String>
        get() = ArrayList()

    override val proxyHost: String?
        get() = null

    override val proxyPassword: String?
        get() = null

    override val proxyPort: String?
        get() = null

    override val proxySocks5Host: String?
        get() = null

    override val proxySocks5ServerPort: String?
        get() = null

    override val proxyType: String?
        get() = null

    override val proxyUser: String?
        get() = null

    override val reachableAddressPorts: String
        get() = "*:80,*:443"

    override val relayNickname: String?
        get() = null

    override val relayPort: Int
        get() = 9001

    override val socksPort: String
        get() = "9050"

    override val virtualAddressNetwork: String?
        get() = null

    override val hasBridges: Boolean
        get() = true

    override val hasConnectionPadding: Boolean
        get() = false

    override val hasCookieAuthentication: Boolean
        get() = true

    override val hasDebugLogs: Boolean
        get() = false

    override val hasDormantCanceledByStartup: Boolean
        get() = false

    override val hasIsolationAddressFlagForTunnel: Boolean
        get() = false

    override val hasOpenProxyOnAllInterfaces: Boolean
        get() = false

    override val hasReachableAddress: Boolean
        get() = false

    override val hasReducedConnectionPadding: Boolean
        get() = true

    override val hasSafeSocks: Boolean
        get() = false

    override val hasStrictNodes: Boolean
        get() = false

    override val hasTestSocks: Boolean
        get() = false

    override val isAutoMapHostsOnResolve: Boolean
        get() = true

    override val isRelay: Boolean
        get() = false

    override val runAsDaemon: Boolean
        get() = true

    override val transPort: String
        get() = "9040"

    override val useSocks5: Boolean
        get() = false
}