[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](index.md) / [startTor](./start-tor.md)

# startTor

`fun startTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L341)

Starts [TorService](#) and then Tor. You can call this as much as you want. If
the Tor [Process](https://docs.oracle.com/javase/6/docs/api/java/lang/Process.html) is already running, it will do nothing.

### Exceptions

`RuntimeException` - if called before [Builder.build](-builder/build.md)