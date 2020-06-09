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
package com.msopentech.thali.toronionproxy

import java.io.File
import java.io.IOException

/**
 * Holds Tor configuration information.
 */
class TorConfig private constructor(
    val geoIpFile: File,
    val geoIpv6File: File,
    torrcFile: File,
    val torExecutableFile: File,
    val hiddenServiceDir: File,
    val dataDir: File,
    val configDir: File,
    val homeDir: File,

    /**
     * The <base32-encoded-fingerprint>.onion domain name for this hidden service. If the hidden service is
     * restricted to authorized clients only, this file also contains authorization data for all clients.
     *
     * @return hostname file
    </base32-encoded-fingerprint> */
    val hostnameFile: File,

    /**
     * Used for cookie authentication with the controller. Location can be overridden by the CookieAuthFile config option.
     * Regenerated on startup. See control-spec.txt in torspec for details. Only used when cookie authentication is enabled.
     *
     * @return
     */
    val cookieAuthFile: File,
    val libraryPath: File,
    val resolveConf: File,
    val controlPortFile: File,
    val installDir: File,

    /**
     * When tor starts it waits for the control port and cookie auth files to be created before it proceeds to the
     * next step in startup. If these files are not created after a certain amount of time, then the startup has
     * failed.
     *
     * This method returns how much time to wait in seconds until failing the startup.
     */
    val fileCreationTimeout: Int
) {

    companion object {
        const val GEO_IP_NAME = "geoip"
        const val GEO_IPV_6_NAME = "geoip6"
        const val TORRC_NAME = "torrc"
        private const val HIDDEN_SERVICE_NAME = "hiddenservice"

        /**
         * Creates simplest default config. All tor files will be relative to the configDir root.
         *
         * @param configDir
         * @return
         */
        fun createDefault(configDir: File): TorConfig =
            Builder(configDir, configDir).build()

        /**
         * All files will be in single directory: collapses the data and config directories
         *
         * @param configDir
         * @return
         */
        fun createFlatConfig(configDir: File): TorConfig =
            createConfig(configDir, configDir, configDir)

        fun createConfig(installDir: File, configDir: File, dataDir: File): TorConfig {
            val builder = Builder(installDir, configDir)
            builder.dataDir(dataDir)
            return builder.build()
        }
    }

    private val configLock = Object()
    private var mTorrcFile = torrcFile
    fun getTorrcFile(): File = mTorrcFile

    /**
     * Resolves the tor configuration file. If the torrc file hasn't been set, then this method will attempt to
     * resolve the config file by looking in the root of the $configDir and then in $user.home directory
     *
     * @return torrc file
     * @throws IOException if torrc file is not resolved
     */
    @Throws(IOException::class)
    fun resolveTorrcFile(): File {

        synchronized(configLock) {
            if (!mTorrcFile.exists()) {
                var tmpTorrcFile = File(configDir, TORRC_NAME)

                if (!tmpTorrcFile.exists()) {
                    tmpTorrcFile = File(homeDir, ".$TORRC_NAME")

                    if (!tmpTorrcFile.exists()) {
                        mTorrcFile = File(configDir, TORRC_NAME)

                        if (!mTorrcFile.createNewFile()) {
                            throw IOException("Failed to create torrc file")
                        }

                    } else {
                        mTorrcFile = tmpTorrcFile
                    }
                } else {
                    mTorrcFile = tmpTorrcFile
                }
            }
            return mTorrcFile
        }
    }

    override fun toString(): String {
        return "TorConfig{" +
                "geoIpFile=" + geoIpFile +
                ", geoIpv6File=" + geoIpv6File +
                ", torrcFile=" + mTorrcFile +
                ", torExecutableFile=" + torExecutableFile +
                ", hiddenServiceDir=" + hiddenServiceDir +
                ", dataDir=" + dataDir +
                ", configDir=" + configDir +
                ", installDir=" + installDir +
                ", homeDir=" + homeDir +
                ", hostnameFile=" + hostnameFile +
                ", cookieAuthFile=" + cookieAuthFile +
                ", libraryPath=" + libraryPath +
                '}'
    }

    /**
     * Builder for TorConfig.
     */
    class Builder(private val installDir: File, private val configDir: File) {

        companion object {
            const val torExecutableFileName: String = "libTor.so"
        }

        private lateinit var mTorExecutableFile: File
        private lateinit var mGeoIpFile: File
        private lateinit var mGeoIpv6File: File
        private lateinit var mTorrcFile: File
        private lateinit var mHiddenServiceDir: File
        private lateinit var mDataDir: File
        private lateinit var mHomeDir: File
        private lateinit var mLibraryPath: File
        private lateinit var mCookieAuthFile: File
        private lateinit var mHostnameFile: File
        private lateinit var mResolveConf: File
        private lateinit var mControlPortFile: File
        private var mFileCreationTimeout = 0

        /**
         * Home directory of user.
         *
         *
         * Default value: $home.user if $home.user environment property exists, otherwise $configDir. On Android, this
         * will always default to $configDir.
         *
         * @param homeDir the home directory of the user
         * @return builder
         */
        fun homeDir(homeDir: File): Builder {
            this.mHomeDir = homeDir
            return this
        }

        fun torExecutable(file: File): Builder {
            mTorExecutableFile = file
            return this
        }

        /**
         * Store data files for a hidden service in DIRECTORY. Every hidden service must have a separate directory.
         * You may use this option multiple times to specify multiple services. If DIRECTORY does not exist, Tor will
         * create it. (Note: in current versions of Tor, if DIRECTORY is a relative path, it will be relative to the
         * current working directory of Tor instance, not to its DataDirectory. Do not rely on this behavior; it is not
         * guaranteed to remain the same in future versions.)
         *
         * Default value: $configDir/hiddenservices
         *
         * @param directory hidden services directory
         * @return builder
         */
        fun hiddenServiceDir(directory: File): Builder {
            mHiddenServiceDir = directory
            return this
        }

        /**
         * A filename containing IPv6 GeoIP data, for use with by-country statistics.
         *
         * Default value: $configDir/geoip6
         *
         * @param file geoip6 file
         * @return builder
         */
        fun geoipv6(file: File): Builder {
            mGeoIpv6File = file
            return this
        }

        /**
         * A filename containing IPv4 GeoIP data, for use with by-country statistics.
         *
         * Default value: $configDir/geoip
         *
         * @param file geoip file
         * @return builder
         */
        fun geoip(file: File): Builder {
            mGeoIpFile = file
            return this
        }

        /**
         * Store working data in DIR. Can not be changed while tor is running.
         *
         * Default value: $configDir/lib/tor
         *
         * @param directory directory where tor runtime data is stored
         * @return builder
         */
        fun dataDir(directory: File): Builder {
            mDataDir = directory
            return this
        }

        /**
         * The configuration file, which contains "option value" pairs.
         *
         * Default value: $configDir/torrc
         *
         * @param file
         * @return
         */
        fun torrc(file: File): Builder {
            mTorrcFile = file
            return this
        }

        fun libraryPath(directory: File): Builder {
            mLibraryPath = directory
            return this
        }

        fun cookieAuthFile(file: File): Builder {
            mCookieAuthFile = file
            return this
        }

        fun hostnameFile(file: File): Builder {
            mHostnameFile = file
            return this
        }

        fun resolveConf(resolveConf: File): Builder {
            this.mResolveConf = resolveConf
            return this
        }

        /**
         * When tor starts it waits for the control port and cookie auth files to be created before it proceeds to the
         * next step in startup. If these files are not created after a certain amount of time, then the startup has
         * failed.
         *
         * This method specifies how much time to wait until failing the startup.
         *
         * @param timeout in seconds
         */
        fun fileCreationTimeout(timeout: Int): Builder {
            mFileCreationTimeout = timeout
            return this
        }

        /**
         * Builds torConfig and sets default values if not explicitly configured through builder.
         *
         * @return torConfig
         */
        fun build(): TorConfig {
            if (!::mHomeDir.isInitialized) {
                val userHome = System.getProperty("user.home")
                mHomeDir =
                    if (userHome != null && "" != userHome && "/" != userHome)
                        File(userHome)
                    else
                        configDir
            }

            if (!::mTorExecutableFile.isInitialized)
                mTorExecutableFile = File(installDir, torExecutableFileName)

            if (!::mGeoIpFile.isInitialized)
                mGeoIpFile = File(configDir, GEO_IP_NAME)

            if (!::mGeoIpv6File.isInitialized)
                mGeoIpv6File = File(configDir, GEO_IPV_6_NAME)

            if (!::mTorrcFile.isInitialized)
                mTorrcFile = File(configDir, TORRC_NAME)

            if (!::mHiddenServiceDir.isInitialized)
                mHiddenServiceDir = File(configDir, HIDDEN_SERVICE_NAME)

            if (!::mDataDir.isInitialized)
                mDataDir = File(configDir, "lib/tor")

            if (!::mLibraryPath.isInitialized)
                mLibraryPath = File(installDir, torExecutableFileName).parentFile

            if (!::mHostnameFile.isInitialized)
                mHostnameFile = File(mDataDir, "hostname")

            if (!::mCookieAuthFile.isInitialized)
                mCookieAuthFile = File(mDataDir, "control_auth_cookie")

            if (!::mResolveConf.isInitialized)
                mResolveConf = File(configDir, "resolv.conf")

            if (!::mControlPortFile.isInitialized)
                mControlPortFile = File(mDataDir, "control.txt")

            if (mFileCreationTimeout <= 0)
                mFileCreationTimeout = 15

            return TorConfig(
                mGeoIpFile,
                mGeoIpv6File,
                mTorrcFile,
                mTorExecutableFile,
                mHiddenServiceDir,
                mDataDir,
                configDir,
                mHomeDir,
                mHostnameFile,
                mCookieAuthFile,
                mLibraryPath,
                mResolveConf,
                mControlPortFile,
                installDir,
                mFileCreationTimeout
            )
        }
    }
}