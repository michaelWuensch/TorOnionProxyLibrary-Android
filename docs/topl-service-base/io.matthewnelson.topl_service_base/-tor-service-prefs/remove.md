[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorServicePrefs](index.md) / [remove](./remove.md)

# remove

`@WorkerThread fun remove(prefsKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/TorServicePrefs.kt#L239)

Removes from the SharedPreference the value associated with [prefsKey](remove.md#io.matthewnelson.topl_service_base.TorServicePrefs$remove(kotlin.String)/prefsKey) if there is one.
Accepts the following annotation type String values:

* [BaseServiceConsts.PrefKeyBoolean](../-base-service-consts/-pref-key-boolean/index.md)
* [BaseServiceConsts.PrefKeyInt](../-base-service-consts/-pref-key-int/index.md)
* [BaseServiceConsts.PrefKeyList](../-base-service-consts/-pref-key-list/index.md)
* [BaseServiceConsts.PrefKeyString](../-base-service-consts/-pref-key-string/index.md)

### Parameters

`prefsKey` - String of type ServiceConsts.PrefKey*