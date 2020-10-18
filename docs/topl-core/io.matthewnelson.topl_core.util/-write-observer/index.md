[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [WriteObserver](./index.md)

# WriteObserver

`class WriteObserver : `[`FileObserver`](https://developer.android.com/reference/android/os/FileObserver.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/WriteObserver.kt#L106)

Adapted from the Briar WriteObserver code

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Adapted from the Briar WriteObserver code`WriteObserver(file: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`)` |

### Functions

| Name | Summary |
|---|---|
| [onEvent](on-event.md) | `fun onEvent(i: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, s: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [poll](poll.md) | `fun poll(timeout: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, unit: `[`TimeUnit`](https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/TimeUnit.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
