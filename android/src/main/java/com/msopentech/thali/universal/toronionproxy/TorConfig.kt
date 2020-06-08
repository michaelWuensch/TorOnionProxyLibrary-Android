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

import com.msopentech.thali.universal.toronionproxy.OsData.OsType
import java.io.File
import java.io.IOException

/**
 * Holds Tor configuration information.
 */
class TorConfig {
    private val configLock = Any()
    var geoIpFile: File? = null
        private set
    var geoIpv6File: File? = null
        private set
    var torrcFile: File? = null
        private set
    var torExecutableFile: File? = null
        private set
    var hiddenServiceDir: File? = null
        private set
    var dataDir: File? = null
        private set
    var configDir: File? = null
        private set
    var homeDir: File? = null
        private set

    /**
     * The <base32-encoded-fingerprint>.onion domain name for this hidden service. If the hidden service is
     * restricted to authorized clients only, this file also contains authorization data for all clients.
     *
     * @return hostname file
    </base32-encoded-fingerprint> */
    var hostnameFile: File? = null
        private set

    /**
     * Used for cookie authentication with the controller. Location can be overridden by the CookieAuthFile config option.
     * Regenerated on startup. See control-spec.txt in torspec for details. Only used when cookie authentication is enabled.
     *
     * @return
     */
    var cookieAuthFile: File? = null
        private set
    var libraryPath: File? = null
        private set
    var resolveConf: File? = null
        private set
    var controlPortFile: File? = null
        private set
    var installDir: File? = null
        private set

    /**
     * When tor starts it waits for the control port and cookie auth files to be created before it proceeds to the
     * next step in startup. If these files are not created after a certain amount of time, then the startup has
     * failed.
     *
     * This method returns how much time to wait in seconds until failing the startup.
     */
    var fileCreationTimeout = 0
        private set

    /**
     * Resolves the tor configuration file. If the torrc file hasn't been set, then this method will attempt to
     * resolve the config file by looking in the root of the $configDir and then in $user.home directory
     *
     * @return torrc file
     * @throws IOException if torrc file is not resolved
     */
    @Throws(IOException::class)
    fun resolveTorrcFile(): File? {
        synchronized(configLock) {
            if (torrcFile == null || !torrcFile!!.exists()) {
                var tmpTorrcFile =
                    File(configDir, TORRC_NAME)
                if (!tmpTorrcFile.exists()) {
                    tmpTorrcFile = File(homeDir, ".$TORRC_NAME")
                    if (!tmpTorrcFile.exists()) {
                        torrcFile = File(configDir, TORRC_NAME)
                        if (!torrcFile!!.createNewFile()) {
                            throw IOException("Failed to create torrc file")
                        }
                    } else {
                        torrcFile = tmpTorrcFile
                    }
                } else {
                    torrcFile = tmpTorrcFile
                }
            }
            return torrcFile
        }
    }

