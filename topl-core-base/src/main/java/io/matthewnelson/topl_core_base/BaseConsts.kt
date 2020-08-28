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
package io.matthewnelson.topl_core_base

import androidx.annotation.StringDef

abstract class BaseConsts {


    ////////////////////////////
    /// EventBroadcast Types ///
    ////////////////////////////
    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        BroadcastType.DEBUG,
        BroadcastType.ERROR,
        BroadcastType.EXCEPTION,
        BroadcastType.NOTICE,
        BroadcastType.WARN
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class BroadcastType {
        companion object {
            const val DEBUG = "DEBUG"
            const val ERROR = "ERROR"
            const val EXCEPTION = "EXCEPTION"
            const val NOTICE = "NOTICE"
            const val WARN = "WARN"
        }
    }


    ///////////////////
    /// Tor's State ///
    ///////////////////
    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        TorState.OFF,
        TorState.ON,
        TorState.STARTING,
        TorState.STOPPING
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class TorState {
        companion object {
            const val OFF = "Tor: Off"
            const val ON = "Tor: On"
            const val STARTING = "Tor: Starting"
            const val STOPPING = "Tor: Stopping"
        }
    }

    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        TorNetworkState.DISABLED,
        TorNetworkState.ENABLED
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class TorNetworkState {
        companion object {
            const val DISABLED = "Network: disabled"
            const val ENABLED = "Network: enabled"
        }
    }


