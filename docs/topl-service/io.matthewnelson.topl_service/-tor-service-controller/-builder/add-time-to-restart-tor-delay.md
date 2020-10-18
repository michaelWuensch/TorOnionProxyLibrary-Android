[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [addTimeToRestartTorDelay](./add-time-to-restart-tor-delay.md)

# addTimeToRestartTorDelay

`fun addTimeToRestartTorDelay(milliseconds: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L181)

Default is set to 500ms, (what this method adds time to).

A slight delay is required when starting and stopping Tor to allow the [Process](https://docs.oracle.com/javase/6/docs/api/java/lang/Process.html)
for which it is running in to settle. This method adds time to the cautionary
delay between execution of stopTor and startTor, which are the individual calls
executed when using the [restartTor](../restart-tor.md) method.

The call to [restartTor](../restart-tor.md) executes individual commands to:

* stop tor + delay (300ms)
* delay (500ms) &lt;---------------------- what this method will add to
* start tor + delay (300ms)

### Parameters

`milliseconds` - A value greater than 0

**See Also**

[io.matthewnelson.topl_service.service.components.actions.ServiceAction.RestartTor](#)

[io.matthewnelson.topl_service.service.components.actions.ServiceActionProcessor.processServiceAction](#)

