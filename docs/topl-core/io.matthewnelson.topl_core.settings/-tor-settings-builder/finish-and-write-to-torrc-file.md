[topl-core](../../index.md) / [io.matthewnelson.topl_core.settings](../index.md) / [TorSettingsBuilder](index.md) / [finishAndWriteToTorrcFile](./finish-and-write-to-torrc-file.md)

# finishAndWriteToTorrcFile

`fun finishAndWriteToTorrcFile(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/settings/TorSettingsBuilder.kt#L171)

A convenience method for after populating the [buffer](#) by calling
[updateTorSettings](update-tor-settings.md). It will overwrite your current torrc file (or
create a new one if it doesn't exist) with the new settings.

TODO: Devise a more elegant solution using a diff to simply update it if
    need be.

