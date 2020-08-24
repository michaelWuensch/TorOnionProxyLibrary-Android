[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [ServiceTorSettings](index.md) / [connectionPaddingSave](./connection-padding-save.md)

# connectionPaddingSave

`fun connectionPaddingSave(connectionPadding: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/ServiceTorSettings.kt#L656)

Saves the value for [connectionPadding](connection-padding-save.md#io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings$connectionPaddingSave(kotlin.String)/connectionPadding) to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). If the value is the same
as what is declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) is queried to remove the
setting if it exists.

### Parameters

`connectionPadding` - A [io.matthewnelson.topl_core_base.BaseConsts.ConnectionPadding](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-connection-padding/index.md)

### Exceptions

`IllegalArgumentException` - if the value is not 0 (Off), 1 (On), or auto

**See Also**

[io.matthewnelson.topl_core_base.BaseConsts.ConnectionPadding](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-connection-padding/index.md)

[TorSettings.connectionPadding](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/connection-padding.md)

