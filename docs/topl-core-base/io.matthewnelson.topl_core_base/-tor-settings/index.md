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
| [connectionPadding](connection-padding.md) | See [DEFAULT__HAS_CONNECTION_PADDING](-d-e-f-a-u-l-t__-h-a-s_-c-o-n-n-e-c-t-i-o-n_-p-a-d-d-i-n-g.md)`abstract val connectionPadding: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [customTorrc](custom-torrc.md) | Default [java.null](#)`abstract val customTorrc: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [disableNetwork](disable-network.md) | See [DEFAULT__DISABLE_NETWORK](-d-e-f-a-u-l-t__-d-i-s-a-b-l-e_-n-e-t-w-o-r-k.md)`abstract val disableNetwork: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [dnsPort](dns-port.md) | TorBrowser and Orbot use "5400" by default. It may be wise to pick something that won't conflict.`abstract val dnsPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [entryNodes](entry-nodes.md) | See [DEFAULT__ENTRY_NODES](-d-e-f-a-u-l-t__-e-n-t-r-y_-n-o-d-e-s.md)`abstract val entryNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [excludeNodes](exclude-nodes.md) | See [DEFAULT__EXCLUDED_NODES](-d-e-f-a-u-l-t__-e-x-c-l-u-d-e-d_-n-o-d-e-s.md)`abstract val excludeNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [exitNodes](exit-nodes.md) | See [DEFAULT__EXIT_NODES](-d-e-f-a-u-l-t__-e-x-i-t_-n-o-d-e-s.md)`abstract val exitNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [hasBridges](has-bridges.md) | See [DEFAULT__HAS_BRIDGES](-d-e-f-a-u-l-t__-h-a-s_-b-r-i-d-g-e-s.md)`abstract val hasBridges: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasCookieAuthentication](has-cookie-authentication.md) | See [DEFAULT__HAS_COOKIE_AUTHENTICATION](-d-e-f-a-u-l-t__-h-a-s_-c-o-o-k-i-e_-a-u-t-h-e-n-t-i-c-a-t-i-o-n.md)`abstract val hasCookieAuthentication: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasDebugLogs](has-debug-logs.md) | See [DEFAULT__HAS_DEBUG_LOGS](-d-e-f-a-u-l-t__-h-a-s_-d-e-b-u-g_-l-o-g-s.md)`abstract val hasDebugLogs: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasDormantCanceledByStartup](has-dormant-canceled-by-startup.md) | See [DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP](-d-e-f-a-u-l-t__-h-a-s_-d-o-r-m-a-n-t_-c-a-n-c-e-l-e-d_-b-y_-s-t-a-r-t-u-p.md)`abstract val hasDormantCanceledByStartup: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasIsolationAddressFlagForTunnel](has-isolation-address-flag-for-tunnel.md) | See [DEFAULT__HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL](-d-e-f-a-u-l-t__-h-a-s_-i-s-o-l-a-t-i-o-n_-a-d-d-r-e-s-s_-f-l-a-g_-f-o-r_-t-u-n-n-e-l.md)`abstract val hasIsolationAddressFlagForTunnel: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasOpenProxyOnAllInterfaces](has-open-proxy-on-all-interfaces.md) | See [DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES](-d-e-f-a-u-l-t__-h-a-s_-o-p-e-n_-p-r-o-x-y_-o-n_-a-l-l_-i-n-t-e-r-f-a-c-e-s.md)`abstract val hasOpenProxyOnAllInterfaces: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasReachableAddress](has-reachable-address.md) | See [DEFAULT__HAS_REACHABLE_ADDRESS](-d-e-f-a-u-l-t__-h-a-s_-r-e-a-c-h-a-b-l-e_-a-d-d-r-e-s-s.md)`abstract val hasReachableAddress: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasReducedConnectionPadding](has-reduced-connection-padding.md) | See [DEFAULT__HAS_REDUCED_CONNECTION_PADDING](-d-e-f-a-u-l-t__-h-a-s_-r-e-d-u-c-e-d_-c-o-n-n-e-c-t-i-o-n_-p-a-d-d-i-n-g.md)`abstract val hasReducedConnectionPadding: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasSafeSocks](has-safe-socks.md) | See [DEFAULT__HAS_SAFE_SOCKS](-d-e-f-a-u-l-t__-h-a-s_-s-a-f-e_-s-o-c-k-s.md)`abstract val hasSafeSocks: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasStrictNodes](has-strict-nodes.md) | See [DEFAULT__HAS_STRICT_NODES](-d-e-f-a-u-l-t__-h-a-s_-s-t-r-i-c-t_-n-o-d-e-s.md)`abstract val hasStrictNodes: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hasTestSocks](has-test-socks.md) | See [DEFAULT__HAS_TEST_SOCKS](-d-e-f-a-u-l-t__-h-a-s_-t-e-s-t_-s-o-c-k-s.md)`abstract val hasTestSocks: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [httpTunnelPort](http-tunnel-port.md) | Could be "auto" or a specific port, such as "8288".`abstract val httpTunnelPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [isAutoMapHostsOnResolve](is-auto-map-hosts-on-resolve.md) | See [DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE](-d-e-f-a-u-l-t__-i-s_-a-u-t-o_-m-a-p_-h-o-s-t-s_-o-n_-r-e-s-o-l-v-e.md)`abstract val isAutoMapHostsOnResolve: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isRelay](is-relay.md) | See [DEFAULT__IS_RELAY](-d-e-f-a-u-l-t__-i-s_-r-e-l-a-y.md)`abstract val isRelay: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [listOfSupportedBridges](list-of-supported-bridges.md) | Must have the transport binaries for obfs4 and/or snowflake, depending on if you wish to include them in your bridges file to use.`abstract val listOfSupportedBridges: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [proxyHost](proxy-host.md) | See [DEFAULT__PROXY_HOST](-d-e-f-a-u-l-t__-p-r-o-x-y_-h-o-s-t.md)`abstract val proxyHost: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [proxyPassword](proxy-password.md) | See [DEFAULT__PROXY_PASSWORD](-d-e-f-a-u-l-t__-p-r-o-x-y_-p-a-s-s-w-o-r-d.md)`abstract val proxyPassword: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [proxyPort](proxy-port.md) | Default = [java.null](#)`abstract val proxyPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [proxySocks5Host](proxy-socks5-host.md) | See [DEFAULT__PROXY_SOCKS5_HOST](-d-e-f-a-u-l-t__-p-r-o-x-y_-s-o-c-k-s5_-h-o-s-t.md)`abstract val proxySocks5Host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [proxySocks5ServerPort](proxy-socks5-server-port.md) | Default = [java.null](#)`abstract val proxySocks5ServerPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [proxyType](proxy-type.md) | See [DEFAULT__PROXY_TYPE](-d-e-f-a-u-l-t__-p-r-o-x-y_-t-y-p-e.md)`abstract val proxyType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [proxyUser](proxy-user.md) | See [DEFAULT__PROXY_USER](-d-e-f-a-u-l-t__-p-r-o-x-y_-u-s-e-r.md)`abstract val proxyUser: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [reachableAddressPorts](reachable-address-ports.md) | See [DEFAULT__REACHABLE_ADDRESS_PORTS](-d-e-f-a-u-l-t__-r-e-a-c-h-a-b-l-e_-a-d-d-r-e-s-s_-p-o-r-t-s.md)`abstract val reachableAddressPorts: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [relayNickname](relay-nickname.md) | See [DEFAULT__RELAY_NICKNAME](-d-e-f-a-u-l-t__-r-e-l-a-y_-n-i-c-k-n-a-m-e.md)`abstract val relayNickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [relayPort](relay-port.md) | TorBrowser and Orbot use 9001 by default. It may be wise to pick something that won't conflict.`abstract val relayPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [runAsDaemon](run-as-daemon.md) | See [DEFAULT__RUN_AS_DAEMON](-d-e-f-a-u-l-t__-r-u-n_-a-s_-d-a-e-m-o-n.md)`abstract val runAsDaemon: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [socksPort](socks-port.md) | Could be "auto" or a specific port, such as "9051".`abstract val socksPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [transPort](trans-port.md) | Can be "auto", or a specified port such as "9141"`abstract val transPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [useSocks5](use-socks5.md) | See [DEFAULT__USE_SOCKS5](-d-e-f-a-u-l-t__-u-s-e_-s-o-c-k-s5.md)`abstract val useSocks5: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [virtualAddressNetwork](virtual-address-network.md) | TorBrowser and Orbot use "10.192.0.1/10", it may be wise to pick something that won't conflict if you are using this setting.`abstract val virtualAddressNetwork: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [DEFAULT__DISABLE_NETWORK](-d-e-f-a-u-l-t__-d-i-s-a-b-l-e_-n-e-t-w-o-r-k.md) | `const val DEFAULT__DISABLE_NETWORK: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__DNS_PORT](-d-e-f-a-u-l-t__-d-n-s_-p-o-r-t.md) | `const val DEFAULT__DNS_PORT: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__ENTRY_NODES](-d-e-f-a-u-l-t__-e-n-t-r-y_-n-o-d-e-s.md) | `const val DEFAULT__ENTRY_NODES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__EXCLUDED_NODES](-d-e-f-a-u-l-t__-e-x-c-l-u-d-e-d_-n-o-d-e-s.md) | `const val DEFAULT__EXCLUDED_NODES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__EXIT_NODES](-d-e-f-a-u-l-t__-e-x-i-t_-n-o-d-e-s.md) | `const val DEFAULT__EXIT_NODES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__HAS_BRIDGES](-d-e-f-a-u-l-t__-h-a-s_-b-r-i-d-g-e-s.md) | `const val DEFAULT__HAS_BRIDGES: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_CONNECTION_PADDING](-d-e-f-a-u-l-t__-h-a-s_-c-o-n-n-e-c-t-i-o-n_-p-a-d-d-i-n-g.md) | `const val DEFAULT__HAS_CONNECTION_PADDING: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__HAS_COOKIE_AUTHENTICATION](-d-e-f-a-u-l-t__-h-a-s_-c-o-o-k-i-e_-a-u-t-h-e-n-t-i-c-a-t-i-o-n.md) | `const val DEFAULT__HAS_COOKIE_AUTHENTICATION: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_DEBUG_LOGS](-d-e-f-a-u-l-t__-h-a-s_-d-e-b-u-g_-l-o-g-s.md) | `const val DEFAULT__HAS_DEBUG_LOGS: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP](-d-e-f-a-u-l-t__-h-a-s_-d-o-r-m-a-n-t_-c-a-n-c-e-l-e-d_-b-y_-s-t-a-r-t-u-p.md) | `const val DEFAULT__HAS_DORMANT_CANCELED_BY_STARTUP: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL](-d-e-f-a-u-l-t__-h-a-s_-i-s-o-l-a-t-i-o-n_-a-d-d-r-e-s-s_-f-l-a-g_-f-o-r_-t-u-n-n-e-l.md) | `const val DEFAULT__HAS_ISOLATION_ADDRESS_FLAG_FOR_TUNNEL: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES](-d-e-f-a-u-l-t__-h-a-s_-o-p-e-n_-p-r-o-x-y_-o-n_-a-l-l_-i-n-t-e-r-f-a-c-e-s.md) | `const val DEFAULT__HAS_OPEN_PROXY_ON_ALL_INTERFACES: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_REACHABLE_ADDRESS](-d-e-f-a-u-l-t__-h-a-s_-r-e-a-c-h-a-b-l-e_-a-d-d-r-e-s-s.md) | `const val DEFAULT__HAS_REACHABLE_ADDRESS: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_REDUCED_CONNECTION_PADDING](-d-e-f-a-u-l-t__-h-a-s_-r-e-d-u-c-e-d_-c-o-n-n-e-c-t-i-o-n_-p-a-d-d-i-n-g.md) | `const val DEFAULT__HAS_REDUCED_CONNECTION_PADDING: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_SAFE_SOCKS](-d-e-f-a-u-l-t__-h-a-s_-s-a-f-e_-s-o-c-k-s.md) | `const val DEFAULT__HAS_SAFE_SOCKS: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_STRICT_NODES](-d-e-f-a-u-l-t__-h-a-s_-s-t-r-i-c-t_-n-o-d-e-s.md) | `const val DEFAULT__HAS_STRICT_NODES: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HAS_TEST_SOCKS](-d-e-f-a-u-l-t__-h-a-s_-t-e-s-t_-s-o-c-k-s.md) | `const val DEFAULT__HAS_TEST_SOCKS: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__HTTP_TUNNEL_PORT](-d-e-f-a-u-l-t__-h-t-t-p_-t-u-n-n-e-l_-p-o-r-t.md) | `const val DEFAULT__HTTP_TUNNEL_PORT: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE](-d-e-f-a-u-l-t__-i-s_-a-u-t-o_-m-a-p_-h-o-s-t-s_-o-n_-r-e-s-o-l-v-e.md) | `const val DEFAULT__IS_AUTO_MAP_HOSTS_ON_RESOLVE: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__IS_RELAY](-d-e-f-a-u-l-t__-i-s_-r-e-l-a-y.md) | `const val DEFAULT__IS_RELAY: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__PROXY_HOST](-d-e-f-a-u-l-t__-p-r-o-x-y_-h-o-s-t.md) | `const val DEFAULT__PROXY_HOST: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__PROXY_PASSWORD](-d-e-f-a-u-l-t__-p-r-o-x-y_-p-a-s-s-w-o-r-d.md) | `const val DEFAULT__PROXY_PASSWORD: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__PROXY_SOCKS5_HOST](-d-e-f-a-u-l-t__-p-r-o-x-y_-s-o-c-k-s5_-h-o-s-t.md) | `const val DEFAULT__PROXY_SOCKS5_HOST: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__PROXY_TYPE](-d-e-f-a-u-l-t__-p-r-o-x-y_-t-y-p-e.md) | `const val DEFAULT__PROXY_TYPE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__PROXY_USER](-d-e-f-a-u-l-t__-p-r-o-x-y_-u-s-e-r.md) | `const val DEFAULT__PROXY_USER: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__REACHABLE_ADDRESS_PORTS](-d-e-f-a-u-l-t__-r-e-a-c-h-a-b-l-e_-a-d-d-r-e-s-s_-p-o-r-t-s.md) | `const val DEFAULT__REACHABLE_ADDRESS_PORTS: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__RELAY_NICKNAME](-d-e-f-a-u-l-t__-r-e-l-a-y_-n-i-c-k-n-a-m-e.md) | `const val DEFAULT__RELAY_NICKNAME: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__RUN_AS_DAEMON](-d-e-f-a-u-l-t__-r-u-n_-a-s_-d-a-e-m-o-n.md) | `const val DEFAULT__RUN_AS_DAEMON: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [DEFAULT__TRANS_PORT](-d-e-f-a-u-l-t__-t-r-a-n-s_-p-o-r-t.md) | `const val DEFAULT__TRANS_PORT: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DEFAULT__USE_SOCKS5](-d-e-f-a-u-l-t__-u-s-e_-s-o-c-k-s5.md) | `const val DEFAULT__USE_SOCKS5: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
