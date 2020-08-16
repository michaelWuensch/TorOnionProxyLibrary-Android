/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash `74407114cbfa8ea6f2ac51417dda8be98d8aba86`. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify it
*     under the terms of the GNU General Public License as published by the
*     Free Software Foundation, either version 3 of the License, or (at your
*     option) any later version.
*
*     This program is distributed in the hope that it will be useful, but
*     WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*     or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
*     for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program. If not, see <https://www.gnu.org/licenses/>.
*
* `===========================================================================`
* `+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++`
* `===========================================================================`
*
* The following exception is an additional permission under section 7 of the
* GNU General Public License, version 3 (“GPLv3”).
*
*     "The Interfaces" is henceforth defined as Application Programming Interfaces
*     that are publicly available classes/functions/etc (ie: do not contain the
*     visibility modifiers `internal`, `private`, `protected`, or are within
*     classes/functions/etc that contain the aforementioned visibility modifiers)
*     to TorOnionProxyLibrary-Android users that are needed to implement
*     TorOnionProxyLibrary-Android and reside in ONLY the following modules:
*
*      - topl-core-base
*      - topl-service
*
*     The following are excluded from "The Interfaces":
*
*       - All other code
*
*     Linking TorOnionProxyLibrary-Android statically or dynamically with other
*     modules is making a combined work based on TorOnionProxyLibrary-Android.
*     Thus, the terms and conditions of the GNU General Public License cover the
*     whole combination.
*
*     As a special exception, the copyright holder of TorOnionProxyLibrary-Android
*     gives you permission to combine TorOnionProxyLibrary-Android program with free
*     software programs or libraries that are released under the GNU LGPL and with
*     independent modules that communicate with TorOnionProxyLibrary-Android solely
*     through "The Interfaces". You may copy and distribute such a system following
*     the terms of the GNU GPL for TorOnionProxyLibrary-Android and the licenses of
*     the other code concerned, provided that you include the source code of that
*     other code when and as the GNU GPL requires distribution of source code and
*     provided that you do not modify "The Interfaces".
*
*     Note that people who make modified versions of TorOnionProxyLibrary-Android
*     are not obligated to grant this special exception for their modified versions;
*     it is their choice whether to do so. The GNU General Public License gives
*     permission to release a modified version without this exception; this exception
*     also makes it possible to release a modified version which carries forward this
*     exception. If you modify "The Interfaces", this exception does not apply to your
*     modified version of TorOnionProxyLibrary-Android, and you must remove this
*     exception when you distribute your modified version.
* */
package io.matthewnelson.topl_service.service.components.onionproxy

import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyString
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyList
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyInt
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyBoolean
import io.matthewnelson.topl_service.prefs.TorServicePrefs

/**
 * This class enables the querying of [TorServicePrefs] to obtain values potentially set by
 * the User such that they are *preferred* over static/default values you may have set in the
 * [io.matthewnelson.topl_service.TorServiceController.Builder]'s constructor argument for
 * [TorSettings].
 *
 * It enables the updating of settings in a standardized manner such that library users can
 * simply instantiate [TorServicePrefs], change things, and then call
 * [io.matthewnelson.topl_service.TorServiceController.restartTor] to have them applied to the
 * Tor Process.
 *
 * It also makes designing of a settings screen much easier for your application.
 *
 * @param [servicePrefs] [TorServicePrefs] to query shared preferences for potential values set by user.
 * @param [defaultTorSettings] Default values to fall back on if nothing is returned from
 *   [TorServicePrefs]. Use [io.matthewnelson.topl_service.TorServiceController.getTorSettings] for
 *   this value.
 * */
