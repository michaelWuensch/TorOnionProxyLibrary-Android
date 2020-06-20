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
package io.matthewnelson.topl_android_settings

import android.content.Context
import java.io.File
import java.io.IOException

/**
 * Holds Tor configuration information for files and directories that Tor will use.
 *
 * See [Companion.createConfig] or [Builder] to instantiate.
 * */
class TorConfigFiles private constructor(
    val geoIpFile: File,
    val geoIpv6File: File,
    torrcFile: File,
    val torExecutableFile: File,
    val hiddenServiceDir: File,
    val dataDir: File,
    val configDir: File,

    /**
     * The <base32-encoded-fingerprint>.onion domain name for this hidden service.
     * If the hidden service is restricted to authorized clients only, this file
     * also contains authorization data for all clients.
     *
     * @return [hostnameFile] </base32-encoded-fingerprint>
     * */
    val hostnameFile: File,

    /**
     * Used for cookie authentication with the controller. Location can be
     * overridden by the CookieAuthFile config option. Regenerated on startup.
     * See control-spec.txt in torspec for details.
     *
     * Only used when cookie authentication is enabled.
     *
     * @return [cookieAuthFile]
     * */
    val cookieAuthFile: File,
    val libraryPath: File,
    val resolveConf: File,
    val controlPortFile: File,
    val installDir: File,

    /**
     * When tor starts it waits for the control port and cookie auth files to be created
     * before it proceeds to the next step in startup. If these files are not created
     * after a certain amount of time, then the startup has failed.
     *
     * This method returns how much time to wait in seconds until failing the startup.
     * */
    val fileCreationTimeout: Int
) {

    companion object {
        const val GEO_IP_NAME = "geoip"
        const val GEO_IPV_6_NAME = "geoip6"
        const val TORRC_NAME = "torrc"
        const val HIDDEN_SERVICE_NAME = "hiddenservice"
        const val HOST_FILE_NAME = "hostname"
        const val COOKIE_AUTH_FILE_NAME = "control_auth_cookie"
        const val RESOLVE_CONF_FILE_NAME = "resolv.conf"
        const val CONTROL_PORT_FILE_NAME = "control.txt"

        /**
         * If your .so file name is different, call [Builder.torExecutable] to set it
         * manually rather than using the [createConfig] convenience method.
         * */
        private const val torExecutableFileName: String = "libTor.so"

        /**
         * Convenience method for if you're including in your App's jniLibs directory
         * the `libTor.so` binary, or utilizing those maintained by this project.
         *
         * @param [context] Context
         * @param [configDir] context.getDir("dir_name_here", Context.MODE_PRIVATE)
         * @param [dataDir] if you wish it in a different location than lib/tor
         * */
        fun createConfig(context: Context, configDir: File, dataDir: File? = null): TorConfigFiles {
            val installDir = File(context.applicationInfo.nativeLibraryDir)
            val builder =
                Builder(
                    installDir,
                    configDir
                )
            if (dataDir != null)
                builder.dataDir(dataDir)
            return builder.build()
        }

        /**
         * Convenience method for setting up all of your files and directories in their
         * default locations.
         *
         * @param context Context
         * */
        fun createConfig(context: Context): TorConfigFiles =
            createConfig(
                context,
                context.getDir("torservice", Context.MODE_PRIVATE)
            )
    }

    private val configLock = Object()
    var torrcFile = torrcFile
        private set

    /**
     * Resolves the tor configuration file. If the torrc file hasn't been set, then
     * this method will attempt to resolve the config file by looking in the root of
     * the $configDir and then in $user.home directory
     *
     * @return [torrcFile]
     * @throws [IOException] If torrc file is not resolved.
     * @throws [SecurityException] Unauthorized access to file/directory.
     * */
    @Throws(IOException::class, SecurityException::class)
    fun resolveTorrcFile(): File {
        synchronized(configLock) {
            if (torrcFile.exists()) {
                return torrcFile
            }

            val tmpTorrcFile = File(configDir, TORRC_NAME)
            if (tmpTorrcFile.exists()) {
                torrcFile = tmpTorrcFile
                return torrcFile
            }

            torrcFile = File(configDir, TORRC_NAME)
            if (torrcFile.createNewFile()) {
                return torrcFile
            }

            throw IOException("Failed to create torrc file")
        }
    }

    override fun toString(): String {
        return "TorConfigFiles{ " +
                "geoIpFile=$geoIpFile, " +
                "geoIpv6File=$geoIpv6File, " +
                "torrcFile=$torrcFile, " +
                "torExecutableFile=$torExecutableFile, " +
                "hiddenServiceDir=$hiddenServiceDir, " +
                "dataDir=$dataDir, " +
                "configDir=$configDir, " +
                "installDir=$installDir, " +
                "hostnameFile=$hostnameFile, " +
                "cookieAuthFile=$cookieAuthFile, " +
                "libraryPath=$libraryPath }"
    }

    /**
     * Builder for TorConfig.
     *
     * See also [Companion.createConfig] for convenience methods.
     *
     * @param [installDir] directory where the tor binaries are installed.
     * @param [configDir] directory where the filesystem will be setup for tor.
     */
    class Builder(private val installDir: File, private val configDir: File) {

        private lateinit var mTorExecutableFile: File
        private lateinit var mGeoIpFile: File
        private lateinit var mGeoIpv6File: File
        private lateinit var mTorrcFile: File
        private lateinit var mHiddenServiceDir: File
        private lateinit var mDataDir: File
        private lateinit var mLibraryPath: File
        private lateinit var mCookieAuthFile: File
        private lateinit var mHostnameFile: File
        private lateinit var mResolveConf: File
        private lateinit var mControlPortFile: File
        private var mFileCreationTimeout = 0

        fun torExecutable(file: File): Builder {
            mTorExecutableFile = file
            return this
        }

        /**
         * Store data files for a hidden service in DIRECTORY. Every hidden service must
         * have a separate directory. You may use this option multiple times to specify
         * multiple services. If DIRECTORY does not exist, Tor will create it. (Note: in
         * current versions of Tor, if DIRECTORY is a relative path, it will be relative
         * to the current working directory of Tor instance, not to its DataDirectory. Do
         * not rely on this behavior; it is not guaranteed to remain the same in future
         * versions.)
         *
         * Default value: $configDir/hiddenservices
         *
         * @param [directory] hidden services directory
         *
         * @return [Builder]
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
         * @param [file] geoip6 file
         *
         * @return [Builder]
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
         * @param [file] geoip file
         *
         * @return [Builder]
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
         * @param [directory] directory where tor runtime data is stored
         *
         * @return [Builder]
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
         * @param [file] your torrc file
         *
         * @return [Builder]
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
         * When tor starts it waits for the control port and cookie auth files to be
         * created before it proceeds to the next step in startup. If these files are
         * not created after a certain amount of time, then the startup has failed.
         *
         * This method specifies how much time to wait until failing the startup.
         *
         * Default value is 15 seconds
         *
         * @param [timeoutSeconds] Int
         *
         * @return [Builder]
         */
        fun fileCreationTimeout(timeoutSeconds: Int): Builder {
            mFileCreationTimeout = timeoutSeconds
            return this
        }

        /**
         * Builds torConfig and sets default values if not explicitly configured through builder.
         *
         * @return [TorConfigFiles]
         */
        fun build(): TorConfigFiles {
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
                mLibraryPath = mTorExecutableFile.parentFile

            if (!::mHostnameFile.isInitialized)
                mHostnameFile = File(mDataDir, HOST_FILE_NAME)

            if (!::mCookieAuthFile.isInitialized)
                mCookieAuthFile = File(mDataDir, COOKIE_AUTH_FILE_NAME)

            if (!::mResolveConf.isInitialized)
                mResolveConf = File(configDir, RESOLVE_CONF_FILE_NAME)

            if (!::mControlPortFile.isInitialized)
                mControlPortFile = File(mDataDir, CONTROL_PORT_FILE_NAME)

            if (mFileCreationTimeout <= 0)
                mFileCreationTimeout = 15

            return TorConfigFiles(
                mGeoIpFile,
                mGeoIpv6File,
                mTorrcFile,
                mTorExecutableFile,
                mHiddenServiceDir,
                mDataDir,
                configDir,
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