[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](index.md) / [getServiceTorSettings](./get-service-tor-settings.md)

# getServiceTorSettings

`@JvmStatic fun getServiceTorSettings(): `[`BaseServiceTorSettings`](../../..//topl-service-base/io.matthewnelson.topl_service_base/-base-service-tor-settings/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L385)

This method will *never* throw the [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) if you call it after
[Builder.build](-builder/build.md).

### Exceptions

`RuntimeException` - if called before [Builder.build](-builder/build.md)

**Return**
[BaseServiceTorSettings](../../..//topl-service-base/io.matthewnelson.topl_service_base/-base-service-tor-settings/index.md)

