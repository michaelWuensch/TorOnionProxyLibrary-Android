[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorServicePrefs](index.md) / [getBoolean](./get-boolean.md)

# getBoolean

`@WorkerThread fun getBoolean(booleanKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/TorServicePrefs.kt#L156)

Returns a Boolean value for the provided [BaseServiceConsts.PrefKeyBoolean](../-base-service-consts/-pref-key-boolean/index.md). If no
value is stored in the SharedPreference, [defValue](get-boolean.md#io.matthewnelson.topl_service_base.TorServicePrefs$getBoolean(kotlin.String, kotlin.Boolean)/defValue) will be returned.

### Parameters

`booleanKey` - String of type [BaseServiceConsts.PrefKeyBoolean](../-base-service-consts/-pref-key-boolean/index.md)

`defValue` - Use the [io.matthewnelson.topl_core_base.TorSettings](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) value
associated with the [booleanKey](get-boolean.md#io.matthewnelson.topl_service_base.TorServicePrefs$getBoolean(kotlin.String, kotlin.Boolean)/booleanKey).

**Return**
The Boolean value associated with the [booleanKey](get-boolean.md#io.matthewnelson.topl_service_base.TorServicePrefs$getBoolean(kotlin.String, kotlin.Boolean)/booleanKey), otherwise [defValue](get-boolean.md#io.matthewnelson.topl_service_base.TorServicePrefs$getBoolean(kotlin.String, kotlin.Boolean)/defValue)

