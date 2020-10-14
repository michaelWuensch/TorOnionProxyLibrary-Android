[topl-service-base](../index.md) / [io.matthewnelson.topl_service_base](./index.md)

## Package io.matthewnelson.topl_service_base

### Types

| Name | Summary |
|---|---|
| [ApplicationDefaultTorSettings](-application-default-tor-settings/index.md) | Simply extends [TorSettings](../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) so that the `topl-service` module cannot be initialized with [io.matthewnelson.topl_service_base.BaseServiceTorSettings](-base-service-tor-settings/index.md).`abstract class ApplicationDefaultTorSettings : `[`TorSettings`](../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) |
| [BaseServiceConsts](-base-service-consts/index.md) | `abstract class BaseServiceConsts : `[`BaseConsts`](../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/index.md) |
| [BaseServiceTorSettings](-base-service-tor-settings/index.md) | This class enables the querying of [TorServicePrefs](-tor-service-prefs/index.md) to obtain values potentially set by the User such that they are *preferred* over static/default values you may have set in your [ApplicationDefaultTorSettings](-application-default-tor-settings/index.md).`abstract class BaseServiceTorSettings : `[`TorSettings`](../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) |
| [BaseV3ClientAuthManager](-base-v3-client-auth-manager/index.md) | `abstract class BaseV3ClientAuthManager` |
| [ServiceUtilities](-service-utilities/index.md) | `object ServiceUtilities` |
| [TorPortInfo](-tor-port-info/index.md) | Holder for information regarding what ports Tor is operating on that is broadcast to the implementing application via [io.matthewnelson.topl_service_base.TorServiceEventBroadcaster](-tor-service-event-broadcaster/index.md)`class TorPortInfo` |
| [TorServiceEventBroadcaster](-tor-service-event-broadcaster/index.md) | Adds broadcasting methods to the [EventBroadcaster](../..//topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) for updating you with information about what addresses Tor is operating on. Very helpful when choosing "auto" in your [io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings](-application-default-tor-settings/index.md) to easily identify what addresses to use for making network calls, as well as being notified when Tor is ready to be used.`abstract class TorServiceEventBroadcaster : `[`EventBroadcaster`](../..//topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) |
| [TorServicePrefs](-tor-service-prefs/index.md) | This class provides a standardized way for library users to change settings used by the `topl-service` module such that the values expressed as default [io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings](-application-default-tor-settings/index.md) when initializing things can be modified by the implementing application.`class TorServicePrefs : `[`BaseServiceConsts`](-base-service-consts/index.md) |
| [V3ClientAuthContent](-v3-client-auth-content/index.md) | Holder for v3 client authentication data used by [io.matthewnelson.topl_service_base.BaseV3ClientAuthManager](-base-v3-client-auth-manager/index.md)`class V3ClientAuthContent` |
