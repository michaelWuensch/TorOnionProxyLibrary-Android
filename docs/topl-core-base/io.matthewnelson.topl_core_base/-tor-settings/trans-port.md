[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [transPort](./trans-port.md)

# transPort

`abstract val transPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L328)

Can be "auto", or a specified port such as "9141"

See [listOfSupportedBridges](list-of-supported-bridges.md) documentation.

Orbot and TorBrowser default to "9140". It may be wise to pick something
that won't conflict.

See [DEFAULT__TRANS_PORT](-d-e-f-a-u-l-t__-t-r-a-n-s_-p-o-r-t.md)

TODO: Change to a List? to support multiple ports

