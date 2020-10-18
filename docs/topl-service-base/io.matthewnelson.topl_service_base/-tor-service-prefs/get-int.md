[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorServicePrefs](index.md) / [getInt](./get-int.md)

# getInt

`@WorkerThread fun getInt(intKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/TorServicePrefs.kt#L169)

Returns an Int value for the provided [BaseServiceConsts.PrefKeyInt](../-base-service-consts/-pref-key-int/index.md). If no
value is stored in the SharedPreference, [defValue](get-int.md#io.matthewnelson.topl_service_base.TorServicePrefs$getInt(kotlin.String, kotlin.Int)/defValue) will be returned.

### Parameters

`intKey` - String of type [BaseServiceConsts.PrefKeyInt](../-base-service-consts/-pref-key-int/index.md)

`defValue` - Use the [io.matthewnelson.topl_core_base.TorSettings](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) value
associated with the [intKey](get-int.md#io.matthewnelson.topl_service_base.TorServicePrefs$getInt(kotlin.String, kotlin.Int)/intKey).

**Return**
The Int value associated with [intKey](get-int.md#io.matthewnelson.topl_service_base.TorServicePrefs$getInt(kotlin.String, kotlin.Int)/intKey), otherwise [defValue](get-int.md#io.matthewnelson.topl_service_base.TorServicePrefs$getInt(kotlin.String, kotlin.Int)/defValue)

