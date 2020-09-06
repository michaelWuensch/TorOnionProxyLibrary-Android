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

import androidx.annotation.WorkerThread
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
 * Use [io.matthewnelson.topl_service.TorServiceController.getServiceTorSettings] to instantiate
 *
 * @param [servicePrefs] [TorServicePrefs] to query/save values to shared preferences
 * @param [defaultTorSettings] Default values to fall back on if nothing is returned from
 *   [TorServicePrefs]. Use [io.matthewnelson.topl_service.TorServiceController.getTorSettings] for
 *   this value.
 * */
class ServiceTorSettings internal constructor(
    val servicePrefs: TorServicePrefs,
    val defaultTorSettings: TorSettings
): TorSettings() {

    override val dormantClientTimeout: Int?
        @WorkerThread
        get() = servicePrefs.getInt(PrefKeyInt.DORMANT_CLIENT_TIMEOUT, defaultTorSettings.dormantClientTimeout)

    @WorkerThread
    fun dormantClientTimeoutSave(minutes: Int?) {
        if (minutes == defaultTorSettings.dormantClientTimeout)
            servicePrefs.remove(PrefKeyInt.DORMANT_CLIENT_TIMEOUT)
        else
            servicePrefs.putInt(PrefKeyInt.DORMANT_CLIENT_TIMEOUT, minutes)
    }

    override val disableNetwork: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.DISABLE_NETWORK, defaultTorSettings.disableNetwork)

    /**
     * Saves the value for [disableNetwork] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [boolean]
     * @see [TorSettings.disableNetwork]
     * */
    @WorkerThread
    fun disableNetworkSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.disableNetwork)
            servicePrefs.remove(PrefKeyBoolean.DISABLE_NETWORK)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.DISABLE_NETWORK, boolean)
    }

    override val dnsPort: String
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.DNS_PORT, defaultTorSettings.dnsPort)
            ?: defaultTorSettings.dnsPort

    /**
     * Saves the value for [dnsPort] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [dnsPort] A String value of 0, auto, or number between 1024 and 65535
     * @see [checkPortSelection]
     * @see [TorSettings.dnsPort]
     * @throws [IllegalArgumentException] if the value is not 0, auto, or between 1024 and 65535
     * */
    @WorkerThread
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
        @WorkerThread
        get() = servicePrefs.getList(
            PrefKeyList.DNS_PORT_ISOLATION_FLAGS,
            defaultTorSettings.dnsPortIsolationFlags ?: arrayListOf()
        )

    /**
     * Saves the value for [isolationFlags] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [isolationFlags] A List of [io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag]'s
     *   for the [dnsPort]
     * @see [io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag]
     * @see [TorSettings.dnsPortIsolationFlags]
     * */
    @WorkerThread
    fun dnsPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>) {
        if (isolationFlags == defaultTorSettings.dnsPortIsolationFlags) {
            servicePrefs.remove(PrefKeyList.DNS_PORT_ISOLATION_FLAGS)
        } else {
            servicePrefs.putList(PrefKeyList.DNS_PORT_ISOLATION_FLAGS, isolationFlags)
        }
    }

    override val customTorrc: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.CUSTOM_TORRC, defaultTorSettings.customTorrc)

    /**
     * Saves the value for [customTorrc] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [customTorrc] A String of values to be added to the torrc file
     * @see [TorSettings.customTorrc]
     * */
    @WorkerThread
    fun customTorrcSave(customTorrc: String?) {
        if (customTorrc == defaultTorSettings.customTorrc)
            servicePrefs.remove(PrefKeyString.CUSTOM_TORRC)
        else
            servicePrefs.putString(PrefKeyString.CUSTOM_TORRC, customTorrc)
    }

    override val entryNodes: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.ENTRY_NODES, defaultTorSettings.entryNodes)

    /**
     * Saves the value for [entryNodes] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [entryNodes] A comma separated list of nodes
     * @see [TorSettings.entryNodes]
     * */
    @WorkerThread
    fun entryNodesSave(entryNodes: String?) {
        if (entryNodes == defaultTorSettings.entryNodes)
            servicePrefs.remove(PrefKeyString.ENTRY_NODES)
        else
            servicePrefs.putString(PrefKeyString.ENTRY_NODES, entryNodes)
    }

    override val excludeNodes: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.EXCLUDED_NODES, defaultTorSettings.excludeNodes)

    /**
     * Saves the value for [excludeNodes] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [excludeNodes] A comma separated list of nodes
     * @see [TorSettings.excludeNodes]
     * */
    @WorkerThread
    fun excludeNodesSave(excludeNodes: String?) {
        if (excludeNodes == defaultTorSettings.excludeNodes)
            servicePrefs.remove(PrefKeyString.EXCLUDED_NODES)
        else
            servicePrefs.putString(PrefKeyString.EXCLUDED_NODES, excludeNodes)
    }

    override val exitNodes: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.EXIT_NODES, defaultTorSettings.exitNodes)

    /**
     * Saves the value for [exitNodes] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [exitNodes] A comma separated list of nodes
     * @see [TorSettings.exitNodes]
     * */
    @WorkerThread
    fun exitNodesSave(exitNodes: String?) {
        if (exitNodes == defaultTorSettings.exitNodes)
            servicePrefs.remove(PrefKeyString.EXIT_NODES)
        else
            servicePrefs.putString(PrefKeyString.EXIT_NODES, exitNodes)
    }

    override val httpTunnelPort: String
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.HTTP_TUNNEL_PORT, defaultTorSettings.httpTunnelPort)
            ?: defaultTorSettings.httpTunnelPort

    /**
     * Saves the value for [httpPort] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [httpPort] A String value of 0, auto, or number between 1024 and 65535
     * @see [checkPortSelection]
     * @see [TorSettings.httpTunnelPort]
     * @throws [IllegalArgumentException] if the value is not 0, auto, or between 1024 and 65535
     * */
    @WorkerThread
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
        @WorkerThread
        get() = servicePrefs.getList(
            PrefKeyList.HTTP_TUNNEL_PORT_ISOLATION_FLAGS,
            defaultTorSettings.httpTunnelPortIsolationFlags ?: arrayListOf()
        )

    /**
     * Saves the value for [isolationFlags] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [isolationFlags] A List of [io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag]'s
     *   for the [httpTunnelPort]
     * @see [io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag]
     * @see [TorSettings.httpTunnelPortIsolationFlags]
     * */
    @WorkerThread
    fun httpPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>) {
        if (isolationFlags == defaultTorSettings.httpTunnelPortIsolationFlags) {
            servicePrefs.remove(PrefKeyList.HTTP_TUNNEL_PORT_ISOLATION_FLAGS)
        } else {
            servicePrefs.putList(PrefKeyList.HTTP_TUNNEL_PORT_ISOLATION_FLAGS, isolationFlags)
        }
    }

    override val listOfSupportedBridges: List<@SupportedBridgeType String>
        get() = defaultTorSettings.listOfSupportedBridges

    // TODO: write a save method after refactor

    override val proxyHost: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.PROXY_HOST, defaultTorSettings.proxyHost)

    /**
     * Saves the value for [proxyHost] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [proxyHost]
     * @see [TorSettings.proxyHost]
     * */
    @WorkerThread
    fun proxyHostSave(proxyHost: String?) {
        if (proxyHost == defaultTorSettings.proxyHost)
            servicePrefs.remove(PrefKeyString.PROXY_HOST)
        else
            servicePrefs.putString(PrefKeyString.PROXY_HOST, proxyHost)
    }

    override val proxyPassword: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.PROXY_PASSWORD, defaultTorSettings.proxyPassword)

    /**
     * Saves the value for [proxyPassword] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [proxyPassword]
     * @see [TorSettings.proxyPassword]
     * */
    @WorkerThread
    fun proxyPasswordSave(proxyPassword: String?) {
        if (proxyPassword == defaultTorSettings.proxyPassword)
            servicePrefs.remove(PrefKeyString.PROXY_PASSWORD)
        else
            servicePrefs.putString(PrefKeyString.PROXY_PASSWORD, proxyPassword)
    }

    override val proxyPort: Int?
        @WorkerThread
        get() = servicePrefs.getInt(PrefKeyInt.PROXY_PORT, defaultTorSettings.proxyPort)

    /**
     * Saves the value for [proxyPort] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [proxyPort] An Int value between 1024 and 65535, or `null`
     * @see [checkPortSelection]
     * @see [TorSettings.proxyPort]
     * @throws [IllegalArgumentException] if the value is not `null`, or between 1024 and 65535
     * */
    @WorkerThread
    @Throws(IllegalArgumentException::class)
    fun proxyPortSave(proxyPort: Int?) {
        when {
            proxyPort == defaultTorSettings.proxyPort -> {
                servicePrefs.remove(PrefKeyInt.PROXY_PORT)
            }
            proxyPort == null ||
            checkPortSelection(proxyPort, checkZero = false) -> {
                servicePrefs.putInt(PrefKeyInt.PROXY_PORT, proxyPort)
            }
            else -> {
                throw IllegalArgumentException(
                    "Proxy Port must null, or between 1024 and 65535"
                )
            }
        }
    }

    override val proxySocks5Host: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.PROXY_SOCKS5_HOST, defaultTorSettings.proxySocks5Host)

    /**
     * Saves the value for [proxySocks5Host] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [proxySocks5Host]
     * @see [TorSettings.proxySocks5Host]
     * */
    @WorkerThread
    fun proxySocks5HostSave(proxySocks5Host: String?) {
        if (proxySocks5Host == defaultTorSettings.proxySocks5Host)
            servicePrefs.remove(PrefKeyString.PROXY_SOCKS5_HOST)
        else
            servicePrefs.putString(PrefKeyString.PROXY_SOCKS5_HOST, proxySocks5Host)
    }

    override val proxySocks5ServerPort: Int?
        @WorkerThread
        get() = servicePrefs.getInt(PrefKeyInt.PROXY_SOCKS5_SERVER_PORT, defaultTorSettings.proxySocks5ServerPort)

    /**
     * Saves the value for [proxySocks5ServerPort] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [proxySocks5ServerPort] An Int value between 1024 and 65535, or `null`
     * @see [checkPortSelection]
     * @see [TorSettings.proxySocks5ServerPort]
     * @throws [IllegalArgumentException] if the value is not `null`, or between 1024 and 65535
     * */
    @WorkerThread
    @Throws(IllegalArgumentException::class)
    fun proxySocks5ServerPortSave(proxySocks5ServerPort: Int?) {
        when {
            proxySocks5ServerPort == defaultTorSettings.proxySocks5ServerPort -> {
                servicePrefs.remove(PrefKeyInt.PROXY_SOCKS5_SERVER_PORT)
            }
            proxySocks5ServerPort == null ||
            checkPortSelection(proxySocks5ServerPort, checkZero = false) -> {
                servicePrefs.putInt(PrefKeyInt.PROXY_SOCKS5_SERVER_PORT, proxySocks5ServerPort)
            }
            else -> {
                throw IllegalArgumentException(
                    "ProxySocks5Server Port must null, or between 1024 and 65535"
                )
            }
        }
    }

    override val proxyType: @ProxyType String
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.PROXY_TYPE, defaultTorSettings.proxyType) ?: defaultTorSettings.proxyType

    /**
     * Saves the value for [proxyType] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [proxyType] A [io.matthewnelson.topl_core_base.BaseConsts.ProxyType]
     * @see [io.matthewnelson.topl_core_base.BaseConsts.ProxyType]
     * @see [TorSettings.proxyType]
     * @throws [IllegalArgumentException] if the value is not empty (disabled), HTTPS, or Socks5
     * */
    @WorkerThread
    @Throws(IllegalArgumentException::class)
    fun proxyTypeSave(@ProxyType proxyType: String) {
        when (proxyType) {
            ProxyType.DISABLED,
            ProxyType.HTTPS,
            ProxyType.SOCKS_5 -> {
                if (proxyType == defaultTorSettings.proxyType)
                    servicePrefs.remove(PrefKeyString.PROXY_TYPE)
                else
                    servicePrefs.putString(PrefKeyString.PROXY_TYPE, proxyType)
            }
            else -> {
                throw IllegalArgumentException(
                    "ProxyType must be '' (empty/disabled), HTTPS, or Socks5"
                )
            }
        }
    }

    override val proxyUser: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.PROXY_USER, defaultTorSettings.proxyUser)

    /**
     * Saves the value for [proxyUser] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [proxyUser]
     * @see [TorSettings.proxyUser]
     * */
    @WorkerThread
    fun proxyUserSave(proxyUser: String?) {
        if (proxyUser == defaultTorSettings.proxyUser)
            servicePrefs.remove(PrefKeyString.PROXY_USER)
        else
            servicePrefs.putString(PrefKeyString.PROXY_USER, proxyUser)
    }

    override val reachableAddressPorts: String
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.REACHABLE_ADDRESS_PORTS, defaultTorSettings.reachableAddressPorts)
            ?: defaultTorSettings.reachableAddressPorts

    /**
     * Saves the value for [reachableAddressPorts] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [reachableAddressPorts]
     * @see [TorSettings.reachableAddressPorts]
     * */
    @WorkerThread
    fun reachableAddressPortsSave(reachableAddressPorts: String) {
        if (reachableAddressPorts == defaultTorSettings.reachableAddressPorts)
            servicePrefs.remove(PrefKeyString.REACHABLE_ADDRESS_PORTS)
        else
            servicePrefs.putString(PrefKeyString.REACHABLE_ADDRESS_PORTS, reachableAddressPorts)
    }

    override val relayNickname: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.RELAY_NICKNAME, defaultTorSettings.relayNickname)

    /**
     * Saves the value for [relayNickname] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [relayNickname]
     * @see [TorSettings.relayNickname]
     * */
    @WorkerThread
    fun relayNicknameSave(relayNickname: String) {
        if (relayNickname == defaultTorSettings.relayNickname)
            servicePrefs.remove(PrefKeyString.RELAY_NICKNAME)
        else
            servicePrefs.putString(PrefKeyString.RELAY_NICKNAME, relayNickname)
    }

    override val relayPort: String
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.RELAY_PORT, defaultTorSettings.relayPort)
            ?: defaultTorSettings.relayPort

    /**
     * Saves the value for [relayPort] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [relayPort] A String value of 0, auto, or number between 1024 and 65535
     * @see [checkPortSelection]
     * @see [TorSettings.relayPort]
     * @throws [IllegalArgumentException] if the value is not 0, auto, or between 1024 and 65535
     * */
    @WorkerThread
    @Throws(IllegalArgumentException::class)
    fun relayPortSave(relayPort: String) {
        when {
            relayPort == defaultTorSettings.relayPort -> {
                servicePrefs.remove(PrefKeyString.RELAY_PORT)
            }
            checkPortSelection(relayPort) -> {
                servicePrefs.putString(PrefKeyString.RELAY_PORT, relayPort)
            }
            else -> {
                throw IllegalArgumentException(
                    "Relay Port must be 0 (disabled), auto, or between 1024 and 65535"
                )
            }
        }
    }

    override val socksPort: String
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.SOCKS_PORT, defaultTorSettings.socksPort)
            ?: defaultTorSettings.socksPort

    /**
     * Saves the value for [socksPort] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [socksPort] A String value of 0, auto, or number between 1024 and 65535
     * @see [checkPortSelection]
     * @see [TorSettings.socksPort]
     * @throws [IllegalArgumentException] if the value is not 0, auto, or between 1024 and 65535
     * */
    @WorkerThread
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
        @WorkerThread
        get() = servicePrefs.getList(
            PrefKeyList.SOCKS_PORT_ISOLATION_FLAGS,
            defaultTorSettings.socksPortIsolationFlags ?: arrayListOf()
        )

    /**
     * Saves the value for [isolationFlags] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [isolationFlags] A List of [io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag]'s
     *   for the [socksPort]
     * @see [io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag]
     * @see [TorSettings.socksPortIsolationFlags]
     * */
    @WorkerThread
    fun socksPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>) {
        if (isolationFlags == defaultTorSettings.socksPortIsolationFlags) {
            servicePrefs.remove(PrefKeyList.SOCKS_PORT_ISOLATION_FLAGS)
        } else {
            servicePrefs.putList(PrefKeyList.SOCKS_PORT_ISOLATION_FLAGS, isolationFlags)
        }
    }

    override val virtualAddressNetwork: String?
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.VIRTUAL_ADDRESS_NETWORK, defaultTorSettings.virtualAddressNetwork)

    /**
     * Saves the value for [virtualAddressNetwork] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [virtualAddressNetwork]
     * @see [TorSettings.virtualAddressNetwork]
     * */
    @WorkerThread
    fun virtualAddressNetworkSave(virtualAddressNetwork: String) {
        if (virtualAddressNetwork == defaultTorSettings.virtualAddressNetwork)
            servicePrefs.remove(PrefKeyString.VIRTUAL_ADDRESS_NETWORK)
        else
            servicePrefs.putString(PrefKeyString.VIRTUAL_ADDRESS_NETWORK, virtualAddressNetwork)
    }

    override val hasBridges: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_BRIDGES, defaultTorSettings.hasBridges)

    /**
     * Saves the value for [hasBridges] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasBridges]
     * */
    @WorkerThread
    fun hasBridgesSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasBridges)
            servicePrefs.remove(PrefKeyBoolean.HAS_BRIDGES)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_BRIDGES, boolean)
    }

    override val connectionPadding: String
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.HAS_CONNECTION_PADDING, defaultTorSettings.connectionPadding)
            ?: defaultTorSettings.connectionPadding

    /**
     * Saves the value for [connectionPadding] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [connectionPadding] A [io.matthewnelson.topl_core_base.BaseConsts.ConnectionPadding]
     * @see [io.matthewnelson.topl_core_base.BaseConsts.ConnectionPadding]
     * @see [TorSettings.connectionPadding]
     * @throws [IllegalArgumentException] if the value is not 0 (Off), 1 (On), or auto
     * */
    @WorkerThread
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
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_COOKIE_AUTHENTICATION, defaultTorSettings.hasCookieAuthentication)

    /**
     * Saves the value for [hasCookieAuthentication] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasCookieAuthentication]
     * */
    @WorkerThread
    fun hasCookieAuthenticationSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasCookieAuthentication)
            servicePrefs.remove(PrefKeyBoolean.HAS_COOKIE_AUTHENTICATION)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_COOKIE_AUTHENTICATION, boolean)
    }

    override val hasDebugLogs: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, defaultTorSettings.hasDebugLogs)

    /**
     * Saves the value for [hasDebugLogs] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasDebugLogs]
     * */
    @WorkerThread
    fun hasDebugLogsSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasDebugLogs)
            servicePrefs.remove(PrefKeyBoolean.HAS_DEBUG_LOGS)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, boolean)
    }

    override val hasDormantCanceledByStartup: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_DORMANT_CANCELED_BY_STARTUP, defaultTorSettings.hasDormantCanceledByStartup)

    /**
     * Saves the value for [hasDormantCanceledByStartup] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasDormantCanceledByStartup]
     * */
    @WorkerThread
    fun hasDormantCanceledByStartupSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasDormantCanceledByStartup)
            servicePrefs.remove(PrefKeyBoolean.HAS_DORMANT_CANCELED_BY_STARTUP)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_DORMANT_CANCELED_BY_STARTUP, boolean)
    }

    override val hasOpenProxyOnAllInterfaces: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_OPEN_PROXY_ON_ALL_INTERFACES, defaultTorSettings.hasOpenProxyOnAllInterfaces)

    /**
     * Saves the value for [hasOpenProxyOnAllInterfaces] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasOpenProxyOnAllInterfaces]
     * */
    @WorkerThread
    fun hasOpenProxyOnAllInterfacesSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasOpenProxyOnAllInterfaces)
            servicePrefs.remove(PrefKeyBoolean.HAS_OPEN_PROXY_ON_ALL_INTERFACES)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_OPEN_PROXY_ON_ALL_INTERFACES, boolean)
    }

    override val hasReachableAddress: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_REACHABLE_ADDRESS, defaultTorSettings.hasReachableAddress)

    /**
     * Saves the value for [hasReachableAddress] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasReachableAddress]
     * */
    @WorkerThread
    fun hasReachableAddressSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasReachableAddress)
            servicePrefs.remove(PrefKeyBoolean.HAS_REACHABLE_ADDRESS)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_REACHABLE_ADDRESS, boolean)
    }

    override val hasReducedConnectionPadding: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_REDUCED_CONNECTION_PADDING, defaultTorSettings.hasReducedConnectionPadding)

    /**
     * Saves the value for [hasReducedConnectionPadding] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasReducedConnectionPadding]
     * */
    @WorkerThread
    fun hasReducedConnectionPaddingSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasReducedConnectionPadding)
            servicePrefs.remove(PrefKeyBoolean.HAS_REDUCED_CONNECTION_PADDING)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_REDUCED_CONNECTION_PADDING, boolean)
    }

    override val hasSafeSocks: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_SAFE_SOCKS, defaultTorSettings.hasSafeSocks)

    /**
     * Saves the value for [hasSafeSocks] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasSafeSocks]
     * */
    @WorkerThread
    fun hasSafeSocksSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasSafeSocks)
            servicePrefs.remove(PrefKeyBoolean.HAS_SAFE_SOCKS)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_SAFE_SOCKS, boolean)
    }

    override val hasStrictNodes: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_STRICT_NODES, defaultTorSettings.hasStrictNodes)

    /**
     * Saves the value for [hasStrictNodes] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasStrictNodes]
     * */
    @WorkerThread
    fun hasStrictNodesSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasStrictNodes)
            servicePrefs.remove(PrefKeyBoolean.HAS_STRICT_NODES)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_STRICT_NODES, boolean)
    }

    override val hasTestSocks: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.HAS_TEST_SOCKS, defaultTorSettings.hasTestSocks)

    /**
     * Saves the value for [hasTestSocks] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.hasTestSocks]
     * */
    @WorkerThread
    fun hasTestSocksSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.hasTestSocks)
            servicePrefs.remove(PrefKeyBoolean.HAS_TEST_SOCKS)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.HAS_TEST_SOCKS, boolean)
    }

    override val isAutoMapHostsOnResolve: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.IS_AUTO_MAP_HOSTS_ON_RESOLVE, defaultTorSettings.isAutoMapHostsOnResolve)

    /**
     * Saves the value for [isAutoMapHostsOnResolve] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.isAutoMapHostsOnResolve]
     * */
    @WorkerThread
    fun isAutoMapHostsOnResolveSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.isAutoMapHostsOnResolve)
            servicePrefs.remove(PrefKeyBoolean.IS_AUTO_MAP_HOSTS_ON_RESOLVE)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.IS_AUTO_MAP_HOSTS_ON_RESOLVE, boolean)
    }

    override val isRelay: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.IS_RELAY, defaultTorSettings.isRelay)

    /**
     * Saves the value for [isRelay] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.isRelay]
     * */
    @WorkerThread
    fun isRelaySave(boolean: Boolean) {
        if (boolean == defaultTorSettings.isRelay)
            servicePrefs.remove(PrefKeyBoolean.IS_RELAY)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.IS_RELAY, boolean)
    }

    override val runAsDaemon: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.RUN_AS_DAEMON, defaultTorSettings.runAsDaemon)

    /**
     * Saves the value for [runAsDaemon] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.runAsDaemon]
     * */
    @WorkerThread
    fun runAsDaemonSave(boolean: Boolean) {
        if (boolean == defaultTorSettings.runAsDaemon)
            servicePrefs.remove(PrefKeyBoolean.RUN_AS_DAEMON)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.RUN_AS_DAEMON, boolean)
    }

    override val transPort: String
        @WorkerThread
        get() = servicePrefs.getString(PrefKeyString.TRANS_PORT, defaultTorSettings.transPort) ?: defaultTorSettings.transPort

    /**
     * Saves the value for [transPort] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [transPort] A String value of 0, auto, or number between 1024 and 65535
     * @see [checkPortSelection]
     * @see [TorSettings.transPort]
     * @throws [IllegalArgumentException] if the value is not 0, auto, or between 1024 and 65535
     * */
    @WorkerThread
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
        @WorkerThread
        get() = servicePrefs.getList(
            PrefKeyList.TRANS_PORT_ISOLATION_FLAGS,
            defaultTorSettings.transPortIsolationFlags ?: arrayListOf()
        )

    /**
     * Saves the value for [isolationFlags] to [TorServicePrefs]. If the value is the same as what is
     * declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the setting if
     * it exists.
     *
     * @param [isolationFlags] A List of [io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag]'s
     *   for the [transPort]
     * @see [io.matthewnelson.topl_core_base.BaseConsts.IsolationFlag]
     * @see [TorSettings.transPortIsolationFlags]
     * */
    @WorkerThread
    fun transPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>) {
        if (isolationFlags == defaultTorSettings.transPortIsolationFlags) {
            servicePrefs.remove(PrefKeyList.TRANS_PORT_ISOLATION_FLAGS)
        } else {
            servicePrefs.putList(PrefKeyList.TRANS_PORT_ISOLATION_FLAGS, isolationFlags)
        }
    }

    override val useSocks5: Boolean
        @WorkerThread
        get() = servicePrefs.getBoolean(PrefKeyBoolean.USE_SOCKS5, defaultTorSettings.useSocks5)

    /**
     * Saves the value for [useSocks5] to [TorServicePrefs]. If the value is the same
     * as what is declared in [defaultTorSettings], [TorServicePrefs] is queried to remove the
     * setting if it exists.
     *
     * @param [boolean] to enable/disable
     * @see [TorSettings.useSocks5]
     * */
    @WorkerThread
    fun useSocks5Save(boolean: Boolean) {
        if (boolean == defaultTorSettings.useSocks5)
            servicePrefs.remove(PrefKeyBoolean.USE_SOCKS5)
        else
            servicePrefs.putBoolean(PrefKeyBoolean.USE_SOCKS5, boolean)
    }

    private fun checkPortSelection(port: Int, checkZero: Boolean): Boolean =
        checkPortSelection(port.toString(), checkAuto = false, checkZero = checkZero)

    private fun checkPortSelection(
        port: String,
        checkAuto: Boolean = true,
        checkZero: Boolean = true
    ): Boolean {
        if (checkAuto && port == PortOption.AUTO)
            return true

        return try {
            val portInt = port.toInt()
            if (checkZero && portInt == 0)
                return true
            portInt in 1024..65535
        } catch (e: Exception) {
            false
        }
    }
}