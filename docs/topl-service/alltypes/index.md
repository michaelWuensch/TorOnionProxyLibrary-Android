

### All Types

| Name | Summary |
|---|---|
|

##### [io.matthewnelson.topl_service.lifecycle.BackgroundManager](../io.matthewnelson.topl_service.lifecycle/-background-manager/index.md)

When your application is sent to the background (the Recent App's tray or lock screen), the
chosen [BackgroundManager.Builder.Policy](../io.matthewnelson.topl_service.lifecycle/-background-manager/-builder/-policy.md) will be triggered.


|

##### [io.matthewnelson.topl_service.util.ServiceConsts](../io.matthewnelson.topl_service.util/-service-consts/index.md)


|

##### [io.matthewnelson.topl_service.notification.ServiceNotification](../io.matthewnelson.topl_service.notification/-service-notification/index.md)

Everything to do with [TorService](#)'s notification.


|

##### [io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings](../io.matthewnelson.topl_service.service.components.onionproxy/-service-tor-settings/index.md)

This class enables the querying of [TorServicePrefs](../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) to obtain values potentially set by
the User such that they are *preferred* over static/default values you may have set in the
[io.matthewnelson.topl_service.TorServiceController.Builder](../io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md)'s constructor argument for
[TorSettings](../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md).


|

##### [io.matthewnelson.topl_service.util.ServiceUtilities](../io.matthewnelson.topl_service.util/-service-utilities/index.md)


|

##### [io.matthewnelson.topl_service.service.components.onionproxy.model.TorPortInfo](../io.matthewnelson.topl_service.service.components.onionproxy.model/-tor-port-info/index.md)

Contains information regarding what ports Tor is operating on.


|

##### [io.matthewnelson.topl_service.TorServiceController](../io.matthewnelson.topl_service/-tor-service-controller/index.md)


|

##### [io.matthewnelson.topl_service.service.components.onionproxy.model.TorServiceEventBroadcaster](../io.matthewnelson.topl_service.service.components.onionproxy.model/-tor-service-event-broadcaster/index.md)

Adds broadcasting methods to the [EventBroadcaster](../..//topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) to update you with information about
what addresses Tor is operating on. Very helpful when choosing "auto" in your
[io.matthewnelson.topl_core_base.TorSettings](../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) to easily identify what addresses to
use for making network calls, as well as being notified when Tor is ready to be used.


|

##### [io.matthewnelson.topl_service.prefs.TorServicePrefs](../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md)

This class provides a standardized way for library users to change settings used
by [io.matthewnelson.topl_service.service.TorService](#) such that the values expressed
as default [io.matthewnelson.topl_core_base.TorSettings](../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) when initializing things via
the [io.matthewnelson.topl_service.TorServiceController.Builder](../io.matthewnelson.topl_service/-tor-service-controller/-builder/index.md) can be updated. The
values saved to [TorServicePrefs](../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) are always preferred over the defaults declared.


