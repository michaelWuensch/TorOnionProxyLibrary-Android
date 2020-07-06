[topl-service](../../index.md) / [io.matthewnelson.topl_service.util](../index.md) / [ServiceUtilities](index.md) / [getFormattedBandwidthString](./get-formatted-bandwidth-string.md)

# getFormattedBandwidthString

`fun getFormattedBandwidthString(download: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, upload: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/util/ServiceUtilities.kt#L30)

Formats the supplied values to look like: `20kbps ↓ / 85kbps ↑`

### Parameters

`download` - Long value associated with download (bytesRead)

`upload` - Long value associated with upload (bytesWritten)