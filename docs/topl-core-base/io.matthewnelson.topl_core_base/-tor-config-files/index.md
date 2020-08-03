[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorConfigFiles](./index.md)

# TorConfigFiles

`class TorConfigFiles : `[`BaseConsts`](../-base-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorConfigFiles.kt#L107)

Holds Tor configuration information for files and directories that Tor will use.

See [Companion.createConfig](create-config.md) or [Builder](-builder/index.md) to instantiate.

When modifying/querying Files, ensure you are using `synchronized` and acquiring
the appropriate `FileLock` object pertaining to that File. This inhibits errors
across the library.

See extension function [readTorConfigFile](../java.io.-file/read-tor-config-file.md).

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | Builder for TorConfig.`class Builder` |

### Properties

| Name | Summary |
|---|---|
| [configDir](config-dir.md) | `val configDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [controlPortFile](control-port-file.md) | `val controlPortFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [controlPortFileLock](control-port-file-lock.md) | `val controlPortFileLock: `[`Object`](https://docs.oracle.com/javase/6/docs/api/java/lang/Object.html) |
| [cookieAuthFile](cookie-auth-file.md) | Used for cookie authentication with the controller. Location can be overridden by the CookieAuthFile config option. Regenerated on startup. See control-spec.txt in torspec for details.`val cookieAuthFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [cookieAuthFileLock](cookie-auth-file-lock.md) | `val cookieAuthFileLock: `[`Object`](https://docs.oracle.com/javase/6/docs/api/java/lang/Object.html) |
| [dataDir](data-dir.md) | `val dataDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [dataDirLock](data-dir-lock.md) | `val dataDirLock: `[`Object`](https://docs.oracle.com/javase/6/docs/api/java/lang/Object.html) |
| [fileCreationTimeout](file-creation-timeout.md) | When tor starts it waits for the control port and cookie auth files to be created before it proceeds to the next step in startup. If these files are not created after a certain amount of time, then the startup has failed.`val fileCreationTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [geoIpFile](geo-ip-file.md) | `val geoIpFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [geoIpFileLock](geo-ip-file-lock.md) | `val geoIpFileLock: `[`Object`](https://docs.oracle.com/javase/6/docs/api/java/lang/Object.html) |
| [geoIpv6File](geo-ipv6-file.md) | `val geoIpv6File: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [geoIpv6FileLock](geo-ipv6-file-lock.md) | `val geoIpv6FileLock: `[`Object`](https://docs.oracle.com/javase/6/docs/api/java/lang/Object.html) |
| [hiddenServiceDir](hidden-service-dir.md) | `val hiddenServiceDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [hostnameFile](hostname-file.md) | The &lt;base32-encoded-fingerprint&gt;.onion domain name for this hidden service. If the hidden service is restricted to authorized clients only, this file also contains authorization data for all clients.`val hostnameFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [hostnameFileLock](hostname-file-lock.md) | `val hostnameFileLock: `[`Object`](https://docs.oracle.com/javase/6/docs/api/java/lang/Object.html) |
| [installDir](install-dir.md) | `val installDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [libraryPath](library-path.md) | `val libraryPath: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`?` |
| [resolvConfFileLock](resolv-conf-file-lock.md) | `val resolvConfFileLock: `[`Object`](https://docs.oracle.com/javase/6/docs/api/java/lang/Object.html) |
| [resolveConf](resolve-conf.md) | `val resolveConf: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [torExecutableFile](tor-executable-file.md) | `val torExecutableFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [torrcFile](torrc-file.md) | `var torrcFile: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [torrcFileLock](torrc-file-lock.md) | `val torrcFileLock: `[`Object`](https://docs.oracle.com/javase/6/docs/api/java/lang/Object.html) |

### Functions

| Name | Summary |
|---|---|
| [resolveTorrcFile](resolve-torrc-file.md) | Resolves the tor configuration file. If the torrc file hasn't been set, then this method will attempt to resolve the config file by looking in the root of the $configDir and then in $user.home directory`fun resolveTorrcFile(): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [createConfig](create-config.md) | Convenience method for if you're including in your App's jniLibs directory the `libTor.so` binary, or utilizing those maintained by this project.`fun createConfig(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, configDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`, dataDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`? = null): `[`TorConfigFiles`](./index.md)<br>Convenience method for setting up all of your files and directories in their default locations.`fun createConfig(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`TorConfigFiles`](./index.md) |
