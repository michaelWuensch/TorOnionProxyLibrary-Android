[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](index.md) / [getTorConfigFiles](./get-tor-config-files.md)

# getTorConfigFiles

`@JvmStatic fun getTorConfigFiles(): `[`TorConfigFiles`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L337)

Get the [TorConfigFiles](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) that have been set after calling [Builder.build](-builder/build.md)

This method will *never* throw the [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) if you call it after
[Builder.build](-builder/build.md).

### Exceptions

`RuntimeException` - if called before [Builder.build](-builder/build.md)

**Return**
Instance of [TorConfigFiles](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) that are being used throughout TOPL-Android