    ///////////////////
    /// TorSettings ///
    ///////////////////
    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE
    )
    @StringDef(
        SupportedBridgeType.MEEK,
        SupportedBridgeType.OBFS4,
        SupportedBridgeType.SNOWFLAKE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class SupportedBridgeType {
        companion object {
            const val MEEK = "meek"
            const val OBFS4 = "obfs4"
            const val SNOWFLAKE = "snowflake"

            fun getAll(): List<@SupportedBridgeType String> {
                return arrayListOf(
                    MEEK,
                    OBFS4,
                    SNOWFLAKE
                )
            }
        }
    }

    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        ConnectionPadding.AUTO,
        ConnectionPadding.OFF,
        ConnectionPadding.ON
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ConnectionPadding {
        companion object {
            const val AUTO = "auto"
            const val OFF = "0"
            const val ON = "1"

            fun getAll(): List<@ConnectionPadding String> {
                return arrayListOf(
                    AUTO,
                    OFF,
                    ON
                )
            }
        }
    }

    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        PortOption.AUTO,
        PortOption.DISABLED
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class PortOption {
        companion object {
            const val AUTO = "auto"
            const val DISABLED = "0"
        }
    }

    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        ProxyType.DISABLED,
        ProxyType.HTTPS,
        ProxyType.SOCKS_5
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ProxyType {
        companion object {
            const val DISABLED = ""
            const val HTTPS = "HTTPS"
            const val SOCKS_5 = "Socks5"

            fun getAll(): List<@ProxyType String> {
                return arrayListOf(
                    DISABLED,
                    HTTPS,
                    SOCKS_5
                )
            }
        }
    }

    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        IsolationFlag.NO_ISOLATE_CLIENT_ADDR,
        IsolationFlag.NO_ISOLATE_SOCKS_AUTH,
        IsolationFlag.ISOLATE_CLIENT_PROTOCOL,
        IsolationFlag.ISOLATE_DEST_PORT,
        IsolationFlag.ISOLATE_DEST_ADDR,
        IsolationFlag.KEEP_ALIVE_ISOLATE_SOCKS_AUTH,
        IsolationFlag.NO_IPV4_TRAFFIC,
        IsolationFlag.IPV6_TRAFFIC,
        IsolationFlag.PREFER_IPV6,
        IsolationFlag.NO_DNS_REQUEST,
        IsolationFlag.NO_ONION_TRAFFIC,
        IsolationFlag.ONION_TRAFFIC_ONLY,
        IsolationFlag.CACHE_IPV4_DNS,
        IsolationFlag.CACHE_IPV6_DNS,
        IsolationFlag.CACHE_DNS,
        IsolationFlag.USE_IPV4_CACHE,
        IsolationFlag.USE_IPV6_CACHE,
        IsolationFlag.USE_DNS_CACHE,
        IsolationFlag.PREFER_IPV6_AUTOMAP,
        IsolationFlag.PREFER_SOCKS_NO_AUTH
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class IsolationFlag {
        companion object {
            const val NO_ISOLATE_CLIENT_ADDR = "NoIsolateClientAddr"
            const val NO_ISOLATE_SOCKS_AUTH = "NoIsolateSOCKSAuth"
            const val ISOLATE_CLIENT_PROTOCOL = "IsolateClientProtocol"
            const val ISOLATE_DEST_PORT = "IsolateDestPort"
            const val ISOLATE_DEST_ADDR = "IsolateDestAddr"
            const val KEEP_ALIVE_ISOLATE_SOCKS_AUTH = "KeepAliveIsolateSOCKSAuth"
            const val NO_IPV4_TRAFFIC = "NoIPv4Traffic"
            const val IPV6_TRAFFIC = "IPv6Traffic"
            const val PREFER_IPV6 = "PreferIPv6"
            const val NO_DNS_REQUEST = "NoDNSRequest"
            const val NO_ONION_TRAFFIC = "NoOnionTraffic"
            const val ONION_TRAFFIC_ONLY = "OnionTrafficOnly"
            const val CACHE_IPV4_DNS = "CacheIPv4DNS"
            const val CACHE_IPV6_DNS = "CacheIPv6DNS"
            const val CACHE_DNS = "CacheDNS"
            const val USE_IPV4_CACHE = "UseIPv4Cache"
            const val USE_IPV6_CACHE = "UseIPv6Cache"
            const val USE_DNS_CACHE = "UseDNSCache"
            const val PREFER_IPV6_AUTOMAP = "PreferIPv6Automap"
            const val PREFER_SOCKS_NO_AUTH = "PreferSOCKSNoAuth"

            fun getAll(): List<@IsolationFlag String> {
                return arrayListOf(
                    NO_ISOLATE_CLIENT_ADDR,
                    NO_ISOLATE_SOCKS_AUTH,
                    ISOLATE_CLIENT_PROTOCOL,
                    ISOLATE_DEST_PORT,
                    ISOLATE_DEST_ADDR,
                    KEEP_ALIVE_ISOLATE_SOCKS_AUTH,
                    NO_IPV4_TRAFFIC,
                    IPV6_TRAFFIC,
                    PREFER_IPV6,
                    NO_DNS_REQUEST,
                    NO_ONION_TRAFFIC,
                    ONION_TRAFFIC_ONLY,
                    CACHE_IPV4_DNS,
                    CACHE_IPV6_DNS,
                    CACHE_DNS,
                    USE_IPV4_CACHE,
                    USE_IPV6_CACHE,
                    USE_DNS_CACHE,
                    PREFER_IPV6_AUTOMAP,
                    PREFER_SOCKS_NO_AUTH
                )
            }
        }
    }


    ///////////////////////////
    /// TorConfigFile Names ///
    ///////////////////////////
    @Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.TYPE,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.PROPERTY
    )
    @StringDef(
        ConfigFileName.CONTROL_PORT,
        ConfigFileName.COOKIE_AUTH,
        ConfigFileName.DATA_DIR,
        ConfigFileName.GEO_IP,
        ConfigFileName.GEO_IPV_6,
        ConfigFileName.HOST,
        ConfigFileName.HIDDEN_SERVICE,
        ConfigFileName.RESOLVE_CONF,
        ConfigFileName.TOR_EXECUTABLE,
        ConfigFileName.TORRC,
        ConfigFileName.V3_AUTH_PRIVATE_DIR
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ConfigFileName {
        companion object {
            const val CONTROL_PORT = "control.txt"
            const val COOKIE_AUTH = "control_auth_cookie"
            const val DATA_DIR = "lib/tor"
            const val GEO_IP = "geoip"
            const val GEO_IPV_6 = "geoip6"
            const val HOST = "hostname"
            const val HIDDEN_SERVICE = "hiddenservice"
            const val RESOLVE_CONF = "resolv.conf"
            const val TOR_EXECUTABLE = "libTor.so"
            const val TORRC = "torrc"
            const val V3_AUTH_PRIVATE_DIR = "auth_private_files"
        }
    }
}