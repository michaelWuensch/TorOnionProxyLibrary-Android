[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [dnsPort](./dns-port.md)

# dnsPort

`abstract val dnsPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L190)

TorBrowser and Orbot use "5400" by default. It may be wise to pick something
that won't conflict.

Disabled by default by Tor. Set to "O" to disable. Can also be "auto", or a specific
port between "1024" and "65535"

Adds to the torrc file "DNSPort  &lt;[dnsPortIsolationFlags](dns-port-isolation-flags.md)&gt;"

See [BaseConsts.PortOption.DISABLED](../-base-consts/-port-option/-d-i-s-a-b-l-e-d.md)

