[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [FileUtilities](./index.md)

# FileUtilities

`object FileUtilities` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/FileUtilities.kt#L148)

### Functions

| Name | Summary |
|---|---|
| [cleanInstallOneFile](clean-install-one-file.md) | Reads the input stream, deletes fileToWriteTo if it exists and over writes it with the stream.`fun cleanInstallOneFile(readFrom: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`, fileToWriteTo: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [copy](copy.md) | Closes both input and output streams when done.`fun copy(in: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`, out: `[`OutputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [copyDoNotCloseInput](copy-do-not-close-input.md) | Won't close the input stream when it's done, needed to handle ZipInputStreams`fun copyDoNotCloseInput(in: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`, out: `[`OutputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/OutputStream.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [extractContentFromZip](extract-content-from-zip.md) | This has to exist somewhere! Why isn't it a part of the standard Java library?`fun extractContentFromZip(destinationDirectory: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`, zipFileInputStream: `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [listFilesToLog](list-files-to-log.md) | `fun listFilesToLog(f: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [recursiveFileDelete](recursive-file-delete.md) | `fun recursiveFileDelete(fileOrDirectory: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setPerms](set-perms.md) | Sets readable/executable for all users and writable by owner`fun setPerms(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setToReadOnlyPermissions](set-to-read-only-permissions.md) | `fun setToReadOnlyPermissions(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
