[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorSettings](index.md) / [socksPort](./socks-port.md)

# socksPort

`abstract val socksPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorSettings.kt#L472)

Could be "auto" or a specific port, such as "9051".

TorBrowser uses "9150", and Orbot uses "9050" by default. It may be wise
to pick something that won't conflict.

See [BaseConsts.PortOption](../-base-consts/-port-option/index.md)

