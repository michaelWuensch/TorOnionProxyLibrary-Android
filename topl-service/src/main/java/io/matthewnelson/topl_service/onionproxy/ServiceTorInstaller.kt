/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core.util.FileUtilities
import io.matthewnelson.topl_core.util.TorInstaller
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.R
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyList
import io.matthewnelson.topl_service.prefs.TorServicePrefs
import java.io.*
import java.util.concurrent.TimeoutException

/**
 * Installs things needed for Tor.
 *
 * See [io.matthewnelson.topl_service.TorServiceController.Builder]
 *
 * @param [torService] for context
 * */
internal class ServiceTorInstaller(private val torService: TorService): TorInstaller() {

    private val torConfigFiles: TorConfigFiles
        get() = TorServiceController.getTorConfigFiles()
    private val buildConfigVersionCode: Int
        get() = TorService.buildConfigVersionCode
    private val buildConfigDebug: Boolean
        get() = TorService.buildConfigDebug ?: false
    private val geoIpAssetPath: String
        get() = TorService.geoipAssetPath
    private val geoIp6AssetPath: String
        get() = TorService.geoip6AssetPath

    private val torServicePrefs = TorServicePrefs(torService)

    private val localPrefs = TorService.getLocalPrefs(torService)
    private lateinit var geoIpFileCopied: String
    private lateinit var geoIpv6FileCopied: String

    // broadcastLogger is available from TorInstaller and is instantiated as soon as
    // OnionProxyManager gets initialized.
//    private lateinit var broadcastLogger: BroadcastLogger

    companion object {
        private const val APP_VERSION_CODE = "APP_VERSION_CODE"
    }

    @Throws(IOException::class, SecurityException::class)
    override fun setup() {
        if (!torConfigFiles.geoIpFile.exists()) {
            copyGeoIpAsset()
            geoIpFileCopied = ""
        }
        if (!torConfigFiles.geoIpv6File.exists()) {
            copyGeoIpv6Asset()
            geoIpv6FileCopied = ""
        }

        // If the app version has been increased, or if this is a debug build, copy over
        // geoip assets then update SharedPreferences with the new version code. This
        // mitigates copying to be done only if a version upgrade is had.
        if (buildConfigDebug || buildConfigVersionCode > localPrefs.getInt(APP_VERSION_CODE, -1)) {
            if (!::geoIpFileCopied.isInitialized) {
                copyGeoIpAsset()
            }
            if (!::geoIpv6FileCopied.isInitialized) {
                copyGeoIpv6Asset()
            }
            localPrefs.edit().putInt(APP_VERSION_CODE, buildConfigVersionCode).apply()
        }
    }

    private fun copyGeoIpAsset() =
        synchronized(torConfigFiles.geoIpFileLock) {
            copyAsset(geoIpAssetPath, torConfigFiles.geoIpFile)
        }

    private fun copyGeoIpv6Asset() =
        synchronized(torConfigFiles.geoIpv6FileLock) {
            copyAsset(geoIp6AssetPath, torConfigFiles.geoIpv6File)
        }

    @Throws(IOException::class)
    private fun copyAsset(assetPath: String, file: File) {
        try {
            FileUtilities.copy(torService.assets.open(assetPath), file.outputStream())
            broadcastLogger?.debug("Asset copied from $assetPath -> $file")
        } catch (e: Exception) {
            throw IOException("Failed copying asset from $assetPath", e)
        }
    }

    @Throws(IOException::class, TimeoutException::class)
    override fun updateTorConfigCustom(content: String?) {

    }

    @Throws(IOException::class)
    override fun openBridgesStream(): InputStream? {
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
            torServicePrefs.getList(PrefKeyList.LIST_OF_SUPPORTED_BRIDGES, arrayListOf()).joinToString()
        var bridgeType = (if (userDefinedBridgeList.length > 9) 1 else 0).toByte()
        // Terrible hack. Must keep in sync with topl::addBridgesFromResources.
        if (bridgeType.toInt() == 0) {
            when (userDefinedBridgeList) {
                SupportedBridges.OBFS4 -> bridgeType = 2
                SupportedBridges.MEEK -> bridgeType = 3
                SupportedBridges.SNOWFLAKE -> bridgeType = 4
            }
        }

        val bridgeTypeStream = ByteArrayInputStream(byteArrayOf(bridgeType))
        val bridgeStream =
            if (bridgeType.toInt() == 1)
                ByteArrayInputStream(userDefinedBridgeList.toByteArray())
            else
                torService.resources.openRawResource(R.raw.bridges)
        return SequenceInputStream(bridgeTypeStream, bridgeStream)
    }

}