[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](index.md) / [getTorSettings](./get-tor-settings.md)

# getTorSettings

`fun getTorSettings(): `[`TorSettings`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L329)

Get the [TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) that have been set after calling [Builder.build](-builder/build.md). These are
the [TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) you initialized [TorServiceController.Builder](-builder/index.md) with.

### Exceptions

`RuntimeException` - if called before [Builder.build](-builder/build.md)

**Return**
Instance of [TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) that are being used throughout TOPL-Android

