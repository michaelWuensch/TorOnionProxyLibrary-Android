[topl-service](../../index.md) / [io.matthewnelson.topl_service.prefs](../index.md) / [TorServicePrefs](index.md) / [getBoolean](./get-boolean.md)

# getBoolean

`fun getBoolean(booleanKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/prefs/TorServicePrefs.kt#L145)

Returns a Boolean value for the provided [ServiceConsts.PrefKeyBoolean](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-boolean/index.md). If no
value is stored in the SharedPreference, [defValue](get-boolean.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getBoolean(kotlin.String, kotlin.Boolean)/defValue) will be returned.

### Parameters

`booleanKey` - String of type [ServiceConsts.PrefKeyBoolean](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-boolean/index.md)

`defValue` - Use the [io.matthewnelson.topl_core_base.TorSettings](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) value
associated with the [booleanKey](get-boolean.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getBoolean(kotlin.String, kotlin.Boolean)/booleanKey).

**Return**
The Boolean value associated with the [booleanKey](get-boolean.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getBoolean(kotlin.String, kotlin.Boolean)/booleanKey), otherwise [defValue](get-boolean.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getBoolean(kotlin.String, kotlin.Boolean)/defValue)

