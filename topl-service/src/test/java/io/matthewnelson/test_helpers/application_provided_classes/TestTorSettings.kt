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
package io.matthewnelson.test_helpers.application_provided_classes

import io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings

internal class TestTorSettings: ApplicationDefaultTorSettings() {

    override val dormantClientTimeout: Int?
        get() = DEFAULT__DORMANT_CLIENT_TIMEOUT

    override val disableNetwork: Boolean
        get() = DEFAULT__DISABLE_NETWORK

    override val dnsPort: String
        get() = PortOption.DISABLED

    override val dnsPortIsolationFlags: List<String>?
        get() = arrayListOf(
            IsolationFlag.ISOLATE_CLIENT_PROTOCOL
        )

    override val customTorrc: String?
        get() = null

    override val entryNodes: String?
        get() = DEFAULT__ENTRY_NODES

    override val excludeNodes: String?
        get() = DEFAULT__EXCLUDED_NODES

    override val exitNodes: String?
        get() = DEFAULT__EXIT_NODES

    override val httpTunnelPort: String
        get() = PortOption.DISABLED

    override val httpTunnelPortIsolationFlags: List<String>?
        get() = arrayListOf(
            IsolationFlag.ISOLATE_CLIENT_PROTOCOL
        )

    override val listOfSupportedBridges: List<@SupportedBridgeType String>
        get() = arrayListOf(SupportedBridgeType.MEEK, SupportedBridgeType.OBFS4)

    override val proxyHost: String?
        get() = DEFAULT__PROXY_HOST

    override val proxyPassword: String?
        get() = DEFAULT__PROXY_PASSWORD

    override val proxyPort: Int?
        get() = null

    override val proxySocks5Host: String?
        get() = DEFAULT__PROXY_SOCKS5_HOST

    override val proxySocks5ServerPort: Int?
        get() = null

    override val proxyType: String
        get() = ProxyType.DISABLED

    override val proxyUser: String?
        get() = DEFAULT__PROXY_USER

    override val reachableAddressPorts: String
        get() = DEFAULT__REACHABLE_ADDRESS_PORTS

    override val relayNickname: String?
        get() = DEFAULT__RELAY_NICKNAME

    override val relayPort: String
        get() = PortOption.DISABLED

    override val socksPort: String
        get() = "9051"
    override val socksPortIsolationFlags: List<String>?
        get() = arrayListOf(
            IsolationFlag.KEEP_ALIVE_ISOLATE_SOCKS_AUTH,
            IsolationFlag.IPV6_TRAFFIC,
            IsolationFlag.PREFER_IPV6,
            IsolationFlag.ISOLATE_CLIENT_PROTOCOL
        )

    override val virtualAddressNetwork: String?
        get() = "10.192.0.2/10"

    override val hasBridges: Boolean
        get() = DEFAULT__HAS_BRIDGES

    override val connectionPadding: @ConnectionPadding String
        get() = ConnectionPadding.OFF

    override val hasCookieAuthentication: Boolean
        get() = DEFAULT__HAS_COOKIE_AUTHENTICATION

    override val hasDebugLogs: Boolean
        get() = DEFAULT__HAS_DEBUG_LOGS

    override val hasDormantCanceledByStartup: Boolean
        get() = DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP

    override val hasOpenProxyOnAllInterfaces: Boolean
        get() = DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES

    override val hasReachableAddress: Boolean
        get() = DEFAULT__HAS_REACHABLE_ADDRESS

    override val hasReducedConnectionPadding: Boolean
        get() = DEFAULT__HAS_REDUCED_CONNECTION_PADDING

    override val hasSafeSocks: Boolean
        get() = DEFAULT__HAS_SAFE_SOCKS

    override val hasStrictNodes: Boolean
        get() = DEFAULT__HAS_STRICT_NODES

    override val hasTestSocks: Boolean
        get() = DEFAULT__HAS_TEST_SOCKS

    override val isAutoMapHostsOnResolve: Boolean
        get() = DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE

    override val isRelay: Boolean
        get() = DEFAULT__IS_RELAY

    override val runAsDaemon: Boolean
        get() = DEFAULT__RUN_AS_DAEMON

    override val transPort: String
        get() = PortOption.DISABLED

    override val transPortIsolationFlags: List<String>?
        get() = arrayListOf(
            IsolationFlag.ISOLATE_CLIENT_PROTOCOL
        )

    override val useSocks5: Boolean
        get() = DEFAULT__USE_SOCKS5
}