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
class ServiceTorSettings internal constructor(
    val servicePrefs: TorServicePrefs,
    val defaultTorSettings: TorSettings
): TorSettings() {

    override val disableNetwork: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.DISABLE_NETWORK, defaultTorSettings.disableNetwork)

    fun disableNetworkSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.disableNetwork)
            servicePrefs.remove(PrefKeyBoolean.DISABLE_NETWORK)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.DISABLE_NETWORK, boolean)
    }

    override val dnsPort: String
        get() = servicePrefs.getString(PrefKeyString.DNS_PORT, defaultTorSettings.dnsPort)
            ?: defaultTorSettings.dnsPort

    @Throws(IllegalArgumentException::class)
    fun dnsPortSave(dnsPort: String) {
        when {
            dnsPort == defaultTorSettings.dnsPort -> {
                servicePrefs.remove(PrefKeyString.DNS_PORT)
            }
            checkPortSelection(dnsPort) -> {
                servicePrefs.putString(PrefKeyString.DNS_PORT, dnsPort)
            }
            else -> {
                throw IllegalArgumentException(
                    "DNS Port must be 0 (disabled), auto, or between 1024 and 65535"
                )
            }
        }
    }

    override val dnsPortIsolationFlags: List<@IsolationFlag String>?
        get() = servicePrefs.getList(
            PrefKeyList.DNS_PORT_ISOLATION_FLAGS,
            defaultTorSettings.dnsPortIsolationFlags ?: arrayListOf()
        )

    fun dnsPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>) {
        if (isolationFlags == defaultTorSettings.dnsPortIsolationFlags) {
            servicePrefs.remove(PrefKeyList.DNS_PORT_ISOLATION_FLAGS)
        } else {
            servicePrefs.putList(PrefKeyList.DNS_PORT_ISOLATION_FLAGS, isolationFlags)
        }
    }

    override val customTorrc: String?
        get() = servicePrefs.getString(PrefKeyString.CUSTOM_TORRC, defaultTorSettings.customTorrc)

    fun customTorrcSave(customTorrc: String?) {
        if (customTorrc == defaultTorSettings.customTorrc)
            servicePrefs.remove(PrefKeyString.CUSTOM_TORRC)
        else
            servicePrefs.putString(PrefKeyString.CUSTOM_TORRC, customTorrc)
    }

    override val entryNodes: String?
        get() = servicePrefs.getString(PrefKeyString.ENTRY_NODES, defaultTorSettings.entryNodes)

    fun entryNodesSave(entryNodes: String?) {
        if (entryNodes == defaultTorSettings.entryNodes)
            servicePrefs.remove(PrefKeyString.ENTRY_NODES)
        else
            servicePrefs.putString(PrefKeyString.ENTRY_NODES, entryNodes)
    }

    override val excludeNodes: String?
        get() = servicePrefs.getString(PrefKeyString.EXCLUDED_NODES, defaultTorSettings.excludeNodes)

    fun excludeNodesSave(excludeNodes: String?) {
        if (excludeNodes == defaultTorSettings.excludeNodes)
            servicePrefs.remove(PrefKeyString.EXCLUDED_NODES)
        else
            servicePrefs.putString(PrefKeyString.EXCLUDED_NODES, excludeNodes)
    }

    override val exitNodes: String?
        get() = servicePrefs.getString(PrefKeyString.EXIT_NODES, defaultTorSettings.exitNodes)

    fun exitNodesSave(exitNodes: String?) {
        if (exitNodes == defaultTorSettings.exitNodes)
            servicePrefs.remove(PrefKeyString.EXIT_NODES)
        else
            servicePrefs.putString(PrefKeyString.EXIT_NODES, exitNodes)
    }

    override val httpTunnelPort: String
        get() = servicePrefs.getString(PrefKeyString.HTTP_TUNNEL_PORT, defaultTorSettings.httpTunnelPort)
            ?: defaultTorSettings.httpTunnelPort

    @Throws(IllegalArgumentException::class)
    fun httpTunnelPortSave(httpPort: String) {
        when {
            httpPort == defaultTorSettings.httpTunnelPort -> {
                servicePrefs.remove(PrefKeyString.HTTP_TUNNEL_PORT)
            }
            checkPortSelection(httpPort) -> {
                servicePrefs.putString(PrefKeyString.HTTP_TUNNEL_PORT, httpPort)
            }
            else -> {
                throw IllegalArgumentException(
                    "HTTP Port must be 0 (disabled), auto, or between 1024 and 65535"
                )
            }
        }
    }

    override val httpTunnelPortIsolationFlags: List<@IsolationFlag String>?
        get() = servicePrefs.getList(
            PrefKeyList.HTTP_TUNNEL_PORT_ISOLATION_FLAGS,
            defaultTorSettings.httpTunnelPortIsolationFlags ?: arrayListOf()
        )

    fun httpPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>) {
        if (isolationFlags == defaultTorSettings.httpTunnelPortIsolationFlags) {
            servicePrefs.remove(PrefKeyList.HTTP_TUNNEL_PORT_ISOLATION_FLAGS)
        } else {
            servicePrefs.putList(PrefKeyList.HTTP_TUNNEL_PORT_ISOLATION_FLAGS, isolationFlags)
        }
    }

    override val listOfSupportedBridges: List<@SupportedBridgeType String>
        get() = defaultTorSettings.listOfSupportedBridges

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

    override val proxyType: @ProxyType String
        get() = servicePrefs.getString(PrefKeyString.PROXY_TYPE, defaultTorSettings.proxyType) ?: defaultTorSettings.proxyType

    override val proxyUser: String?
        get() = servicePrefs.getString(PrefKeyString.PROXY_USER, defaultTorSettings.proxyUser)

    override val reachableAddressPorts: String
        get() = servicePrefs.getString(PrefKeyString.REACHABLE_ADDRESS_PORTS, defaultTorSettings.reachableAddressPorts)
            ?: defaultTorSettings.reachableAddressPorts

    fun reachableAddressPortsSave(reachableAddressPorts: String) {
        if (reachableAddressPorts == defaultTorSettings.reachableAddressPorts)
            servicePrefs.remove(PrefKeyString.REACHABLE_ADDRESS_PORTS)
        else
            servicePrefs.putString(PrefKeyString.REACHABLE_ADDRESS_PORTS, reachableAddressPorts)
    }

    override val relayNickname: String?
        get() = servicePrefs.getString(PrefKeyString.RELAY_NICKNAME, defaultTorSettings.relayNickname)

    override val relayPort: String
        get() = servicePrefs.getString(PrefKeyString.RELAY_PORT, defaultTorSettings.relayPort)
            ?: defaultTorSettings.relayPort

    @Throws(IllegalArgumentException::class)
    fun relayPortSave(relayPort: String) {
        when {
            relayPort == defaultTorSettings.relayPort -> {
                servicePrefs.remove(PrefKeyString.RELAY_PORT)
            }
            checkPortSelection(relayPort) || relayPort.isEmpty() -> {
                servicePrefs.putString(PrefKeyString.RELAY_PORT, relayPort)
            }
            else -> {
                throw IllegalArgumentException(
                    "Relay Port must be empty, 0 (disabled), auto, or between 1024 and 65535"
                )
            }
        }
    }

    override val socksPort: String
        get() = servicePrefs.getString(PrefKeyString.SOCKS_PORT, defaultTorSettings.socksPort)
            ?: defaultTorSettings.socksPort

    @Throws(IllegalArgumentException::class)
    fun socksPortSave(socksPort: String) {
        when {
            socksPort == defaultTorSettings.socksPort -> {
                servicePrefs.remove(PrefKeyString.SOCKS_PORT)
            }
            checkPortSelection(socksPort) -> {
                servicePrefs.putString(PrefKeyString.SOCKS_PORT, socksPort)
            }
            else -> {
                throw IllegalArgumentException(
                    "Socks Port must be 0 (disabled), auto, or between 1024 and 65535"
                )
            }
        }
    }

    override val socksPortIsolationFlags: List<@IsolationFlag String>?
        get() = servicePrefs.getList(
            PrefKeyList.SOCKS_PORT_ISOLATION_FLAGS,
            defaultTorSettings.socksPortIsolationFlags ?: arrayListOf()
        )

    fun socksPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>) {
        if (isolationFlags == defaultTorSettings.socksPortIsolationFlags) {
            servicePrefs.remove(PrefKeyList.SOCKS_PORT_ISOLATION_FLAGS)
        } else {
            servicePrefs.putList(PrefKeyList.SOCKS_PORT_ISOLATION_FLAGS, isolationFlags)
        }
    }

    override val virtualAddressNetwork: String?
        get() = servicePrefs.getString(PrefKeyString.VIRTUAL_ADDRESS_NETWORK, defaultTorSettings.virtualAddressNetwork)

    override val hasBridges: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_BRIDGES, defaultTorSettings.hasBridges)

    override val connectionPadding: String
        get() = servicePrefs.getString(PrefKeyString.HAS_CONNECTION_PADDING, defaultTorSettings.connectionPadding)
            ?: defaultTorSettings.connectionPadding

    @Throws(IllegalArgumentException::class)
    fun connectionPaddingSave(@ConnectionPadding connectionPadding: String) {
        when (connectionPadding) {
            ConnectionPadding.AUTO,
            ConnectionPadding.OFF,
            ConnectionPadding.ON -> {
                if (connectionPadding == defaultTorSettings.connectionPadding)
                    servicePrefs.remove(PrefKeyString.HAS_CONNECTION_PADDING)
                else
                    servicePrefs.putString(PrefKeyString.HAS_CONNECTION_PADDING, connectionPadding)
            }
            else -> {
                throw IllegalArgumentException(
                    "ConnectionPadding must be 0 (Off), 1 (On), or auto"
                )
            }
        }
    }

    override val hasCookieAuthentication: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_COOKIE_AUTHENTICATION, defaultTorSettings.hasCookieAuthentication)

    fun hasCookieAuthenticationSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasCookieAuthentication)
            servicePrefs.remove(PrefKeyBoolean.HAS_COOKIE_AUTHENTICATION)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_COOKIE_AUTHENTICATION, boolean)
    }

    override val hasDebugLogs: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, defaultTorSettings.hasDebugLogs)

    fun hasDebugLogsSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasDebugLogs)
            servicePrefs.remove(PrefKeyBoolean.HAS_DEBUG_LOGS)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, boolean)
    }

    override val hasDormantCanceledByStartup: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_DORMANT_CANCELED_BY_STARTUP, defaultTorSettings.hasDormantCanceledByStartup)

    fun hasDormantCanceledByStartupSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasDormantCanceledByStartup)
            servicePrefs.remove(PrefKeyBoolean.HAS_DORMANT_CANCELED_BY_STARTUP)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_DORMANT_CANCELED_BY_STARTUP, boolean)
    }

    override val hasOpenProxyOnAllInterfaces: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_OPEN_PROXY_ON_ALL_INTERFACES, defaultTorSettings.hasOpenProxyOnAllInterfaces)

    fun hasOpenProxyOnAllInterfacesSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasOpenProxyOnAllInterfaces)
            servicePrefs.remove(PrefKeyBoolean.HAS_OPEN_PROXY_ON_ALL_INTERFACES)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_OPEN_PROXY_ON_ALL_INTERFACES, boolean)
    }

    override val hasReachableAddress: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_REACHABLE_ADDRESS, defaultTorSettings.hasReachableAddress)

    fun hasReachableAddressSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasReachableAddress)
            servicePrefs.remove(PrefKeyBoolean.HAS_REACHABLE_ADDRESS)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_REACHABLE_ADDRESS, boolean)
    }

    override val hasReducedConnectionPadding: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_REDUCED_CONNECTION_PADDING, defaultTorSettings.hasReducedConnectionPadding)

    fun hasReducedConnectionPaddingSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasReducedConnectionPadding)
            servicePrefs.remove(PrefKeyBoolean.HAS_REDUCED_CONNECTION_PADDING)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_REDUCED_CONNECTION_PADDING, boolean)
    }

    override val hasSafeSocks: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_SAFE_SOCKS, defaultTorSettings.hasSafeSocks)

    fun hasSafeSocksSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasSafeSocks)
            servicePrefs.remove(PrefKeyBoolean.HAS_SAFE_SOCKS)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_SAFE_SOCKS, boolean)
    }

    override val hasStrictNodes: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_STRICT_NODES, defaultTorSettings.hasStrictNodes)

    fun hasStrictNodesSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasStrictNodes)
            servicePrefs.remove(PrefKeyBoolean.HAS_STRICT_NODES)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_STRICT_NODES, boolean)
    }

    override val hasTestSocks: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_TEST_SOCKS, defaultTorSettings.hasTestSocks)

    fun hasTestSocksSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasTestSocks)
            servicePrefs.remove(PrefKeyBoolean.HAS_TEST_SOCKS)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_TEST_SOCKS, boolean)
    }

    override val isAutoMapHostsOnResolve: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.IS_AUTO_MAP_HOSTS_ON_RESOLVE, defaultTorSettings.isAutoMapHostsOnResolve)

    fun isAutoMapHostsOnResolveSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.isAutoMapHostsOnResolve)
            servicePrefs.remove(PrefKeyBoolean.IS_AUTO_MAP_HOSTS_ON_RESOLVE)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.IS_AUTO_MAP_HOSTS_ON_RESOLVE, boolean)
    }

    override val isRelay: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.IS_RELAY, defaultTorSettings.isRelay)

    fun isRelaySave(boolean: Boolean) {
        if (boolean == defaultTorSettings.isRelay)
            servicePrefs.remove(PrefKeyBoolean.IS_RELAY)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.IS_RELAY, boolean)
    }

    override val runAsDaemon: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.RUN_AS_DAEMON, defaultTorSettings.runAsDaemon)

    fun runAsDaemonSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.runAsDaemon)
            servicePrefs.remove(PrefKeyBoolean.RUN_AS_DAEMON)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.RUN_AS_DAEMON, boolean)
    }

    override val transPort: String
        get() = servicePrefs.getString(PrefKeyString.TRANS_PORT, defaultTorSettings.transPort) ?: defaultTorSettings.transPort

    @Throws(IllegalArgumentException::class)
    fun transPortSave(transPort: String) {
        when {
            transPort == defaultTorSettings.transPort -> {
                servicePrefs.remove(PrefKeyString.TRANS_PORT)
            }
            checkPortSelection(transPort) -> {
                servicePrefs.putString(PrefKeyString.TRANS_PORT, transPort)
            }
            else -> {
                throw IllegalArgumentException(
                    "Socks Trans Port must be 0 (disabled), auto, or between 1024 and 65535"
                )
            }
        }
    }

    override val transPortIsolationFlags: List<@IsolationFlag String>?
        get() = servicePrefs.getList(
            PrefKeyList.TRANS_PORT_ISOLATION_FLAGS,
            defaultTorSettings.transPortIsolationFlags ?: arrayListOf()
        )

    fun transPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>) {
        if (isolationFlags == defaultTorSettings.transPortIsolationFlags) {
            servicePrefs.remove(PrefKeyList.TRANS_PORT_ISOLATION_FLAGS)
        } else {
            servicePrefs.putList(PrefKeyList.TRANS_PORT_ISOLATION_FLAGS, isolationFlags)
        }
    }

    override val useSocks5: Boolean
        get() = servicePrefs.getBoolean(PrefKeyBoolean.USE_SOCKS5, defaultTorSettings.useSocks5)

    fun useSocks5Save(boolean: Boolean) {
        if (boolean == defaultTorSettings.useSocks5)
            servicePrefs.remove(PrefKeyBoolean.USE_SOCKS5)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.USE_SOCKS5, boolean)
    }

    private fun checkPortSelection(port: String): Boolean {
        if (port == PortOption.AUTO) return true

        return try {
            val portInt = port.toInt()
            portInt == 0 || portInt in 1024..65535
        } catch (e: Exception) {
            false
        }
    }
}