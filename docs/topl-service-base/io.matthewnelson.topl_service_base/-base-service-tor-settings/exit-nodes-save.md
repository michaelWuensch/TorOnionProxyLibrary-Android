[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseServiceTorSettings](index.md) / [exitNodesSave](./exit-nodes-save.md)

# exitNodesSave

`@WorkerThread abstract fun exitNodesSave(exitNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseServiceTorSettings.kt#L231)

Saves the value for [exitNodes](exit-nodes-save.md#io.matthewnelson.topl_service_base.BaseServiceTorSettings$exitNodesSave(kotlin.String)/exitNodes) to [TorServicePrefs](../-tor-service-prefs/index.md). If the value is the same as what is
declared in [defaultTorSettings](default-tor-settings.md), [TorServicePrefs](../-tor-service-prefs/index.md) is queried to remove the setting if
it exists.

### Parameters

`exitNodes` - A comma separated list of nodes

**See Also**

[TorSettings.exitNodes](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/exit-nodes.md)

