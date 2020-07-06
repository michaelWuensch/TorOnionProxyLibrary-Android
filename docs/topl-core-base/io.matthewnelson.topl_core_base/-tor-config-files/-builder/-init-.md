[topl-core-base](../../../index.md) / [io.matthewnelson.topl_core_base](../../index.md) / [TorConfigFiles](../index.md) / [Builder](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Builder(installDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`, configDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`)`

Builder for TorConfig.

See also [Companion.createConfig](../create-config.md) for convenience methods.

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

### Parameters

`installDir` - directory where the tor binaries are installed.

`configDir` - directory where the filesystem will be setup for tor.