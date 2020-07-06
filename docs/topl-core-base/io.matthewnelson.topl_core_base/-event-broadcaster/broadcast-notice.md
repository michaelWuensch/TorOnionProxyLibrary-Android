[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [EventBroadcaster](index.md) / [broadcastNotice](./broadcast-notice.md)

# broadcastNotice

`abstract fun broadcastNotice(msg: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/EventBroadcaster.kt#L84)

Will be one of:

* ("ERROR|ClassName|msg")
* ("NOTICE|ClassName|msg")
* ("WARN|ClassName|msg")
