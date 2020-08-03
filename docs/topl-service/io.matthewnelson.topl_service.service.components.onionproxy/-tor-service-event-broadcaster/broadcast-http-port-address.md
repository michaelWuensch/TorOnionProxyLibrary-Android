[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [TorServiceEventBroadcaster](index.md) / [broadcastHttpPortAddress](./broadcast-http-port-address.md)

# broadcastHttpPortAddress

`abstract fun broadcastHttpPortAddress(httpPortAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/TorServiceEventBroadcaster.kt#L117)

Override this method to implement receiving of the http port address that Tor
is operating on (if you've specified a
[io.matthewnelson.topl_core_base.TorSettings.httpTunnelPort](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/http-tunnel-port.md)).

Example of what will be broadcast:

* "127.0.0.1:33432"
