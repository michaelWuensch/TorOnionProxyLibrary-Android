[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [ServiceTorSettings](index.md) / [excludeNodesSave](./exclude-nodes-save.md)

# excludeNodesSave

`fun excludeNodesSave(excludeNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/ServiceTorSettings.kt#L231)

Saves the value for [excludeNodes](exclude-nodes-save.md#io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings$excludeNodesSave(kotlin.String)/excludeNodes) to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`excludeNodes` - A comma separated list of nodes

**See Also**

[TorSettings.excludeNodes](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/exclude-nodes.md)

