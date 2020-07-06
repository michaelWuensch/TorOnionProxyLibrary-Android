[topl-core](../../index.md) / [io.matthewnelson.topl_core](../index.md) / [OnionProxyManager](index.md) / [start](./start.md)

# start

`@Synchronized fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/OnionProxyManager.kt#L479)

Starts tor control service if it isn't already running.

### Exceptions

`IOException` - File errors

`SecurityException` - Unauthorized access to file/directory.

`IllegalArgumentException` - if [onionProxyContext](#) methods are passed incorrect
[CoreConsts.ConfigFile](#) string values