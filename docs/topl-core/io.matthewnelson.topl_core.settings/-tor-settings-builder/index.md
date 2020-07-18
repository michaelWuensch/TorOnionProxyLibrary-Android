[topl-core](../../index.md) / [io.matthewnelson.topl_core.settings](../index.md) / [TorSettingsBuilder](./index.md)

# TorSettingsBuilder

`class TorSettingsBuilder : `[`CoreConsts`](../../io.matthewnelson.topl_core.util/-core-consts/index.md) [(source)](https://github.com/05nelsonm/TorOnionProxyLibrary-Android/blob/master/topl-core/src/main/java/io/matthewnelson/topl_core/settings/TorSettingsBuilder.kt#L79)

Call [io.matthewnelson.topl_core.OnionProxyManager.getNewSettingsBuilder](../../io.matthewnelson.topl_core/-onion-proxy-manager/get-new-settings-builder.md) to obtain
this class.

This class is basically a torrc file builder. Every method you call adds a
specific value to the [buffer](#) which Tor understands.

You can call [addLine](add-line.md) if something isn't covered here so you can customize your torrc
file however you wish.

Calling [finishAndReturnString](finish-and-return-string.md) will return to you the String that has been
built for you to write to the
[io.matthewnelson.topl_core_base.TorConfigFiles.torrcFile](http://FIX_DOKKA_LINKS/topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/torrc-file.md).

Calling [finishAndWriteToTorrcFile](finish-and-write-to-torrc-file.md) will do just that.

``` kotlin
onionProxyManager.getNewSettingsBuilder()
    .updateTorSettings()
    .setGeoIpFiles()
    .finishAndWriteToTorrcFile()
```

### Parameters

`onionProxyContext` - [OnionProxyContext](#)

`broadcastLogger` - for broadcasting/logging

### Functions

| Name | Summary |
|---|---|
| [addBridge](add-bridge.md) | `fun addBridge(type: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, config: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [addBridgesFromResources](add-bridges-from-resources.md) | Adds bridges from a resource stream. This relies on the [io.matthewnelson.topl_core.util.TorInstaller](../../io.matthewnelson.topl_core.util/-tor-installer/index.md) to know how to obtain this stream. These entries may be type-specified like:`fun addBridgesFromResources(): `[`TorSettingsBuilder`](./index.md) |
| [addCustomBridge](add-custom-bridge.md) | `fun addCustomBridge(config: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [addLine](add-line.md) | Add a new line to the [buffer](#) if a setting here is not available.`fun addLine(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [automapHostsOnResolve](automap-hosts-on-resolve.md) | `fun automapHostsOnResolve(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [automapHostsOnResolveFromSettings](automap-hosts-on-resolve-from-settings.md) | `fun automapHostsOnResolveFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [bridgesFromSettings](bridges-from-settings.md) | `fun bridgesFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [configurePluggableTransportsFromSettings](configure-pluggable-transports-from-settings.md) | `fun configurePluggableTransportsFromSettings(pluggableTransportClient: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [connectionPadding](connection-padding.md) | `fun connectionPadding(setting: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [connectionPaddingFromSettings](connection-padding-from-settings.md) | `fun connectionPaddingFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [controlPortWriteToFile](control-port-write-to-file.md) | `fun controlPortWriteToFile(torConfigFiles: `[`TorConfigFiles`](http://FIX_DOKKA_LINKS/topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/index.md)`): `[`TorSettingsBuilder`](./index.md) |
| [controlPortWriteToFileFromConfig](control-port-write-to-file-from-config.md) | `fun controlPortWriteToFileFromConfig(): `[`TorSettingsBuilder`](./index.md) |
| [cookieAuthentication](cookie-authentication.md) | `fun cookieAuthentication(): `[`TorSettingsBuilder`](./index.md) |
| [cookieAuthenticationFromSettings](cookie-authentication-from-settings.md) | `fun cookieAuthenticationFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [debugLogs](debug-logs.md) | `fun debugLogs(): `[`TorSettingsBuilder`](./index.md) |
| [debugLogsFromSettings](debug-logs-from-settings.md) | `fun debugLogsFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [disableNetwork](disable-network.md) | `fun disableNetwork(disable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [disableNetworkFromSettings](disable-network-from-settings.md) | `fun disableNetworkFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [dnsPort](dns-port.md) | `fun dnsPort(dnsPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [dnsPortFromSettings](dns-port-from-settings.md) | `fun dnsPortFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [dormantCanceledByStartup](dormant-canceled-by-startup.md) | `fun dormantCanceledByStartup(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [dormantCanceledByStartupFromSettings](dormant-canceled-by-startup-from-settings.md) | `fun dormantCanceledByStartupFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [entryNodes](entry-nodes.md) | `fun entryNodes(entryNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [excludeNodes](exclude-nodes.md) | `fun excludeNodes(excludeNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [exitNodes](exit-nodes.md) | `fun exitNodes(exitNodes: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [finishAndReturnString](finish-and-return-string.md) | This returns what's in the [buffer](#) as a String and then clears it. You still need to write the String to the [io.matthewnelson.topl_core_base.TorConfigFiles.torrcFile](http://FIX_DOKKA_LINKS/topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/torrc-file.md).`fun finishAndReturnString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [finishAndWriteToTorrcFile](finish-and-write-to-torrc-file.md) | A convenience method for after populating the [buffer](#) by calling [updateTorSettings](update-tor-settings.md). It will overwrite your current torrc file (or create a new one if it doesn't exist) with the new settings.`fun finishAndWriteToTorrcFile(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [geoIpFile](geo-ip-file.md) | `fun geoIpFile(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [geoIpV6File](geo-ip-v6-file.md) | `fun geoIpV6File(path: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [httpTunnelPort](http-tunnel-port.md) | `fun httpTunnelPort(port: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, isolationFlags: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [httpTunnelPortFromSettings](http-tunnel-port-from-settings.md) | `fun httpTunnelPortFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [makeNonExitRelay](make-non-exit-relay.md) | `fun makeNonExitRelay(dnsFile: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, orPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, nickname: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [nodesFromSettings](nodes-from-settings.md) | Sets the entry/exit/exclude nodes`fun nodesFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [nonExitRelayFromSettings](non-exit-relay-from-settings.md) | Adds non exit relay to builder. This method uses a default Quad9 nameserver.`fun nonExitRelayFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [proxyOnAllInterfaces](proxy-on-all-interfaces.md) | `fun proxyOnAllInterfaces(): `[`TorSettingsBuilder`](./index.md) |
| [proxyOnAllInterfacesFromSettings](proxy-on-all-interfaces-from-settings.md) | `fun proxyOnAllInterfacesFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [proxySocks5](proxy-socks5.md) | Set socks5 proxy with no authentication.`fun proxySocks5(host: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, port: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [proxySocks5FromSettings](proxy-socks5-from-settings.md) | `fun proxySocks5FromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [proxyWithAuthentication](proxy-with-authentication.md) | Sets proxyWithAuthentication information. If proxyType, proxyHost or proxyPort is empty/null, then this method does nothing.`fun proxyWithAuthentication(proxyType: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, proxyHost: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, proxyPort: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?, proxyUser: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, proxyPass: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [proxyWithAuthenticationFromSettings](proxy-with-authentication-from-settings.md) | `fun proxyWithAuthenticationFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [reachableAddressesFromSettings](reachable-addresses-from-settings.md) | `fun reachableAddressesFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [reachableAddressPorts](reachable-address-ports.md) | `fun reachableAddressPorts(reachableAddressesPorts: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [reducedConnectionPadding](reduced-connection-padding.md) | `fun reducedConnectionPadding(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [reducedConnectionPaddingFromSettings](reduced-connection-padding-from-settings.md) | `fun reducedConnectionPaddingFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [reset](reset.md) | `fun reset(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [runAsDaemon](run-as-daemon.md) | `fun runAsDaemon(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [runAsDaemonFromSettings](run-as-daemon-from-settings.md) | `fun runAsDaemonFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [safeSocks](safe-socks.md) | `fun safeSocks(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [safeSocksFromSettings](safe-socks-from-settings.md) | `fun safeSocksFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [setGeoIpFiles](set-geo-ip-files.md) | Ensure that you have setup [io.matthewnelson.topl_core.util.TorInstaller](../../io.matthewnelson.topl_core.util/-tor-installer/index.md) such that you've copied the geoip/geoip6 files over prior to calling this.`fun setGeoIpFiles(): `[`TorSettingsBuilder`](./index.md) |
| [socksPort](socks-port.md) | `fun socksPort(socksPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, isolationFlag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [socksPortFromSettings](socks-port-from-settings.md) | `fun socksPortFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [strictNodes](strict-nodes.md) | `fun strictNodes(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [strictNodesFromSettings](strict-nodes-from-settings.md) | `fun strictNodesFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [testSocks](test-socks.md) | `fun testSocks(enable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [testSocksFromSettings](test-socks-from-settings.md) | `fun testSocksFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [torrcCustomFromSettings](torrc-custom-from-settings.md) | `fun torrcCustomFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [transPort](trans-port.md) | `fun transPort(transPort: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [transPortFromSettings](trans-port-from-settings.md) | `fun transPortFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [transportPlugin](transport-plugin.md) | `fun transportPlugin(clientPath: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [updateTorSettings](update-tor-settings.md) | Updates the buffer for all methods annotated with [SettingsConfig](#). You still need to call [finishAndReturnString](finish-and-return-string.md) and then write the returned String to your [io.matthewnelson.topl_core_base.TorConfigFiles.torrcFile](http://FIX_DOKKA_LINKS/topl-core-base/io.matthewnelson.topl_core_base/-tor-config-files/torrc-file.md).`fun updateTorSettings(): `[`TorSettingsBuilder`](./index.md) |
| [useBridges](use-bridges.md) | `fun useBridges(useThem: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`TorSettingsBuilder`](./index.md) |
| [useBridgesFromSettings](use-bridges-from-settings.md) | `fun useBridgesFromSettings(): `[`TorSettingsBuilder`](./index.md) |
| [virtualAddressNetwork](virtual-address-network.md) | `fun virtualAddressNetwork(address: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?): `[`TorSettingsBuilder`](./index.md) |
| [virtualAddressNetworkFromSettings](virtual-address-network-from-settings.md) | `fun virtualAddressNetworkFromSettings(): `[`TorSettingsBuilder`](./index.md) |
