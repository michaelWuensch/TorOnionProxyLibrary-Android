[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [ServiceTorSettings](index.md) / [proxyTypeSave](./proxy-type-save.md)

# proxyTypeSave

`fun proxyTypeSave(proxyType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/ServiceTorSettings.kt#L446)

Saves the value for [proxyType](proxy-type-save.md#io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings$proxyTypeSave(kotlin.String)/proxyType) to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). If the value is the same
as what is declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) is queried to remove the
setting if it exists.

### Parameters

`proxyType` - A [io.matthewnelson.topl_core_base.BaseConsts.ProxyType](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-proxy-type/index.md)

### Exceptions

`IllegalArgumentException` - if the value is not empty (disabled), HTTPS, or Socks5

**See Also**

[io.matthewnelson.topl_core_base.BaseConsts.ProxyType](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-proxy-type/index.md)

[TorSettings.proxyType](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/proxy-type.md)

