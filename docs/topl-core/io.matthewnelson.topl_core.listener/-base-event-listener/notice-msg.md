[topl-core](../../index.md) / [io.matthewnelson.topl_core.listener](../index.md) / [BaseEventListener](index.md) / [noticeMsg](./notice-msg.md)

# noticeMsg

`open fun noticeMsg(data: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/listener/BaseEventListener.kt#L133)

Requires that when you extend this class and override [noticeMsg](./notice-msg.md), you **must**
use `super.noticeMsg(data)` within your overridden method; otherwise, [noticeMsgBuffer](#) and [doesNoticeMsgBufferContain](#) will not work correctly.

