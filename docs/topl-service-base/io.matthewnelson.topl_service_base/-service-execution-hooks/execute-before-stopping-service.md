[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [ServiceExecutionHooks](index.md) / [executeBeforeStoppingService](./execute-before-stopping-service.md)

# executeBeforeStoppingService

`open suspend fun executeBeforeStoppingService(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/ServiceExecutionHooks.kt#L77)

Is executed from TorService.stopService on Dispatchers.IO

This is meant to provide synchronous code execution prior to calling
stopSelf() on the service. When this is called for execution,
[executeOnStartCommandBeforeStartTor](execute-on-start-command-before-start-tor.md), will be allowed to execute again
if TorServiceController.startTor() is called prior to this method's completion.

**WARNING**: Indefinitely suspending the coroutine (ex. collecting
Flow or something) will also indefinitely suspend the ServiceActionProcessor
which will inhibit processing of any other commands; perform those tasks
from [executeOnCreateTorService](execute-on-create-tor-service.md).

**NOTE**: Exceptions thrown from your implementation will be broadcast via
the EventBroadcaster on Dispatchers.Main.

``` kotlin
// Executed on Dispatchers.IO
withContext(Dispatchers.Main) {
    TorServiceController.getServiceTorSettings()
}.let { settings ->
    if (!settings.hasDebugLogs) {
        return
    }

    withContext(Dispatchers.Main) {
        TorServiceController.appEventBroadcaster?.broadcastDebug(
            "${BroadcastType.DEBUG}|" +
                    "${this@MyServiceExecutionHooks.javaClass.simpleName}|" +
                    "BeforeStoppingService execution hook started"
        )

        delay(2_000L)

        TorServiceController.appEventBroadcaster?.broadcastDebug(
            "${BroadcastType.DEBUG}|" +
                    "${this@MyServiceExecutionHooks.javaClass.simpleName}|" +
                    "BeforeStoppingService execution hook completed"
        )
    }
}
```

