[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [proxyPortSave](./proxy-port-save.md)

# proxyPortSave

`@WorkerThread abstract fun proxyPortSave(proxyPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L328)

Saves the value for [proxyPort](proxy-port-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$proxyPortSave(kotlin.Int)/proxyPort) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`proxyPort` - An Int value between 1024 and 65535, or `null`

### Exceptions

`IllegalArgumentException` - if the value is not `null`, or between 1024 and 65535

**See Also**

[TorSettings.proxyPort](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/proxy-port.md)

