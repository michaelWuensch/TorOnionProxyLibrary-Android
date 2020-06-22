package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core.util.FileUtilities
import io.matthewnelson.topl_core.util.TorInstaller
import io.matthewnelson.topl_core_base.SettingsConsts.SupportedBridges
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.BuildConfig
import io.matthewnelson.topl_service.R
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.util.PrefsKeys.ListKey
import io.matthewnelson.topl_service.util.TorServicePrefs
import java.io.*
import java.util.concurrent.TimeoutException

/**
 * Installs assets needed for Tor.
 *
 * @param [torService] for context.
 * @param [torConfigFiles] [TorConfigFiles] to know where files/directories are.
 * @param [appVersionCode] Mitigate copying of geoip files to app updates only. Use [BuildConfig.VERSION_CODE].
 * @param [geoIpAssetPath] The path to geoip file within the application, ex: "common/geoip"
 * @param [geoIp6AssetPath] The path to geoip6 file within the application, ex: "common/geoip6"
 *
 * See [io.matthewnelson.topl_service.TorServiceController.Builder]
 * */
internal class OnionProxyInstaller(
    private val torService: TorService,
    private val torConfigFiles: TorConfigFiles,
    private val appVersionCode: Int,
    private val geoIpAssetPath: String,
    private val geoIp6AssetPath: String
): TorInstaller() {

    private val prefs = TorServicePrefs(torService)
    private lateinit var geoIpFileCoppied: String
    private lateinit var geoIp6FileCoppied: String

    @Throws(IOException::class, SecurityException::class)
    override fun setup() {
        if (!torConfigFiles.geoIpFile.exists())
            copyAsset(geoIpAssetPath, torConfigFiles.geoIpFile)
            geoIpFileCoppied = ""
        if (!torConfigFiles.geoIpv6File.exists())
            copyAsset(geoIp6AssetPath, torConfigFiles.geoIpv6File)
            geoIp6FileCoppied = ""

        // If the app version has been increased, or if this is a debug build of topl-service
        // module, copy over geoip assets then update SharedPreferences with the new appVersion
        if (appVersionCode > prefs.getInt("APP_VERSION", -1) ?: -1 || BuildConfig.DEBUG) {
            if (!::geoIpFileCoppied.isInitialized)
                copyAsset(geoIpAssetPath, torConfigFiles.geoIpFile)
            if (!::geoIp6FileCoppied.isInitialized)
                copyAsset(geoIp6AssetPath, torConfigFiles.geoIpv6File)
            prefs.putInt("APP_VERSION", appVersionCode)
        }
    }

    private fun copyAsset(assetPath: String, file: File) {
        FileUtilities.copy(
            torService.assets.open(assetPath),
            file.outputStream()
        )
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
        val userDefinedBridgeList: String =
            prefs.getList(ListKey.LIST_OF_SUPPORTED_BRIDGES, arrayListOf()).joinToString()
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