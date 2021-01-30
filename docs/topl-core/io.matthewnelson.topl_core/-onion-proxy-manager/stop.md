[topl-core](../../index.md) / [io.matthewnelson.topl_core](../index.md) / [OnionProxyManager](index.md) / [stop](./stop.md)

# stop

`@Synchronized fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/OnionProxyManager.kt#L360)

Kills the Tor OP Process. Once you have called this method nothing is going
to work until you either call startWithRepeat or start

### Exceptions

`NullPointerException` - If [controlConnection](#) magically changes to null.

`IOException` - If [controlConnection](#) is not responding to `shutdownTor`.