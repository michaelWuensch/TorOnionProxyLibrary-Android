[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [httpTunnelPort](./http-tunnel-port.md)

# httpTunnelPort

`abstract val httpTunnelPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L341)

Could be "auto" or a specific port, such as "8288".

TorBrowser and Orbot use "8218" and "8118", respectively, by default.
It may be wise to pick something that won't conflict if you're using this setting.

**Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#HTTPTunnelPort

See [BaseConsts.PortOption.DISABLED](../-base-consts/-port-option/-d-i-s-a-b-l-e-d.md)

