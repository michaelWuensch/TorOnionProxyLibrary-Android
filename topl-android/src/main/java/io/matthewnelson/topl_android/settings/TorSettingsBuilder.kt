/*
Copyright (c) Microsoft Open Technologies, Inc.
All Rights Reserved
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED,
INCLUDING WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
MERCHANTABLITY OR NON-INFRINGEMENT.

See the Apache 2 License for the specific language governing permissions and limitations under the License.
*/
package io.matthewnelson.topl_android.settings

import io.matthewnelson.topl_android.OnionProxyContext
import io.matthewnelson.topl_android.TorConfig
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*

class TorSettingsBuilder(private val onionProxyContext: OnionProxyContext) {

    private var buffer = StringBuffer()

    /**
     * Updates the tor config for all methods annotated with SettingsConfig
     *
     * @throws [SecurityException] If denied access to the class
     * @throws [IllegalAccessException] see [java.lang.reflect.Method.invoke]
     * @throws [IllegalArgumentException] see [java.lang.reflect.Method.invoke]
     * @throws [InvocationTargetException] see [java.lang.reflect.Method.invoke]
     * @throws [NullPointerException] see [java.lang.reflect.Method.invoke]
     * @throws [ExceptionInInitializerError] see [java.lang.reflect.Method.invoke]
     */
    @Throws(
        SecurityException::class,
        IllegalAccessException::class,
        IllegalArgumentException::class,
        InvocationTargetException::class,
        NullPointerException::class,
        ExceptionInInitializerError::class
    )
    fun updateTorConfig(): TorSettingsBuilder {
        for (method in this.javaClass.methods)
            for (annotation in method.annotations)
                if (annotation is SettingsConfig) {
                    method.invoke(this)
                    break
                }

        return this
    }

    fun asString(): String = buffer.toString()

    fun automapHostsOnResolve(): TorSettingsBuilder {
        buffer.append("AutomapHostsOnResolve 1").append("\n")
        return this
    }

