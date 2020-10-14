[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [java.io.File](index.md) / [readTorConfigFile](./read-tor-config-file.md)

# readTorConfigFile

`fun `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`.readTorConfigFile(): `[`ByteArray`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/FileExtensions.kt#L88)

Reads a [File](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)

### Exceptions

`IOException` - File errors

`EOFException` - File errors

`SecurityException` - Unauthorized access to file/directory

**Return**
a [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) of the contents of the [File](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)

