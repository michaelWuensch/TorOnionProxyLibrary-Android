[topl-service](../../index.md) / [io.matthewnelson.topl_service.prefs](../index.md) / [TorServicePrefs](index.md) / [getString](./get-string.md)

# getString

`fun getString(stringKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/prefs/TorServicePrefs.kt#L193)

Returns a String value for the provided [ServiceConsts.PrefKeyString](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-string/index.md). If no
value is stored in the SharedPreference, [defValue](get-string.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getString(kotlin.String, kotlin.String)/defValue) will be returned.

### Parameters

`stringKey` - String of type [ServiceConsts.PrefKeyString](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-string/index.md)

`defValue` - Use the [io.matthewnelson.topl_core_base.TorSettings](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) value
associated with the [stringKey](get-string.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getString(kotlin.String, kotlin.String)/stringKey).

**Return**
The String value associated with [stringKey](get-string.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getString(kotlin.String, kotlin.String)/stringKey), otherwise [defValue](get-string.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getString(kotlin.String, kotlin.String)/defValue)

