[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [transPort](./trans-port.md)

# transPort

`abstract val transPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L493)

Can be "auto", or a specified port such as "9141"

See [listOfSupportedBridges](list-of-supported-bridges.md) documentation.

Orbot and TorBrowser default to "9140" and "9040" respectively. It may be wise to pick
something that won't conflict.

See [BaseConsts.PortOption.DISABLED](../-base-consts/-port-option/-d-i-s-a-b-l-e-d.md)

