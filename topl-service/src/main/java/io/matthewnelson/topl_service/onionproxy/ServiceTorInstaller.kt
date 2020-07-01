package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core.util.FileUtilities
import io.matthewnelson.topl_core.util.TorInstaller
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.BuildConfig
import io.matthewnelson.topl_service.R
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyList
import io.matthewnelson.topl_service.util.TorServicePrefs
import java.io.*
import java.util.concurrent.TimeoutException

/**
 * Installs assets needed for Tor.
 *
 * @param [torService] for context.
 * @param [torConfigFiles] [TorConfigFiles] to know where files/directories are.
 * @param [buildConfigVersionCode] Use [BuildConfig.VERSION_CODE]
 * @param [buildConfigDebug] Use [BuildConfig.DEBUG]
 * @param [geoIpAssetPath] The path to geoip file within the application, ex: "common/geoip"
 * @param [geoIp6AssetPath] The path to geoip6 file within the application, ex: "common/geoip6"
 *
 * See [io.matthewnelson.topl_service.TorServiceController.Builder]
 * */
internal class ServiceTorInstaller(
    private val torService: TorService,
    private val torConfigFiles: TorConfigFiles,
    private val buildConfigVersionCode: Int,
    private val buildConfigDebug: Boolean,
    private val geoIpAssetPath: String,
    private val geoIp6AssetPath: String
): TorInstaller() {

    private val prefs = TorServicePrefs(torService)
    private lateinit var geoIpFileCopied: String
    private lateinit var geoIpv6FileCopied: String

    // broadcastLogger is available from TorInstaller and is instantiated as soon as
    // OnionProxyManager gets initialized.
//    private lateinit var broadcastLogger: BroadcastLogger

    companion object {
        private const val VERSION_CODE = "BUILD_CONFIG_VERSION_CODE"
    }

    @Throws(IOException::class, SecurityException::class)
    override fun setup() {
        if (!torConfigFiles.geoIpFile.exists())
            copyAsset(geoIpAssetPath, torConfigFiles.geoIpFile)
            geoIpFileCopied = ""
        if (!torConfigFiles.geoIpv6File.exists())
            copyAsset(geoIp6AssetPath, torConfigFiles.geoIpv6File)
            geoIpv6FileCopied = ""

        // If the app version has been increased, or if this is a debug build, copy over
        // geoip assets then update SharedPreferences with the new version code. This
        // mitigates copying to be done only if a version upgrade is had.
        if (buildConfigVersionCode > prefs.getInt(VERSION_CODE, -1) ?: -1 || buildConfigDebug) {
            if (!::geoIpFileCopied.isInitialized)
                copyAsset(geoIpAssetPath, torConfigFiles.geoIpFile)
            if (!::geoIpv6FileCopied.isInitialized)
                copyAsset(geoIp6AssetPath, torConfigFiles.geoIpv6File)
            prefs.putInt(VERSION_CODE, buildConfigVersionCode)
        }
    }

    @Throws(IOException::class)
    private fun copyAsset(assetPath: String, file: File) {
        try {
            FileUtilities.copy(torService.assets.open(assetPath), file.outputStream())
            broadcastLogger?.debug("Asset copied from $assetPath -> ${file.name}")
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
            prefs.getList(PrefKeyList.LIST_OF_SUPPORTED_BRIDGES, arrayListOf()).joinToString()
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