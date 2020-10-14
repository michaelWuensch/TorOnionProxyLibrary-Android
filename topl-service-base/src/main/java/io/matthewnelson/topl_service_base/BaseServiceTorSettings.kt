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
*     needed to implement TorOnionProxyLibrary-Android, as listed below:
*
*      - From the `topl-core-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service` module:
*          - The TorServiceController class and it's contained classes/methods/variables
*          - The ServiceNotification.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Companion class and it's contained methods/variables
*
*     The following code is excluded from "The Interfaces":
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
package io.matthewnelson.topl_service_base

import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service_base.BaseServiceConsts.PrefKeyString
import io.matthewnelson.topl_service_base.BaseServiceConsts.PrefKeyList
import io.matthewnelson.topl_service_base.BaseServiceConsts.PrefKeyInt
import io.matthewnelson.topl_service_base.BaseServiceConsts.PrefKeyBoolean

/**
 * This class enables the querying of [TorServicePrefs] to obtain values potentially set by
 * the User such that they are *preferred* over static/default values you may have set in
 * your [ApplicationDefaultTorSettings].
 *
 * It enables the updating of settings in a standardized manner so library users can
 * simply instantiate [TorServicePrefs], modify settings, and then call `restartTor` from the
 * `topl-service::TorServiceController` to have them applied to the Tor Process. It also makes
 * designing of a settings screen much easier for your application.
 *
 * @param [servicePrefs] [TorServicePrefs] to query/save values to shared preferences
 * @param [defaultTorSettings] Default values to fall back on if nothing is returned from
 *   [TorServicePrefs]
 * */
abstract class BaseServiceTorSettings(
    val servicePrefs: TorServicePrefs,
    val defaultTorSettings: ApplicationDefaultTorSettings
): TorSettings() {

    override val dormantClientTimeout: Int?
        @WorkerThread
        get() = servicePrefs.getInt(PrefKeyInt.DORMANT_CLIENT_TIMEOUT, defaultTorSettings.dormantClientTimeout)

    @WorkerThread
    abstract fun dormantClientTimeoutSave(minutes: Int?)

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
    abstract fun disableNetworkSave(boolean: Boolean)

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
    abstract fun dnsPortSave(dnsPort: String)

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
    abstract fun dnsPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>)

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
    abstract fun customTorrcSave(customTorrc: String?)

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
    abstract fun entryNodesSave(entryNodes: String?)

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
    abstract fun excludeNodesSave(excludeNodes: String?)

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
    abstract fun exitNodesSave(exitNodes: String?)

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
    abstract fun httpTunnelPortSave(httpPort: String)

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
    abstract fun httpPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>)

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
    abstract fun proxyHostSave(proxyHost: String?)

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
    abstract fun proxyPasswordSave(proxyPassword: String?)

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
    abstract fun proxyPortSave(proxyPort: Int?)

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
    abstract fun proxySocks5HostSave(proxySocks5Host: String?)

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
    abstract fun proxySocks5ServerPortSave(proxySocks5ServerPort: Int?)

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
    abstract fun proxyTypeSave(@ProxyType proxyType: String)

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
    abstract fun proxyUserSave(proxyUser: String?)

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
    abstract fun reachableAddressPortsSave(reachableAddressPorts: String)

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
    abstract fun relayNicknameSave(relayNickname: String)

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
    abstract fun relayPortSave(relayPort: String)

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
    abstract fun socksPortSave(socksPort: String)

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
    abstract fun socksPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>)

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
    abstract fun virtualAddressNetworkSave(virtualAddressNetwork: String)

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
    abstract fun hasBridgesSave(boolean: Boolean)

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
    abstract fun connectionPaddingSave(@ConnectionPadding connectionPadding: String)

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
    abstract fun hasCookieAuthenticationSave(boolean: Boolean)

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
    abstract fun hasDebugLogsSave(boolean: Boolean)

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
    abstract fun hasDormantCanceledByStartupSave(boolean: Boolean)

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
    abstract fun hasOpenProxyOnAllInterfacesSave(boolean: Boolean)

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
    abstract fun hasReachableAddressSave(boolean: Boolean)

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
    abstract fun hasReducedConnectionPaddingSave(boolean: Boolean)

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
    abstract fun hasSafeSocksSave(boolean: Boolean)

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
    abstract fun hasStrictNodesSave(boolean: Boolean)

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
    abstract fun hasTestSocksSave(boolean: Boolean)

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
    abstract fun isAutoMapHostsOnResolveSave(boolean: Boolean)

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
    abstract fun isRelaySave(boolean: Boolean)

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
    abstract fun runAsDaemonSave(boolean: Boolean)

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
    abstract fun transPortSave(transPort: String)

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
    abstract fun transPortIsolationFlagsSave(isolationFlags: List<@IsolationFlag String>)

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
    abstract fun useSocks5Save(boolean: Boolean)
}