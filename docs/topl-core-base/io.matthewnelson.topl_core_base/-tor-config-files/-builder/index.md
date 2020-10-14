[topl-core-base](../../../index.md) / [io.matthewnelson.topl_core_base](../../index.md) / [TorConfigFiles](../index.md) / [Builder](./index.md)

# Builder

`class Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorConfigFiles.kt#L261)

Builder for TorConfig.

See also [Companion.createConfig](../create-config.md) for convenience methods.

``` kotlin
//  fun customTorConfigFilesSetup(context: Context): TorConfigFiles {

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
        // be in your module's src/main/jniLibs directory. If you are getting your
        // executable files via a dependency be sure to consult that Library's documentation.
        builder.torExecutable(File(installDir, "libtor.so"))

        // customize further via the builder methods...

        return builder.build()
//  }
```

### Parameters

`installDir` - directory where the tor binaries are installed.

`configDir` - directory where the filesystem will be setup for tor.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Builder for TorConfig.`Builder(installDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`, configDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [build](build.md) | Builds torConfig and sets default values if not explicitly configured through builder.`fun build(): `[`TorConfigFiles`](../index.md) |
| [cookieAuthFile](cookie-auth-file.md) | `fun cookieAuthFile(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [dataDir](data-dir.md) | Store working data in DIR. Can not be changed while tor is running.`fun dataDir(directory: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [fileCreationTimeout](file-creation-timeout.md) | When tor starts it waits for the control port and cookie auth files to be created before it proceeds to the next step in startup. If these files are not created after a certain amount of time, then the startup has failed.`fun fileCreationTimeout(timeoutSeconds: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): Builder` |
| [geoip](geoip.md) | A filename containing IPv4 GeoIP data, for use with by-country statistics.`fun geoip(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [geoipv6](geoipv6.md) | A filename containing IPv6 GeoIP data, for use with by-country statistics.`fun geoipv6(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [hiddenServiceDir](hidden-service-dir.md) | Store data files for a hidden service in DIRECTORY. Every hidden service must have a separate directory. You may use this option multiple times to specify multiple services. If DIRECTORY does not exist, Tor will create it. (Note: in current versions of Tor, if DIRECTORY is a relative path, it will be relative to the current working directory of Tor instance, not to its DataDirectory. Do not rely on this behavior; it is not guaranteed to remain the same in future versions.)`fun hiddenServiceDir(directory: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [hostnameFile](hostname-file.md) | `fun hostnameFile(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [libraryPath](library-path.md) | `fun libraryPath(directory: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [resolveConf](resolve-conf.md) | `fun resolveConf(resolveConf: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [torExecutable](tor-executable.md) | `fun torExecutable(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [torrc](torrc.md) | The configuration file, which contains "option value" pairs.`fun torrc(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
| [v3AuthPrivateDir](v3-auth-private-dir.md) | `fun v3AuthPrivateDir(directory: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` |
