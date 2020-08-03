[topl-core-base](../../../index.md) / [io.matthewnelson.topl_core_base](../../index.md) / [TorConfigFiles](../index.md) / [Builder](index.md) / [fileCreationTimeout](./file-creation-timeout.md)

# fileCreationTimeout

`fun fileCreationTimeout(timeoutSeconds: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): Builder` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/TorConfigFiles.kt#L378)

When tor starts it waits for the control port and cookie auth files to be
created before it proceeds to the next step in startup. If these files are
not created after a certain amount of time, then the startup has failed.

This method specifies how much time to wait until failing the startup.

Default value is 15 seconds

### Parameters

`timeoutSeconds` - Int

**Return**
[Builder](index.md)

