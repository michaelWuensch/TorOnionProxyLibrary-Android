[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [addTimeToDisableNetworkDelay](./add-time-to-disable-network-delay.md)

# addTimeToDisableNetworkDelay

`fun addTimeToDisableNetworkDelay(milliseconds: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L156)

Default is set to 6_000ms, (what this method adds time to).

When network connectivity is lost a delay is had before setting Tor's config
"DisableNetwork" to true, in case connectivity is re-gained within the delay period.

Tor is not stopped on connectivity change, but the network setting will be disabled
to inhibit continual launching and building of circuits (which drains the battery).

This delay is particularly useful when setting ports to "auto" because disabling
Tor's network and then re-enabling it (when connectivity is regained) will set up
listeners on different ports.

The delay also helps in stabilizing longer running network calls amidst areas where
the User may have bad service. The calls will simply continue if connectivity is
regained within the delay period and ports will not be cycled (if using "auto").

### Parameters

`milliseconds` - A value greater than 0

**See Also**

[io.matthewnelson.topl_service.service.components.actions.ServiceAction.SetDisableNetwork](#)

[io.matthewnelson.topl_service.service.components.actions.ServiceActionProcessor.processServiceAction](#)

