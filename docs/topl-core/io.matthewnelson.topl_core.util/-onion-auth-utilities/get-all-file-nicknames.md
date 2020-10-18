[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [OnionAuthUtilities](index.md) / [getAllFileNicknames](./get-all-file-nicknames.md)

# getAllFileNicknames

`@WorkerThread @JvmStatic fun getAllFileNicknames(torConfigFiles: `[`TorConfigFiles`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/OnionAuthUtilities.kt#L321)

From the v3 Client Authentication directory, all files that contain the
".auth_private" extension will have their name w/o the extension returned
in an array. If the directory is empty, returns `null`.

### Parameters

`torConfigFiles` - 