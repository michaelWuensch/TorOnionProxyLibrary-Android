[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [ServiceTorSettings](index.md) / [reachableAddressPortsSave](./reachable-address-ports-save.md)

# reachableAddressPortsSave

`fun reachableAddressPortsSave(reachableAddressPorts: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/ServiceTorSettings.kt#L494)

Saves the value for [reachableAddressPorts](reachable-address-ports-save.md#io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings$reachableAddressPortsSave(kotlin.String)/reachableAddressPorts) to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`reachableAddressPorts` -

**See Also**

[TorSettings.reachableAddressPorts](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/reachable-address-ports.md)

