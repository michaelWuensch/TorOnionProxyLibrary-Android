[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [ServiceTorSettings](index.md) / [hasOpenProxyOnAllInterfacesSave](./has-open-proxy-on-all-interfaces-save.md)

# hasOpenProxyOnAllInterfacesSave

`fun hasOpenProxyOnAllInterfacesSave(boolean: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/ServiceTorSettings.kt#L739)

Saves the value for [hasOpenProxyOnAllInterfaces](has-open-proxy-on-all-interfaces.md) to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). If the value is the same
as what is declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) is queried to remove the
setting if it exists.

### Parameters

`boolean` - to enable/disable

**See Also**

[TorSettings.hasOpenProxyOnAllInterfaces](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/has-open-proxy-on-all-interfaces.md)

