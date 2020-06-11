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

import android.os.Process
import com.msopentech.thali.toronionproxy.settings.DefaultSettings
import io.matthewnelson.topl_settings.TorSettings
import com.msopentech.thali.toronionproxy.settings.TorSettingsBuilder
import com.msopentech.thali.toronionproxy.util.FileUtilities
import com.msopentech.thali.toronionproxy.util.TorInstaller
import com.msopentech.thali.toronionproxy.util.WriteObserver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Provides context information about the environment. Implementing classes provide logic
 * for setting up the specific environment
 *
 * Constructs instance of [OnionProxyContext] with the specified [torConfig]. Typically
 * this constructor will be used when tor is currently installed on the system, with the
 * tor executable and config files in different locations.
 *
 * @param [torConfig] [TorConfig] tor configuration info used for running and installing tor
 * @param [torInstaller] [TorInstaller]
 * @param [torSettings] [TorSettings]
 */
class OnionProxyContext(
    val torConfig: TorConfig,
    val torInstaller: TorInstaller,
    val torSettings: TorSettings
) {

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(OnionProxyContext::class.java)
    }

    private val dataDirLock = Object()
    private val dnsLock = Object()
    private val cookieLock = Object()
    private val hostnameLock = Object()

    /**
     * Creates the configured tor data directory
     *
     * @return true if directory already exists or has been successfully created, otherwise false
     */
    fun createDataDir(): Boolean =
        synchronized(dataDirLock) {
            return torConfig.dataDir.exists() || torConfig.dataDir.mkdirs()
        }

    /**
     * Deletes the configured tor data directory
     */
    fun deleteDataDir() =
        synchronized(dataDirLock) {
            for (file in torConfig.dataDir.listFiles())
                if (file.isDirectory)
                    if (file.absolutePath != torConfig.hiddenServiceDir.absolutePath)
                        FileUtilities.recursiveFileDelete(file)
                else
                    if (!file.delete())
                        throw RuntimeException("Could not delete file ${file.absolutePath}")
        }

    /**
     * Creates an empty cookie auth file
     *
     * @return true if cookie file is created, otherwise false
     */
    fun createCookieAuthFile(): Boolean =
        synchronized(cookieLock) {
            val cookieAuthFile = torConfig.cookieAuthFile
            if (!cookieAuthFile.parentFile.exists() && !cookieAuthFile.parentFile.mkdirs()) {
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

    fun createHostnameFile(): Boolean =
        synchronized(hostnameLock) {
            val hostnameFile = torConfig.hostnameFile
            if (!hostnameFile.parentFile.exists() && !hostnameFile.parentFile.mkdirs()) {
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

    /**
     * Creates a default resolv.conf file using the Google name server. This is a convenience method.
     */
    @Throws(IOException::class)
    fun createGoogleNameserverFile(): File =
        synchronized(dnsLock) {
            val file = torConfig.resolveConf
            val writer = BufferedWriter(FileWriter(file))
            writer.write("nameserver 8.8.8.8\n")
            writer.write("nameserver 8.8.4.4\n")
            writer.close()
            return file
        }

    /**
     * Creates an observer for the configured control port file
     *
     * @return write observer for cookie auth file
     */
    @Throws(IOException::class)
    fun createControlPortFileObserver(): WriteObserver =
        synchronized(cookieLock) {
            return generateWriteObserver(torConfig.controlPortFile)
        }

    @Throws(IOException::class)
    fun createCookieAuthFileObserver(): WriteObserver =
        synchronized(cookieLock) {
            return generateWriteObserver(torConfig.cookieAuthFile)
        }

    /**
     * Creates an observer for the configured hostname file
     *
     * @return write observer for hostname file
     */
    @Throws(IOException::class)
    fun createHostnameDirObserver(): WriteObserver =
        synchronized(hostnameLock) {
            return generateWriteObserver(torConfig.hostnameFile)
        }

    fun newConfigBuilder(): TorSettingsBuilder =
        TorSettingsBuilder(this)

    /**
     * Returns the system process id of the process running this onion proxy
     *
     * @return process id
     */
    val processId: String
        get() = Process.myPid().toString()

    @Throws(IOException::class)
    fun generateWriteObserver(file: File): WriteObserver =
        WriteObserver(file)
}