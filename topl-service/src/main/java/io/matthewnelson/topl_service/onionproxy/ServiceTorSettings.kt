package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyString
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyList
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyInt
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyBoolean
import io.matthewnelson.topl_service.util.TorServicePrefs


/**
 * This class is for enabling the updating of settings in a standardized manner
 * such that library users can simply instantiate [TorServicePrefs], change things,
 * and then call [io.matthewnelson.topl_service.TorServiceController.restartTor] to have
 * them applied to the Tor Process.
 *
 * @param [defaults] Default values to fall back on if nothing is returned from [TorServicePrefs]
 * @param [torService] To instantiate [TorServicePrefs]
 * */
internal class ServiceTorSettings(
    private val defaults: TorSettings,
    torService: TorService
): TorSettings() {

    private val prefs = TorServicePrefs(torService)

    override val disableNetwork: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.DISABLE_NETWORK, defaults.disableNetwork)

    override val dnsPort: String
        get() = prefs.getString(PrefKeyString.DNS_PORT, defaults.dnsPort)
            ?: defaults.dnsPort

    override val customTorrc: String?
        get() = prefs.getString(PrefKeyString.CUSTOM_TORRC, defaults.customTorrc)

    override val entryNodes: String?
        get() = prefs.getString(PrefKeyString.ENTRY_NODES, defaults.entryNodes)

    override val excludeNodes: String?
        get() = prefs.getString(PrefKeyString.EXCLUDED_NODES, defaults.excludeNodes)

    override val exitNodes: String?
        get() = prefs.getString(PrefKeyString.EXIT_NODES, defaults.exitNodes)

    override val httpTunnelPort: String
        get() = prefs.getString(PrefKeyString.HTTP_TUNNEL_PORT, defaults.httpTunnelPort)
            ?: defaults.httpTunnelPort

    override val listOfSupportedBridges: List<String>
        get() = prefs.getList(PrefKeyList.LIST_OF_SUPPORTED_BRIDGES, defaults.listOfSupportedBridges)

    override val proxyHost: String?
        get() = prefs.getString(PrefKeyString.PROXY_HOST, defaults.proxyHost)

    override val proxyPassword: String?
        get() = prefs.getString(PrefKeyString.PROXY_PASSWORD, defaults.proxyPassword)

    override val proxyPort: Int?
        get() = prefs.getInt(PrefKeyInt.PROXY_PORT, defaults.proxyPort)

    override val proxySocks5Host: String?
        get() = prefs.getString(PrefKeyString.PROXY_SOCKS5_HOST, defaults.proxySocks5Host)

    override val proxySocks5ServerPort: Int?
        get() = prefs.getInt(PrefKeyInt.PROXY_SOCKS5_SERVER_PORT, defaults.proxySocks5ServerPort)

    override val proxyType: String?
        get() = prefs.getString(PrefKeyString.PROXY_TYPE, defaults.proxyType)

    override val proxyUser: String?
        get() = prefs.getString(PrefKeyString.PROXY_USER, defaults.proxyUser)

    override val reachableAddressPorts: String
        get() = prefs.getString(PrefKeyString.REACHABLE_ADDRESS_PORTS, defaults.reachableAddressPorts)
            ?: defaults.reachableAddressPorts

    override val relayNickname: String?
        get() = prefs.getString(PrefKeyString.RELAY_NICKNAME, defaults.relayNickname)

    override val relayPort: Int?
        get() = prefs.getInt(PrefKeyInt.RELAY_PORT, defaults.relayPort)
            ?: defaults.relayPort

    override val socksPort: String
        get() = prefs.getString(PrefKeyString.SOCKS_PORT, defaults.socksPort)
            ?: defaults.socksPort

    override val virtualAddressNetwork: String?
        get() = prefs.getString(PrefKeyString.VIRTUAL_ADDRESS_NETWORK, defaults.virtualAddressNetwork)

    override val hasBridges: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_BRIDGES, defaults.hasBridges)

    override val connectionPadding: String
        get() = prefs.getString(PrefKeyString.HAS_CONNECTION_PADDING, defaults.connectionPadding)
            ?: defaults.connectionPadding

    override val hasCookieAuthentication: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_COOKIE_AUTHENTICATION, defaults.hasCookieAuthentication)

    override val hasDebugLogs: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, defaults.hasDebugLogs)

    override val hasDormantCanceledByStartup: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_DORMANT_CANCELED_BY_STARTUP, defaults.hasDormantCanceledByStartup)

    override val hasIsolationAddressFlagForTunnel: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL, defaults.hasIsolationAddressFlagForTunnel)

    override val hasOpenProxyOnAllInterfaces: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_OPEN_PROXY_ON_ALL_INTERFACES, defaults.hasOpenProxyOnAllInterfaces)

    override val hasReachableAddress: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_REACHABLE_ADDRESS, defaults.hasReachableAddress)

    override val hasReducedConnectionPadding: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_REDUCED_CONNECTION_PADDING, defaults.hasReducedConnectionPadding)

    override val hasSafeSocks: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_SAFE_SOCKS, defaults.hasSafeSocks)

    override val hasStrictNodes: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_STRICT_NODES, defaults.hasStrictNodes)

    override val hasTestSocks: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.HAS_TEST_SOCKS, defaults.hasTestSocks)

    override val isAutoMapHostsOnResolve: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.IS_AUTO_MAP_HOSTS_ON_RESOLVE, defaults.isAutoMapHostsOnResolve)

    override val isRelay: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.IS_RELAY, defaults.isRelay)

    override val runAsDaemon: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.RUN_AS_DAEMON, defaults.runAsDaemon)

    override val transPort: String
        get() = prefs.getString(PrefKeyString.TRANS_PORT, defaults.transPort) ?: defaults.transPort

    override val useSocks5: Boolean
        get() = prefs.getBoolean(PrefKeyBoolean.USE_SOCKS5, defaults.useSocks5)

}