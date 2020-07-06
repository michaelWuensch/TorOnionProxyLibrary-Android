[topl-core](../../index.md) / [io.matthewnelson.topl_core.settings](../index.md) / [TorSettingsBuilder](index.md) / [proxyWithAuthentication](./proxy-with-authentication.md)

# proxyWithAuthentication

`fun proxyWithAuthentication(proxyType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, proxyHost: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, proxyPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?, proxyUser: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, proxyPass: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/settings/TorSettingsBuilder.kt#L436)

Sets proxyWithAuthentication information. If proxyType, proxyHost or proxyPort is
empty/null, then this method does nothing.

HTTPProxyAuthenticator is deprecated as of 0.3.1.0-alpha, *use HTTPS/Socks5* authentication.

TODO: Remove support for HTTPProxyAuthenticator
TODO: Re-work this mess with annotation types and when statements...

