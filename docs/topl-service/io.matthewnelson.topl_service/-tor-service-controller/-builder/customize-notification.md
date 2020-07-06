[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [customizeNotification](./customize-notification.md)

# customizeNotification

`fun customizeNotification(channelName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelDescription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, notificationID: `[`Short`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short/index.html)`): NotificationBuilder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L147)

Customize the service notification to your application.

See [Builder](index.md) for code samples.

### Parameters

`channelName` - Your notification channel's name (cannot be empty).

`channelID` - Your notification channel's ID (cannot be empty).

`channelDescription` - Your notification channel's description (cannot be empty).

`notificationID` - Your foreground notification's ID.

### Exceptions

`IllegalArgumentException` - If String fields are empty.

**Return**
[NotificationBuilder](-notification-builder/index.md) To obtain methods specific to notification customization.

