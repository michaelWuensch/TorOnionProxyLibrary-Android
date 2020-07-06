[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](index.md) / [startTor](./start-tor.md)

# startTor

`fun startTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L419)

Starts [TorService](#). Does nothing if called prior to:

* Initializing [TorServiceController.Builder](-builder/index.md) by calling [Builder.build](-builder/build.md)

You can call this as much as you want. If Tor is already on, it will do nothing.

