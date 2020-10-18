[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [excludeNodesSave](./exclude-nodes-save.md)

# excludeNodesSave

`@WorkerThread abstract fun excludeNodesSave(excludeNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L214)

Saves the value for [excludeNodes](exclude-nodes-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$excludeNodesSave(kotlin.String)/excludeNodes) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`excludeNodes` - A comma separated list of nodes

**See Also**

[TorSettings.excludeNodes](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/exclude-nodes.md)

