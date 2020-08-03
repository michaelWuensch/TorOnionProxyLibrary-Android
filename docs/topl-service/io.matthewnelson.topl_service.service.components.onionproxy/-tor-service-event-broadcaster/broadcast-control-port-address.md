[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [TorServiceEventBroadcaster](index.md) / [broadcastControlPortAddress](./broadcast-control-port-address.md)

# broadcastControlPortAddress

`abstract fun broadcastControlPortAddress(controlPortAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/TorServiceEventBroadcaster.kt#L95)

Override this method to implement receiving of the control port address that Tor
is operating on.

Example of what will be broadcast:

* "127.0.0.1:33432"