    override fun toString(): String {
        return "TorConfig{" +
                "geoIpFile=" + geoIpFile +
                ", geoIpv6File=" + geoIpv6File +
                ", torrcFile=" + torrcFile +
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
    class Builder(installDir: File?, configDir: File?) {
        private var torExecutableFile: File? = null
        private val configDir: File
        private var geoIpFile: File? = null
        private var geoIpv6File: File? = null
        private var torrcFile: File? = null
        private var hiddenServiceDir: File? = null
        private var dataDir: File? = null
        private var homeDir: File? = null
        private var libraryPath: File? = null
        private var cookieAuthFile: File? = null
        private var hostnameFile: File? = null
        private var resolveConf: File? = null
        private var controlPortFile: File? = null
        private var installDir: File
        private var fileCreationTimeout = 0

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
        fun homeDir(homeDir: File?): Builder {
            this.homeDir = homeDir
            return this
        }

        fun torExecutable(file: File?): Builder {
            torExecutableFile = file
            return this
        }

        /**
         * Store data files for a hidden service in DIRECTORY. Every hidden service must have a separate directory.
         * You may use this option multiple times to specify multiple services. If DIRECTORY does not exist, Tor will
         * create it. (Note: in current versions of Tor, if DIRECTORY is a relative path, it will be relative to the
         * current working directory of Tor instance, not to its DataDirectory. Do not rely on this behavior; it is not
         * guaranteed to remain the same in future versions.)
         *
         *
         * Default value: $configDir/hiddenservices
         *
         * @param directory hidden services directory
         * @return builder
         */
        fun hiddenServiceDir(directory: File?): Builder {
            hiddenServiceDir = directory
            return this
        }

        /**
         * A filename containing IPv6 GeoIP data, for use with by-country statistics.
         *
         *
         * Default value: $configDir/geoip6
         *
         * @param file geoip6 file
         * @return builder
         */
        fun geoipv6(file: File?): Builder {
            geoIpv6File = file
            return this
        }

        /**
         * A filename containing IPv4 GeoIP data, for use with by-country statistics.
         *
         *
         * Default value: $configDir/geoip
         *
         * @param file geoip file
         * @return builder
         */
        fun geoip(file: File?): Builder {
            geoIpFile = file
            return this
        }

        /**
         * Store working data in DIR. Can not be changed while tor is running.
         *
         *
         * Default value: $configDir/lib/tor
         *
         * @param directory directory where tor runtime data is stored
         * @return builder
         */
        fun dataDir(directory: File?): Builder {
            dataDir = directory
            return this
        }

        /**
         * The configuration file, which contains "option value" pairs.
         *
         *
         * Default value: $configDir/torrc
         *
         * @param file
         * @return
         */
        fun torrc(file: File?): Builder {
            torrcFile = file
            return this
        }

        fun installDir(file: File): Builder {
            installDir = file
            return this
        }

        fun libraryPath(directory: File?): Builder {
            libraryPath = directory
            return this
        }

        fun cookieAuthFile(file: File?): Builder {
            cookieAuthFile = file
            return this
        }

        fun hostnameFile(file: File?): Builder {
            hostnameFile = file
            return this
        }

        fun resolveConf(resolveConf: File?): Builder {
            this.resolveConf = resolveConf
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
            fileCreationTimeout = timeout
            return this
        }

        /**
         * Builds torConfig and sets default values if not explicitly configured through builder.
         *
         * @return torConfig
         */
        fun build(): TorConfig {
            if (homeDir == null) {
                val userHome = System.getProperty("user.home")
                homeDir =
                    if (userHome != null && "" != userHome && "/" != userHome) File(
                        userHome
                    ) else configDir
            }
            if (torExecutableFile == null) {
                torExecutableFile = File(
                    installDir,
                    torExecutableFileName
                )
            }
            if (geoIpFile == null) {
                geoIpFile = File(configDir, GEO_IP_NAME)
            }
            if (geoIpv6File == null) {
                geoIpv6File = File(configDir, GEO_IPV_6_NAME)
            }
            if (torrcFile == null) {
                torrcFile = File(configDir, TORRC_NAME)
            }
            if (hiddenServiceDir == null) {
                hiddenServiceDir = File(configDir, HIDDEN_SERVICE_NAME)
            }
            if (dataDir == null) {
                dataDir = File(configDir, "lib/tor")
            }
            if (libraryPath == null) {
                libraryPath = torExecutableFile!!.parentFile
            }
            if (hostnameFile == null) {
                hostnameFile = File(dataDir, "hostname")
            }
            if (cookieAuthFile == null) {
                cookieAuthFile = File(dataDir, "control_auth_cookie")
            }
            if (resolveConf == null) {
                resolveConf = File(configDir, "resolv.conf")
            }
            if (controlPortFile == null) {
                controlPortFile = File(dataDir, "control.txt")
            }
            if (fileCreationTimeout <= 0) {
                fileCreationTimeout = 15
            }
            val config = TorConfig()
            config.hiddenServiceDir = hiddenServiceDir
            config.torExecutableFile = torExecutableFile
            config.dataDir = dataDir
            config.torrcFile = torrcFile
            config.geoIpv6File = geoIpv6File
            config.geoIpFile = geoIpFile
            config.homeDir = homeDir
            config.configDir = configDir
            config.hostnameFile = hostnameFile
            config.cookieAuthFile = cookieAuthFile
            config.libraryPath = libraryPath
            config.resolveConf = resolveConf
            config.controlPortFile = controlPortFile
            config.installDir = installDir
            config.fileCreationTimeout = fileCreationTimeout
            return config
        }

        companion object {
            private val torExecutableFileName: String
                private get() = when (OsData.getOsType()) {
                    OsType.ANDROID -> "tor.so"
                    OsType.LINUX_32, OsType.LINUX_64, OsType.MAC -> "tor"
                    OsType.WINDOWS -> "tor.exe"
                    else -> throw RuntimeException("We don't support Tor on this OS")
                }
        }

        /**
         * Constructs a builder with the specified configDir and installDir. The install directory contains executable
         * and libraries, while the configDir is for writeable files.
         *
         *
         * For Linux, the LD_LIBRARY_PATH will be set to the home directory, Any libraries must be in the installDir.
         *
         *
         * For all platforms the configDir will be the default parent location of all files unless they are explicitly set
         * to a different location in this builder.
         *
         * @param configDir
         * @throws IllegalArgumentException if configDir is null
         */
        init {
            requireNotNull(installDir) { "installDir is null" }
            requireNotNull(configDir) { "configDir is null" }
            this.configDir = configDir
            this.installDir = installDir
        }
    }

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
        fun createDefault(configDir: File?): TorConfig {
            return Builder(
                configDir,
                configDir
            ).build()
        }

        /**
         * All files will be in single directory: collapses the data and config directories
         *
         * @param configDir
         * @return
         */
        fun createFlatConfig(configDir: File?): TorConfig {
            return createConfig(configDir, configDir, configDir)
        }

        fun createConfig(
            installDir: File?,
            configDir: File?,
            dataDir: File?
        ): TorConfig {
            val builder =
                Builder(
                    installDir,
                    configDir
                )
            builder.dataDir(dataDir)
            return builder.build()
        }
    }
}