[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [proxyHostSave](./proxy-host-save.md)

# proxyHostSave

`@WorkerThread abstract fun proxyHostSave(proxyHost: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L292)

Saves the value for [proxyHost](proxy-host-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$proxyHostSave(kotlin.String)/proxyHost) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`proxyHost` -

**See Also**

[TorSettings.proxyHost](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/proxy-host.md)

