[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [customTorrcSave](./custom-torrc-save.md)

# customTorrcSave

`@WorkerThread abstract fun customTorrcSave(customTorrc: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L180)

Saves the value for [customTorrc](custom-torrc-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$customTorrcSave(kotlin.String)/customTorrc) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`customTorrc` - A String of values to be added to the torrc file

**See Also**

[TorSettings.customTorrc](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/custom-torrc.md)

