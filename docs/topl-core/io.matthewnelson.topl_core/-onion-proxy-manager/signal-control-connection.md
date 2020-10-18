[topl-core](../../index.md) / [io.matthewnelson.topl_core](../index.md) / [OnionProxyManager](index.md) / [signalControlConnection](./signal-control-connection.md)

# signalControlConnection

`fun signalControlConnection(torControlSignalCommand: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/OnionProxyManager.kt#L955)

Sends a signal to the  [TorControlConnection](#)

### Parameters

`torControlSignalCommand` - See [TorControlCommands](#) for acceptable `SIGNAL_` values.

**Return**
`true` if the signal was received by [TorControlConnection](#), `false` if not.

