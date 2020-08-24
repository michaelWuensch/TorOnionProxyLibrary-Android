[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](./index.md)

# TorServiceController

`class TorServiceController : `[`ServiceConsts`](../../io.matthewnelson.topl_service.util/-service-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L86)

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | The [TorServiceController.Builder](-builder/index.md) is where you get to customize how [TorService](#) works for your application. Call it in `Application.onCreate` and follow along.`class Builder` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [appEventBroadcaster](app-event-broadcaster.md) | `var appEventBroadcaster: `[`TorServiceEventBroadcaster`](../../io.matthewnelson.topl_service.service.components.onionproxy.model/-tor-service-event-broadcaster/index.md)`?` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [getServiceTorSettings](get-service-tor-settings.md) | Helper method for easily obtaining [ServiceTorSettings](../../io.matthewnelson.topl_service.service.components.onionproxy/-service-tor-settings/index.md).`fun getServiceTorSettings(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`ServiceTorSettings`](../../io.matthewnelson.topl_service.service.components.onionproxy/-service-tor-settings/index.md) |
| [getTorConfigFiles](get-tor-config-files.md) | Get the [TorConfigFiles](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) that have been set after calling [Builder.build](-builder/build.md)`fun getTorConfigFiles(): `[`TorConfigFiles`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) |
| [getTorSettings](get-tor-settings.md) | Get the [TorSettings](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) that have been set after calling [Builder.build](-builder/build.md).`fun getTorSettings(): `[`TorSettings`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) |
| [newIdentity](new-identity.md) | Changes identities.`fun newIdentity(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [restartTor](restart-tor.md) | Restarts Tor.`fun restartTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [startTor](start-tor.md) | Starts [TorService](#) and then Tor. You can call this as much as you want. If the Tor [Process](https://docs.oracle.com/javase/6/docs/api/java/lang/Process.html) is already running, it will do nothing.`fun startTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [stopTor](stop-tor.md) | Stops [TorService](#).`fun stopTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
