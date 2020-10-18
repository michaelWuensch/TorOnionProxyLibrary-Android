[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorServicePrefs](index.md) / [contains](./contains.md)

# contains

`@WorkerThread fun contains(prefsKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/TorServicePrefs.kt#L138)

Checks if the SharedPreference contains a value for the supplied [prefsKey](contains.md#io.matthewnelson.topl_service_base.TorServicePrefs$contains(kotlin.String)/prefsKey).
Accepts the following annotation type String values:

* [BaseServiceConsts.PrefKeyBoolean](../-base-service-consts/-pref-key-boolean/index.md)
* [BaseServiceConsts.PrefKeyInt](../-base-service-consts/-pref-key-int/index.md)
* [BaseServiceConsts.PrefKeyList](../-base-service-consts/-pref-key-list/index.md)
* [BaseServiceConsts.PrefKeyString](../-base-service-consts/-pref-key-string/index.md)

### Parameters

`prefsKey` - String of type ServiceConsts.PrefKey*

**Return**
True if the SharedPreference contains a value for the associated
[prefsKey](contains.md#io.matthewnelson.topl_service_base.TorServicePrefs$contains(kotlin.String)/prefsKey), false if not

