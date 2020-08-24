[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [ServiceTorSettings](index.md) / [proxyPortSave](./proxy-port-save.md)

# proxyPortSave

`fun proxyPortSave(proxyPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/ServiceTorSettings.kt#L366)

Saves the value for [proxyPort](proxy-port-save.md#io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings$proxyPortSave(kotlin.Int)/proxyPort) to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`proxyPort` - An Int value between 1024 and 65535, or `null`

### Exceptions

`IllegalArgumentException` - if the value is not `null`, or between 1024 and 65535

**See Also**

[checkPortSelection](#)

[TorSettings.proxyPort](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/proxy-port.md)

