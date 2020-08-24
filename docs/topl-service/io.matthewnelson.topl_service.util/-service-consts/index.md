[topl-service](../../index.md) / [io.matthewnelson.topl_service.util](../index.md) / [ServiceConsts](./index.md)

# ServiceConsts

`abstract class ServiceConsts : `[`BaseConsts`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/util/ServiceConsts.kt#L74)

### Annotations

| Name | Summary |
|---|---|
| [BackgroundPolicy](-background-policy/index.md) | `annotation class BackgroundPolicy` |
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
| [BackgroundManager](../../io.matthewnelson.topl_service.lifecycle/-background-manager/index.md) | When your application is sent to the background (the Recent App's tray or lock screen), the chosen [BackgroundManager.Builder.Policy](../../io.matthewnelson.topl_service.lifecycle/-background-manager/-builder/-policy.md) will be triggered.`class BackgroundManager : `[`ServiceConsts`](./index.md)`, LifecycleObserver` |
| [ServiceNotification](../../io.matthewnelson.topl_service.notification/-service-notification/index.md) | Everything to do with [TorService](#)'s notification.`class ServiceNotification : `[`ServiceConsts`](./index.md) |
| [TorServiceController](../../io.matthewnelson.topl_service/-tor-service-controller/index.md) | `class TorServiceController : `[`ServiceConsts`](./index.md) |
| [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) | This class provides a standardized way for library users to change settings used by [io.matthewnelson.topl_service.service.TorService](#) such that the values expressed as default [io.matthewnelson.topl_core_base.TorSettings](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) when initializing things via the [io.matthewnelson.topl_service.TorServiceController.Builder](../../io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md) can be updated. The values saved to [TorServicePrefs](../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) are always preferred over the defaults declared.`class TorServicePrefs : `[`ServiceConsts`](./index.md) |
