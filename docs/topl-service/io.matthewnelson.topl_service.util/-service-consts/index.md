[topl-service](../../index.md) / [io.matthewnelson.topl_service.util](../index.md) / [ServiceConsts](./index.md)

# ServiceConsts

`abstract class ServiceConsts : `[`BaseConsts`](file:/home/matthew/AndroidStudioProjects/personal_projects/TorOnionProxyLibrary-Android/docs/topl-core-base/io.matthewnelson.topl_core_base/-base-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/util/ServiceConsts.kt#L23)

### Annotations

| Name | Summary |
|---|---|
| [PrefKeyBoolean](-pref-key-boolean/index.md) | `annotation class PrefKeyBoolean` |
| [PrefKeyInt](-pref-key-int/index.md) | `annotation class PrefKeyInt` |
| [PrefKeyList](-pref-key-list/index.md) | `annotation class PrefKeyList` |
| [PrefKeyString](-pref-key-string/index.md) | `annotation class PrefKeyString` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ServiceConsts()` |

### Inheritors

| Name | Summary |
|---|---|
| [TorServiceController](../../io.matthewnelson.topl_service/-tor-service-controller/index.md) | `class TorServiceController : `[`ServiceConsts`](./index.md) |
| [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) | This class provides a standardized way for library users to change settings used by [io.matthewnelson.topl_service.service.TorService](#) such that the values expressed as default [io.matthewnelson.topl_core_base.TorSettings](file:/home/matthew/AndroidStudioProjects/personal_projects/TorOnionProxyLibrary-Android/docs/topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) when initializing things via the [io.matthewnelson.topl_service.TorServiceController.Builder](../../io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md) can be updated. The values saved to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) are always preferred over the defaults declared.`class TorServicePrefs : `[`ServiceConsts`](./index.md) |
