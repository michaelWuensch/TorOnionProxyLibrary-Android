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
package com.msopentech.thali.universal.toronionproxy

import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*

class TorConfigBuilder(context: OnionProxyContext) {
    private val settings: TorSettings?
    private val context: OnionProxyContext
    private var buffer = StringBuffer()

    /**
     * Updates the tor config for all methods annotated with SettingsConfig
     */
    @Throws(Exception::class)
    fun updateTorConfig(): TorConfigBuilder {
        for (method in javaClass.methods) {
            for (annotation in method.annotations) {
                if (annotation is SettingsConfig) {
                    method.invoke(this)
                    break
                }
            }
        }
        return this
    }

    fun asString(): String {
        return buffer.toString()
    }

    fun automapHostsOnResolve(): TorConfigBuilder {
        buffer.append("AutomapHostsOnResolve 1").append('\n')
        return this
    }

    @SettingsConfig
    fun automapHostsOnResolveFromSettings(): TorConfigBuilder {
        return if (settings!!.isAutomapHostsOnResolve) automapHostsOnResolve() else this
    }

    fun bridge(type: String?, config: String?): TorConfigBuilder {
        if (!isNullOrEmpty(type) && !isNullOrEmpty(
                config
            )
        ) {
            buffer.append("Bridge ").append(type).append(' ').append(config).append('\n')
        }
        return this
    }

    fun bridgeCustom(config: String?): TorConfigBuilder {
        if (!isNullOrEmpty(config)) {
            buffer.append("Bridge ").append(config).append('\n')
        }
        return this
    }

