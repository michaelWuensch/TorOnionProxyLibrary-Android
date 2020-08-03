[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [TorInstaller](index.md) / [openBridgesStream](./open-bridges-stream.md)

# openBridgesStream

`abstract fun openBridgesStream(): `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/TorInstaller.kt#L149)

If first byte of stream is 0, then the following stream will have the form

`($bridge_type $bridge_info \r\n)*`

if first byte is 1, the the stream will have the form

`($bridge_info \r\n)*`

The second form is used for custom bridges from the user.

``` kotlin
//Unresolved: io.matthewnelson.topl_service.onionproxy.ServiceTorInstaller.openBridgesStream
```

