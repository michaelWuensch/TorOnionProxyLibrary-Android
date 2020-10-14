[topl-core-base](../../index.md) / [io.matthewnelson.topl_core_base](../index.md) / [java.io.File](index.md) / [createNewFileIfDoesNotExist](./create-new-file-if-does-not-exist.md)

# createNewFileIfDoesNotExist

`fun `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`.createNewFileIfDoesNotExist(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core-base/src/main/java/io/matthewnelson/topl_core_base/FileExtensions.kt#L117)

Creates the file and the necessary parent directories if it does not exist. Be sure
to acquire the proper lock from
[io.matthewnelson.topl_core_base.TorConfigFiles](../-tor-config-files/index.md) when utilizing this method.

### Exceptions

`SecurityException` - Unauthorized access to file/directory

**Return**
`null` if the parent directories of that File could not be created, `false` if
the File was not able to be created, `true` if the file exists/was created.

