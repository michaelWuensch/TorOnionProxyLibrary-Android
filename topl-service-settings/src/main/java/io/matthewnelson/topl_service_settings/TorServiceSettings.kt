package io.matthewnelson.topl_service_settings

import android.content.Context
import io.matthewnelson.topl_android_settings.TorSettings
import io.matthewnelson.topl_service_prefs.PrefsKeys.IntKey
import io.matthewnelson.topl_service_prefs.PrefsKeys.StringKey
import io.matthewnelson.topl_service_prefs.PrefsKeys.ListKey
import io.matthewnelson.topl_service_prefs.PrefsKeys.BooleanKey
import io.matthewnelson.topl_service_prefs.TorServicePrefs


/**
 * This class is for enabling the updating of settings in a standardized manner
 * such that library users can simply instantiate [TorServicePrefs], change things,
 * and then call restartTor to have them applied.
 *
 * @param [defaults] Default values to fall back on if nothing is returned from [TorServicePrefs]
 * @param [context] To instantiate [TorServicePrefs]
 * */
class TorServiceSettings(private val defaults: TorSettings, context: Context): TorSettings() {

    private val prefs = TorServicePrefs(context)

    override val disableNetwork: Boolean
        get() = prefs.getBoolean(BooleanKey.DISABLE_NETWORK, defaults.disableNetwork)

    override val dnsPort: Int
        get() = prefs.getInt(IntKey.DNS_PORT, defaults.dnsPort)
            ?: defaults.dnsPort

    override val customTorrc: String?
        get() = prefs.getString(StringKey.CUSTOM_TORRC, defaults.customTorrc)

    override val entryNodes: String?
        get() = prefs.getString(StringKey.ENTRY_NODES, defaults.entryNodes)

    override val excludeNodes: String?
        get() = prefs.getString(StringKey.EXCLUDED_NODES, defaults.excludeNodes)

    override val exitNodes: String?
        get() = prefs.getString(StringKey.EXIT_NODES, defaults.exitNodes)

    override val httpTunnelPort: Int
        get() = prefs.getInt(IntKey.HTTP_TUNNEL_PORT, defaults.httpTunnelPort)
            ?: defaults.httpTunnelPort

    override val listOfSupportedBridges: List<String>
        get() = prefs.getList(ListKey.LIST_OF_SUPPORTED_BRIDGES, defaults.listOfSupportedBridges)

    override val proxyHost: String?
        get() = prefs.getString(StringKey.PROXY_HOST, defaults.proxyHost)

    override val proxyPassword: String?
        get() = prefs.getString(StringKey.PROXY_PASSWORD, defaults.proxyPassword)

    override val proxyPort: Int?
        get() = prefs.getInt(IntKey.PROXY_PORT, defaults.proxyPort)

    override val proxySocks5Host: String?
        get() = prefs.getString(StringKey.PROXY_SOCKS5_HOST, defaults.proxySocks5Host)

    override val proxySocks5ServerPort: Int?
        get() = prefs.getInt(IntKey.PROXY_SOCKS5_SERVER_PORT, defaults.proxySocks5ServerPort)

    override val proxyType: String?
        get() = prefs.getString(StringKey.PROXY_TYPE, defaults.proxyType)

    override val proxyUser: String?
        get() = prefs.getString(StringKey.PROXY_USER, defaults.proxyUser)

    override val reachableAddressPorts: String
        get() = prefs.getString(StringKey.REACHABLE_ADDRESS_PORTS, defaults.reachableAddressPorts)
            ?: defaults.reachableAddressPorts

    override val relayNickname: String?
        get() = prefs.getString(StringKey.RELAY_NICKNAME, defaults.relayNickname)

    override val relayPort: Int
        get() = prefs.getInt(IntKey.RELAY_PORT, defaults.relayPort)
            ?: defaults.relayPort

    override val socksPort: String
        get() = prefs.getString(StringKey.SOCKS_PORT, defaults.socksPort)
            ?: defaults.socksPort

    override val virtualAddressNetwork: String?
        get() = prefs.getString(StringKey.VIRTUAL_ADDRESS_NETWORK, defaults.virtualAddressNetwork)

    override val hasBridges: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_BRIDGES, defaults.hasBridges)

    override val hasConnectionPadding: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_CONNECTION_PADDING, defaults.hasConnectionPadding)

    override val hasCookieAuthentication: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_COOKIE_AUTHENTICATION, defaults.hasCookieAuthentication)

    override val hasDebugLogs: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_DEBUG_LOGS, defaults.hasDebugLogs)

    override val hasDormantCanceledByStartup: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_DORMANT_CANCELED_BY_STARTUP, defaults.hasDormantCanceledByStartup)

    override val hasIsolationAddressFlagForTunnel: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL, defaults.hasIsolationAddressFlagForTunnel)

    override val hasOpenProxyOnAllInterfaces: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_OPEN_PROXY_ON_ALL_INTERFACES, defaults.hasOpenProxyOnAllInterfaces)

    override val hasReachableAddress: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_REACHABLE_ADDRESS, defaults.hasReachableAddress)

    override val hasReducedConnectionPadding: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_REDUCED_CONNECTION_PADDING, defaults.hasReducedConnectionPadding)

    override val hasSafeSocks: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_SAFE_SOCKS, defaults.hasSafeSocks)

    override val hasStrictNodes: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_STRICT_NODES, defaults.hasStrictNodes)

    override val hasTestSocks: Boolean
        get() = prefs.getBoolean(BooleanKey.HAS_TEST_SOCKS, defaults.hasTestSocks)

    override val isAutoMapHostsOnResolve: Boolean
        get() = prefs.getBoolean(BooleanKey.IS_AUTO_MAP_HOSTS_ON_RESOLVE, defaults.isAutoMapHostsOnResolve)

    override val isRelay: Boolean
        get() = prefs.getBoolean(BooleanKey.IS_RELAY, defaults.isRelay)

    override val runAsDaemon: Boolean
        get() = prefs.getBoolean(BooleanKey.RUN_AS_DAEMON, defaults.runAsDaemon)

    override val transPort: Int?
        get() = prefs.getInt(IntKey.TRANS_PORT, defaults.transPort)

    override val useSocks5: Boolean
        get() = prefs.getBoolean(BooleanKey.USE_SOCKS5, defaults.useSocks5)

}