[topl-service](../../index.md) / [io.matthewnelson.topl_service.prefs](../index.md) / [TorServicePrefs](index.md) / [contains](./contains.md)

# contains

`fun contains(prefsKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/prefs/TorServicePrefs.kt#L129)

Checks if the SharedPreference contains a value for the supplied [prefsKey](contains.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$contains(kotlin.String)/prefsKey).
Accepts the following annotation type String values:

* [ServiceConsts.PrefKeyBoolean](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-boolean/index.md)
* [ServiceConsts.PrefKeyInt](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-int/index.md)
* [ServiceConsts.PrefKeyList](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-list/index.md)
* [ServiceConsts.PrefKeyString](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-string/index.md)

### Parameters

`prefsKey` - String of type ServiceConsts.PrefKey*

**Return**
True if the SharedPreference contains a value for the associated
[prefsKey](contains.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$contains(kotlin.String)/prefsKey), false if not

