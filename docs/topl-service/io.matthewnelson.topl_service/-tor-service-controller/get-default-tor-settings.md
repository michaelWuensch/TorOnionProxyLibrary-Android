[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](index.md) / [getDefaultTorSettings](./get-default-tor-settings.md)

# getDefaultTorSettings

`@JvmStatic fun getDefaultTorSettings(): `[`ApplicationDefaultTorSettings`](../../..//topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L355)

This method will *never* throw the [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) if you call it after
[Builder.build](-builder/build.md).

### Exceptions

`RuntimeException` - if called before [Builder.build](-builder/build.md)

**Return**
Instance of [ApplicationDefaultTorSettings](../../..//topl-service-base/io.matthewnelson.topl_service_base/-application-default-tor-settings/index.md) that were used to instantiate
[TorServiceController.Builder](-builder/index.md) with

