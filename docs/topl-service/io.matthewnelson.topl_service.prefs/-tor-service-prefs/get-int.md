[topl-service](../../index.md) / [io.matthewnelson.topl_service.prefs](../index.md) / [TorServicePrefs](index.md) / [getInt](./get-int.md)

# getInt

`fun getInt(intKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/prefs/TorServicePrefs.kt#L101)

Returns an Int value for the provided [ServiceConsts.PrefKeyInt](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-int/index.md). If no
value is stored in the SharedPreference, [defValue](get-int.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getInt(kotlin.String, kotlin.Int)/defValue) will be returned.

### Parameters

`intKey` - String of type [ServiceConsts.PrefKeyInt](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-int/index.md)

`defValue` - Use the [io.matthewnelson.topl_core_base.TorSettings](http://FIX_DOKKA_LINKS/topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) value
associated with the [intKey](get-int.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getInt(kotlin.String, kotlin.Int)/intKey).

**Return**
The Int value associated with [intKey](get-int.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getInt(kotlin.String, kotlin.Int)/intKey), otherwise [defValue](get-int.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getInt(kotlin.String, kotlin.Int)/defValue)

