[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseV3ClientAuthManager](index.md) / [getAllFileNicknames](./get-all-file-nicknames.md)

# getAllFileNicknames

`@WorkerThread abstract fun getAllFileNicknames(): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseV3ClientAuthManager.kt#L149)

From the v3 Client Authentication directory, all files that contain the
".auth_private" extension will have their name w/o the extension returned
in an array. If the directory is empty, returns `null`.

