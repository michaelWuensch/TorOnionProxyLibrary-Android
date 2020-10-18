[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseV3ClientAuthManager](./index.md)

# BaseV3ClientAuthManager

`abstract class BaseV3ClientAuthManager` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseV3ClientAuthManager.kt#L77)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `BaseV3ClientAuthManager()` |

### Functions

| Name | Summary |
|---|---|
| [addV3ClientAuthenticationPrivateKey](add-v3-client-authentication-private-key.md) | Creates a file containing v3 Client Authorization for a Hidden Service in the format of:`abstract fun addV3ClientAuthenticationPrivateKey(nickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, onionAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, base32EncodedPrivateKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`?` |
| [deleteFile](delete-file.md) | `abstract fun deleteFile(nickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?`<br>`abstract fun deleteFile(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`?` |
| [getAllFileNicknames](get-all-file-nicknames.md) | From the v3 Client Authentication directory, all files that contain the ".auth_private" extension will have their name w/o the extension returned in an array. If the directory is empty, returns `null`.`abstract fun getAllFileNicknames(): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>?` |
| [getAllFiles](get-all-files.md) | All files within the v3 Client Authentication directory are returned. If the directory is empty, returns `null`.`abstract fun getAllFiles(): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`>?` |
| [getFileByNickname](get-file-by-nickname.md) | Retrieve a v3 client authentication file by the nickname, whether the file extension ".auth_private" is included or not.`abstract fun getFileByNickname(nickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`?` |
| [getFileContent](get-file-content.md) | `abstract fun getFileContent(nickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`V3ClientAuthContent`](../-v3-client-auth-content/index.md)`?`<br>`abstract fun getFileContent(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`): `[`V3ClientAuthContent`](../-v3-client-auth-content/index.md)`?` |
