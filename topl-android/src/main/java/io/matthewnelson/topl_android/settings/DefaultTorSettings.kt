package io.matthewnelson.topl_android.settings

import io.matthewnelson.topl_android_settings.TorSettings
import java.util.*

/**
 * Provides some reasonable default settings. Create your own by extending [TorSettings]
 * or overriding this class.
 */
open class DefaultTorSettings : TorSettings() {

    override val disableNetwork: Boolean
        get() = true

    override val dnsPort: Int
        get() = 5400

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

    override val proxyPort: Int?
        get() = null

    override val proxySocks5Host: String?
        get() = "127.0.0.1"

    override val proxySocks5ServerPort: Int?
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
        get() = "9051"

    override val virtualAddressNetwork: String?
        get() = "10.192.0.1/10"

    override val hasBridges: Boolean
        get() = false

    override val hasConnectionPadding: Boolean
        get() = false

    override val hasCookieAuthentication: Boolean
        get() = true

    override val hasDebugLogs: Boolean
        get() = false

    override val hasDormantCanceledByStartup: Boolean
        get() = true

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

    override val transPort: Int?
        get() = 9141

    override val useSocks5: Boolean
        get() = true
}