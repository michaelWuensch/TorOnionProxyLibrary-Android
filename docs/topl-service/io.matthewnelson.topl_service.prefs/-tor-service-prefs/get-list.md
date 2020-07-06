[topl-service](../../index.md) / [io.matthewnelson.topl_service.prefs](../index.md) / [TorServicePrefs](index.md) / [getList](./get-list.md)

# getList

`fun getList(listKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/prefs/TorServicePrefs.kt#L119)

Returns a List of Strings for the provided [ServiceConsts.PrefKeyList](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-list/index.md). If no
value is stored in the SharedPreference, [defValue](get-list.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getList(kotlin.String, kotlin.collections.List((kotlin.String)))/defValue) will be returned.

### Parameters

`listKey` - String of type [ServiceConsts.PrefKeyList](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-list/index.md)

`defValue` - Use the [io.matthewnelson.topl_core_base.TorSettings](file:/home/matthew/AndroidStudioProjects/personal_projects/TorOnionProxyLibrary-Android/docs/topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) value
associated with the [listKey](get-list.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getList(kotlin.String, kotlin.collections.List((kotlin.String)))/listKey).

**Return**
The List of Strings associated with the [listKey](get-list.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getList(kotlin.String, kotlin.collections.List((kotlin.String)))/listKey), otherwise [defValue](get-list.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$getList(kotlin.String, kotlin.collections.List((kotlin.String)))/defValue)

