[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Builder(application: `[`Application`](https://developer.android.com/reference/android/app/Application.html)`, torServiceNotificationBuilder: Builder, backgroundManagerPolicy: Policy, buildConfigVersionCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, defaultTorSettings: `[`ApplicationDefaultTorSettings`](../../../..//topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md)`, geoipAssetPath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, geoip6AssetPath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`

The [TorServiceController.Builder](index.md) is where you get to customize how [TorService](#) works
for your application. Call it in `Application.onCreate` and follow along.

``` kotlin
//  private fun generateTorServiceNotificationBuilder(): ServiceNotification.Builder {
        return ServiceNotification.Builder(
            channelName = "TOPL-Android Demo",
            channelDescription = "TorOnionProxyLibrary-Android Demo",
            channelID = "TOPL-Android Demo",
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
            .setVisibility(visibility = NotificationCompat.VISIBILITY_PRIVATE)
            .setCustomColor(colorRes = R.color.primaryColor)
            .enableTorRestartButton(enable = true)
            .enableTorStopButton(enable = true)
            .showNotification(show = true)
//  }
```

``` kotlin
//  private fun generateBackgroundManagerPolicy(): BackgroundManager.Builder.Policy {
        return BackgroundManager.Builder()

            // All available options present. Only 1 is able to be chosen.
            .respectResourcesWhileInBackground(secondsFrom5To45 = 20)
            //.runServiceInForeground(killAppIfTaskIsRemoved = true)
//  }
```

``` kotlin
//  private fun setupTorServices(application: Application, torConfigFiles: TorConfigFiles ) {
        TorServiceController.Builder(
            application = application,
            torServiceNotificationBuilder = generateTorServiceNotificationBuilder(),
            backgroundManagerPolicy = generateBackgroundManagerPolicy(),
            buildConfigVersionCode = BuildConfig.VERSION_CODE,

            // Can instantiate directly here then access it from
            // TorServiceController.Companion.getTorSettings() and cast what's returned
            // as MyTorSettings
            defaultTorSettings = MyTorSettings(),

            // These should live somewhere in your module's assets directory,
            // ex: my-project/my-application-module/src/main/assets/common/geoip
            // ex: my-project/my-application-module/src/main/assets/common/geoip6
            geoipAssetPath = "common/geoip",
            geoip6AssetPath = "common/geoip6"
        )
            .addTimeToDisableNetworkDelay(milliseconds = 1_000L)
            .addTimeToRestartTorDelay(milliseconds = 100L)
            .addTimeToStopServiceDelay(milliseconds = 100L)
            .disableStopServiceOnTaskRemoved(disable = false)
            .setBuildConfigDebug(buildConfigDebug = BuildConfig.DEBUG)

            // Can instantiate directly here then access it from
            // TorServiceController.Companion?.appEventBroadcaster and cast what's returned
            // as MyEventBroadcaster
            .setEventBroadcaster(eventBroadcaster = MyEventBroadcaster())

            // Only needed if you wish to customize the directories/files used by Tor if
            // the defaults aren't to your liking.
            .useCustomTorConfigFiles(torConfigFiles = torConfigFiles)

            .build()
//  }
```

### Parameters

`application` - [Application](https://developer.android.com/reference/android/app/Application.html), for obtaining context

`torServiceNotificationBuilder` - The [ServiceNotification.Builder](../../../io.matthewnelson.topl_service.notification/-service-notification/-builder/index.md) for
customizing [TorService](#)'s notification

`backgroundManagerPolicy` - The [BackgroundManager.Builder.Policy](../../../io.matthewnelson.topl_service.lifecycle/-background-manager/-builder/-policy.md) to be executed
while your application is in the background (the Recent App's tray).

`buildConfigVersionCode` - send [BuildConfig.VERSION_CODE](#). Mitigates copying of geoip
files to app updates only

`defaultTorSettings` - [ApplicationDefaultTorSettings](../../../..//topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md) used to create your torrc file
on start of Tor

`geoipAssetPath` - The path to where you have your geoip file located (ex: in
assets/common directory, send this variable "common/geoip")

`geoip6AssetPath` - The path to where you have your geoip6 file located (ex: in
assets/common directory, send this variable "common/geoip6")