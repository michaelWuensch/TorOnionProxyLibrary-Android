[topl-service](../../index.md) / [io.matthewnelson.topl_service](../index.md) / [TorServiceController](index.md) / [getV3ClientAuthManager](./get-v3-client-auth-manager.md)

# getV3ClientAuthManager

`@JvmStatic fun getV3ClientAuthManager(): `[`BaseV3ClientAuthManager`](../../..//topl-service-base/io.matthewnelson.topl_service_base/-base-v3-client-auth-manager/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L373)

This method will *never* throw the [RuntimeException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html) if you call it after
[Builder.build](-builder/build.md).

### Exceptions

`RuntimeException` - if called before [Builder.build](-builder/build.md)

**Return**
The implemented [BaseV3ClientAuthManager](../../..//topl-service-base/io.matthewnelson.topl_service_base/-base-v3-client-auth-manager/index.md) for adding, querying, and removing
v3 Client Authorization private key files

