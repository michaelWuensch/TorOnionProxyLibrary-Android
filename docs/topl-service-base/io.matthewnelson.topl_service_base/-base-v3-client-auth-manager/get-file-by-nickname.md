[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseV3ClientAuthManager](index.md) / [getFileByNickname](./get-file-by-nickname.md)

# getFileByNickname

`@WorkerThread abstract fun getFileByNickname(nickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseV3ClientAuthManager.kt#L134)

Retrieve a v3 client authentication file by the nickname, whether the file
extension ".auth_private" is included or not.

### Parameters

`nickname` - The pre file extension name