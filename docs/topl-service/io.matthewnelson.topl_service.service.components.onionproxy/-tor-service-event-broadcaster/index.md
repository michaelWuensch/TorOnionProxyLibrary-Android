[topl-service](../../index.md) / [io.matthewnelson.topl_service.service.components.onionproxy](../index.md) / [TorServiceEventBroadcaster](./index.md)

# TorServiceEventBroadcaster

`abstract class TorServiceEventBroadcaster : `[`EventBroadcaster`](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-service/src/main/java/io/matthewnelson/topl_service/service/components/onionproxy/TorServiceEventBroadcaster.kt#L85)

Adds broadcasting methods to the [EventBroadcaster](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) to update you with information about
what addresses Tor is operating on. Very helpful when choosing "auto" in your
[io.matthewnelson.topl_core_base.TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) to easily identifying what addresses to
use for making network calls, as well as being notified when Tor is ready to be used.

The addresses will be broadcast to you after Tor has been fully Bootstrapped. If Tor is
stopped (ie. it's [io.matthewnelson.topl_core_base.BaseConsts.TorState](../../../topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-tor-state/index.md) changes from **ON**
to **OFF**), `null` will be broadcast.

All broadcasts to your implementation to this class will occur on the Main thread.

``` kotlin
/**
 * @suppress
 * @see [TorServiceEventBroadcaster]
 * @see [io.matthewnelson.topl_core_base.EventBroadcaster]
 * */
class MyEventBroadcaster: TorServiceEventBroadcaster() {


    //////////////////////////
    /// ControlPortAddress ///
    //////////////////////////

    // Make Volatile so that if referencing on a different thread, we get
    // the update immediately
    @Volatile
    var controlPortAddress: String? = null
        private set

    override fun broadcastControlPortAddress(controlPortAddress: String?) {
        this.controlPortAddress = controlPortAddress
    }


    ////////////////////////
    /// SocksPortAddress ///
    ////////////////////////

    // Make Volatile so that if referencing on a different thread, we get
    // the update immediately
    @Volatile
    var socksPortAddress: String? = null
        private set

    override fun broadcastSocksPortAddress(socksPortAddress: String?) {
        this.socksPortAddress = socksPortAddress
    }


    ///////////////////////
    /// HttpPortAddress ///
    ///////////////////////

    // Make Volatile so that if referencing on a different thread, we get
    // the update immediately
    @Volatile
    var httpPortAddress: String? = null
        private set

    override fun broadcastHttpPortAddress(httpPortAddress: String?) {
        this.httpPortAddress = httpPortAddress
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

    private var lastState = BaseConsts.TorState.OFF
    private var lastNetworkState = BaseConsts.TorNetworkState.DISABLED

    private val _liveTorState = MutableLiveData<TorStateData>(
        TorStateData(lastState, lastNetworkState)
    )
    val liveTorState: LiveData<TorStateData> = _liveTorState

    override fun broadcastTorState(@BaseConsts.TorState state: String, @BaseConsts.TorNetworkState networkState: String) {
        if (state == lastState && networkState == lastNetworkState) return

        lastState = state
        lastNetworkState = networkState
        _liveTorState.value = TorStateData(state, networkState)
    }
}
```

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Adds broadcasting methods to the [EventBroadcaster](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) to update you with information about what addresses Tor is operating on. Very helpful when choosing "auto" in your [io.matthewnelson.topl_core_base.TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) to easily identifying what addresses to use for making network calls, as well as being notified when Tor is ready to be used.`TorServiceEventBroadcaster()` |

### Functions

| Name | Summary |
|---|---|
| [broadcastControlPortAddress](broadcast-control-port-address.md) | Override this method to implement receiving of the control port address that Tor is operating on.`abstract fun broadcastControlPortAddress(controlPortAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [broadcastHttpPortAddress](broadcast-http-port-address.md) | Override this method to implement receiving of the http port address that Tor is operating on (if you've specified a [io.matthewnelson.topl_core_base.TorSettings.httpTunnelPort](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/http-tunnel-port.md)).`abstract fun broadcastHttpPortAddress(httpPortAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [broadcastSocksPortAddress](broadcast-socks-port-address.md) | Override this method to implement receiving of the Socks port address that Tor is operating on (if you've specified a [io.matthewnelson.topl_core_base.TorSettings.socksPort](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/socks-port.md)).`abstract fun broadcastSocksPortAddress(socksPortAddress: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
