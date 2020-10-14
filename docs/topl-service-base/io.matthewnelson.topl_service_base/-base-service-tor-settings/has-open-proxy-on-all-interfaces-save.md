[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [hasOpenProxyOnAllInterfacesSave](./has-open-proxy-on-all-interfaces-save.md)

# hasOpenProxyOnAllInterfacesSave

`@WorkerThread abstract fun hasOpenProxyOnAllInterfacesSave(boolean: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L615)

Saves the value for [hasOpenProxyOnAllInterfaces](has-open-proxy-on-all-interfaces.md) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same
as what is declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the
setting if it exists.

### Parameters

`boolean` - to enable/disable

**See Also**

[TorSettings.hasOpenProxyOnAllInterfaces](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/has-open-proxy-on-all-interfaces.md)

