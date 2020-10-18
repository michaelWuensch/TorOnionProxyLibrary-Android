[topl-core](../../index.md) / [io.matthewnelson.topl_core.settings](../index.md) / [TorSettingsBuilder](index.md) / [proxyWithAuthentication](./proxy-with-authentication.md)

# proxyWithAuthentication

`fun proxyWithAuthentication(proxyType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, proxyHost: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, proxyPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?, proxyUser: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, proxyPass: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/settings/TorSettingsBuilder.kt#L583)

Sets proxyWithAuthentication information.

requires that [TorSettings.useSocks5](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/use-socks5.md) && [TorSettings.hasBridges](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/has-bridges.md) are set to `false`

NOTE: Only supports Socks5 or HTTPS

