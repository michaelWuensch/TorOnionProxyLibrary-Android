[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy.model](../index.md) / [TorServiceEventBroadcaster](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`TorServiceEventBroadcaster()`

Adds broadcasting methods to the [EventBroadcaster](../../..//topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) to update you with information about
what addresses Tor is operating on. Very helpful when choosing "auto" in your
[io.matthewnelson.topl_core_base.TorSettings](../../..//topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) to easily identify what addresses to
use for making network calls, as well as being notified when Tor is ready to be used.

The addresses will be broadcast to you after Tor has been fully Bootstrapped. If Tor is
stopped (ie. it's [io.matthewnelson.topl_core_base.BaseConsts.TorState](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-tor-state/index.md) changes from **ON**
to **OFF**), a [TorPortInfo](../-tor-port-info/index.md) object containing 'null' for all fields will be broadcast.

All broadcasts to your implementation to this class will occur on the Main Thread.

``` kotlin
/**
 * @suppress
 * @see [TorServiceEventBroadcaster]
 * @see [io.matthewnelson.topl_core_base.EventBroadcaster]
 * */
class MyEventBroadcaster: TorServiceEventBroadcaster() {


    ///////////////////
    /// TorPortInfo ///
    ///////////////////
    private val _liveTorPortInfo = MutableLiveData<TorPortInfo>(null)
    val liveTorPortInfo: LiveData<TorPortInfo> = _liveTorPortInfo

    override fun broadcastPortInformation(torPortInfo: TorPortInfo) {
        _liveTorPortInfo.value = torPortInfo
    }


    /////////////////
    /// Bandwidth ///
    /////////////////
    private var lastDownload = "0"
    private var lastUpload = "0"

    private val _liveBandwidth = MutableLiveData<String>(
        ServiceUtilities.getFormattedBandwidthString(0L, 0L)
    )
    val liveBandwidth: LiveData<String> = _liveBandwidth

    override fun broadcastBandwidth(bytesRead: String, bytesWritten: String) {
        if (bytesRead == lastDownload && bytesWritten == lastUpload) return

        lastDownload = bytesRead
        lastUpload = bytesWritten
        if (!liveBandwidth.hasActiveObservers()) return

        _liveBandwidth.value = ServiceUtilities.getFormattedBandwidthString(
            bytesRead.toLong(), bytesWritten.toLong()
        )
    }

    override fun broadcastDebug(msg: String) {
        broadcastLogMessage(msg)
    }

    override fun broadcastException(msg: String?, e: Exception) {
        if (msg.isNullOrEmpty()) return

        broadcastLogMessage(msg)
        e.printStackTrace()
    }


    ////////////////////
    /// Log Messages ///
    ////////////////////
    override fun broadcastLogMessage(logMessage: String?) {
        if (logMessage.isNullOrEmpty()) return

        val splitMsg = logMessage.split("|")
        if (splitMsg.size < 3) return

        LogMessageAdapter.addLogMessageNotifyAndCurate(
            "${splitMsg[0]} | ${splitMsg[1]} | ${splitMsg[2]}"
        )

    }

    override fun broadcastNotice(msg: String) {
        broadcastLogMessage(msg)
    }


    ///////////////////
    /// Tor's State ///
    ///////////////////
    inner class TorStateData(val state: String, val networkState: String)

    private var lastState = TorState.OFF
    private var lastNetworkState = TorNetworkState.DISABLED

    private val _liveTorState = MutableLiveData<TorStateData>(
        TorStateData(lastState, lastNetworkState)
    )
    val liveTorState: LiveData<TorStateData> = _liveTorState

    override fun broadcastTorState(@TorState state: String, @TorNetworkState networkState: String) {
        if (state == lastState && networkState == lastNetworkState) return

        lastState = state
        lastNetworkState = networkState
        _liveTorState.value = TorStateData(state, networkState)
    }
}
```

