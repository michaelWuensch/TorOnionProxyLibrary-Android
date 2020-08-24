[topl-core-base](../../../index.md) / [io.matthewnelson.topl_core_base](../../index.md) / [TorConfigFiles](../index.md) / [Builder](index.md) / [hiddenServiceDir](./hidden-service-dir.md)

# hiddenServiceDir

`fun hiddenServiceDir(directory: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorConfigFiles.kt#L287)

Store data files for a hidden service in DIRECTORY. Every hidden service must
have a separate directory. You may use this option multiple times to specify
multiple services. If DIRECTORY does not exist, Tor will create it. (Note: in
current versions of Tor, if DIRECTORY is a relative path, it will be relative
to the current working directory of Tor instance, not to its DataDirectory. Do
not rely on this behavior; it is not guaranteed to remain the same in future
versions.)

Default value: $configDir/hiddenservices

### Parameters

`directory` - hidden services directory

**Return**
[Builder](index.md)

