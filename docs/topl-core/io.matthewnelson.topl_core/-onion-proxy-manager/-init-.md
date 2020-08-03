[topl-core](../../index.md) / [io.matthewnelson.topl_core](../index.md) / [OnionProxyManager](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`OnionProxyManager(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, torConfigFiles: `[`TorConfigFiles`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`, torInstaller: `[`TorInstaller`](../../io.matthewnelson.topl_core.util/-tor-installer/index.md)`, torSettings: `[`TorSettings`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md)`, eventListener: `[`BaseEventListener`](../../io.matthewnelson.topl_core.listener/-base-event-listener/index.md)`, eventBroadcaster: `[`EventBroadcaster`](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md)`, buildConfigDebug: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null)`

This is where all the fun is, this is the class which acts as a gateway into the `topl-core`
module, and ensures synchronicity is had.

This class is thread safe but that's mostly because we hit everything over the head
with 'synchronized'. Given the way this class is used there shouldn't be any performance
implications of this.

This class began life as TorPlugin from the Briar Project

``` kotlin
//Unresolved: io.matthewnelson.topl_service.service.TorService.initTOPLCore
```

### Parameters

`context` - Context.

`torConfigFiles` - [TorConfigFiles](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) For setting up [OnionProxyContext](#)

`torInstaller` - [TorInstaller](../../io.matthewnelson.topl_core.util/-tor-installer/index.md) For setting up [OnionProxyContext](#)

`torSettings` - [TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) For setting up [OnionProxyContext](#)

`eventListener` - [BaseEventListener](../../io.matthewnelson.topl_core.listener/-base-event-listener/index.md) For processing Tor OP messages.

`eventBroadcaster` - Your own broadcaster which extends [EventBroadcaster](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md)

`buildConfigDebug` - Send [BuildConfig.DEBUG](#) which will show Logcat messages for this
module on Debug builds of your Application. If `null`, all the messages will still be
broadcast to the provided [EventBroadcaster](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) and you can handle them there how you'd like.