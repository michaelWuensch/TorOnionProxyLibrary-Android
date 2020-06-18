package io.matthewnelson.topl_service.onionproxy

import android.content.Context
import io.matthewnelson.topl_android.util.TorInstaller
import io.matthewnelson.topl_android_settings.TorConfigFiles
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeoutException

class OnionProxyInstaller(
    private val context: Context,
    private val torConfigFiles: TorConfigFiles,
    private val appVersionCode: Int,
    private val geoipAssetPath: String,
    private val geoip6AssetPath: String
): TorInstaller() {

    @Throws(IOException::class)
    override fun setup() {
        TODO("Not yet implemented")
    }

    private fun copyAssets() {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class, TimeoutException::class)
    override fun updateTorConfigCustom(content: String?) {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    override fun openBridgesStream(): InputStream? {
        TODO("Not yet implemented")
    }

}