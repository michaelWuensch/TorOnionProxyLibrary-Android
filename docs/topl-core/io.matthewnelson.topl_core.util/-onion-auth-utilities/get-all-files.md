[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [OnionAuthUtilities](index.md) / [getAllFiles](./get-all-files.md)

# getAllFiles

`@WorkerThread @JvmStatic fun getAllFiles(torConfigFiles: `[`TorConfigFiles`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`>?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/OnionAuthUtilities.kt#L307)

All files within the v3 Client Authentication directory are returned. If
the directory is empty, returns `null`.

### Parameters

`torConfigFiles` - 