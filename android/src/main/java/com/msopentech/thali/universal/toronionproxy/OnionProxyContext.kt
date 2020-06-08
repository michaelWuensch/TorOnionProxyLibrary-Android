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

import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Provides context information about the environment. Implementating classes provide logic for setting up
 * the specific environment
 */
abstract class OnionProxyContext(
    torConfig: TorConfig?,
    torInstaller: TorInstaller?,
    settings: TorSettings?
) {
    /**
     * Gets tor configuration info used for running and installing tor
     *
     * @return tor config info
     */
    /**
     * Tor configuration info used for running and installing tor
     */
    val config: TorConfig
    private val dataDirLock = Any()
    private val dnsLock = Any()
    private val cookieLock = Any()
    private val hostnameLock = Any()
    val settings: TorSettings
    val installer: TorInstaller

    /**
     * Constructs instance of `OnionProxyContext` with specified configDir. Use this constructor when
     * all tor files (including the executable) are under a single directory. Currently, this is used with installers
     * that assemble all necessary files into one location.
     *
     * @param configDir
     * @throws IllegalArgumentException if specified config in null
     */
    constructor(
        configDir: File?,
        torInstaller: TorInstaller?
    ) : this(TorConfig.Companion.createDefault(configDir), torInstaller, null) {
    }

    /**
     * Creates the configured tor data directory
     *
     * @return true is directory already exists or has been successfully created, otherwise false
     */
    fun createDataDir(): Boolean {
        synchronized(
            dataDirLock
        ) { return config.dataDir.exists() || config.dataDir.mkdirs() }
    }

    /**
     * Deletes the configured tor data directory
     */
    fun deleteDataDir() {
        synchronized(dataDirLock) {
            for (file in config.dataDir.listFiles()) {
                if (file.isDirectory) {
                    if (file.absolutePath != config.hiddenServiceDir
                            .absolutePath
                    ) {
                        FileUtilities.recursiveFileDelete(file)
                    }
                } else {
                    if (!file.delete()) {
                        throw RuntimeException("Could not delete file " + file.absolutePath)
                    }
                }
            }
        }
    }

    /**
     * Creates an empty cookie auth file
     *
     * @return true if cookie file is created, otherwise false
     */
    fun createCookieAuthFile(): Boolean {
        synchronized(cookieLock) {
            val cookieAuthFile = config.cookieAuthFile
            if (!cookieAuthFile!!.parentFile.exists() &&
                !cookieAuthFile.parentFile.mkdirs()
            ) {
                LOG.warn("Could not create cookieFile parent directory")
                return false
            }
            return try {
                cookieAuthFile.exists() || cookieAuthFile.createNewFile()
            } catch (e: IOException) {
                LOG.warn("Could not create cookieFile")
                false
            }
        }
    }

    fun createHostnameFile(): Boolean {
        synchronized(hostnameLock) {
            val hostnameFile = config.hostnameFile
            if (!hostnameFile!!.parentFile.exists() &&
                !hostnameFile.parentFile.mkdirs()
            ) {
                LOG.warn("Could not create hostnameFile parent directory")
                return false
            }
            return try {
                hostnameFile.exists() || hostnameFile.createNewFile()
            } catch (e: IOException) {
                LOG.warn("Could not create hostnameFile")
                false
            }
        }
    }

    /**
     * Creates a default resolve.conf file using the Google nameserver. This is a convenience method.
     */
    @Throws(IOException::class)
    fun createGoogleNameserverFile(): File? {
        synchronized(dnsLock) {
            val file = config.resolveConf
            val writer = BufferedWriter(FileWriter(file))
            writer.write("nameserver 8.8.8.8\n")
            writer.write("nameserver 8.8.4.4\n")
            writer.close()
            return file
        }
    }

    /**
     * Creates an observer for the configured control port file
     *
     * @return write observer for cookie auth file
     */
    @Throws(IOException::class)
    fun createControlPortFileObserver(): WriteObserver {
        synchronized(cookieLock) { return generateWriteObserver(config.controlPortFile) }
    }

    @Throws(IOException::class)
    fun createCookieAuthFileObserver(): WriteObserver {
        synchronized(cookieLock) { return generateWriteObserver(config.cookieAuthFile) }
    }

    /**
     * Creates an observer for the configured hostname file
     *
     * @return write observer for hostname file
     */
    @Throws(IOException::class)
    fun createHostnameDirObserver(): WriteObserver {
        synchronized(hostnameLock) { return generateWriteObserver(config.hostnameFile) }
    }

    fun newConfigBuilder(): TorSettingsBuilder {
        return TorSettingsBuilder(this)
    }

    /**
     * Returns the system process id of the process running this onion proxy
     *
     * @return process id
     */
    abstract val processId: String

    @Throws(IOException::class)
    abstract fun generateWriteObserver(file: File?): WriteObserver

    companion object {
        protected val LOG = LoggerFactory.getLogger(OnionProxyContext::class.java)
    }

    /**
     * Constructs instance of `OnionProxyContext` with the specified torConfig. Typically this constructor
     * will be used when tor is currently installed on the system, with the tor executable and config files in different
     * locations.
     *
     * @param torConfig tor configuration info used for running and installing tor
     * @throws IllegalArgumentException if specified config in null
     */
    init {
        requireNotNull(torConfig) { "torConfig is null" }
        requireNotNull(torInstaller) { "torInstaller is null" }
        config = torConfig
        this.settings = settings ?: DefaultSettings()
        installer = torInstaller
    }
}