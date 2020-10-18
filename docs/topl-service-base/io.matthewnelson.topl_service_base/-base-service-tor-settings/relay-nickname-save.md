[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [relayNicknameSave](./relay-nickname-save.md)

# relayNicknameSave

`@WorkerThread abstract fun relayNicknameSave(relayNickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L435)

Saves the value for [relayNickname](relay-nickname-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$relayNicknameSave(kotlin.String)/relayNickname) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`relayNickname` -

**See Also**

[TorSettings.relayNickname](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/relay-nickname.md)

