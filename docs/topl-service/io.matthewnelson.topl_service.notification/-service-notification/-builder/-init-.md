[topl-service](../../../index.md) / [io.matthewnelson.topl_service.notification](../../index.md) / [ServiceNotification](../index.md) / [Builder](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Builder(channelName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelDescription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, notificationID: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`)`

Where you get to customize how your notification will look and function.

A notification is required to be displayed while [TorService](#) is running in the
Foreground. Even if you set [Builder.showNotification](show-notification.md) to false, [TorService](#)
is brought to the Foreground when the user removes your task from the recent apps tray
in order to properly shut down Tor and clean up w/o being killed by the OS.

``` kotlin
//  private fun generateTorServiceNotificationBuilder(context: Context): ServiceNotification.Builder {
        return ServiceNotification.Builder(
            channelName = "TOPL-Android Demo",
            channelDescription = "TorOnionProxyLibrary-Android Demo",
            channelID = "TOPL-Android Demo",
            notificationID = 615
        )
            .setImageTorNetworkingEnabled(drawableRes = R.drawable.tor_stat_network_enabled)
            .setImageTorNetworkingDisabled(drawableRes = R.drawable.tor_stat_network_disabled)
            .setImageTorDataTransfer(drawableRes = R.drawable.tor_stat_network_dataxfer)
            .setImageTorErrors(drawableRes = R.drawable.tor_stat_notifyerr)
            .setVisibility(visibility = NotificationCompat.VISIBILITY_PRIVATE)
            .setCustomColor(colorRes = R.color.primaryColor)
            .enableTorRestartButton(enable = true)
            .enableTorStopButton(enable = true)
            .showNotification(show = true)

            // Set the notification's contentIntent for when the user clicks the notification
            .also { builder ->
                context.applicationContext.packageManager
                    ?.getLaunchIntentForPackage(context.applicationContext.packageName)
                    ?.let { intent ->

                        // Set in your manifest for the launch activity so the intent won't launch
                        // a new activity over top of your already created activity if the app is
                        // open when the user clicks the notification:
                        //
                        // android:launchMode="singleInstance"
                        //
                        // For more info on launchMode and Activity Intent flags, see:
                        //
                        // https://medium.com/swlh/truly-understand-tasks-and-back-stack-intent-flags-of-activity-2a137c401eca

                        builder.setContentIntent(
                            PendingIntent.getActivity(
                                context.applicationContext,
                                0, // Your desired request code
                                intent,
                                0 // flags
                            // can also include a bundle if desired
                            )
                        )
                }
            }
//  }
```

### Parameters

`channelName` - Your notification channel's name (Cannot be Empty).

`channelID` - Your notification channel's ID (Cannot be Empty).

`channelDescription` - Your notification channel's description (Cannot be Empty).

`notificationID` - Your foreground notification's ID.

### Exceptions

`IllegalArgumentException` - If String fields are empty.