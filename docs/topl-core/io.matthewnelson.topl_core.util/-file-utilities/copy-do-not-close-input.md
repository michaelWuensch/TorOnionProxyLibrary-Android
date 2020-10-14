[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [FileUtilities](index.md) / [copyDoNotCloseInput](./copy-do-not-close-input.md)

# copyDoNotCloseInput

`@JvmStatic fun copyDoNotCloseInput(in: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`, out: `[`OutputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/FileUtilities.kt#L197)

Won't close the input stream when it's done, needed to handle ZipInputStreams

### Parameters

`in` - Won't be closed

`out` - Will be closed

### Exceptions

`java.io.IOException` - If close on output fails