[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [ServiceExecutionHooks](index.md) / [executeOnStartCommandBeforeStartTor](./execute-on-start-command-before-start-tor.md)

# executeOnStartCommandBeforeStartTor

`open suspend fun executeOnStartCommandBeforeStartTor(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/ServiceExecutionHooks.kt#L109)

Is executed from TorService.onStartCommand on Dispatchers.Default. This works in
conjunction with [executeBeforeStoppingService](execute-before-stopping-service.md) such that if
[executeBeforeStoppingService](execute-before-stopping-service.md) is active, [executeOnStartCommandBeforeStartTor](./execute-on-start-command-before-start-tor.md) will
suspend until that has been completed, then execute, then start Tor.

This is meant to provide quasi one-time synchronous code execution at first start of
TorService, prior to starting Tor.

Example usage:

If you want to query a DB for V3 Client Authorization Keys stored in
an encrypted fashion, write them to disk prior to first start of Tor, then in
[executeBeforeStoppingService](execute-before-stopping-service.md) delete the private keys.

If the user (or some other process) executes TorServiceController.startTor()
while [executeBeforeStoppingService](execute-before-stopping-service.md) is active, TorService.stopSelf() be prevented
from being called, execution of this method will suspend until
[executeBeforeStoppingService](execute-before-stopping-service.md) completes, then execute, then Tor will be started again.

**WARNING**: Indefinitely suspending the coroutine (ex. collecting
Flow or something) will also indefinitely suspend the ServiceActionProcessor
which will inhibit processing of any other commands; perform those tasks
from [executeOnCreateTorService](execute-on-create-tor-service.md).

**NOTE**: Exceptions thrown from your implementation will be broadcast via
the EventBroadcaster on Dispatchers.Main.

``` kotlin
// Executed on Dispatchers.Default, so querying shared prefs has to switch
// context to IO.
withContext(Dispatchers.Main) {
    TorServiceController.getServiceTorSettings()
}.let { settings ->
    withContext(Dispatchers.IO) {
        settings.hasDebugLogs
    }.let { debugLogs ->
        if (!debugLogs) {
            return
        }

        withContext(Dispatchers.Main) {
            TorServiceController.appEventBroadcaster?.broadcastDebug(
                "${BroadcastType.DEBUG}|" +
                        "${this@MyServiceExecutionHooks.javaClass.simpleName}|" +
                        "OnStartCommandBeforeStartTor execution hook started"
            )

            delay(1_000L)

            TorServiceController.appEventBroadcaster?.broadcastDebug(
                "${BroadcastType.DEBUG}|" +
                        "${this@MyServiceExecutionHooks.javaClass.simpleName}|" +
                        "OnStartCommandBeforeStartTor execution hook completed"
            )
        }
    }
}
```

