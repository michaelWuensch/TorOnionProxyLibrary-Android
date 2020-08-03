[topl-service](../../../index.md) / [io.matthewnelson.topl_service.notification](../../index.md) / [ServiceNotification](../index.md) / [Builder](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Builder(channelName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelDescription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, notificationID: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`

Where you get to customize how your notification will look and function.

A notification is required to be displayed while [TorService](#) is running in the
Foreground. Even if you set [Builder.showNotification](show-notification.md) to false, [TorService](#)
is brought to the Foreground when the user removes your task from the recent apps tray
in order to properly shut down Tor and clean up w/o being killed by the OS.

``` kotlin
//  private fun generateTorServiceNotificationBuilder(): ServiceNotification.Builder {
        return ServiceNotification.Builder(
            channelName = "TorService Channel",
            channelDescription = "Tor Channel",
            channelID = "My Sample Application",
            notificationID = 615
        )
            .setActivityToBeOpenedOnTap(
                clazz = MainActivity::class.java,
                intentExtrasKey = null,
                intentExtras = null,
                intentRequestCode = null
            )
            .setImageTorNetworkingEnabled(drawableRes = R.drawable.tor_stat_network_enabled)
            .setImageTorNetworkingDisabled(drawableRes = R.drawable.tor_stat_network_disabled)
            .setImageTorDataTransfer(drawableRes = R.drawable.tor_stat_network_dataxfer)
            .setImageTorErrors(drawableRes = R.drawable.tor_stat_notifyerr)
            .setCustomColor(colorRes = R.color.tor_service_white)
            .setVisibility(visibility = NotificationCompat.VISIBILITY_PRIVATE)
            .setCustomColor(colorRes = R.color.primaryColor)
            .enableTorRestartButton(enable = true)
            .enableTorStopButton(enable = true)
            .showNotification(show = true)
//  }
```

### Parameters

`channelName` - Your notification channel's name (Cannot be Empty).

`channelID` - Your notification channel's ID (Cannot be Empty).

`channelDescription` - Your notification channel's description (Cannot be Empty).

`notificationID` - Your foreground notification's ID.

### Exceptions

`IllegalArgumentException` - If String fields are empty.