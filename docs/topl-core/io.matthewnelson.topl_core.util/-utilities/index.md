[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [Utilities](./index.md)

# Utilities

`object Utilities` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/Utilities.kt#L104)

### Functions

| Name | Summary |
|---|---|
| [socks4aSocketConnection](socks4a-socket-connection.md) | When making a request via the Tor Proxy one needs to establish the socket using SOCKS4a. However Android only supports SOCKS4 so this class provides a wrapper when getting a socket to handle things.`fun socks4aSocketConnection(networkHost: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, networkPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, socksHost: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, socksPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Socket`](https://docs.oracle.com/javase/6/docs/api/java/net/Socket.html) |
