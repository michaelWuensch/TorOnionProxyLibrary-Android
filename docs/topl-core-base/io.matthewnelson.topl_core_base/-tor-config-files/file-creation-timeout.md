[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [TorConfigFiles](index.md) / [fileCreationTimeout](./file-creation-timeout.md)

# fileCreationTimeout

`val fileCreationTimeout: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorConfigFiles.kt#L96)

When tor starts it waits for the control port and cookie auth files to be created
before it proceeds to the next step in startup. If these files are not created
after a certain amount of time, then the startup has failed.

This method returns how much time to wait in seconds until failing the startup.

