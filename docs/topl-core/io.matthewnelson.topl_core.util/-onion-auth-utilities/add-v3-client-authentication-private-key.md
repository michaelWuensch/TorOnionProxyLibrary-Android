[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [OnionAuthUtilities](index.md) / [addV3ClientAuthenticationPrivateKey](./add-v3-client-authentication-private-key.md)

# addV3ClientAuthenticationPrivateKey

`@WorkerThread @JvmStatic fun addV3ClientAuthenticationPrivateKey(nickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, onionAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, base32EncodedPrivateKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, torConfigFiles: `[`TorConfigFiles`](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`): `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/OnionAuthUtilities.kt#L124)

Creates a file containing v3 Client Authorization for a Hidden Service in the format of:

* Filename: [nickname](add-v3-client-authentication-private-key.md#io.matthewnelson.topl_core.util.OnionAuthUtilities$addV3ClientAuthenticationPrivateKey(kotlin.String, kotlin.String, kotlin.String, io.matthewnelson.topl_core_base.TorConfigFiles)/nickname).auth_private
* File Contents:  &lt;56-char-onion-addr-without-.onion-part&gt;:descriptor:x25519:

Exceptions are thrown for you with adequate messages if the values passed
are non-compliant.

**Docs:** https://2019.www.torproject.org/docs/tor-onion-service.html.en#ClientAuthorization

### Parameters

`nickname` - The nickname for the file. Is appended with `.auth_private` and used as the File name
[nickname](add-v3-client-authentication-private-key.md#io.matthewnelson.topl_core.util.OnionAuthUtilities$addV3ClientAuthenticationPrivateKey(kotlin.String, kotlin.String, kotlin.String, io.matthewnelson.topl_core_base.TorConfigFiles)/nickname) requirements are:

`onionAddress` - The .onion address for which this Private Key will exist for

`base32EncodedPrivateKey` - The private key for authenticating to the Hidden Service

### Exceptions

`IllegalArgumentException` - If passed arguments are not compliant with the spec

`IllegalStateException` - If the file already exists (and must be deleted before
overwriting), or if a file exists with the same onion address &amp; private key

`SecurityException` - If access is not authorized

**Return**
The File if it was created properly, `null` if it was not

