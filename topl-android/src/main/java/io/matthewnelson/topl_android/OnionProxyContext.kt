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
package io.matthewnelson.topl_android

import android.os.Process
import io.matthewnelson.topl_android_settings.TorSettings
import io.matthewnelson.topl_android.settings.TorSettingsBuilder
import io.matthewnelson.topl_android.util.FileUtilities
import io.matthewnelson.topl_android.util.TorInstaller
import io.matthewnelson.topl_android.util.WriteObserver
import io.matthewnelson.topl_android_settings.TorConfigFiles
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
 * Constructs instance of [OnionProxyContext] with the specified [torConfigFiles]. Typically
 * this constructor will be used when tor is currently installed on the system, with the
 * tor executable and config files in different locations.
 *
 * @param [torConfigFiles] [TorConfigFiles] tor configuration info used for running and installing tor
 * @param [torInstaller] [TorInstaller]
 * @param [torSettings] [TorSettings]
 */
class OnionProxyContext(
    val torConfigFiles: TorConfigFiles,
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

    // Try creating the data dir upon instantiation. Everything hinges on it.
    init {
        try {
            createDataDir()
        } catch (e: SecurityException) {
            e.printStackTrace()
            LOG.warn("Could not create directory dataDir: ${torConfigFiles.dataDir}")
        }
    }

    /**
     * Creates the configured tor data directory
     *
     * @throws [SecurityException]
     *
     * @return true if directory already exists or has been successfully created, otherwise false
     */
    @Throws(SecurityException::class)
    fun createDataDir(): Boolean =
        synchronized(dataDirLock) {
            return torConfigFiles.dataDir.exists() || torConfigFiles.dataDir.mkdirs()
        }

    /**
     * Deletes the configured tor data directory
     */
    @Throws(RuntimeException::class, SecurityException::class)
    fun deleteDataDirExceptHiddenService() =
        synchronized(dataDirLock) {
            for (file in torConfigFiles.dataDir.listFiles())
                if (file.isDirectory)
                    if (file.absolutePath != torConfigFiles.hiddenServiceDir.absolutePath)
                        FileUtilities.recursiveFileDelete(file)
                else
                    if (!file.delete())
                        throw RuntimeException("Could not delete file ${file.absolutePath}")
        }

    /**
     * Creates an empty cookie auth file
     *
     * @throws [SecurityException]
     *
     * @return True if cookie file is created, otherwise false.
     */
    @Throws(SecurityException::class)
    fun createCookieAuthFile(): Boolean =
        synchronized(cookieLock) {
            val cookieAuthFile = torConfigFiles.cookieAuthFile
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

    @Throws(SecurityException::class)
    fun createHostnameFile(): Boolean =
        synchronized(hostnameLock) {
            val hostnameFile = torConfigFiles.hostnameFile
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
     * Creates a default resolv.conf file using the Quad9 name server. This is a convenience method.
     */
    @Throws(IOException::class)
    fun createQuad9NameserverFile(): File =
        synchronized(dnsLock) {
            val file = torConfigFiles.resolveConf
            val writer = BufferedWriter(FileWriter(file))
            writer.write("nameserver 9.9.9.9\n")
            writer.write("nameserver 149.112.112.112\n")
            writer.close()
            return file
        }

    /**
     * Creates an observer for the configured control port file.
     *
     * @throws [IOException] File errors
     * @throws [IllegalArgumentException] see [WriteObserver]
     * @throws [SecurityException] see [WriteObserver]
     *
     * @return write observer for the control port file
     */
    @Throws(IOException::class, IllegalArgumentException::class, SecurityException::class)
    fun createControlPortFileObserver(): WriteObserver =
        synchronized(cookieLock) {
            return generateWriteObserver(torConfigFiles.controlPortFile)
        }

    /**
     * Creates an observer for the configured cookie auth file.
     *
     * @throws [IOException] File errors
     * @throws [IllegalArgumentException] see [WriteObserver]
     * @throws [SecurityException] see [WriteObserver]
     *
     * @return write observer for the cookie auth file
     */
    @Throws(IOException::class, IllegalArgumentException::class, SecurityException::class)
    fun createCookieAuthFileObserver(): WriteObserver =
        synchronized(cookieLock) {
            return generateWriteObserver(torConfigFiles.cookieAuthFile)
        }

    /**
     * Creates an observer for the configured hostname file.
     *
     * @throws [IOException] File errors
     * @throws [IllegalArgumentException] see [WriteObserver]
     * @throws [SecurityException] see [WriteObserver]
     *
     * @return write observer for the hostname file
     */
    @Throws(IOException::class, IllegalArgumentException::class, SecurityException::class)
    fun createHostnameDirObserver(): WriteObserver =
        synchronized(hostnameLock) {
            return generateWriteObserver(torConfigFiles.hostnameFile)
        }

    fun newSettingsBuilder(): TorSettingsBuilder =
        TorSettingsBuilder(this)

    /**
     * Returns the system process id of the process running this onion proxy
     *
     * @return process id
     */
    val processId: String
        get() = Process.myPid().toString()

    /**
     * Generates a write observer for the given file
     *
     * @throws [IllegalArgumentException] if the file does not exist.
     * @throws [SecurityException] File errors
     * */
    @Throws(IllegalArgumentException::class, SecurityException::class)
    fun generateWriteObserver(file: File): WriteObserver =
        WriteObserver(file)
}