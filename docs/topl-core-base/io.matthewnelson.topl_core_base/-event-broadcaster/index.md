[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [EventBroadcaster](./index.md)

# EventBroadcaster

`abstract class EventBroadcaster : `[`BaseConsts`](../-base-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/EventBroadcaster.kt#L55)

Service for sending event logs to the system.

Both `topl-core` and `topl-service` utilize this class to broadcast messages. This
allows for easier separation of messages based on the type, process or class.

See [BaseConsts.BroadcastType](../-base-consts/-broadcast-type/index.md)s

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Service for sending event logs to the system.`EventBroadcaster()` |

### Functions

| Name | Summary |
|---|---|
| [broadcastBandwidth](broadcast-bandwidth.md) | [bytesRead](broadcast-bandwidth.md#io.matthewnelson.topl_core_base.EventBroadcaster$broadcastBandwidth(kotlin.String, kotlin.String)/bytesRead) = bytes downloaded [bytesWritten](broadcast-bandwidth.md#io.matthewnelson.topl_core_base.EventBroadcaster$broadcastBandwidth(kotlin.String, kotlin.String)/bytesWritten) = bytes uploaded`abstract fun broadcastBandwidth(bytesRead: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, bytesWritten: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [broadcastDebug](broadcast-debug.md) | ("DEBUG|ClassName|msg")`abstract fun broadcastDebug(msg: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [broadcastException](broadcast-exception.md) | ("EXCEPTION|ClassName|msg", e)`abstract fun broadcastException(msg: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, e: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [broadcastLogMessage](broadcast-log-message.md) | Not yet implemented in either module.`abstract fun broadcastLogMessage(logMessage: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [broadcastNotice](broadcast-notice.md) | Will be one of:`abstract fun broadcastNotice(msg: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [broadcastTorState](broadcast-tor-state.md) | See [BaseConsts.TorState](../-base-consts/-tor-state/index.md) and [BaseConsts.TorNetworkState](../-base-consts/-tor-network-state/index.md)`abstract fun broadcastTorState(state: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, networkState: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
