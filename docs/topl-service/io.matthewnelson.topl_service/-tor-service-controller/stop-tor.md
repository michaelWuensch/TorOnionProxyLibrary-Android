[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](index.md) / [stopTor](./stop-tor.md)

# stopTor

`fun stopTor(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L428)

Stops [TorService](#). Does nothing if called prior to:

* Initializing [TorServiceController.Builder](-builder/index.md) by calling [Builder.build](-builder/build.md)
* Calling [startTor](start-tor.md)
