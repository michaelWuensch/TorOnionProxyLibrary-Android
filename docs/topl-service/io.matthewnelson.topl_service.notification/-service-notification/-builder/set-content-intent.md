[topl-service](../../../index.md) / [io.matthewnelson.topl_service.notification](../../index.md) / [ServiceNotification](../index.md) / [Builder](index.md) / [setContentIntent](./set-content-intent.md)

# setContentIntent

`fun setContentIntent(pendingIntent: `[`PendingIntent`](https://developer.android.com/reference/android/app/PendingIntent.html)`?): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/notification/ServiceNotification.kt#L225)

Allows for full control over the [PendingIntent](https://developer.android.com/reference/android/app/PendingIntent.html) used when the user taps the
[ServiceNotification](../index.md).

**NOTE**: use applicationContext when building your pending intent.

