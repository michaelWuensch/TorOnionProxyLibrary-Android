[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](./index.md)

# TorSettings

`abstract class TorSettings : `[`BaseConsts`](../-base-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L106)

This class is for defining default values for your torrc file. Extend this class and define
your own settings.

Keep in mind that Orbot and TorBrowser are the 2 most widely used applications
using Tor, and to use settings that won't conflict (those settings are documented
as such, and contain further details).

[TorSettings.Companion](#) contains pretty standard default values which'll get you a Socks5 proxy
running, nothing more.

Would **highly recommend** reading up on what's what in the manual:

* https://2019.www.torproject.org/docs/tor-manual.html.en

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | This class is for defining default values for your torrc file. Extend this class and define your own settings.`TorSettings()` |

### Properties

| Name | Summary |
|---|---|
| [connectionPadding](connection-padding.md) | Adds to the torrc file "ConnectionPadding &lt;0, 1, or auto&gt;"`abstract val connectionPadding: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [customTorrc](custom-torrc.md) | If not null/not empty, will add the string value to the torrc file`abstract val customTorrc: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [disableNetwork](disable-network.md) | OnionProxyManager will enable this on startup using the TorControlConnection based off of the device's network state. Setting this to `true` is highly recommended.`abstract val disableNetwork: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [dnsPort](dns-port.md) | TorBrowser and Orbot use "5400" by default. It may be wise to pick something that won't conflict.`abstract val dnsPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [dnsPortIsolationFlags](dns-port-isolation-flags.md) | Express isolation flags to be added when enabling the [dnsPort](dns-port.md)`abstract val dnsPortIsolationFlags: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [dormantClientTimeout](dormant-client-timeout.md) | Adds to the torrc file "DormantClientTimeout  minutes"`abstract val dormantClientTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [entryNodes](entry-nodes.md) | Set with a comma separated list of Entry Nodes.`abstract val entryNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [excludeNodes](exclude-nodes.md) | Set with a comma separated list of Exit Nodes to be excluded.`abstract val excludeNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [exitNodes](exit-nodes.md) | Set with a comma separated list of Exit Nodes to use.`abstract val exitNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [hasBridges](has-bridges.md) | If `true`, adds to the torrc file "UseBridges 1" and will proc the adding of bridges.`abstract val hasBridges: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasCookieAuthentication](has-cookie-authentication.md) | **Highly** recommended to be set to `true` for securing the ControlPort`abstract val hasCookieAuthentication: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasDebugLogs](has-debug-logs.md) | Adds to the torrc file:`abstract val hasDebugLogs: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasDormantCanceledByStartup](has-dormant-canceled-by-startup.md) | **Highly** recommended to be set to `true` for Android applications.`abstract val hasDormantCanceledByStartup: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasOpenProxyOnAllInterfaces](has-open-proxy-on-all-interfaces.md) | If true, adds to the torrc file "SocksListenAddress 0.0.0.0"`abstract val hasOpenProxyOnAllInterfaces: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasReachableAddress](has-reachable-address.md) | If true, adds to the torrc file "ReachableAddresses &lt;[reachableAddressPorts](reachable-address-ports.md)&gt;"`abstract val hasReachableAddress: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasReducedConnectionPadding](has-reduced-connection-padding.md) | If true, adds to the torrc file "ReducedConnectionPadding 1"`abstract val hasReducedConnectionPadding: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasSafeSocks](has-safe-socks.md) | If true, adds to the torrc file "SafeSocks 1"`abstract val hasSafeSocks: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasStrictNodes](has-strict-nodes.md) | If true, adds to the torrc file "StrictNodes 1"`abstract val hasStrictNodes: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasTestSocks](has-test-socks.md) | If true, adds to the torrc file "TestSocks 1"`abstract val hasTestSocks: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [httpTunnelPort](http-tunnel-port.md) | Could be "auto" or a specific port, such as "8288".`abstract val httpTunnelPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [httpTunnelPortIsolationFlags](http-tunnel-port-isolation-flags.md) | Express isolation flags to be added when enabling the [httpTunnelPort](http-tunnel-port.md)`abstract val httpTunnelPortIsolationFlags: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [isAutoMapHostsOnResolve](is-auto-map-hosts-on-resolve.md) | See [DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE](-d-e-f-a-u-l-t__-i-s_-a-u-t-o_-m-a-p_-h-o-s-t-s_-o-n_-r-e-s-o-l-v-e.md)`abstract val isAutoMapHostsOnResolve: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isRelay](is-relay.md) | See [DEFAULT__IS_RELAY](-d-e-f-a-u-l-t__-i-s_-r-e-l-a-y.md)`abstract val isRelay: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [listOfSupportedBridges](list-of-supported-bridges.md) | Must have the transport binaries for obfs4 and/or snowflake, depending on if you wish to include them in your bridges file to use.`abstract val listOfSupportedBridges: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [proxyHost](proxy-host.md) | See [DEFAULT__PROXY_HOST](-d-e-f-a-u-l-t__-p-r-o-x-y_-h-o-s-t.md)`abstract val proxyHost: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [proxyPassword](proxy-password.md) | See [DEFAULT__PROXY_PASSWORD](-d-e-f-a-u-l-t__-p-r-o-x-y_-p-a-s-s-w-o-r-d.md)`abstract val proxyPassword: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [proxyPort](proxy-port.md) | Default = [java.null](#)`abstract val proxyPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [proxySocks5Host](proxy-socks5-host.md) | Adds to the torrc file "Socks5Proxy [proxySocks5Host](proxy-socks5-host.md):[proxySocks5ServerPort](proxy-socks5-server-port.md)"`abstract val proxySocks5Host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [proxySocks5ServerPort](proxy-socks5-server-port.md) | Adds to the torrc file "Socks5Proxy [proxySocks5Host](proxy-socks5-host.md):[proxySocks5ServerPort](proxy-socks5-server-port.md)"`abstract val proxySocks5ServerPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [proxyType](proxy-type.md) | Depending on the [BaseConsts.ProxyType](../-base-consts/-proxy-type/index.md), will add authenticated Socks5 or HTTPS proxy, if other settings are configured properly.`abstract val proxyType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [proxyUser](proxy-user.md) | See [DEFAULT__PROXY_USER](-d-e-f-a-u-l-t__-p-r-o-x-y_-u-s-e-r.md)`abstract val proxyUser: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [reachableAddressPorts](reachable-address-ports.md) | Adds to the torrc file "ReachableAddresses &lt;[reachableAddressPorts](reachable-address-ports.md)&gt;"`abstract val reachableAddressPorts: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [relayNickname](relay-nickname.md) | See [DEFAULT__RELAY_NICKNAME](-d-e-f-a-u-l-t__-r-e-l-a-y_-n-i-c-k-n-a-m-e.md)`abstract val relayNickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [relayPort](relay-port.md) | TorBrowser and Orbot use 9001 by default. It may be wise to pick something that won't conflict.`abstract val relayPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [runAsDaemon](run-as-daemon.md) | If `true`, adds to the torrc file "RunAsDaemon 1" See [DEFAULT__RUN_AS_DAEMON](-d-e-f-a-u-l-t__-r-u-n_-a-s_-d-a-e-m-o-n.md)`abstract val runAsDaemon: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [socksPort](socks-port.md) | Could be "auto" or a specific port, such as "9051".`abstract val socksPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [socksPortIsolationFlags](socks-port-isolation-flags.md) | Express isolation flags to be added when enabling the [socksPort](socks-port.md)`abstract val socksPortIsolationFlags: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [transPort](trans-port.md) | Can be "auto", or a specified port such as "9141"`abstract val transPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [transPortIsolationFlags](trans-port-isolation-flags.md) | Express isolation flags to be added when enabling the [transPort](trans-port.md)`abstract val transPortIsolationFlags: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [useSocks5](use-socks5.md) | See [DEFAULT__USE_SOCKS5](-d-e-f-a-u-l-t__-u-s-e_-s-o-c-k-s5.md)`abstract val useSocks5: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [virtualAddressNetwork](virtual-address-network.md) | TorBrowser and Orbot use "10.192.0.1/10", it may be wise to pick something that won't conflict if you are using this setting.`abstract val virtualAddressNetwork: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [DEFAULT__DISABLE_NETWORK](-d-e-f-a-u-l-t__-d-i-s-a-b-l-e_-n-e-t-w-o-r-k.md) | `const val DEFAULT__DISABLE_NETWORK: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__DORMANT_CLIENT_TIMEOUT](-d-e-f-a-u-l-t__-d-o-r-m-a-n-t_-c-l-i-e-n-t_-t-i-m-e-o-u-t.md) | `const val DEFAULT__DORMANT_CLIENT_TIMEOUT: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [DEFAULT__ENTRY_NODES](-d-e-f-a-u-l-t__-e-n-t-r-y_-n-o-d-e-s.md) | `const val DEFAULT__ENTRY_NODES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__EXCLUDED_NODES](-d-e-f-a-u-l-t__-e-x-c-l-u-d-e-d_-n-o-d-e-s.md) | `const val DEFAULT__EXCLUDED_NODES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__EXIT_NODES](-d-e-f-a-u-l-t__-e-x-i-t_-n-o-d-e-s.md) | `const val DEFAULT__EXIT_NODES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__HAS_BRIDGES](-d-e-f-a-u-l-t__-h-a-s_-b-r-i-d-g-e-s.md) | `const val DEFAULT__HAS_BRIDGES: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_COOKIE_AUTHENTICATION](-d-e-f-a-u-l-t__-h-a-s_-c-o-o-k-i-e_-a-u-t-h-e-n-t-i-c-a-t-i-o-n.md) | `const val DEFAULT__HAS_COOKIE_AUTHENTICATION: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_DEBUG_LOGS](-d-e-f-a-u-l-t__-h-a-s_-d-e-b-u-g_-l-o-g-s.md) | `const val DEFAULT__HAS_DEBUG_LOGS: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP](-d-e-f-a-u-l-t__-h-a-s_-d-o-r-m-a-n-t_-c-a-n-c-e-l-e-d_-b-y_-s-t-a-r-t-u-p.md) | `const val DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES](-d-e-f-a-u-l-t__-h-a-s_-o-p-e-n_-p-r-o-x-y_-o-n_-a-l-l_-i-n-t-e-r-f-a-c-e-s.md) | `const val DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_REACHABLE_ADDRESS](-d-e-f-a-u-l-t__-h-a-s_-r-e-a-c-h-a-b-l-e_-a-d-d-r-e-s-s.md) | `const val DEFAULT__HAS_REACHABLE_ADDRESS: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_REDUCED_CONNECTION_PADDING](-d-e-f-a-u-l-t__-h-a-s_-r-e-d-u-c-e-d_-c-o-n-n-e-c-t-i-o-n_-p-a-d-d-i-n-g.md) | `const val DEFAULT__HAS_REDUCED_CONNECTION_PADDING: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_SAFE_SOCKS](-d-e-f-a-u-l-t__-h-a-s_-s-a-f-e_-s-o-c-k-s.md) | `const val DEFAULT__HAS_SAFE_SOCKS: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_STRICT_NODES](-d-e-f-a-u-l-t__-h-a-s_-s-t-r-i-c-t_-n-o-d-e-s.md) | `const val DEFAULT__HAS_STRICT_NODES: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_TEST_SOCKS](-d-e-f-a-u-l-t__-h-a-s_-t-e-s-t_-s-o-c-k-s.md) | `const val DEFAULT__HAS_TEST_SOCKS: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE](-d-e-f-a-u-l-t__-i-s_-a-u-t-o_-m-a-p_-h-o-s-t-s_-o-n_-r-e-s-o-l-v-e.md) | `const val DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__IS_RELAY](-d-e-f-a-u-l-t__-i-s_-r-e-l-a-y.md) | `const val DEFAULT__IS_RELAY: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__PROXY_HOST](-d-e-f-a-u-l-t__-p-r-o-x-y_-h-o-s-t.md) | `const val DEFAULT__PROXY_HOST: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__PROXY_PASSWORD](-d-e-f-a-u-l-t__-p-r-o-x-y_-p-a-s-s-w-o-r-d.md) | `const val DEFAULT__PROXY_PASSWORD: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__PROXY_SOCKS5_HOST](-d-e-f-a-u-l-t__-p-r-o-x-y_-s-o-c-k-s5_-h-o-s-t.md) | `const val DEFAULT__PROXY_SOCKS5_HOST: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__PROXY_USER](-d-e-f-a-u-l-t__-p-r-o-x-y_-u-s-e-r.md) | `const val DEFAULT__PROXY_USER: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__REACHABLE_ADDRESS_PORTS](-d-e-f-a-u-l-t__-r-e-a-c-h-a-b-l-e_-a-d-d-r-e-s-s_-p-o-r-t-s.md) | `const val DEFAULT__REACHABLE_ADDRESS_PORTS: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__RELAY_NICKNAME](-d-e-f-a-u-l-t__-r-e-l-a-y_-n-i-c-k-n-a-m-e.md) | `const val DEFAULT__RELAY_NICKNAME: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__RUN_AS_DAEMON](-d-e-f-a-u-l-t__-r-u-n_-a-s_-d-a-e-m-o-n.md) | `const val DEFAULT__RUN_AS_DAEMON: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__USE_SOCKS5](-d-e-f-a-u-l-t__-u-s-e_-s-o-c-k-s5.md) | `const val DEFAULT__USE_SOCKS5: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
