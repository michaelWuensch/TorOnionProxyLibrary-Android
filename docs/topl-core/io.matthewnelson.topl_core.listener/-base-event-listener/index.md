[topl-core](../../index.md) / [io.matthewnelson.topl_core.listener](../index.md) / [BaseEventListener](./index.md)

# BaseEventListener

`abstract class BaseEventListener : EventListener` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/listener/BaseEventListener.kt#L78)

Extend this class to customize implementation of the member overrides.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Extend this class to customize implementation of the member overrides.`BaseEventListener()` |

### Properties

| Name | Summary |
|---|---|
| [broadcastLogger](broadcast-logger.md) | This gets set as soon as [io.matthewnelson.topl_core.OnionProxyManager](../../io.matthewnelson.topl_core/-onion-proxy-manager/index.md) is instantiated, and can be used to broadcast messages in your class which extends [TorInstaller](../../io.matthewnelson.topl_core.util/-tor-installer/index.md).`var broadcastLogger: `[`BroadcastLogger`](../../io.matthewnelson.topl_core.broadcaster/-broadcast-logger/index.md)`?` |
| [CONTROL_COMMAND_EVENTS](-c-o-n-t-r-o-l_-c-o-m-m-a-n-d_-e-v-e-n-t-s.md) | See [TorControlCommands.EVENT_NAMES](#) values. These are **REQUIRED** for registering them in [io.matthewnelson.topl_core.OnionProxyManager.start](../../io.matthewnelson.topl_core/-onion-proxy-manager/start.md) which allows you full control over what you wish to listen for.`abstract val CONTROL_COMMAND_EVENTS: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [noticeMsg](notice-msg.md) | Requires that when you extend this class and override [noticeMsg](notice-msg.md), you **must** use `super.noticeMsg(data)` within your overridden method; otherwise, [noticeMsgBuffer](#) and [doesNoticeMsgBufferContain](#) will not work correctly.`open fun noticeMsg(data: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
