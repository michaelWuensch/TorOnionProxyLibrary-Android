[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [CoreConsts](./index.md)

# CoreConsts

`abstract class CoreConsts : `[`BaseConsts`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/CoreConsts.kt#L72)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CoreConsts()` |

### Inheritors

| Name | Summary |
|---|---|
| [BroadcastLogger](../../io.matthewnelson.topl_core.broadcaster/-broadcast-logger/index.md) | This class is for standardizing broadcast messages across all classes for this Library. Debugging is important while hacking on TOPL-Android, but those Logcat messages should **never** make it to a release build.`class BroadcastLogger : `[`CoreConsts`](./index.md) |
| [OnionProxyManager](../../io.matthewnelson.topl_core/-onion-proxy-manager/index.md) | This is where all the fun is, this is the class which acts as a gateway into the `topl-core` module, and ensures synchronicity is had.`class OnionProxyManager : `[`CoreConsts`](./index.md) |
| [TorInstaller](../-tor-installer/index.md) | Extend this class and implement the need methods.`abstract class TorInstaller : `[`CoreConsts`](./index.md) |
| [TorSettingsBuilder](../../io.matthewnelson.topl_core.settings/-tor-settings-builder/index.md) | Call [io.matthewnelson.topl_core.OnionProxyManager.getNewSettingsBuilder](../../io.matthewnelson.topl_core/-onion-proxy-manager/get-new-settings-builder.md) to obtain this class.`class TorSettingsBuilder : `[`CoreConsts`](./index.md) |
| [TorStateMachine](../../io.matthewnelson.topl_core.broadcaster/-tor-state-machine/index.md) | Current State of Tor`class TorStateMachine : `[`CoreConsts`](./index.md) |
