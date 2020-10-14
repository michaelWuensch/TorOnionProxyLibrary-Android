[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorServicePrefs](index.md) / [putList](./put-list.md)

# putList

`@WorkerThread fun putList(listKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/TorServicePrefs.kt#L279)

Inserts a List of Strings as a comma separated String into the SharedPreference
for the supplied [listKey](put-list.md#io.matthewnelson.topl_service_base.TorServicePrefs$putList(kotlin.String, kotlin.collections.List((kotlin.String)))/listKey).

### Parameters

`listKey` - String of type [BaseServiceConsts.PrefKeyList](../-base-service-consts/-pref-key-list/index.md)

`value` - Your List value