[topl-service](../../../../index.md) / [io.matthewnelson.topl_service](../../../index.md) / [TorServiceController](../../index.md) / [Builder](../index.md) / [NotificationBuilder](./index.md)

# NotificationBuilder

`class NotificationBuilder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L182)

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

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Where you get to customize how your foreground notification will look/function. Calling [customizeNotification](../customize-notification.md) will return this class to you which provides methods specific to customization of notifications. Call [applyNotificationSettings](apply-notification-settings.md) when done to return to [Builder](../index.md) to continue with it's methods for customization.`NotificationBuilder(builder: Builder, channelName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelDescription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, notificationID: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [applyNotificationSettings](apply-notification-settings.md) | Initialize settings.`fun applyNotificationSettings(): Builder` |
| [enableTorRestartButton](enable-tor-restart-button.md) | Disabled by Default`fun enableTorRestartButton(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): NotificationBuilder` |
| [enableTorStopButton](enable-tor-stop-button.md) | Disabled by Default`fun enableTorStopButton(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): NotificationBuilder` |
| [setActivityToBeOpenedOnTap](set-activity-to-be-opened-on-tap.md) | For when your user taps the TorService notification.`fun setActivityToBeOpenedOnTap(clazz: `[`Class`](https://docs.oracle.com/javase/6/docs/api/java/lang/Class.html)`<*>, intentExtrasKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, intentExtras: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, intentRequestCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): NotificationBuilder` |
| [setCustomColor](set-custom-color.md) | Defaults to [R.color.tor_service_white](#)`fun setCustomColor(colorRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, colorizeBackground: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): NotificationBuilder` |
| [setImageTorDataTransfer](set-image-tor-data-transfer.md) | Defaults to Orbot/TorBrowser's icon [R.drawable.tor_stat_network_dataxfer](#).`fun setImageTorDataTransfer(drawableRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): NotificationBuilder` |
| [setImageTorErrors](set-image-tor-errors.md) | Defaults to Orbot/TorBrowser's icon [R.drawable.tor_stat_notifyerr](#).`fun setImageTorErrors(drawableRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): NotificationBuilder` |
| [setImageTorNetworkingDisabled](set-image-tor-networking-disabled.md) | Defaults to Orbot/TorBrowser's icon [R.drawable.tor_stat_network_disabled](#).`fun setImageTorNetworkingDisabled(drawableRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): NotificationBuilder` |
| [setImageTorNetworkingEnabled](set-image-tor-networking-enabled.md) | Defaults to Orbot/TorBrowser's icon [R.drawable.tor_stat_network_enabled](#).`fun setImageTorNetworkingEnabled(drawableRes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): NotificationBuilder` |
| [setVisibility](set-visibility.md) | Defaults to NotificationVisibility.VISIBILITY_SECRET`fun setVisibility(visibility: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): NotificationBuilder` |
