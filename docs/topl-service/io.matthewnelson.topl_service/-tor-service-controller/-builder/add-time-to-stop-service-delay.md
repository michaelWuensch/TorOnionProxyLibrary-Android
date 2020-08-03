[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [addTimeToStopServiceDelay](./add-time-to-stop-service-delay.md)

# addTimeToStopServiceDelay

`fun addTimeToStopServiceDelay(milliseconds: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L187)

Default is set to 100ms (what this method adds time to).

A slight delay is required when starting and stopping Tor to allow the [Process](https://docs.oracle.com/javase/6/docs/api/java/lang/Process.html)
for which it is running in to settle. This method adds time to the cautionary
delay between execution of stopping Tor and stopping [TorService](#).

The call to [stopTor](../stop-tor.md) executes individual commands to:

* stop tor + delay (300ms)
* delay (100ms) &lt;---------------------- what this method will add to
* stop service

### Parameters

`milliseconds` - A value greater than 0

**See Also**

[io.matthewnelson.topl_service.service.components.actions.ServiceActions.Stop](#)

[io.matthewnelson.topl_service.service.components.actions.ServiceActionProcessor.processServiceAction](#)

