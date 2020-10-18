[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [TorInstaller](./index.md)

# TorInstaller

`abstract class TorInstaller : `[`CoreConsts`](../-core-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/TorInstaller.kt#L110)

Extend this class and implement the need methods.

[setup](setup.md) is called from [io.matthewnelson.topl_core.OnionProxyManager.setup](../../io.matthewnelson.topl_core/-onion-proxy-manager/setup.md) after
instantiation, and [openBridgesStream](open-bridges-stream.md) is called from
[io.matthewnelson.topl_core.settings.TorSettingsBuilder.addBridgesFromResources](../../io.matthewnelson.topl_core.settings/-tor-settings-builder/add-bridges-from-resources.md)
when configuring bridge support.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Extend this class and implement the need methods.`TorInstaller()` |

### Properties

| Name | Summary |
|---|---|
| [broadcastLogger](broadcast-logger.md) | This gets set as soon as [io.matthewnelson.topl_core.OnionProxyManager](../../io.matthewnelson.topl_core/-onion-proxy-manager/index.md) is instantiated, and can be used to broadcast messages in your class which extends [TorInstaller](./index.md).`var broadcastLogger: `[`BroadcastLogger`](../../io.matthewnelson.topl_core.broadcaster/-broadcast-logger/index.md)`?` |

### Functions

| Name | Summary |
|---|---|
| [getAssetOrResourceByName](get-asset-or-resource-by-name.md) | `fun getAssetOrResourceByName(fileName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`?` |
| [openBridgesStream](open-bridges-stream.md) | If first byte of stream is 0, then the following stream will have the form`abstract fun openBridgesStream(): `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`?` |
| [setup](setup.md) | Sets up and installs any files needed to run tor. If the tor files are already on the system this does not need to be invoked.`abstract fun setup(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [updateTorConfigCustom](update-tor-config-custom.md) | `abstract fun updateTorConfigCustom(content: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