    @SettingsConfig
    fun automapHostsOnResolveFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.isAutoMapHostsOnResolve)
            automapHostsOnResolve()
        else
            this

    fun addBridge(type: String?, config: String?): TorSettingsBuilder {
        if (!type.isNullOrEmpty() && !config.isNullOrEmpty())
            buffer.append("Bridge ").append(type).append(" ").append(config).append("\n")
        return this
    }

    fun addCustomBridge(config: String?): TorSettingsBuilder {
        if (!config.isNullOrEmpty())
            buffer.append("Bridge ").append(config).append("\n")
        return this
    }

    @SettingsConfig
    fun bridgesFromSettings(): TorSettingsBuilder {
        try {
            addBridgesFromResources()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return this
    }

    @Throws(IOException::class, SecurityException::class)
    fun configurePluggableTransportsFromSettings(pluggableTransportClient: File?): TorSettingsBuilder {
        if (pluggableTransportClient == null) return this

        if (!pluggableTransportClient.exists())
            throw IOException(
                "Bridge binary does not exist: ${pluggableTransportClient.canonicalPath}"
            )

        if (!pluggableTransportClient.canExecute())
            throw IOException(
                "Bridge binary is not executable: ${pluggableTransportClient.canonicalPath}"
            )

        transportPlugin(pluggableTransportClient.canonicalPath)
        return this
    }

    fun cookieAuthentication(): TorSettingsBuilder {
        buffer.append("CookieAuthentication 1 ").append("\n")
        buffer.append("CookieAuthFile ")
            .append(onionProxyContext.torConfig.cookieAuthFile.absolutePath)
            .append("\n")
        return this
    }

    @SettingsConfig
    fun cookieAuthenticationFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.hasCookieAuthentication)
            cookieAuthentication()
        else
            this

    fun connectionPadding(): TorSettingsBuilder {
        buffer.append("ConnectionPadding 1").append("\n")
        return this
    }

    @SettingsConfig
    fun connectionPaddingFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.hasConnectionPadding)
            connectionPadding()
        else
            this

    fun controlPortWriteToFile(torConfig: TorConfig): TorSettingsBuilder {
        buffer.append("ControlPortWriteToFile ")
            .append(torConfig.controlPortFile.absolutePath).append("\n")
        buffer.append("ControlPort auto").append("\n")
        return this
    }

    @SettingsConfig
    fun controlPortWriteToFileFromConfig(): TorSettingsBuilder =
        controlPortWriteToFile(onionProxyContext.torConfig)

    fun debugLogs(): TorSettingsBuilder {
        buffer.append("Log debug syslog").append("\n")
        buffer.append("Log info syslog").append("\n")
        buffer.append("SafeLogging 0").append("\n")
        return this
    }

    @SettingsConfig
    fun debugLogsFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.hasDebugLogs)
            debugLogs()
        else
            this

    fun disableNetwork(): TorSettingsBuilder {
        buffer.append("DisableNetwork 1").append("\n")
        return this
    }

    @SettingsConfig
    fun disableNetworkFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.disableNetwork)
            disableNetwork()
        else
            this

    fun dnsPort(dnsPort: Int): TorSettingsBuilder {
        buffer.append("DNSPort ").append(dnsPort.toString()).append("\n")
        return this
    }

    @SettingsConfig
    fun dnsPortFromSettings(): TorSettingsBuilder =
        dnsPort(onionProxyContext.torSettings.dnsPort)

    fun dontUseBridges(): TorSettingsBuilder {
        buffer.append("UseBridges 0").append("\n")
        return this
    }

    fun dormantCanceledByStartup(): TorSettingsBuilder {
        buffer.append("DormantCanceledByStartup 1").append("\n")
        return this
    }

    @SettingsConfig
    fun dormantCanceledByStartupFromSettings(): TorSettingsBuilder {
        if (onionProxyContext.torSettings.hasDormantCanceledByStartup)
            dormantCanceledByStartup()
        return this
    }

    fun entryNodes(entryNodes: String?): TorSettingsBuilder {
        if (!entryNodes.isNullOrEmpty())
            buffer.append("EntryNodes ").append(entryNodes).append("\n")
        return this
    }

    fun excludeNodes(excludeNodes: String?): TorSettingsBuilder {
        if (!excludeNodes.isNullOrEmpty())
            buffer.append("ExcludeNodes ").append(excludeNodes).append("\n")
        return this
    }

    fun exitNodes(exitNodes: String?): TorSettingsBuilder {
        if (!exitNodes.isNullOrEmpty())
            buffer.append("ExitNodes ").append(exitNodes).append("\n")
        return this
    }

    fun geoIpFile(path: String?): TorSettingsBuilder {
        if (!path.isNullOrEmpty())
            buffer.append("GeoIPFile ").append(path).append("\n")
        return this
    }

    fun geoIpV6File(path: String?): TorSettingsBuilder {
        if (!path.isNullOrEmpty())
            buffer.append("GeoIPv6File ").append(path).append("\n")
        return this
    }

    fun httpTunnelPort(port: Int, isolationFlags: String?): TorSettingsBuilder {
        buffer.append("HTTPTunnelPort ").append(port)
        if (!isolationFlags.isNullOrEmpty())
            buffer.append(" ").append(isolationFlags)
        buffer.append("\n")
        return this
    }

    @SettingsConfig
    fun httpTunnelPortFromSettings(): TorSettingsBuilder =
        httpTunnelPort(
            onionProxyContext.torSettings.httpTunnelPort,
            if (onionProxyContext.torSettings.hasIsolationAddressFlagForTunnel)
                "IsolateDestAddr"
            else
                null
        )

    private fun addLine(value: String?): TorSettingsBuilder {
        if (!value.isNullOrEmpty())
            buffer.append(value).append("\n")
        return this
    }

    fun makeNonExitRelay(dnsFile: String, orPort: Int, nickname: String): TorSettingsBuilder {
        buffer.append("ServerDNSResolvConfFile ").append(dnsFile).append("\n")
        buffer.append("ORPort ").append(orPort).append("\n")
        buffer.append("Nickname ").append(nickname).append("\n")
        buffer.append("ExitPolicy reject *:*").append("\n")
        return this
    }

    /**
     * Sets the entry/exit/exclude nodes
     */
    @SettingsConfig
    fun nodesFromSettings(): TorSettingsBuilder {
        entryNodes(onionProxyContext.torSettings.entryNodes)
            .exitNodes(onionProxyContext.torSettings.exitNodes)
            .excludeNodes(onionProxyContext.torSettings.excludeNodes)
        return this
    }

    /**
     * Adds non exit relay to builder. This method uses a default OpenDNS Home nameserver.
     */
    @SettingsConfig
    fun nonExitRelayFromSettings(): TorSettingsBuilder {
        if (!onionProxyContext.torSettings.hasReachableAddress &&
            !onionProxyContext.torSettings.hasBridges &&
            onionProxyContext.torSettings.isRelay
        ) {
            try {
                val resolv = onionProxyContext.createQuad9NameserverFile()
                makeNonExitRelay(
                    resolv.canonicalPath,
                    onionProxyContext.torSettings.relayPort,
                    onionProxyContext.torSettings.relayNickname ?: "OpenDNSHome"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return this
    }

    fun proxyOnAllInterfaces(): TorSettingsBuilder {
        buffer.append("SocksListenAddress 0.0.0.0").append("\n")
        return this
    }

    @SettingsConfig
    fun proxyOnAllInterfacesFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.hasOpenProxyOnAllInterfaces)
            proxyOnAllInterfaces()
        else
            this

    /**
     * Set socks5 proxy with no authentication. This can be set if you are using a VPN.
     */
    fun proxySocks5(host: String?, port: Int?): TorSettingsBuilder {
        if (!host.isNullOrEmpty() && port != null) {
            buffer.append("socks5Proxy ").append(host).append(":").append(port.toString()).append("\n")
        }
        return this
    }

    @SettingsConfig
    fun proxySocks5FromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.useSocks5 && !onionProxyContext.torSettings.hasBridges)
            proxySocks5(
                onionProxyContext.torSettings.proxySocks5Host,
                onionProxyContext.torSettings.proxySocks5ServerPort
            )
        else
            this

    /**
     * Sets proxyWithAuthentication information. If proxyType, proxyHost or proxyPort is empty,
     * then this method does nothing.
     */
    fun proxyWithAuthentication(
        proxyType: String?,
        proxyHost: String?,
        proxyPort: Int?,
        proxyUser: String?,
        proxyPass: String?
    ): TorSettingsBuilder {
        if (!proxyType.isNullOrEmpty() && !proxyHost.isNullOrEmpty() && proxyPort != null) {
            buffer.append(proxyType).append("Proxy ").append(proxyHost).append(":")
                .append(proxyPort.toString()).append("\n")
            if (proxyUser != null && proxyPass != null) {
                if (proxyType.equals("socks5", ignoreCase = true)) {
                    buffer.append("Socks5ProxyUsername ").append(proxyUser).append("\n")
                    buffer.append("Socks5ProxyPassword ").append(proxyPass).append("\n")
                } else {
                    buffer.append(proxyType).append("ProxyAuthenticator ").append(proxyUser)
                        .append(":").append(proxyPort.toString()).append("\n")
                }
            } else if (proxyPass != null) {
                buffer.append(proxyType).append("ProxyAuthenticator ").append(proxyUser)
                    .append(":").append(proxyPort.toString()).append("\n")
            }
        }
        return this
    }

    @SettingsConfig
    fun proxyWithAuthenticationFromSettings(): TorSettingsBuilder =
        if (!onionProxyContext.torSettings.useSocks5 && !onionProxyContext.torSettings.hasBridges)
            proxyWithAuthentication(
                onionProxyContext.torSettings.proxyType,
                onionProxyContext.torSettings.proxyHost,
                onionProxyContext.torSettings.proxyPort,
                onionProxyContext.torSettings.proxyUser,
                onionProxyContext.torSettings.proxyPassword
            )
        else
            this

    fun reachableAddressPorts(reachableAddressesPorts: String?): TorSettingsBuilder {
        if (!reachableAddressesPorts.isNullOrEmpty())
            buffer.append("ReachableAddresses ").append(reachableAddressesPorts).append("\n")
        return this
    }

    @SettingsConfig
    fun reachableAddressesFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.hasReachableAddress)
            reachableAddressPorts(onionProxyContext.torSettings.reachableAddressPorts)
        else
            this

    fun reducedConnectionPadding(): TorSettingsBuilder {
        buffer.append("ReducedConnectionPadding 1").append("\n")
        return this
    }

    @SettingsConfig
    fun reducedConnectionPaddingFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.hasReducedConnectionPadding)
            reducedConnectionPadding()
        else
            this

    fun reset() {
        buffer = StringBuffer()
    }

    @SettingsConfig
    fun runAsDaemonFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.runAsDaemon)
            runAsDaemon()
        else
            this

    fun runAsDaemon(): TorSettingsBuilder {
        buffer.append("RunAsDaemon 1").append("\n")
        return this
    }

    fun safeSocks(enable: Boolean): TorSettingsBuilder {
        val safeSocksSetting = if (enable) "1" else "0"
        buffer.append("SafeSocks $safeSocksSetting").append("\n")
        return this
    }

    @SettingsConfig
    fun safeSocksFromSettings(): TorSettingsBuilder =
        safeSocks(onionProxyContext.torSettings.hasSafeSocks)

    @Throws(IOException::class, SecurityException::class)
    fun setGeoIpFiles(): TorSettingsBuilder {
        val torConfig = onionProxyContext.torConfig
        if (torConfig.geoIpFile.exists())
            geoIpFile(torConfig.geoIpFile.canonicalPath).geoIpV6File(torConfig.geoIpv6File.canonicalPath)
        return this
    }

    fun socksPort(socksPort: String, isolationFlag: String?): TorSettingsBuilder {
        if (socksPort.isEmpty()) return this

        buffer.append("SOCKSPort ").append(socksPort)

        if (!isolationFlag.isNullOrEmpty())
            buffer.append(" ").append(isolationFlag)

        buffer.append(" KeepAliveIsolateSOCKSAuth")
        buffer.append(" IPv6Traffic")
        buffer.append(" PreferIPv6")
        buffer.append("\n")
        return this
    }

    @SettingsConfig
    fun socksPortFromSettings(): TorSettingsBuilder {
        var socksPort = onionProxyContext.torSettings.socksPort
        if (socksPort.indexOf(':') != -1)
            socksPort = socksPort.split(":".toRegex()).toTypedArray()[1]

        if (!socksPort.equals("auto", ignoreCase = true) && isLocalPortOpen(socksPort.toInt()))
            socksPort = "auto"

        return socksPort(
            socksPort,
            if (onionProxyContext.torSettings.hasIsolationAddressFlagForTunnel)
                "IsolateDestAddr"
            else
                null
        )
    }

    fun strictNodes(enable: Boolean): TorSettingsBuilder {
        val strictNodeSetting = if (enable) "1" else "0"
        buffer.append("StrictNodes $strictNodeSetting").append("\n")
        return this
    }

    @SettingsConfig
    fun strictNodesFromSettings(): TorSettingsBuilder =
        strictNodes(onionProxyContext.torSettings.hasStrictNodes)

    fun testSocks(enable: Boolean): TorSettingsBuilder {
        val testSocksSetting = if (enable) "1" else "0"
        buffer.append("TestSocks $testSocksSetting").append("\n")
        return this
    }

    @SettingsConfig
    fun testSocksFromSettings(): TorSettingsBuilder =
        testSocks(onionProxyContext.torSettings.hasTestSocks)

    @SettingsConfig
    @Throws(UnsupportedEncodingException::class)
    fun torrcCustomFromSettings(): TorSettingsBuilder {
        val customTorrc = onionProxyContext.torSettings.customTorrc
        return if (customTorrc != null)
            addLine(String(customTorrc.toByteArray(Charsets.US_ASCII)))
        else
            this
    }

    fun transPort(transPort: Int?): TorSettingsBuilder {
        if (transPort != null)
            buffer.append("TransPort ").append(transPort.toString()).append("\n")
        return this
    }

    @SettingsConfig
    fun transPortFromSettings(): TorSettingsBuilder =
        transPort(onionProxyContext.torSettings.transPort)

    fun transportPlugin(clientPath: String): TorSettingsBuilder {
        buffer.append("ClientTransportPlugin meek_lite,obfs3,obfs4 exec ").append(clientPath).append("\n")
        return this
    }

    fun useBridges(useThem: Boolean): TorSettingsBuilder {
        val useBridges = if (useThem) "1" else "0"
        buffer.append("UseBridges $useBridges").append("\n")
        return this
    }

    @SettingsConfig
    fun useBridgesFromSettings(): TorSettingsBuilder =
        if (onionProxyContext.torSettings.hasBridges)
            useBridges(onionProxyContext.torSettings.hasBridges)
        else
            this

    fun virtualAddressNetwork(address: String?): TorSettingsBuilder {
        if (!address.isNullOrEmpty())
            buffer.append("VirtualAddrNetwork ").append(address).append("\n")
        return this
    }

    @SettingsConfig
    fun virtualAddressNetworkFromSettings(): TorSettingsBuilder =
        virtualAddressNetwork(onionProxyContext.torSettings.virtualAddressNetwork)

    /**
     * Adds bridges from a resource stream. This relies on the TorInstaller to know how to obtain this stream.
     * These entries may be type-specified like:
     *
     *
     * `obfs3 169.229.59.74:31493 AF9F66B7B04F8FF6F32D455F05135250A16543C9`
     *
     *
     * Or it may just be a custom entry like
     *
     *
     * `69.163.45.129:443 9F090DE98CA6F67DEEB1F87EFE7C1BFD884E6E2F`
     *
     *
     */
    @Throws(IOException::class)
    fun addBridgesFromResources(): TorSettingsBuilder {
        if (onionProxyContext.torSettings.hasBridges) {
            val bridgesStream = onionProxyContext.torInstaller.openBridgesStream()
            if (bridgesStream != null) {
                val formatType = bridgesStream.read()

                if (formatType == 0)
                    addBridges(bridgesStream)
                else
                    addCustomBridges(bridgesStream)
            }
        }
        return this
    }

    /**
     * Add bridges from bridges.txt file.
     */
    private fun addBridges(input: InputStream?) {
        if (input == null) return

        val bridges = readBridgesFromStream(input)
        for (b in bridges)
            addBridge(b.type, b.config)
    }

    /**
     * Add custom bridges defined by the user. These will have a bridgeType of 'custom' as
     * the first field.
     */
    private fun addCustomBridges(input: InputStream?) {
        if (input == null) return

        val bridges = readCustomBridgesFromStream(input)
        for (b in bridges)
            if (b.type == "custom")
                addCustomBridge(b.config)
    }

    private class Bridge(val type: String, val config: String)

    companion object {

        private fun isLocalPortOpen(port: Int): Boolean {
            val socket = Socket()

            return try {
                socket.connect(InetSocketAddress("127.0.0.1", port), 500)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            } finally {
                try {
                    socket.close()
                } catch (ee: Exception) {
                    ee.printStackTrace()
                }
            }
        }

        private fun readBridgesFromStream(input: InputStream): List<Bridge> {
            val bridges: MutableList<Bridge> = ArrayList()
            try {
                val br = BufferedReader(InputStreamReader(input, Charsets.UTF_8))
                var line = br.readLine()

                while (line != null) {
                    val tokens = line.split("\\s+".toRegex(), 2).toTypedArray()
                    if (tokens.size != 2) {
                        line = br.readLine()
                        continue  //bad entry
                    }
                    bridges.add(Bridge(tokens[0], tokens[1]))
                    line = br.readLine()
                }

                br.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bridges
        }

        private fun readCustomBridgesFromStream(input: InputStream): List<Bridge> {
            val bridges: MutableList<Bridge> = ArrayList()
            try {
                val br = BufferedReader(InputStreamReader(input, Charsets.UTF_8))
                var line = br.readLine()

                while (line != null) {
                    if (line.isEmpty()) {
                        line = br.readLine()
                        continue
                    }
                    bridges.add(Bridge("custom", line))
                    line = br.readLine()
                }

                br.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bridges
        }
    }
}