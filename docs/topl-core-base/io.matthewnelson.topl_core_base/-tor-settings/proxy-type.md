[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [proxyType](./proxy-type.md)

# proxyType

`abstract val proxyType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L423)

Depending on the [BaseConsts.ProxyType](../-base-consts/-proxy-type/index.md), will add authenticated Socks5 or HTTPS proxy,
if other settings are configured properly.

This only gets used if you declare the following settings set as:

[useSocks5](use-socks5.md) is set to `false`
[hasBridges](has-bridges.md) is set to `false`
[proxyType](./proxy-type.md) is [BaseConsts.ProxyType.SOCKS_5](../-base-consts/-proxy-type/-s-o-c-k-s_5.md) or [BaseConsts.ProxyType.HTTPS](proxy-host.md) is set (eg. 127.0.0.1)
[proxyPort](proxy-port.md) is `null`, or a port between 1024 and 65535
[proxyUser](proxy-user.md) is set
[proxyPassword](proxy-password.md) is set

See [BaseConsts.ProxyType.DISABLED](../-base-consts/-proxy-type/-d-i-s-a-b-l-e-d.md)

