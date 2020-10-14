[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorPortInfo](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`TorPortInfo(controlPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, dnsPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, httpPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, socksPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, transPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)`

Holder for information regarding what ports Tor is operating on that is broadcast
to the implementing application via [io.matthewnelson.topl_service_base.TorServiceEventBroadcaster](../-tor-service-event-broadcaster/index.md)

Example of what one of the fields will contain:

* "127.0.0.1:33432"
