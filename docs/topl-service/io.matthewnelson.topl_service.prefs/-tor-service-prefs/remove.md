[topl-service](../../index.md) / [io.matthewnelson.topl_service.prefs](../index.md) / [TorServicePrefs](index.md) / [remove](./remove.md)

# remove

`fun remove(prefsKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/prefs/TorServicePrefs.kt#L210)

Removes from the SharedPreference the value associated with [prefsKey](remove.md#io.matthewnelson.topl_service.prefs.TorServicePrefs$remove(kotlin.String)/prefsKey) if there is one.
Accepts the following annotation type String values:

* [ServiceConsts.PrefKeyBoolean](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-boolean/index.md)
* [ServiceConsts.PrefKeyInt](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-int/index.md)
* [ServiceConsts.PrefKeyList](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-list/index.md)
* [ServiceConsts.PrefKeyString](../../io.matthewnelson.topl_service.util/-service-consts/-pref-key-string/index.md)

### Parameters

`prefsKey` - String of type ServiceConsts.PrefKey*