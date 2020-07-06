[topl-core](../../index.md) / [io.matthewnelson.topl_core](../index.md) / [OnionProxyManager](index.md) / [publishHiddenService](./publish-hidden-service.md)

# publishHiddenService

`@Synchronized fun publishHiddenService(hiddenServicePort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, localPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/OnionProxyManager.kt#L241)

Publishes a hidden service

### Parameters

`hiddenServicePort` - The port that the hidden service will accept connections on

`localPort` - The local port that the hidden service will relay connections to

### Exceptions

`IOException` - File errors

`RuntimeException` - See [io.matthewnelson.topl_core.util.WriteObserver.poll](../../io.matthewnelson.topl_core.util/-write-observer/poll.md)

`IllegalStateException` - If [controlConnection](#) is null (service isn't running)

`NullPointerException` - If [controlConnection](#) is null even after checking

`SecurityException` - Unauthorized access to file/directory.

`IllegalArgumentException` -

**Return**
The hidden service's onion address in the form X.onion.

