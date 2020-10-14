[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorServicePrefs](index.md) / [getString](./get-string.md)

# getString

`@WorkerThread fun getString(stringKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/TorServicePrefs.kt#L207)

Returns a String value for the provided [BaseServiceConsts.PrefKeyString](../-base-service-consts/-pref-key-string/index.md). If no
value is stored in the SharedPreference, [defValue](get-string.md#io.matthewnelson.topl_service_base.TorServicePrefs$getString(kotlin.String, kotlin.String)/defValue) will be returned.

### Parameters

`stringKey` - String of type [BaseServiceConsts.PrefKeyString](../-base-service-consts/-pref-key-string/index.md)

`defValue` - Use the [io.matthewnelson.topl_core_base.TorSettings](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) value
associated with the [stringKey](get-string.md#io.matthewnelson.topl_service_base.TorServicePrefs$getString(kotlin.String, kotlin.String)/stringKey).

**Return**
The String value associated with [stringKey](get-string.md#io.matthewnelson.topl_service_base.TorServicePrefs$getString(kotlin.String, kotlin.String)/stringKey), otherwise [defValue](get-string.md#io.matthewnelson.topl_service_base.TorServicePrefs$getString(kotlin.String, kotlin.String)/defValue)

