[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorConfigFiles](index.md) / [createConfig](./create-config.md)

# createConfig

`@JvmOverloads @JvmStatic fun createConfig(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, configDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`, dataDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`? = null): `[`TorConfigFiles`](index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorConfigFiles.kt#L162)

Convenience method for if you're including in your App's jniLibs directory
the `libTor.so` binary, or utilizing those maintained by this project.

### Parameters

`context` - Context

`configDir` - context.getDir("dir_name_here", Context.MODE_PRIVATE)

`dataDir` - if you wish it in a different location than lib/tor`@JvmStatic fun createConfig(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`TorConfigFiles`](index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorConfigFiles.kt#L177)

Convenience method for setting up all of your files and directories in their
default locations.

### Parameters

`context` - Context