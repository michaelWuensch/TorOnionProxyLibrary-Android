[topl-core](../../index.md) / [io.matthewnelson.topl_core.util](../index.md) / [TorInstaller](index.md) / [openBridgesStream](./open-bridges-stream.md)

# openBridgesStream

`abstract fun openBridgesStream(): `[`InputStream`](https://docs.oracle.com/javase/6/docs/api/java/io/InputStream.html)`?` [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/util/TorInstaller.kt#L154)

If first byte of stream is 0, then the following stream will have the form

`($bridge_type $bridge_info \r\n)*`

if first byte is 1, the the stream will have the form

`($bridge_info \r\n)*`

The second form is used for custom bridges from the user.

``` kotlin
/*
    BridgesList is an overloaded field, which can cause some confusion.

    The list can be:
      1) a filter like obfs4, meek, or snowflake OR
      2) it can be a custom bridge

    For (1), we just pass back all bridges, the filter will occur
      elsewhere in the library.
    For (2) we return the bridge list as a raw stream.

    If length is greater than 9, then we know this is a custom bridge
* */
// TODO: Completely refactor how bridges work.
val userDefinedBridgeList: String =
    torServicePrefs.getList(PrefKeyList.USER_DEFINED_BRIDGES, arrayListOf()).joinToString()
var bridgeType = (if (userDefinedBridgeList.length > 9) 1 else 0).toByte()
// Terrible hack. Must keep in sync with topl::addBridgesFromResources.
if (bridgeType.toInt() == 0) {
    when (userDefinedBridgeList) {
        SupportedBridgeType.OBFS4 -> bridgeType = 2
        SupportedBridgeType.MEEK -> bridgeType = 3
        SupportedBridgeType.SNOWFLAKE -> bridgeType = 4
    }
}

val bridgeTypeStream = ByteArrayInputStream(byteArrayOf(bridgeType))
val bridgeStream =
    if (bridgeType.toInt() == 1) {
        ByteArrayInputStream(userDefinedBridgeList.toByteArray())
    } else {
        torService.context.resources.openRawResource(R.raw.bridges)
    }
return SequenceInputStream(bridgeTypeStream, bridgeStream)
```

