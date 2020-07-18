[topl-service](../../../index.md) / [io.matthewnelson.topl_service](../../index.md) / [TorServiceController](../index.md) / [Builder](index.md) / [useCustomTorConfigFiles](./use-custom-tor-config-files.md)

# useCustomTorConfigFiles

`fun useCustomTorConfigFiles(torConfigFiles: `[`TorConfigFiles`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/TorServiceController.kt#L129)

If you wish to customize the file structure of how Tor is installed in your app,
you can do so by instantiating your own [TorConfigFiles](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) and customizing it via
the [TorConfigFiles.Builder](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/-builder/index.md), or overridden method [TorConfigFiles.createConfig](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/-companion/create-config.md).

By default, [TorService](#) will call [TorConfigFiles.createConfig](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/-companion/create-config.md) using your
[Context.getApplicationContext](https://developer.android.com/reference/android/content/Context.html#getApplicationContext()) to set up a standard directory hierarchy for Tor
to operate with.

``` kotlin
//        fun customTorConfigFilesSetup(context: Context): TorConfigFiles {

            // This is modifying the directory hierarchy from TorService's
            // default setup. For example, if you are using binaries for Tor that
            // are named differently that that expressed in TorConfigFiles.createConfig()

            // Post Android API 28 requires that executable files be contained in your
            // application's data/app directory, as they can no longer execute from data/data.
            val installDir = File(context.applicationInfo.nativeLibraryDir)

            // Will create a directory within your application's data/data dir
            val configDir = context.getDir("torservice", Context.MODE_PRIVATE)

            val builder = TorConfigFiles.Builder(installDir, configDir)

            // Customize the tor executable file name. Requires that the executable file
            // be in your project's src/main/jniLibs directory. If you are getting your
            // executable files via a dependency, be sure to consult that Libraries documentation.
            builder.torExecutable(File(installDir, "libtor.so"))

            // customize further via the builder methods...

            return builder.build()
//        }
```

**Return**
[Builder](index.md)

**See Also**

[Builder.build](build.md)

