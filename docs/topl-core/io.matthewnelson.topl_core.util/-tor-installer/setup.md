[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [TorInstaller](index.md) / [setup](./setup.md)

# setup

`abstract fun setup(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/TorInstaller.kt#L81)

Sets up and installs any files needed to run tor. If the tor files are already on
the system this does not need to be invoked.

``` kotlin
if (!torConfigFiles.geoIpFile.exists()) {
    copyAsset(geoIpAssetPath, torConfigFiles.geoIpFile)
    geoIpFileCopied = ""
}
if (!torConfigFiles.geoIpv6File.exists()) {
    copyAsset(geoIp6AssetPath, torConfigFiles.geoIpv6File)
    geoIpv6FileCopied = ""
}

// If the app version has been increased, or if this is a debug build, copy over
// geoip assets then update SharedPreferences with the new version code. This
// mitigates copying to be done only if a version upgrade is had.
if (buildConfigDebug || buildConfigVersionCode > localPrefs.getInt(APP_VERSION_CODE, -1)) {
    if (!::geoIpFileCopied.isInitialized) {
        copyAsset(geoIpAssetPath, torConfigFiles.geoIpFile)
    }
    if (!::geoIpv6FileCopied.isInitialized) {
        copyAsset(geoIp6AssetPath, torConfigFiles.geoIpv6File)
    }
    localPrefs.edit().putInt(APP_VERSION_CODE, buildConfigVersionCode).apply()
}
```

**Return**
true if tor installation is successful, otherwise false.

