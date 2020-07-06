[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorConfigFiles](index.md) / [resolveTorrcFile](./resolve-torrc-file.md)

# resolveTorrcFile

`fun resolveTorrcFile(): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorConfigFiles.kt#L141)

Resolves the tor configuration file. If the torrc file hasn't been set, then
this method will attempt to resolve the config file by looking in the root of
the $configDir and then in $user.home directory

### Exceptions

`IOException` - If torrc file is not resolved.

`SecurityException` - Unauthorized access to file/directory.

**Return**
[torrcFile](torrc-file.md)

