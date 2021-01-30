[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [BaseV3ClientAuthManager](index.md) / [addV3ClientAuthenticationPrivateKey](./add-v3-client-authentication-private-key.md)

# addV3ClientAuthenticationPrivateKey

`@WorkerThread abstract fun addV3ClientAuthenticationPrivateKey(nickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, onionAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, base32EncodedPrivateKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/BaseV3ClientAuthManager.kt#L119)

Creates a file containing v3 Client Authorization for a Hidden Service in the format of:

* Filename: [nickname](add-v3-client-authentication-private-key.md#io.matthewnelson.topl_service_base.BaseV3ClientAuthManager$addV3ClientAuthenticationPrivateKey(kotlin.String, kotlin.String, kotlin.String)/nickname).auth_private
* File Contents:  (56-char-onion-addr-**without**.onion):descriptor:x25519:(x25519 private key in base32)

Exceptions are thrown for you with adequate messages if the values passed
are non-compliant.

**Docs:** https://2019.www.torproject.org/docs/tor-onion-service.html.en#ClientAuthorization

### Parameters

`nickname` - The nickname for the file. Is appended with `.auth_private` and used as the File name
[nickname](add-v3-client-authentication-private-key.md#io.matthewnelson.topl_service_base.BaseV3ClientAuthManager$addV3ClientAuthenticationPrivateKey(kotlin.String, kotlin.String, kotlin.String)/nickname) requirements are:

`onionAddress` - The .onion address for which this Private Key will exist for

`base32EncodedPrivateKey` - The private key for authenticating to the Hidden Service

### Exceptions

`IllegalArgumentException` - If passed arguments are not compliant with the spec

`IllegalStateException` - If the file already exists (and must be deleted before
overwriting), or if a file exists with the same onion address &amp; private key

`SecurityException` - If access is not authorized

**Return**
The File if it was created properly, `null` if it was not

