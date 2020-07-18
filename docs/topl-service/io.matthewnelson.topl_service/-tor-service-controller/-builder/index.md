[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](./index.md)

# Builder

`class Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L70)

The [TorServiceController.Builder](./index.md) is where you get to customize how [TorService](#) works
for your application. Call it in `Application.onCreate` and follow along.

A note about the [TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) you send this. Those are the default settings which
[TorService](#) will fall back on if [io.matthewnelson.topl_service.prefs.TorServicePrefs](../../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md)
has nothing in it for that particular [ServiceConsts](../../../io.matthewnelson.topl_service.util/-service-consts/index.md).PrefKey.

The settings get written to the `torrc` file every time Tor is started (I plan to make
this less sledgehammer-ish in the future).

To update settings while your application is running you need only to instantiate
[io.matthewnelson.topl_service.prefs.TorServicePrefs](../../../io.matthewnelson.topl_service.prefs/-tor-service-prefs/index.md) and save the data using the
appropriately annotated method and [ServiceConsts](../../../io.matthewnelson.topl_service.util/-service-consts/index.md).PrefKey, then
restart Tor (for now... ;-D).

I plan to implement a
[android.content.SharedPreferences.OnSharedPreferenceChangeListener](https://developer.android.com/reference/android/content/SharedPreferences/OnSharedPreferenceChangeListener.html) that will do this
immediately for the settings that don't require a restart, but a stable release comes first).

You can see how the [TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) sent here are used in [TorService](#) by looking at
[io.matthewnelson.topl_service.onionproxy.ServiceTorSettings](#) and
[TorService.initTOPLCore](#).

``` kotlin
//        fun setupTorServices(
//            application: Application,
//            eventBroadcaster: EventBroadcaster,
//            torConfigFiles: TorConfigFiles
//        ) {

            TorServiceController.Builder(
                application = application,
                buildConfigVersionCode = BuildConfig.VERSION_CODE,
                torSettings = App.myTorSettings,

                // These should live somewhere in your project application's assets directory
                geoipAssetPath = "common/geoip",
                geoip6AssetPath = "common/geoip6"
            )

                .setBuildConfigDebug(BuildConfig.DEBUG)
                .setEventBroadcaster(eventBroadcaster)
                .useCustomTorConfigFiles(torConfigFiles)

                // Notification customization
                .customizeNotification(
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
                .setImageTorNetworkingEnabled(R.drawable.tor_stat_network_enabled)
                .setImageTorNetworkingDisabled(R.drawable.tor_stat_network_disabled)
                .setImageTorDataTransfer(R.drawable.tor_stat_network_dataxfer)
                .setImageTorErrors(R.drawable.tor_stat_notifyerr)
                .setCustomColor(R.color.tor_service_white, colorizeBackground = true)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .enableTorRestartButton(enable = true)
                .enableTorStopButton(enable = true)

                // Will return a Builder object to continue with non-notification related options
                .applyNotificationSettings()

                .build()
//        }
```

### Parameters

`application` - [Application](https://developer.android.com/reference/android/app/Application.html), for obtaining context.

`buildConfigVersionCode` - send [BuildConfig.VERSION_CODE](#). Mitigates copying of geoip
files to app updates only.

`torSettings` - [TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) used to create your torrc file on start of Tor.

`geoipAssetPath` - The path to where you have your geoip file located (ex: in
assets/common directory, send this variable "common/geoip").

`geoip6AssetPath` - The path to where you have your geoip6 file located (ex: in
assets/common directory, send this variable "common/geoip6").

### Types

| Name | Summary |
|---|---|
| [NotificationBuilder](-notification-builder/index.md) | Where you get to customize how your foreground notification will look/function. Calling [customizeNotification](customize-notification.md) will return this class to you which provides methods specific to customization of notifications. Call [applyNotificationSettings](-notification-builder/apply-notification-settings.md) when done to return to [Builder](./index.md) to continue with it's methods for customization.`class NotificationBuilder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | The [TorServiceController.Builder](./index.md) is where you get to customize how [TorService](#) works for your application. Call it in `Application.onCreate` and follow along.`Builder(application: `[`Application`](https://developer.android.com/reference/android/app/Application.html)`, buildConfigVersionCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, torSettings: `[`TorSettings`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md)`, geoipAssetPath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, geoip6AssetPath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [build](build.md) | Initializes [TorService](#) setup and enables the ability to call methods in the [Companion](#) object.`fun build(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [customizeNotification](customize-notification.md) | Customize the service notification to your application.`fun customizeNotification(channelName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelID: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, channelDescription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, notificationID: `[`Short`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-short/index.html)`): NotificationBuilder` |
| [setBuildConfigDebug](set-build-config-debug.md) | This makes it such that on your Application's **Debug** builds, the `topl-core` and `topl-service` modules will provide you with Logcat messages (when [TorSettings.hasDebugLogs](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/has-debug-logs.md) is enabled).`fun setBuildConfigDebug(buildConfigDebug: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): Builder` |
| [setEventBroadcaster](set-event-broadcaster.md) | Get broadcasts piped to your Application to do with them what you desire. What you send this will live at [Companion.appEventBroadcaster](../app-event-broadcaster.md) for the remainder of your application's lifecycle to refer to elsewhere in your App.`fun setEventBroadcaster(eventBroadcaster: `[`EventBroadcaster`](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md)`): Builder` |
| [useCustomTorConfigFiles](use-custom-tor-config-files.md) | If you wish to customize the file structure of how Tor is installed in your app, you can do so by instantiating your own [TorConfigFiles](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) and customizing it via the [TorConfigFiles.Builder](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/-builder/index.md), or overridden method [TorConfigFiles.createConfig](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/-companion/create-config.md).`fun useCustomTorConfigFiles(torConfigFiles: `[`TorConfigFiles`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`): Builder` |
