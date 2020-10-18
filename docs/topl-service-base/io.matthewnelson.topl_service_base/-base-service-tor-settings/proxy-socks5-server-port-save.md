[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [proxySocks5ServerPortSave](./proxy-socks5-server-port-save.md)

# proxySocks5ServerPortSave

`@WorkerThread abstract fun proxySocks5ServerPortSave(proxySocks5ServerPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L364)

Saves the value for [proxySocks5ServerPort](proxy-socks5-server-port-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$proxySocks5ServerPortSave(kotlin.Int)/proxySocks5ServerPort) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same
as what is declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the
setting if it exists.

### Parameters

`proxySocks5ServerPort` - An Int value between 1024 and 65535, or `null`

### Exceptions

`IllegalArgumentException` - if the value is not `null`, or between 1024 and 65535

**See Also**

[TorSettings.proxySocks5ServerPort](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/proxy-socks5-server-port.md)

