[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [build](./build.md)

# build

`fun build(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L296)

Initializes [TorService](#) setup and enables the ability to call methods from the
[Companion](#) object w/o throwing exceptions.

See [Builder](index.md) for code samples.

### Exceptions

`IllegalArgumentException` - If [disableStopServiceOnTaskRemoved](disable-stop-service-on-task-removed.md) was elected
and your selected [BackgroundManager.Builder.Policy](../../../io.matthewnelson.topl_service.lifecycle/-background-manager/-builder/-policy.md) is **not**
[io.matthewnelson.topl_service_base.BaseServiceConsts.BackgroundPolicy.RUN_IN_FOREGROUND](../../../..//topl-service-base/io.matthewnelson.topl_service_base/-base-service-consts/-background-policy/-companion/-r-u-n_-i-n_-f-o-r-e-g-r-o-u-n-d.md)
and/or [BackgroundManager.Builder.killAppIfTaskIsRemoved](#) is **not** `true`