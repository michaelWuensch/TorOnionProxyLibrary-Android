[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [relayPort](./relay-port.md)

# relayPort

`abstract val relayPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L456)

TorBrowser and Orbot use 9001 by default. It may be wise to pick something
that won't conflict.

Adds to the torrc file "ORPort &lt;[relayPort](./relay-port.md)&gt;"

This only gets used if you declare the following settings set as:
[hasReachableAddress](has-reachable-address.md) false
[hasBridges](has-bridges.md) false
[isRelay](is-relay.md) true
[relayNickname](relay-nickname.md) "your nickname"
[relayPort](./relay-port.md) "auto", or a port between "1024" and "65535"

**Docs:** https://2019.www.torproject.org/docs/tor-manual.html.en#ORPort

See [BaseConsts.PortOption.DISABLED](../-base-consts/-port-option/-d-i-s-a-b-l-e-d.md)

