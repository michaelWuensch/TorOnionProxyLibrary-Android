[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorServicePrefs](./index.md)

# TorServicePrefs

`class TorServicePrefs : `[`BaseServiceConsts`](../-base-service-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/TorServicePrefs.kt#L89)

This class provides a standardized way for library users to change settings used
by the `topl-service` module such that the values expressed as default
[io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings](../-application-default-tor-settings/index.md) when initializing things
can be modified by the implementing application.

The values saved to [TorServicePrefs](./index.md) are always preferred over the defaults declared
when initializing the `topl-service` module.

Restarting Tor is currently required for the new settings to take effect.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | This class provides a standardized way for library users to change settings used by the `topl-service` module such that the values expressed as default [io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings](../-application-default-tor-settings/index.md) when initializing things can be modified by the implementing application.`TorServicePrefs(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | `fun clear(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [contains](contains.md) | Checks if the SharedPreference contains a value for the supplied [prefsKey](contains.md#io.matthewnelson.topl_service_base.TorServicePrefs$contains(kotlin.String)/prefsKey). Accepts the following annotation type String values:`fun contains(prefsKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getAll](get-all.md) | `fun getAll(): `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, *>` |
| [getBoolean](get-boolean.md) | Returns a Boolean value for the provided [BaseServiceConsts.PrefKeyBoolean](../-base-service-consts/-pref-key-boolean/index.md). If no value is stored in the SharedPreference, [defValue](get-boolean.md#io.matthewnelson.topl_service_base.TorServicePrefs$getBoolean(kotlin.String, kotlin.Boolean)/defValue) will be returned.`fun getBoolean(booleanKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getInt](get-int.md) | Returns an Int value for the provided [BaseServiceConsts.PrefKeyInt](../-base-service-consts/-pref-key-int/index.md). If no value is stored in the SharedPreference, [defValue](get-int.md#io.matthewnelson.topl_service_base.TorServicePrefs$getInt(kotlin.String, kotlin.Int)/defValue) will be returned.`fun getInt(intKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [getList](get-list.md) | Returns a List of Strings for the provided [BaseServiceConsts.PrefKeyList](../-base-service-consts/-pref-key-list/index.md). If no value is stored in the SharedPreference, [defValue](get-list.md#io.matthewnelson.topl_service_base.TorServicePrefs$getList(kotlin.String, kotlin.collections.List((kotlin.String)))/defValue) will be returned.`fun getList(listKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getString](get-string.md) | Returns a String value for the provided [BaseServiceConsts.PrefKeyString](../-base-service-consts/-pref-key-string/index.md). If no value is stored in the SharedPreference, [defValue](get-string.md#io.matthewnelson.topl_service_base.TorServicePrefs$getString(kotlin.String, kotlin.String)/defValue) will be returned.`fun getString(stringKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, defValue: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [putBoolean](put-boolean.md) | Inserts a Boolean value into the SharedPreference for the supplied [booleanKey](put-boolean.md#io.matthewnelson.topl_service_base.TorServicePrefs$putBoolean(kotlin.String, kotlin.Boolean)/booleanKey).`fun putBoolean(booleanKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [putInt](put-int.md) | Inserts an Int value into the SharedPreference for the supplied [intKey](put-int.md#io.matthewnelson.topl_service_base.TorServicePrefs$putInt(kotlin.String, kotlin.Int)/intKey).`fun putInt(intKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [putList](put-list.md) | Inserts a List of Strings as a comma separated String into the SharedPreference for the supplied [listKey](put-list.md#io.matthewnelson.topl_service_base.TorServicePrefs$putList(kotlin.String, kotlin.collections.List((kotlin.String)))/listKey).`fun putList(listKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [putString](put-string.md) | Inserts a String value into the SharedPreference for the supplied [stringKey](put-string.md#io.matthewnelson.topl_service_base.TorServicePrefs$putString(kotlin.String, kotlin.String)/stringKey).`fun putString(stringKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [registerListener](register-listener.md) | Registers a [SharedPreferences.OnSharedPreferenceChangeListener](https://developer.android.com/reference/android/content/SharedPreferences/OnSharedPreferenceChangeListener.html) for the associated SharedPreference`fun registerListener(listener: `[`OnSharedPreferenceChangeListener`](https://developer.android.com/reference/android/content/SharedPreferences/OnSharedPreferenceChangeListener.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [remove](remove.md) | Removes from the SharedPreference the value associated with [prefsKey](remove.md#io.matthewnelson.topl_service_base.TorServicePrefs$remove(kotlin.String)/prefsKey) if there is one. Accepts the following annotation type String values:`fun remove(prefsKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [unregisterListener](unregister-listener.md) | Unregisters a [SharedPreferences.OnSharedPreferenceChangeListener](https://developer.android.com/reference/android/content/SharedPreferences/OnSharedPreferenceChangeListener.html) for the associated SharedPreference`fun unregisterListener(listener: `[`OnSharedPreferenceChangeListener`](https://developer.android.com/reference/android/content/SharedPreferences/OnSharedPreferenceChangeListener.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [NULL_INT_VALUE](-n-u-l-l_-i-n-t_-v-a-l-u-e.md) | `const val NULL_INT_VALUE: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [NULL_STRING_VALUE](-n-u-l-l_-s-t-r-i-n-g_-v-a-l-u-e.md) | `const val NULL_STRING_VALUE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [TOR_SERVICE_PREFS_NAME](-t-o-r_-s-e-r-v-i-c-e_-p-r-e-f-s_-n-a-m-e.md) | `const val TOR_SERVICE_PREFS_NAME: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
