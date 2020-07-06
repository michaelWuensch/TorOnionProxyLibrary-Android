[topl-service](../../../../index.md) / [io.matthewnelson.topl_service](../../../index.md) / [TorServiceController](../../index.md) / [Builder](../index.md) / [NotificationBuilder](index.md) / [enableTorRestartButton](./enable-tor-restart-button.md)

# enableTorRestartButton

`fun enableTorRestartButton(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): NotificationBuilder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L330)

Disabled by Default

Enable on the notification the ability to *restart* Tor.

See [Builder](../index.md) for code samples.

### Parameters

`enable` - Boolean, automatically set to true but provides cleaner option
for implementor to query SharedPreferences for user's settings (if desired).

**Return**
[NotificationBuilder](index.md)

