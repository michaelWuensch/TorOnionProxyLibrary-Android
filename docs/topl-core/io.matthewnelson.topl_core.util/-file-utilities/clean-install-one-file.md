[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [FileUtilities](index.md) / [cleanInstallOneFile](./clean-install-one-file.md)

# cleanInstallOneFile

`fun cleanInstallOneFile(readFrom: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`, fileToWriteTo: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/FileUtilities.kt#L188)

Reads the input stream, deletes fileToWriteTo if it exists and over writes it with the stream.

### Parameters

`readFrom` - Stream to read from

`fileToWriteTo` - File to write to

### Exceptions

`java.io.IOException` -
* If any of the file operations fail
