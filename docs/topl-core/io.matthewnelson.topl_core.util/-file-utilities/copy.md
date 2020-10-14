[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [FileUtilities](index.md) / [copy](./copy.md)

# copy

`@JvmStatic fun copy(in: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`, out: `[`OutputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/FileUtilities.kt#L183)

Closes both input and output streams when done.

### Parameters

`in` - Stream to read from

`out` - Stream to write to

### Exceptions

`java.io.IOException` -
* If close on input or output fails
