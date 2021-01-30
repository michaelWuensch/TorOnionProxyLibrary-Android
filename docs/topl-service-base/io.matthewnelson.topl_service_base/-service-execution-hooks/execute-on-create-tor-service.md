[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [ServiceExecutionHooks](index.md) / [executeOnCreateTorService](./execute-on-create-tor-service.md)

# executeOnCreateTorService

`open suspend fun executeOnCreateTorService(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/ServiceExecutionHooks.kt#L21)

Is executed from TorService.onCreate on Dispatchers.Default launched from
it's own coroutine.

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
                        "onCreateTorService execution hook started"
            )

            delay(20_000L)

            TorServiceController.appEventBroadcaster?.broadcastDebug(
                "${BroadcastType.DEBUG}|" +
                        "${this@MyServiceExecutionHooks.javaClass.simpleName}|" +
                        "onCreateTorService execution hook completed"
            )
        }
    }
}
```

