[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [TorServiceEventBroadcaster](index.md) / [broadcastSocksPortAddress](./broadcast-socks-port-address.md)

# broadcastSocksPortAddress

`abstract fun broadcastSocksPortAddress(socksPortAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/TorServiceEventBroadcaster.kt#L106)

Override this method to implement receiving of the Socks port address that Tor
is operating on (if you've specified a
[io.matthewnelson.topl_core_base.TorSettings.socksPort](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/socks-port.md)).

Example of what will be broadcast:

* "127.0.0.1:9051"
