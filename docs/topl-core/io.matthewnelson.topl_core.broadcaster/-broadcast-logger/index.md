[topl-core](../../index.md) / [io.matthewnelson.topl_core.broadcaster](../index.md) / [BroadcastLogger](./index.md)

# BroadcastLogger

`class BroadcastLogger : `[`CoreConsts`](../../io.matthewnelson.topl_core.util/-core-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/broadcaster/BroadcastLogger.kt#L93)

This class is for standardizing broadcast messages across all classes for this Library.
Debugging is important while hacking on TOPL-Android, but those Logcat messages
should **never** make it to a release build.

To enable Logcat messages, [buildConfigDebug](#) must be `true` (w/e you have sent
[io.matthewnelson.topl_core.OnionProxyManager](../../io.matthewnelson.topl_core/-onion-proxy-manager/index.md) upon instantiation), and
[TorSettings.hasDebugLogs](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/has-debug-logs.md) must also be `true`. This way if your implementation of the
Library is causing problems in your App you need only set [TorSettings.hasDebugLogs](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/has-debug-logs.md) to
`true` for a Debug build of your App.

See helper method [io.matthewnelson.topl_core.OnionProxyManager.getBroadcastLogger](../../io.matthewnelson.topl_core/-onion-proxy-manager/get-broadcast-logger.md) to
instantiate.

### Parameters

`TAG` - Typically, the class name, but able to be set to whatever you wish

`eventBroadcaster` - For broadcasting the info

`buildConfigDebug` - To enable/disable Logcat messages

`hasDebugLogs` - To switch debug logs on/off, as well as Logcat messages on Debug builds.

### Properties

| Name | Summary |
|---|---|
| [eventBroadcaster](event-broadcaster.md) | For broadcasting the info`val eventBroadcaster: `[`EventBroadcaster`](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) |
| [TAG](-t-a-g.md) | Typically, the class name, but able to be set to whatever you wish`val TAG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [debug](debug.md) | Will only broadcast if [hasDebugLogs](#) is on.`fun debug(msg: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [error](error.md) | `fun error(msg: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [exception](exception.md) | `fun exception(e: `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [notice](notice.md) | `fun notice(msg: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [torState](tor-state.md) | `fun torState(state: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, networkState: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [warn](warn.md) | `fun warn(msg: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
