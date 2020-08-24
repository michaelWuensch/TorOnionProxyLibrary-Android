[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [ServiceTorSettings](index.md) / [customTorrcSave](./custom-torrc-save.md)

# customTorrcSave

`fun customTorrcSave(customTorrc: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/ServiceTorSettings.kt#L195)

Saves the value for [customTorrc](custom-torrc-save.md#io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings$customTorrcSave(kotlin.String)/customTorrc) to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`customTorrc` - A String of values to be added to the torrc file

**See Also**

[TorSettings.customTorrc](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/custom-torrc.md)

