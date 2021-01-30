[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [ServiceExecutionHooks](./index.md)

# ServiceExecutionHooks

`abstract class ServiceExecutionHooks` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/ServiceExecutionHooks.kt#L10)

Set Hooks to be executed from TorService.

``` kotlin
class MyServiceExecutionHooks: ServiceExecutionHooks() {

    override suspend fun executeOnCreateTorService(context: Context) {
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
    }

    override suspend fun executeOnStartCommandBeforeStartTor(context: Context) {
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
    }

    override suspend fun executeBeforeStartTor(context: Context) {
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
                            "BeforeStartTor execution hook started"
                )

                delay(500L)

                TorServiceController.appEventBroadcaster?.broadcastDebug(
                    "${BroadcastType.DEBUG}|" +
                            "${this@MyServiceExecutionHooks.javaClass.simpleName}|" +
                            "BeforeStartTor execution hook completed"
                )
            }
        }
    }

    override suspend fun executeAfterStopTor(context: Context) {
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
                            "AfterStopTor execution hook started"
                )

                delay(500L)

                TorServiceController.appEventBroadcaster?.broadcastDebug(
                    "${BroadcastType.DEBUG}|" +
                            "${this@MyServiceExecutionHooks.javaClass.simpleName}|" +
                            "AfterStopTor execution hook completed"
                )
            }
        }
    }

    override suspend fun executeBeforeStoppingService(context: Context) {
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
    }
}
```

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Set Hooks to be executed from TorService.`ServiceExecutionHooks()` |

### Functions

| Name | Summary |
|---|---|
| [executeAfterStopTor](execute-after-stop-tor.md) | Is executed from TorService.stopTor on Dispatchers.IO`open suspend fun executeAfterStopTor(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [executeBeforeStartTor](execute-before-start-tor.md) | Is executed from TorService.startTor on Dispatchers.IO`open suspend fun executeBeforeStartTor(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [executeBeforeStoppingService](execute-before-stopping-service.md) | Is executed from TorService.stopService on Dispatchers.IO`open suspend fun executeBeforeStoppingService(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [executeOnCreateTorService](execute-on-create-tor-service.md) | Is executed from TorService.onCreate on Dispatchers.Default launched from it's own coroutine.`open suspend fun executeOnCreateTorService(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [executeOnStartCommandBeforeStartTor](execute-on-start-command-before-start-tor.md) | Is executed from TorService.onStartCommand on Dispatchers.Default. This works in conjunction with [executeBeforeStoppingService](execute-before-stopping-service.md) such that if [executeBeforeStoppingService](execute-before-stopping-service.md) is active, [executeOnStartCommandBeforeStartTor](execute-on-start-command-before-start-tor.md) will suspend until that has been completed, then execute, then start Tor.`open suspend fun executeOnStartCommandBeforeStartTor(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
