[topl-core](../../index.md) / [io.matthewnelson.topl_core](../index.md) / [OnionProxyManager](index.md) / [disableNetwork](./disable-network.md)

# disableNetwork

`@Synchronized fun disableNetwork(disable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/OnionProxyManager.kt#L445)

Tells the Tor OP if it should accept network connections.

Whenever setting Tor's Conf to `DisableNetwork X`, ONLY use this method to do it
such that [torStateMachine](tor-state-machine.md) will reflect the proper
[io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState](../../..//topl-core-base/io.matthewnelson.topl_core_base/-base-consts/-tor-network-state/index.md).

### Parameters

`disable` - Sets Tor config DisableNetwork (1 if `true`, 0 if `false`)

### Exceptions

`IOException` - if having issues with TorControlConnection#setConf

`NullPointerException` - if [controlConnection](#) is null even after checking.