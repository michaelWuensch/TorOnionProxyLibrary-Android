[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [useSocks5Save](./use-socks5-save.md)

# useSocks5Save

`@WorkerThread abstract fun useSocks5Save(boolean: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L807)

Saves the value for [useSocks5](use-socks5.md) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same
as what is declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the
setting if it exists.

### Parameters

`boolean` - to enable/disable

**See Also**

[TorSettings.useSocks5](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/use-socks5.md)

