[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [proxySocks5HostSave](./proxy-socks5-host-save.md)

# proxySocks5HostSave

`@WorkerThread abstract fun proxySocks5HostSave(proxySocks5Host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L345)

Saves the value for [proxySocks5Host](proxy-socks5-host-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$proxySocks5HostSave(kotlin.String)/proxySocks5Host) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`proxySocks5Host` -

**See Also**

[TorSettings.proxySocks5Host](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/proxy-socks5-host.md)

