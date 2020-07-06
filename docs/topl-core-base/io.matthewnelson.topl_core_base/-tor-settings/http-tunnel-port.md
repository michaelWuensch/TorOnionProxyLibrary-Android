[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [httpTunnelPort](./http-tunnel-port.md)

# httpTunnelPort

`abstract val httpTunnelPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L144)

TorBrowser and Orbot use "8218" by default. It may be wise to pick something
that won't conflict if you're using this setting.

Docs: https://2019.www.torproject.org/docs/tor-manual.html.en#HTTPTunnelPort

See [DEFAULT__HTTP_TUNNEL_PORT](-d-e-f-a-u-l-t__-h-t-t-p_-t-u-n-n-e-l_-p-o-r-t.md)

TODO: Change to List and update TorSettingsBuilder method for
multi-port support.

