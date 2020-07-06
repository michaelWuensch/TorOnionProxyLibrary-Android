[topl-service](../../../../index.md) / [io.matthewnelson.topl_service](../../../index.md) / [TorServiceController](../../index.md) / [Builder](../index.md) / [NotificationBuilder](index.md) / [setActivityToBeOpenedOnTap](./set-activity-to-be-opened-on-tap.md)

# setActivityToBeOpenedOnTap

`fun setActivityToBeOpenedOnTap(clazz: `[`Class`](https://docs.oracle.com/javase/6/docs/api/java/lang/Class.html)`<*>, intentExtrasKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, intentExtras: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, intentRequestCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): NotificationBuilder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L208)

For when your user taps the TorService notification.

See [Builder](../index.md) for code samples.

### Parameters

`clazz` - The Activity to be opened when tapped.

`intentExtrasKey` - ? The key for if you with to add extras in the PendingIntent.

`intentExtras` - ? The extras that will be sent in the PendingIntent.

`intentRequestCode` - ? The request code - Defaults to 0 if not set.