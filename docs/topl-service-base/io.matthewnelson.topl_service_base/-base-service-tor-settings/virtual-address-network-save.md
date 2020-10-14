[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [virtualAddressNetworkSave](./virtual-address-network-save.md)

# virtualAddressNetworkSave

`@WorkerThread abstract fun virtualAddressNetworkSave(virtualAddressNetwork: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L510)

Saves the value for [virtualAddressNetwork](virtual-address-network-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$virtualAddressNetworkSave(kotlin.String)/virtualAddressNetwork) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same
as what is declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the
setting if it exists.

### Parameters

`virtualAddressNetwork` -

**See Also**

[TorSettings.virtualAddressNetwork](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/virtual-address-network.md)

