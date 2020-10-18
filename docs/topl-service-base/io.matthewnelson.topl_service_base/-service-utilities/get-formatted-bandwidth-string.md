[topl-service-base](../../index.md) / [io.matthewnelson.topl_service_base](../index.md) / [ServiceUtilities](index.md) / [getFormattedBandwidthString](./get-formatted-bandwidth-string.md)

# getFormattedBandwidthString

`@JvmStatic fun getFormattedBandwidthString(download: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, upload: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service-base/src/main/java/io/matthewnelson/topl_service_base/ServiceUtilities.kt#L87)

Formats the supplied values to look like: `20KBps ↓ / 85KBps ↑`

### Parameters

`download` - Long value associated with download (bytesRead)

`upload` - Long value associated with upload (bytesWritten)