[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [proxySocks5ServerPort](./proxy-socks5-server-port.md)

# proxySocks5ServerPort

`abstract val proxySocks5ServerPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L400)

Adds to the torrc file "Socks5Proxy [proxySocks5Host](proxy-socks5-host.md):[proxySocks5ServerPort](./proxy-socks5-server-port.md)"

Default = [java.null](#)

Try ((Math.random() * 1000) + 10000).toInt()

