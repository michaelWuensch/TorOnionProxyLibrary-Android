[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [ServiceTorSettings](index.md) / [socksPortSave](./socks-port-save.md)

# socksPortSave

`fun socksPortSave(socksPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/ServiceTorSettings.kt#L565)

Saves the value for [socksPort](socks-port-save.md#io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings$socksPortSave(kotlin.String)/socksPort) to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`socksPort` - A String value of 0, auto, or number between 1024 and 65535

### Exceptions

`IllegalArgumentException` - if the value is not 0, auto, or between 1024 and 65535

**See Also**

[checkPortSelection](#)

[TorSettings.socksPort](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/socks-port.md)

