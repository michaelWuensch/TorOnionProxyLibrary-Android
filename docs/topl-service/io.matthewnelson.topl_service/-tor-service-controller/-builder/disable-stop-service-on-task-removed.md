[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [disableStopServiceOnTaskRemoved](./disable-stop-service-on-task-removed.md)

# disableStopServiceOnTaskRemoved

`@JvmOverloads fun disableStopServiceOnTaskRemoved(disable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L229)

When your task is removed from the Recent App's tray, [TorService.onTaskRemoved](#) is
triggered. Default behaviour is to stop Tor, and then [TorService](#). Electing this
option will inhibit the default behaviour from being carried out.

