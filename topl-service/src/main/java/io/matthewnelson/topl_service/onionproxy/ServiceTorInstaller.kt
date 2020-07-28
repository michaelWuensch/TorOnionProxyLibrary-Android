/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash `74407114cbfa8ea6f2ac51417dda8be98d8aba86`. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify it
*     under the terms of the GNU General Public License as published by the
*     Free Software Foundation, either version 3 of the License, or (at your
*     option) any later version.
*
*     This program is distributed in the hope that it will be useful, but
*     WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*     or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
*     for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program. If not, see <https://www.gnu.org/licenses/>.
*
* `===========================================================================`
* `+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++`
* `===========================================================================`
*
* The following exception is an additional permission under section 7 of the
* GNU General Public License, version 3 (“GPLv3”).
*
*     "The Interfaces" is henceforth defined as Application Programming Interfaces
*     that are publicly available classes/functions/etc (ie: do not contain the
*     visibility modifiers `internal`, `private`, `protected`, or are within
*     classes/functions/etc that contain the aforementioned visibility modifiers)
*     to TorOnionProxyLibrary-Android users that are needed to implement
*     TorOnionProxyLibrary-Android and reside in ONLY the following modules:
*
*      - topl-core-base
*      - topl-service
*
*     The following are excluded from "The Interfaces":
*
*       - All other code
*
*     Linking TorOnionProxyLibrary-Android statically or dynamically with other
*     modules is making a combined work based on TorOnionProxyLibrary-Android.
*     Thus, the terms and conditions of the GNU General Public License cover the
*     whole combination.
*
*     As a special exception, the copyright holder of TorOnionProxyLibrary-Android
*     gives you permission to combine TorOnionProxyLibrary-Android program with free
*     software programs or libraries that are released under the GNU LGPL and with
*     independent modules that communicate with TorOnionProxyLibrary-Android solely
*     through "The Interfaces". You may copy and distribute such a system following
*     the terms of the GNU GPL for TorOnionProxyLibrary-Android and the licenses of
*     the other code concerned, provided that you include the source code of that
*     other code when and as the GNU GPL requires distribution of source code and
*     provided that you do not modify "The Interfaces".
*
*     Note that people who make modified versions of TorOnionProxyLibrary-Android
*     are not obligated to grant this special exception for their modified versions;
*     it is their choice whether to do so. The GNU General Public License gives
*     permission to release a modified version without this exception; this exception
*     also makes it possible to release a modified version which carries forward this
*     exception. If you modify "The Interfaces", this exception does not apply to your
*     modified version of TorOnionProxyLibrary-Android, and you must remove this
*     exception when you distribute your modified version.
* */
package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core.util.FileUtilities
import io.matthewnelson.topl_core.util.TorInstaller
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.R
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyList
import io.matthewnelson.topl_service.prefs.TorServicePrefs
import io.matthewnelson.topl_service.service.BaseService
import java.io.*
import java.util.concurrent.TimeoutException

/**
 * Installs things needed for Tor.
 *
 * See [io.matthewnelson.topl_service.TorServiceController.Builder]
 *
 * @param [torService] [BaseService] for context
 * */
internal class ServiceTorInstaller(private val torService: BaseService): TorInstaller() {

    private val torConfigFiles: TorConfigFiles
        get() = TorServiceController.getTorConfigFiles()
    private val buildConfigVersionCode: Int
        get() = BaseService.buildConfigVersionCode
    private val buildConfigDebug: Boolean
        get() = BaseService.buildConfigDebug ?: false

    private val torServicePrefs = TorServicePrefs(torService.context)

    private val localPrefs = BaseService.getLocalPrefs(torService.context)
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
            copyAsset(BaseService.geoipAssetPath, torConfigFiles.geoIpFile)
        }

    private fun copyGeoIpv6Asset() =
        synchronized(torConfigFiles.geoIpv6FileLock) {
            copyAsset(BaseService.geoip6AssetPath, torConfigFiles.geoIpv6File)
        }

    @Throws(IOException::class)
    private fun copyAsset(assetPath: String, file: File) {
        try {
            FileUtilities.copy(torService.context.assets.open(assetPath), file.outputStream())
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
                torService.context.resources.openRawResource(R.raw.bridges)
        return SequenceInputStream(bridgeTypeStream, bridgeStream)
    }

}