class ServiceTorSettings(
    val servicePrefs: TorServicePrefs,
    val defaultTorSettings: TorSettings
): TorSettings() {

    override val disableNetwork: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.DISABLE_NETWORK, defaultTorSettings.disableNetwork)

    override val dnsPort: String
        get() = servicePrefs.getString(PrefKeyString.DNS_PORT, defaultTorSettings.dnsPort)
            ?: defaultTorSettings.dnsPort

    override val customTorrc: String?
        get() = servicePrefs.getString(PrefKeyString.CUSTOM_TORRC, defaultTorSettings.customTorrc)

    override val entryNodes: String?
        get() = servicePrefs.getString(PrefKeyString.ENTRY_NODES, defaultTorSettings.entryNodes)

    override val excludeNodes: String?
        get() = servicePrefs.getString(PrefKeyString.EXCLUDED_NODES, defaultTorSettings.excludeNodes)

    override val exitNodes: String?
        get() = servicePrefs.getString(PrefKeyString.EXIT_NODES, defaultTorSettings.exitNodes)

    override val httpTunnelPort: String
        get() = servicePrefs.getString(PrefKeyString.HTTP_TUNNEL_PORT, defaultTorSettings.httpTunnelPort)
            ?: defaultTorSettings.httpTunnelPort

    override val listOfSupportedBridges: List<String>
        get() = servicePrefs.getList(PrefKeyList.LIST_OF_SUPPORTED_BRIDGES, defaultTorSettings.listOfSupportedBridges)

    override val proxyHost: String?
        get() = servicePrefs.getString(PrefKeyString.PROXY_HOST, defaultTorSettings.proxyHost)

    override val proxyPassword: String?
        get() = servicePrefs.getString(PrefKeyString.PROXY_PASSWORD, defaultTorSettings.proxyPassword)

    override val proxyPort: Int?
        get() = servicePrefs.getInt(PrefKeyInt.PROXY_PORT, defaultTorSettings.proxyPort)

    override val proxySocks5Host: String?
        get() = servicePrefs.getString(PrefKeyString.PROXY_SOCKS5_HOST, defaultTorSettings.proxySocks5Host)

    override val proxySocks5ServerPort: Int?
        get() = servicePrefs.getInt(PrefKeyInt.PROXY_SOCKS5_SERVER_PORT, defaultTorSettings.proxySocks5ServerPort)

    override val proxyType: String?
        get() = servicePrefs.getString(PrefKeyString.PROXY_TYPE, defaultTorSettings.proxyType)

    override val proxyUser: String?
        get() = servicePrefs.getString(PrefKeyString.PROXY_USER, defaultTorSettings.proxyUser)

    override val reachableAddressPorts: String
        get() = servicePrefs.getString(PrefKeyString.REACHABLE_ADDRESS_PORTS, defaultTorSettings.reachableAddressPorts)
            ?: defaultTorSettings.reachableAddressPorts

    override val relayNickname: String?
        get() = servicePrefs.getString(PrefKeyString.RELAY_NICKNAME, defaultTorSettings.relayNickname)

    override val relayPort: Int?
        get() = servicePrefs.getInt(PrefKeyInt.RELAY_PORT, defaultTorSettings.relayPort)
            ?: defaultTorSettings.relayPort

    override val socksPort: String
        get() = servicePrefs.getString(PrefKeyString.SOCKS_PORT, defaultTorSettings.socksPort)
            ?: defaultTorSettings.socksPort

    override val virtualAddressNetwork: String?
        get() = servicePrefs.getString(PrefKeyString.VIRTUAL_ADDRESS_NETWORK, defaultTorSettings.virtualAddressNetwork)

    override val hasBridges: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_BRIDGES, defaultTorSettings.hasBridges)

    override val connectionPadding: String
        get() = servicePrefs.getString(PrefKeyString.HAS_CONNECTION_PADDING, defaultTorSettings.connectionPadding)
            ?: defaultTorSettings.connectionPadding

    override val hasCookieAuthentication: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_COOKIE_AUTHENTICATION, defaultTorSettings.hasCookieAuthentication)

    override val hasDebugLogs: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, defaultTorSettings.hasDebugLogs)

    override val hasDormantCanceledByStartup: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_DORMANT_CANCELED_BY_STARTUP, defaultTorSettings.hasDormantCanceledByStartup)

    override val hasIsolationAddressFlagForTunnel: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL, defaultTorSettings.hasIsolationAddressFlagForTunnel)

    override val hasOpenProxyOnAllInterfaces: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_OPEN_PROXY_ON_ALL_INTERFACES, defaultTorSettings.hasOpenProxyOnAllInterfaces)

    override val hasReachableAddress: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_REACHABLE_ADDRESS, defaultTorSettings.hasReachableAddress)

    override val hasReducedConnectionPadding: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_REDUCED_CONNECTION_PADDING, defaultTorSettings.hasReducedConnectionPadding)

    override val hasSafeSocks: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_SAFE_SOCKS, defaultTorSettings.hasSafeSocks)

    override val hasStrictNodes: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_STRICT_NODES, defaultTorSettings.hasStrictNodes)

    override val hasTestSocks: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_TEST_SOCKS, defaultTorSettings.hasTestSocks)

    override val isAutoMapHostsOnResolve: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.IS_AUTO_MAP_HOSTS_ON_RESOLVE, defaultTorSettings.isAutoMapHostsOnResolve)

    override val isRelay: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.IS_RELAY, defaultTorSettings.isRelay)

    override val runAsDaemon: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.RUN_AS_DAEMON, defaultTorSettings.runAsDaemon)

    override val transPort: String
        get() = servicePrefs.getString(PrefKeyString.TRANS_PORT, defaultTorSettings.transPort) ?: defaultTorSettings.transPort

    override val useSocks5: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.USE_SOCKS5, defaultTorSettings.useSocks5)

}