[topl-core](../../index.md) / [io.matthewnelson.topl_core.settings](../index.md) / [TorSettingsBuilder](index.md) / [finishAndReturnString](./finish-and-return-string.md)

# finishAndReturnString

`fun finishAndReturnString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/settings/TorSettingsBuilder.kt#L143)

This returns what's in the [buffer](#) as a String and then clears it.
You still need to write the String to the
[io.matthewnelson.topl_core_base.TorConfigFiles.torrcFile](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/torrc-file.md).

