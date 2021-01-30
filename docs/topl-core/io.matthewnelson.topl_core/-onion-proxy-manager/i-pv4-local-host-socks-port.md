[topl-core](../../index.md) / [io.matthewnelson.topl_core](../index.md) / [OnionProxyManager](index.md) / [iPv4LocalHostSocksPort](./i-pv4-local-host-socks-port.md)

# iPv4LocalHostSocksPort

`val iPv4LocalHostSocksPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/OnionProxyManager.kt#L260)

Returns the socks port on the IPv4 localhost address that the Tor OP is listening on

### Exceptions

`IOException` - TorControlConnection or File errors.

`RuntimeException` - If Tor is not running or there's no localhost binding for Socks.

`NullPointerException` - If [controlConnection](#) is null even after checking.

**Return**
Discovered socks port

