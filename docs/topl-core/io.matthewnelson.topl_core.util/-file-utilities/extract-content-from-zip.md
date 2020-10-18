[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [FileUtilities](index.md) / [extractContentFromZip](./extract-content-from-zip.md)

# extractContentFromZip

`@JvmStatic fun extractContentFromZip(destinationDirectory: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`, zipFileInputStream: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/FileUtilities.kt#L260)

This has to exist somewhere! Why isn't it a part of the standard Java library?

### Parameters

`destinationDirectory` - Directory files are to be extracted to

`zipFileInputStream` - Stream to unzip

### Exceptions

`java.io.IOException` -
* If there are any file errors
