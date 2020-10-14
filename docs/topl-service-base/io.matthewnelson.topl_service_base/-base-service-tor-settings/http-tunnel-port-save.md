[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [httpTunnelPortSave](./http-tunnel-port-save.md)

# httpTunnelPortSave

`@WorkerThread abstract fun httpTunnelPortSave(httpPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L250)

Saves the value for [httpPort](http-tunnel-port-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$httpTunnelPortSave(kotlin.String)/httpPort) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`httpPort` - A String value of 0, auto, or number between 1024 and 65535

### Exceptions

`IllegalArgumentException` - if the value is not 0, auto, or between 1024 and 65535

**See Also**

[TorSettings.httpTunnelPort](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/http-tunnel-port.md)

