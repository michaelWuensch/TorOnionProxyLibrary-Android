[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [ServiceExecutionHooks](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`ServiceExecutionHooks()`

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

