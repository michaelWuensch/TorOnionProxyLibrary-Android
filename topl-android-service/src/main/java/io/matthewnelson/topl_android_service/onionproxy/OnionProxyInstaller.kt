package io.matthewnelson.topl_android_service.onionproxy

import android.content.Context
import io.matthewnelson.topl_android.util.TorInstaller
import io.matthewnelson.topl_android_settings.TorConfigFiles
import java.io.InputStream

class OnionProxyInstaller(
    private val context: Context,
    private val torConfigFiles: TorConfigFiles,
    private val appVersionCode: Int,
    private val geoipAssetPath: String,
    private val geoip6AssetPath: String
): TorInstaller() {

    override fun setup(): Boolean {
        TODO("Not yet implemented")
        return true
    }

    private fun copyAssets() {
        TODO("Not yet implemented")
    }

    override fun updateTorConfigCustom(content: String?) {
        TODO("Not yet implemented")
    }

    override fun openBridgesStream(): InputStream? {
        TODO("Not yet implemented")
    }

}