    @SettingsConfig
    fun bridgesFromSettings(): TorConfigBuilder {
        try {
            addBridgesFromResources()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return this
    }

    @Throws(IOException::class)
    fun configurePluggableTransportsFromSettings(pluggableTransportClient: File?): TorConfigBuilder {
        if (pluggableTransportClient == null) {
            return this
        }
        if (!pluggableTransportClient.exists()) {
            throw IOException(
                "Bridge binary does not exist: " + pluggableTransportClient
                    .canonicalPath
            )
        }
        if (!pluggableTransportClient.canExecute()) {
            throw IOException(
                "Bridge binary is not executable: " + pluggableTransportClient
                    .canonicalPath
            )
        }
        transportPlugin(pluggableTransportClient.canonicalPath)
        return this
    }

    fun cookieAuthentication(): TorConfigBuilder {
        buffer.append("CookieAuthentication 1 ").append('\n')
        buffer.append("CookieAuthFile ")
            .append(context.getConfig().cookieAuthFile.absolutePath).append("\n")
        return this
    }

    @SettingsConfig
    fun cookieAuthenticationFromSettings(): TorConfigBuilder {
        return if (settings!!.hasCookieAuthentication()) cookieAuthentication() else this
    }

    fun connectionPadding(): TorConfigBuilder {
        buffer.append("ConnectionPadding 1").append('\n')
        return this
    }

    @SettingsConfig
    fun connectionPaddingFromSettings(): TorConfigBuilder {
        return if (settings!!.hasConnectionPadding()) connectionPadding() else this
    }

    fun controlPortWriteToFile(controlPortFile: String?): TorConfigBuilder {
        buffer.append("ControlPortWriteToFile ").append(controlPortFile).append('\n')
        buffer.append("ControlPort auto\n")
        return this
    }

    @SettingsConfig
    fun controlPortWriteToFileFromConfig(): TorConfigBuilder {
        return controlPortWriteToFile(context.config.controlPortFile.absolutePath)
    }

    fun debugLogs(): TorConfigBuilder {
        buffer.append("Log debug syslog").append('\n')
        buffer.append("Log info syslog").append('\n')
        buffer.append("SafeLogging 0").append('\n')
        return this
    }

    @SettingsConfig
    fun debugLogsFromSettings(): TorConfigBuilder {
        return if (settings!!.hasDebugLogs()) debugLogs() else this
    }

    fun disableNetwork(): TorConfigBuilder {
        buffer.append("DisableNetwork 1").append('\n')
        return this
    }

    @SettingsConfig
    fun disableNetworkFromSettings(): TorConfigBuilder {
        return if (settings!!.disableNetwork()) disableNetwork() else this
    }

    fun dnsPort(dnsPort: String?): TorConfigBuilder {
        if (!isNullOrEmpty(dnsPort)) buffer.append("DNSPort ")
            .append(dnsPort).append('\n')
        return this
    }

    @SettingsConfig
    fun dnsPortFromSettings(): TorConfigBuilder {
        return dnsPort(settings!!.dnsPort())
    }

    fun dontUseBridges(): TorConfigBuilder {
        buffer.append("UseBridges 0").append('\n')
        return this
    }

    fun dormantCanceledByStartup(): TorConfigBuilder {
        buffer.append("DormantCanceledByStartup 1\n")
        return this
    }

    @SettingsConfig
    fun dormantCanceledByStartupFromSettings(): TorConfigBuilder {
        if (settings!!.hasDormantCanceledByStartup()) {
            dormantCanceledByStartup()
        }
        return this
    }

    fun entryNodes(entryNodes: String?): TorConfigBuilder {
        if (!isNullOrEmpty(entryNodes)) buffer.append("EntryNodes ")
            .append(entryNodes).append('\n')
        return this
    }

    fun excludeNodes(excludeNodes: String?): TorConfigBuilder {
        if (!isNullOrEmpty(excludeNodes)) buffer.append("ExcludeNodes ")
            .append(excludeNodes).append('\n')
        return this
    }

    fun exitNodes(exitNodes: String?): TorConfigBuilder {
        if (!isNullOrEmpty(exitNodes)) buffer.append("ExitNodes ")
            .append(exitNodes).append('\n')
        return this
    }

    fun geoIpFile(path: String?): TorConfigBuilder {
        if (!isNullOrEmpty(path)) buffer.append("GeoIPFile ")
            .append(path).append('\n')
        return this
    }

    fun geoIpV6File(path: String?): TorConfigBuilder {
        if (!isNullOrEmpty(path)) buffer.append("GeoIPv6File ")
            .append(path).append('\n')
        return this
    }

    fun httpTunnelPort(port: Int, isolationFlags: String?): TorConfigBuilder {
        buffer.append("HTTPTunnelPort ").append(port)
        if (!isNullOrEmpty(isolationFlags)) {
            buffer.append(" ").append(isolationFlags)
        }
        buffer.append('\n')
        return this
    }

    @SettingsConfig
    fun httpTunnelPortFromSettings(): TorConfigBuilder {
        return httpTunnelPort(
            settings.getHttpTunnelPort(),
            if (settings!!.hasIsolationAddressFlagForTunnel()) "IsolateDestAddr" else null
        )
    }

    fun line(value: String?): TorConfigBuilder {
        if (!isNullOrEmpty(value)) buffer.append(value).append("\n")
        return this
    }

    fun makeNonExitRelay(
        dnsFile: String?,
        orPort: Int,
        nickname: String?
    ): TorConfigBuilder {
        buffer.append("ServerDNSResolvConfFile ").append(dnsFile).append('\n')
        buffer.append("ORPort ").append(orPort).append('\n')
        buffer.append("Nickname ").append(nickname).append('\n')
        buffer.append("ExitPolicy reject *:*").append('\n')
        return this
    }

    /**
     * Sets the entry/exit/exclude nodes
     */
    @SettingsConfig
    fun nodesFromSettings(): TorConfigBuilder {
        entryNodes(settings.getEntryNodes()).exitNodes(settings.getExitNodes())
            .excludeNodes(settings.getExcludeNodes())
        return this
    }

    /**
     * Adds non exit relay to builder. This method uses a default google nameserver.
     */
    @SettingsConfig
    fun nonExitRelayFromSettings(): TorConfigBuilder {
        if (!settings!!.hasReachableAddress() && !settings.hasBridges() && settings.isRelay) {
            try {
                val resolv = context.createGoogleNameserverFile()
                makeNonExitRelay(
                    resolv!!.canonicalPath, settings.relayPort, settings
                        .relayNickname
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return this
    }

    fun proxyOnAllInterfaces(): TorConfigBuilder {
        buffer.append("SocksListenAddress 0.0.0.0").append('\n')
        return this
    }

    @SettingsConfig
    fun proxyOnAllInterfacesFromSettings(): TorConfigBuilder {
        return if (settings!!.hasOpenProxyOnAllInterfaces()) proxyOnAllInterfaces() else this
    }

    /**
     * Set socks5 proxy with no authentication. This can be set if yo uare using a VPN.
     */
    fun proxySocks5(host: String?, port: String?): TorConfigBuilder {
        buffer.append("socks5Proxy ").append(host).append(':').append(port).append('\n')
        return this
    }

    @SettingsConfig
    fun proxySocks5FromSettings(): TorConfigBuilder {
        return if (settings!!.useSocks5() && !settings.hasBridges()) proxySocks5(
            settings
                .proxySocks5Host,
            settings.proxySocks5ServerPort
        ) else this
    }

    /**
     * Sets proxyWithAuthentication information. If proxyType, proxyHost or proxyPort is empty,
     * then this method does nothing.
     */
    fun proxyWithAuthentication(
        proxyType: String?,
        proxyHost: String?,
        proxyPort: String?,
        proxyUser: String?,
        proxyPass: String?
    ): TorConfigBuilder {
        if (!isNullOrEmpty(proxyType) && !isNullOrEmpty(
                proxyHost
            ) && !isNullOrEmpty(proxyPort)
        ) {
            buffer.append(proxyType).append("Proxy ").append(proxyHost).append(':')
                .append(proxyPort).append('\n')
            if (proxyUser != null && proxyPass != null) {
                if (proxyType.equals("socks5", ignoreCase = true)) {
                    buffer.append("Socks5ProxyUsername ").append(proxyUser).append('\n')
                    buffer.append("Socks5ProxyPassword ").append(proxyPass).append('\n')
                } else {
                    buffer.append(proxyType).append("ProxyAuthenticator ").append(proxyUser)
                        .append(':').append(proxyPort).append('\n')
                }
            } else if (proxyPass != null) {
                buffer.append(proxyType).append("ProxyAuthenticator ").append(proxyUser)
                    .append(':').append(proxyPort).append('\n')
            }
        }
        return this
    }

    @SettingsConfig
    fun proxyWithAuthenticationFromSettings(): TorConfigBuilder {
        return if (!settings!!.useSocks5() && !settings.hasBridges()) proxyWithAuthentication(
            settings.proxyType, settings.proxyHost,
            settings.proxyPort, settings.proxyUser, settings
                .proxyPassword
        ) else this
    }

    fun reachableAddressPorts(reachableAddressesPorts: String?): TorConfigBuilder {
        if (!isNullOrEmpty(reachableAddressesPorts)) buffer.append("ReachableAddresses ")
            .append(reachableAddressesPorts).append('\n')
        return this
    }

    @SettingsConfig
    fun reachableAddressesFromSettings(): TorConfigBuilder {
        return if (settings!!.hasReachableAddress()) reachableAddressPorts(
            settings
                .reachableAddressPorts
        ) else this
    }

    fun reducedConnectionPadding(): TorConfigBuilder {
        buffer.append("ReducedConnectionPadding 1").append('\n')
        return this
    }

    @SettingsConfig
    fun reducedConnectionPaddingFromSettings(): TorConfigBuilder {
        return if (settings!!.hasReducedConnectionPadding()) reducedConnectionPadding() else this
    }

    fun reset() {
        buffer = StringBuffer()
    }

    @SettingsConfig
    fun runAsDaemonFromSettings(): TorConfigBuilder {
        return if (settings!!.runAsDaemon()) runAsDaemon() else this
    }

    fun runAsDaemon(): TorConfigBuilder {
        buffer.append("RunAsDaemon 1").append('\n')
        return this
    }

    fun safeSocksDisable(): TorConfigBuilder {
        buffer.append("SafeSocks 0").append('\n')
        return this
    }

    fun safeSocksEnable(): TorConfigBuilder {
        buffer.append("SafeSocks 1").append('\n')
        return this
    }

    @SettingsConfig
    fun safeSocksFromSettings(): TorConfigBuilder {
        return if (!settings!!.hasSafeSocks()) safeSocksDisable() else safeSocksEnable()
    }

    @Throws(IOException::class)
    fun setGeoIpFiles(): TorConfigBuilder {
        val torConfig = context.getConfig()
        if (torConfig.geoIpFile.exists()) {
            geoIpFile(torConfig.geoIpFile.canonicalPath)
                .geoIpV6File(torConfig.geoIpv6File.canonicalPath)
        }
        return this
    }

    fun socksPort(socksPort: String?, isolationFlag: String?): TorConfigBuilder {
        if (isNullOrEmpty(socksPort)) {
            return this
        }
        buffer.append("SOCKSPort ").append(socksPort)
        if (!isNullOrEmpty(isolationFlag)) {
            buffer.append(" ").append(isolationFlag)
        }
        buffer.append(" KeepAliveIsolateSOCKSAuth")
        buffer.append(" IPv6Traffic")
        buffer.append(" PreferIPv6")
        buffer.append('\n')
        return this
    }

    @SettingsConfig
    fun socksPortFromSettings(): TorConfigBuilder {
        var socksPort = settings.getSocksPort()
        if (socksPort!!.indexOf(':') != -1) {
            socksPort = socksPort!!.split(":".toRegex()).toTypedArray()[1]
        }
        if (!socksPort.equals(
                "auto",
                ignoreCase = true
            ) && isLocalPortOpen(socksPort!!.toInt())
        ) {
            socksPort = "auto"
        }
        return socksPort(
            socksPort,
            if (settings!!.hasIsolationAddressFlagForTunnel()) "IsolateDestAddr" else null
        )
    }

    fun strictNodesDisable(): TorConfigBuilder {
        buffer.append("StrictNodes 0\n")
        return this
    }

    fun strictNodesEnable(): TorConfigBuilder {
        buffer.append("StrictNodes 1\n")
        return this
    }

    @SettingsConfig
    fun strictNodesFromSettings(): TorConfigBuilder {
        return if (settings!!.hasStrictNodes()) strictNodesEnable() else strictNodesDisable()
    }

    fun testSocksDisable(): TorConfigBuilder {
        buffer.append("TestSocks 0").append('\n')
        return this
    }

    fun testSocksEnable(): TorConfigBuilder {
        buffer.append("TestSocks 0").append('\n')
        return this
    }

    @SettingsConfig
    fun testSocksFromSettings(): TorConfigBuilder {
        return if (!settings!!.hasTestSocks()) testSocksDisable() else this
    }

    @SettingsConfig
    @Throws(UnsupportedEncodingException::class)
    fun torrcCustomFromSettings(): TorConfigBuilder {
        return if (settings.getCustomTorrc() != null) line(
            String(
                settings.getCustomTorrc().toByteArray(charset("US-ASCII"))
            )
        ) else this
    }

    fun transPort(transPort: String?): TorConfigBuilder {
        if (!isNullOrEmpty(transPort)) buffer.append("TransPort ")
            .append(transPort).append('\n')
        return this
    }

    @SettingsConfig
    fun transPortFromSettings(): TorConfigBuilder {
        return transPort(settings!!.transPort())
    }

    fun transportPlugin(clientPath: String?): TorConfigBuilder {
        buffer.append("ClientTransportPlugin meek_lite,obfs3,obfs4 exec ").append(clientPath)
            .append('\n')
        return this
    }

    fun useBridges(): TorConfigBuilder {
        buffer.append("UseBridges 1").append('\n')
        return this
    }

    @SettingsConfig
    fun useBridgesFromSettings(): TorConfigBuilder {
        return if (settings!!.hasBridges()) useBridges() else this
    }

    fun virtualAddressNetwork(address: String?): TorConfigBuilder {
        if (!isNullOrEmpty(address)) buffer.append("VirtualAddrNetwork ")
            .append(address).append('\n')
        return this
    }

    @SettingsConfig
    fun virtualAddressNetworkFromSettings(): TorConfigBuilder {
        return virtualAddressNetwork(settings.getVirtualAddressNetwork())
    }

    /**
     * Adds bridges from a resource stream. This relies on the TorInstaller to know how to obtain this stream.
     * These entries may be type-specified like:
     *
     * `
     * obfs3 169.229.59.74:31493 AF9F66B7B04F8FF6F32D455F05135250A16543C9
    ` *
     *
     * Or it may just be a custom entry like
     *
     * `
     * 69.163.45.129:443 9F090DE98CA6F67DEEB1F87EFE7C1BFD884E6E2F
    ` *
     *
     */
    @Throws(IOException::class)
    fun addBridgesFromResources(): TorConfigBuilder {
        if (settings!!.hasBridges()) {
            val bridgesStream = context.installer.openBridgesStream()
            val formatType = bridgesStream!!.read()
            if (formatType == 0) {
                addBridges(bridgesStream)
            } else {
                addCustomBridges(bridgesStream)
            }
        }
        return this
    }

    /**
     * Add bridges from bridges.txt file.
     */
    private fun addBridges(input: InputStream?) {
        if (input == null) {
            return
        }
        val bridges =
            readBridgesFromStream(input)
        for (b in bridges) {
            bridge(b.type, b.config)
        }
    }

    /**
     * Add custom bridges defined by the user. These will have a bridgeType of 'custom' as the first field.
     */
    private fun addCustomBridges(input: InputStream?) {
        if (input == null) {
            return
        }
        val bridges =
            readCustomBridgesFromStream(input)
        for (b in bridges) {
            if (b.type == "custom") {
                bridgeCustom(b.config)
            }
        }
    }

    private class Bridge(val type: String, val config: String)

    companion object {
        private fun isNullOrEmpty(value: String?): Boolean {
            return value == null || value.isEmpty()
        }

        private fun isLocalPortOpen(port: Int): Boolean {
            var socket: Socket? = null
            return try {
                socket = Socket()
                socket.connect(InetSocketAddress("127.0.0.1", port), 500)
                true
            } catch (e: Exception) {
                false
            } finally {
                if (socket != null) {
                    try {
                        socket.close()
                    } catch (ee: Exception) {
                    }
                }
            }
        }

        private fun readBridgesFromStream(input: InputStream): List<Bridge> {
            val bridges: MutableList<Bridge> =
                ArrayList()
            try {
                val br =
                    BufferedReader(InputStreamReader(input, "UTF-8"))
                var line = br.readLine()
                while (line != null) {
                    val tokens =
                        line.split("\\s+".toRegex(), 2).toTypedArray()
                    if (tokens.size != 2) {
                        line = br.readLine()
                        continue  //bad entry
                    }
                    bridges.add(
                        Bridge(
                            tokens[0],
                            tokens[1]
                        )
                    )
                    line = br.readLine()
                }
                br.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bridges
        }

        private fun readCustomBridgesFromStream(input: InputStream): List<Bridge> {
            val bridges: MutableList<Bridge> =
                ArrayList()
            try {
                val br =
                    BufferedReader(InputStreamReader(input, "UTF-8"))
                var line = br.readLine()
                while (line != null) {
                    if (line.isEmpty()) {
                        line = br.readLine()
                        continue
                    }
                    bridges.add(
                        Bridge(
                            "custom",
                            line
                        )
                    )
                    line = br.readLine()
                }
                br.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bridges
        }
    }

    init {
        settings = context.settings
        this.context = context
    }
}