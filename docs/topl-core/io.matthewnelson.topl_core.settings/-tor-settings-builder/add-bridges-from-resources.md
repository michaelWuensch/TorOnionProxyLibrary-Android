[topl-core](../../index.md) / [io.matthewnelson.topl_core.settings](../index.md) / [TorSettingsBuilder](index.md) / [addBridgesFromResources](./add-bridges-from-resources.md)

# addBridgesFromResources

`fun addBridgesFromResources(): `[`TorSettingsBuilder`](index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/settings/TorSettingsBuilder.kt#L898)

Adds bridges from a resource stream. This relies on the
[io.matthewnelson.topl_core.util.TorInstaller](../../io.matthewnelson.topl_core.util/-tor-installer/index.md) to know how to obtain this stream.
These entries may be type-specified like:

`obfs3 169.229.59.74:31493 AF9F66B7B04F8FF6F32D455F05135250A16543C9`

Or it may just be a custom entry like

`69.163.45.129:443 9F090DE98CA6F67DEEB1F87EFE7C1BFD884E6E2F`

See [io.matthewnelson.topl_core.util.TorInstaller](../../io.matthewnelson.topl_core.util/-tor-installer/index.md) comment for further details
on how to implement that.

TODO: Re-work format type to use annotations...

