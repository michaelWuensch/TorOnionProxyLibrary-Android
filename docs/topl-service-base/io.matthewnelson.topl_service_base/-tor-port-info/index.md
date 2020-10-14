[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [TorPortInfo](./index.md)

# TorPortInfo

`class TorPortInfo` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/TorPortInfo.kt#L82)

Holder for information regarding what ports Tor is operating on that is broadcast
to the implementing application via [io.matthewnelson.topl_service_base.TorServiceEventBroadcaster](../-tor-service-event-broadcaster/index.md)

Example of what one of the fields will contain:

* "127.0.0.1:33432"

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Holder for information regarding what ports Tor is operating on that is broadcast to the implementing application via [io.matthewnelson.topl_service_base.TorServiceEventBroadcaster](../-tor-service-event-broadcaster/index.md)`TorPortInfo(controlPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, dnsPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, httpPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, socksPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, transPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)` |

### Properties

| Name | Summary |
|---|---|
| [controlPort](control-port.md) | `val controlPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [dnsPort](dns-port.md) | `val dnsPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [httpPort](http-port.md) | `val httpPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [socksPort](socks-port.md) | `val socksPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [transPort](trans-port.md) | `val transPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
