[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](./index.md)

# TorServiceController

`class TorServiceController : `[`ServiceConsts`](../../io.matthewnelson.topl_service.util/-service-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L91)

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | The [TorServiceController.Builder](-builder/index.md) is where you get to customize how [TorService](#) works for your application. Call it in `Application.onCreate` and follow along.`class Builder` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [appEventBroadcaster](app-event-broadcaster.md) | `var appEventBroadcaster: `[`TorServiceEventBroadcaster`](../../..//topl-service-base/io.matthewnelson.topl_service_base/-tor-service-event-broadcaster/index.md)`?` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [getDefaultTorSettings](get-default-tor-settings.md) | This method will *never* throw the [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) if you call it after [Builder.build](-builder/build.md).`fun getDefaultTorSettings(): `[`ApplicationDefaultTorSettings`](../../..//topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md) |
| [getServiceTorSettings](get-service-tor-settings.md) | This method will *never* throw the [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) if you call it after [Builder.build](-builder/build.md).`fun getServiceTorSettings(): `[`BaseServiceTorSettings`](../../..//topl-service-base/io.matthewnelson.topl_service_base/-base-service-tor-settings/index.md) |
| [getTorConfigFiles](get-tor-config-files.md) | Get the [TorConfigFiles](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) that have been set after calling [Builder.build](-builder/build.md)`fun getTorConfigFiles(): `[`TorConfigFiles`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) |
| [getV3ClientAuthManager](get-v3-client-auth-manager.md) | This method will *never* throw the [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) if you call it after [Builder.build](-builder/build.md).`fun getV3ClientAuthManager(): `[`BaseV3ClientAuthManager`](../../..//topl-service-base/io.matthewnelson.topl_service_base/-base-v3-client-auth-manager/index.md) |
| [newIdentity](new-identity.md) | Changes identities.`fun newIdentity(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [restartTor](restart-tor.md) | Restarts Tor.`fun restartTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [startTor](start-tor.md) | This method will *never* throw the [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) if you call it after [Builder.build](-builder/build.md).`fun startTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [stopTor](stop-tor.md) | Stops [TorService](#).`fun stopTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
