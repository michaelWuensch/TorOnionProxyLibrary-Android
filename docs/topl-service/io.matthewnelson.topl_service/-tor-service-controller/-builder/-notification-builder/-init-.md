[topl-service](../../../../index.md) / [io.matthewnelson.topl_service](../../../index.md) / [TorServiceController](../../index.md) / [Builder](../index.md) / [NotificationBuilder](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`NotificationBuilder(builder: Builder, channelName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelDescription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, notificationID: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`

Where you get to customize how your foreground notification will look/function.
Calling [customizeNotification](../customize-notification.md) will return this class to you which provides methods
specific to customization of notifications. Call [applyNotificationSettings](apply-notification-settings.md) when done
to return to [Builder](../index.md) to continue with it's methods for customization.

See [Builder](../index.md) for code samples.

### Parameters

`builder` - [Builder](../index.md) To return to it when calling [applyNotificationSettings](apply-notification-settings.md)

`channelName` - Your notification channel's name.

`channelID` - Your notification channel's ID.

`channelDescription` - Your notification channel's description.

`notificationID` - Your foreground notification's ID.