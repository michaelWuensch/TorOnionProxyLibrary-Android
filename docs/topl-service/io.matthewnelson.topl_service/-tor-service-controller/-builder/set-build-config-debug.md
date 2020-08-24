[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [setBuildConfigDebug](./set-build-config-debug.md)

# setBuildConfigDebug

`fun setBuildConfigDebug(buildConfigDebug: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L271)

This makes it such that on your Application's **Debug** builds, the `topl-core` and
`topl-service` modules will provide you with Logcat messages (when
[TorSettings.hasDebugLogs](../../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/has-debug-logs.md) is enabled).

For your **Release** builds no Logcat messaging will be provided, but you
will still get the same messages sent to your [EventBroadcaster](../../../..//topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) if you set it
via [Builder.setEventBroadcaster](set-event-broadcaster.md).

### Parameters

`buildConfigDebug` - Send [BuildConfig.DEBUG](#)

**See Also**

[io.matthewnelson.topl_core.broadcaster.BroadcastLogger](../../../..//topl-core/io.matthewnelson.topl_core.broadcaster/-broadcast-logger/index.md)

