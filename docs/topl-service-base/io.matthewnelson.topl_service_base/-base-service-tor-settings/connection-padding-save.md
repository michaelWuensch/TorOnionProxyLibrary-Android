[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [connectionPaddingSave](./connection-padding-save.md)

# connectionPaddingSave

`@WorkerThread abstract fun connectionPaddingSave(connectionPadding: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L547)

Saves the value for [connectionPadding](connection-padding-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$connectionPaddingSave(kotlin.String)/connectionPadding) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same
as what is declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the
setting if it exists.

### Parameters

`connectionPadding` - A [io.matthewnelson.topl_core_base.BaseConsts.ConnectionPadding](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-connection-padding/index.md)

### Exceptions

`IllegalArgumentException` - if the value is not 0 (Off), 1 (On), or auto

**See Also**

[io.matthewnelson.topl_core_base.BaseConsts.ConnectionPadding](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-connection-padding/index.md)

[TorSettings.connectionPadding](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/connection-padding.md)

