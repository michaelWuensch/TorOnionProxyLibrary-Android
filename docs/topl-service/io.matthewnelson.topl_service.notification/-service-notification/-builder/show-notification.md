[topl-service](../../../index.md) / [io.matthewnelson.topl_service.notification](../../index.md) / [ServiceNotification](../index.md) / [Builder](index.md) / [showNotification](./show-notification.md)

# showNotification

`@JvmOverloads fun showNotification(show: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/notification/ServiceNotification.kt#L331)

Shown by Default.

Setting it to false will only show a notification when the end user removes your
Application from the Recent App's tray. In that event, [TorService.onTaskRemoved](#)
moves the Service to the Foreground in order to properly shutdown Tor w/o the OS
killing it beforehand.

See [Builder](index.md) for code samples.

### Parameters

`show` - Boolean, automatically set to false but provides cleaner option for
implementor to query SharedPreferences for user's settings (if desired)

**Return**
[Builder](index.md) To continue customizing

