package com.msopentech.thali.universal.toronionproxy

import java.util.*

/**
 * Provides some reasonable default settings. Override this class or create a new implementation to
 * make changes.
 */
open class DefaultSettings : TorSettings {
    override fun disableNetwork(): Boolean {
        return true
    }

    override fun dnsPort(): String {
        return "5400"
    }

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

    override fun hasBridges(): Boolean {
        return false
    }

    override fun hasConnectionPadding(): Boolean {
        return false
    }

    override fun hasCookieAuthentication(): Boolean {
        return true
    }

    override fun hasDebugLogs(): Boolean {
        return false
    }

    override fun hasDormantCanceledByStartup(): Boolean {
        return false
    }

    override fun hasIsolationAddressFlagForTunnel(): Boolean {
        return false
    }

    override fun hasOpenProxyOnAllInterfaces(): Boolean {
        return false
    }

    override fun hasReachableAddress(): Boolean {
        return false
    }

    override fun hasReducedConnectionPadding(): Boolean {
        return true
    }

    override fun hasSafeSocks(): Boolean {
        return false
    }

    override fun hasStrictNodes(): Boolean {
        return false
    }

    override fun hasTestSocks(): Boolean {
        return false
    }

    override val isAutomapHostsOnResolve: Boolean
        get() = true

    override val isRelay: Boolean
        get() = false

    override fun runAsDaemon(): Boolean {
        return true
    }

    override fun transPort(): String {
        return "9040"
    }

    override fun useSocks5(): Boolean {
        return false
    }
}