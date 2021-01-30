

### All Types

| Name | Summary |
|---|---|
|

##### [io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings](../io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md)

Simply extends [TorSettings](../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) so that the `topl-service` module cannot be initialized
with [io.matthewnelson.topl_service_base.BaseServiceTorSettings](../io.matthewnelson.topl_service_base/-base-service-tor-settings/index.md).


|

##### [io.matthewnelson.topl_service_base.BaseServiceConsts](../io.matthewnelson.topl_service_base/-base-service-consts/index.md)


|

##### [io.matthewnelson.topl_service_base.BaseServiceTorSettings](../io.matthewnelson.topl_service_base/-base-service-tor-settings/index.md)

This class enables the querying of [TorServicePrefs](../io.matthewnelson.topl_service_base/-tor-service-prefs/index.md) to obtain values potentially set by
the User such that they are *preferred* over static/default values you may have set in
your [ApplicationDefaultTorSettings](../io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md).


|

##### [io.matthewnelson.topl_service_base.BaseV3ClientAuthManager](../io.matthewnelson.topl_service_base/-base-v3-client-auth-manager/index.md)


|

##### [io.matthewnelson.topl_service_base.ServiceExecutionHooks](../io.matthewnelson.topl_service_base/-service-execution-hooks/index.md)

Set Hooks to be executed from TorService.


|

##### [io.matthewnelson.topl_service_base.ServiceUtilities](../io.matthewnelson.topl_service_base/-service-utilities/index.md)


|

##### [io.matthewnelson.topl_service_base.TorPortInfo](../io.matthewnelson.topl_service_base/-tor-port-info/index.md)

Holder for information regarding what ports Tor is operating on that is broadcast
to the implementing application via [io.matthewnelson.topl_service_base.TorServiceEventBroadcaster](../io.matthewnelson.topl_service_base/-tor-service-event-broadcaster/index.md)


|

##### [io.matthewnelson.topl_service_base.TorServiceEventBroadcaster](../io.matthewnelson.topl_service_base/-tor-service-event-broadcaster/index.md)

Adds broadcasting methods to the [EventBroadcaster](../..//topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) for updating you with information about
what addresses Tor is operating on. Very helpful when choosing "auto" in your
[io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings](../io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md) to easily identify what
addresses to use for making network calls, as well as being notified when Tor is ready to be
used.


|

##### [io.matthewnelson.topl_service_base.TorServicePrefs](../io.matthewnelson.topl_service_base/-tor-service-prefs/index.md)

This class provides a standardized way for library users to change settings used
by the `topl-service` module such that the values expressed as default
[io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings](../io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md) when initializing things
can be modified by the implementing application.


|

##### [io.matthewnelson.topl_service_base.V3ClientAuthContent](../io.matthewnelson.topl_service_base/-v3-client-auth-content/index.md)

Holder for v3 client authentication data used by
[io.matthewnelson.topl_service_base.BaseV3ClientAuthManager](../io.matthewnelson.topl_service_base/-base-v3-client-auth-manager/index.md)


