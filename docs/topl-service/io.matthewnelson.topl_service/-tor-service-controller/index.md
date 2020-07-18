[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](./index.md)

# TorServiceController

`class TorServiceController : `[`ServiceConsts`](../../io.matthewnelson.topl_service.util/-service-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L33)

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | The [TorServiceController.Builder](-builder/index.md) is where you get to customize how [TorService](#) works for your application. Call it in `Application.onCreate` and follow along.`class Builder` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [appEventBroadcaster](app-event-broadcaster.md) | `var appEventBroadcaster: `[`EventBroadcaster`](http://FIX_DOKKA_LINKS/topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md)`?` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [newIdentity](new-identity.md) | Changes identities. Does nothing if called prior to:`fun newIdentity(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [restartTor](restart-tor.md) | Restarts Tor. Does nothing if called prior to:`fun restartTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [startTor](start-tor.md) | Starts [TorService](#). Does nothing if called prior to:`fun startTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [stopTor](stop-tor.md) | Stops [TorService](#). Does nothing if called prior to:`fun stopTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
