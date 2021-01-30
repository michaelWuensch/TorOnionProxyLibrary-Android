[topl-core](../../index.md) / [io.matthewnelson.topl_core.settings](../index.md) / [TorSettingsBuilder](index.md) / [setV3AuthPrivateDir](./set-v3-auth-private-dir.md)

# setV3AuthPrivateDir

`fun setV3AuthPrivateDir(): `[`TorSettingsBuilder`](index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/settings/TorSettingsBuilder.kt#L730)

Will add to the torrc file "ClientOnionAuthDir &lt;/data/data/path/to/directory&gt;, so
be sure to create the directory if it does not exist in [TorInstaller.setup](../../io.matthewnelson.topl_core.util/-tor-installer/setup.md) prior
to utilizing this method when building your torrc file.

