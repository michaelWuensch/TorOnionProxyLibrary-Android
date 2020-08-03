[topl-core](../../index.md) / [io.matthewnelson.topl_core](../index.md) / [OnionProxyManager](./index.md)

# OnionProxyManager

`class OnionProxyManager : `[`CoreConsts`](../../io.matthewnelson.topl_core.util/-core-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/OnionProxyManager.kt#L136)

This is where all the fun is, this is the class which acts as a gateway into the `topl-core`
module, and ensures synchronicity is had.

This class is thread safe but that's mostly because we hit everything over the head
with 'synchronized'. Given the way this class is used there shouldn't be any performance
implications of this.

This class began life as TorPlugin from the Briar Project

``` kotlin
//Unresolved: io.matthewnelson.topl_service.service.TorService.initTOPLCore
```

### Parameters

`context` - Context.

`torConfigFiles` - [TorConfigFiles](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) For setting up [OnionProxyContext](#)

`torInstaller` - [TorInstaller](../../io.matthewnelson.topl_core.util/-tor-installer/index.md) For setting up [OnionProxyContext](#)

`torSettings` - [TorSettings](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) For setting up [OnionProxyContext](#)

`eventListener` - [BaseEventListener](../../io.matthewnelson.topl_core.listener/-base-event-listener/index.md) For processing Tor OP messages.

`eventBroadcaster` - Your own broadcaster which extends [EventBroadcaster](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md)

`buildConfigDebug` - Send [BuildConfig.DEBUG](#) which will show Logcat messages for this
module on Debug builds of your Application. If `null`, all the messages will still be
broadcast to the provided [EventBroadcaster](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md) and you can handle them there how you'd like.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | This is where all the fun is, this is the class which acts as a gateway into the `topl-core` module, and ensures synchronicity is had.`OnionProxyManager(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, torConfigFiles: `[`TorConfigFiles`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`, torInstaller: `[`TorInstaller`](../../io.matthewnelson.topl_core.util/-tor-installer/index.md)`, torSettings: `[`TorSettings`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md)`, eventListener: `[`BaseEventListener`](../../io.matthewnelson.topl_core.listener/-base-event-listener/index.md)`, eventBroadcaster: `[`EventBroadcaster`](../../../topl-core-base/io.matthewnelson.topl_core_base/-event-broadcaster/index.md)`, buildConfigDebug: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`? = null)` |

### Properties

| Name | Summary |
|---|---|
| [hasControlConnection](has-control-connection.md) | `val hasControlConnection: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iPv4LocalHostSocksPort](i-pv4-local-host-socks-port.md) | Returns the socks port on the IPv4 localhost address that the Tor OP is listening on`val iPv4LocalHostSocksPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isIPv4LocalHostSocksPortOpen](is-i-pv4-local-host-socks-port-open.md) | `val isIPv4LocalHostSocksPortOpen: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isRunning](is-running.md) | Checks to see if the Tor OP is running (e.g. fully bootstrapped) and open to network connections.`val isRunning: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [processId](process-id.md) | `val processId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [torConfigFiles](tor-config-files.md) | `val torConfigFiles: `[`TorConfigFiles`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md) |
| [torInstaller](tor-installer.md) | `val torInstaller: `[`TorInstaller`](../../io.matthewnelson.topl_core.util/-tor-installer/index.md) |
| [torPid](tor-pid.md) | `val torPid: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [torSettings](tor-settings.md) | `val torSettings: `[`TorSettings`](../../../topl-core-base/io.matthewnelson.topl_core_base/-tor-settings/index.md) |
| [torStateMachine](tor-state-machine.md) | `val torStateMachine: `[`TorStateMachine`](../../io.matthewnelson.topl_core.broadcaster/-tor-state-machine/index.md) |

### Functions

| Name | Summary |
|---|---|
| [disableNetwork](disable-network.md) | Tells the Tor OP if it should accept network connections.`fun disableNetwork(disable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getBroadcastLogger](get-broadcast-logger.md) | See [BroadcastLoggerHelper.getBroadcastLogger](#)`fun getBroadcastLogger(clazz: `[`Class`](https://docs.oracle.com/javase/6/docs/api/java/lang/Class.html)`<*>): `[`BroadcastLogger`](../../io.matthewnelson.topl_core.broadcaster/-broadcast-logger/index.md)<br>`fun getBroadcastLogger(tagName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`BroadcastLogger`](../../io.matthewnelson.topl_core.broadcaster/-broadcast-logger/index.md) |
| [getInfo](get-info.md) | See the torspec for accepted queries:`fun getInfo(queryCommand: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [getNewSettingsBuilder](get-new-settings-builder.md) | `fun getNewSettingsBuilder(): `[`TorSettingsBuilder`](../../io.matthewnelson.topl_core.settings/-tor-settings-builder/index.md) |
| [killTorProcess](kill-tor-process.md) | `fun killTorProcess(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [publishHiddenService](publish-hidden-service.md) | Publishes a hidden service`fun publishHiddenService(hiddenServicePort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, localPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [refreshBroadcastLoggersHasDebugLogsVar](refresh-broadcast-loggers-has-debug-logs-var.md) | See [BroadcastLoggerHelper.refreshBroadcastLoggersHasDebugLogsVar](#)`fun refreshBroadcastLoggersHasDebugLogsVar(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [reloadTorConfig](reload-tor-config.md) | `fun reloadTorConfig(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [restartTorProcess](restart-tor-process.md) | `fun restartTorProcess(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setExitNode](set-exit-node.md) | Sets the exit nodes through the tor control connection`fun setExitNode(exitNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setup](setup.md) | Sets up and installs any files needed to run tor. If the tor files are already on the system this does not need to be invoked.`fun setup(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [signalControlConnection](signal-control-connection.md) | Sends a signal to the  [TorControlConnection](#)`fun signalControlConnection(torControlSignalCommand: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [signalNewNym](signal-new-nym.md) | Will signal for a NewNym, then broadcast [NEWNYM_SUCCESS_MESSAGE](-n-e-w-n-y-m_-s-u-c-c-e-s-s_-m-e-s-s-a-g-e.md) if successful.`suspend fun signalNewNym(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [start](start.md) | Starts tor control service if it isn't already running.`fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [stop](stop.md) | Kills the Tor OP Process. Once you have called this method nothing is going to work until you either call startWithRepeat or start`fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [NEWNYM_NO_NETWORK](-n-e-w-n-y-m_-n-o_-n-e-t-w-o-r-k.md) | `const val NEWNYM_NO_NETWORK: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [NEWNYM_RATE_LIMIT_PARTIAL_MSG](-n-e-w-n-y-m_-r-a-t-e_-l-i-m-i-t_-p-a-r-t-i-a-l_-m-s-g.md) | `const val NEWNYM_RATE_LIMIT_PARTIAL_MSG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [NEWNYM_SUCCESS_MESSAGE](-n-e-w-n-y-m_-s-u-c-c-e-s-s_-m-e-s-s-a-g-e.md) | `const val NEWNYM_SUCCESS_MESSAGE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
