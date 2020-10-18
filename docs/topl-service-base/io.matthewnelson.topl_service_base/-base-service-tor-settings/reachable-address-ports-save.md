[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [reachableAddressPortsSave](./reachable-address-ports-save.md)

# reachableAddressPortsSave

`@WorkerThread abstract fun reachableAddressPortsSave(reachableAddressPorts: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L418)

Saves the value for [reachableAddressPorts](reachable-address-ports-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$reachableAddressPortsSave(kotlin.String)/reachableAddressPorts) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`reachableAddressPorts` -

**See Also**

[TorSettings.reachableAddressPorts](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/reachable-address-ports.md)

