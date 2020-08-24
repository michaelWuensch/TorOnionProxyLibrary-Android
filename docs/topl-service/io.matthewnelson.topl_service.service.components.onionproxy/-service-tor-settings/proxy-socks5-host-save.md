[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [ServiceTorSettings](index.md) / [proxySocks5HostSave](./proxy-socks5-host-save.md)

# proxySocks5HostSave

`fun proxySocks5HostSave(proxySocks5Host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/ServiceTorSettings.kt#L394)

Saves the value for [proxySocks5Host](proxy-socks5-host-save.md#io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings$proxySocks5HostSave(kotlin.String)/proxySocks5Host) to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`proxySocks5Host` -

**See Also**

[TorSettings.proxySocks5Host](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/proxy-socks5-host.md)

