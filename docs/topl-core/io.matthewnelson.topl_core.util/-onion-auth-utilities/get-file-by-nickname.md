[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [OnionAuthUtilities](index.md) / [getFileByNickname](./get-file-by-nickname.md)

# getFileByNickname

`@WorkerThread @JvmStatic fun getFileByNickname(nickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, torConfigFiles: `[`TorConfigFiles`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/OnionAuthUtilities.kt#L302)

Retrieve a v3 client authentication file by the nickname, whether the file
extension ".auth_private" is included or not.

### Parameters

`nickname` - The pre file extension name

`torConfigFiles` - 