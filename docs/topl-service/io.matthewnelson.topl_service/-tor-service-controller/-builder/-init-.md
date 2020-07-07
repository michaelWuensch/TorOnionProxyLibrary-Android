[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Builder(application: `[`Application`](https://developer.android.com/reference/android/app/Application.html)`, buildConfigVersionCode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, torSettings: `[`TorSettings`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md)`, geoipAssetPath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, geoip6AssetPath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`

The [TorServiceController.Builder](index.md) is where you get to customize how [TorService](#) works
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