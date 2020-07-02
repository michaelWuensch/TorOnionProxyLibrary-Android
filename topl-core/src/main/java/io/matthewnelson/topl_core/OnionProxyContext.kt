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
package io.matthewnelson.topl_core

import android.os.Process
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_core.util.FileUtilities
import io.matthewnelson.topl_core.util.CoreConsts
import io.matthewnelson.topl_core.util.TorInstaller
import io.matthewnelson.topl_core.util.WriteObserver
import io.matthewnelson.topl_core_base.TorConfigFiles
import java.io.*

/**
 * Provides context information about the environment. Implementing classes provide logic
 * for setting up the specific environment. Any and all file modification (creation/deletion,
 * reading/writing) is done here such that synchronicity is had with the environment for which
 * OnionProxyManager is operating in.
 *
 * Most to all methods/values are only accessible within the module. Things that may be needed
 * externally can be accessed from OnionProxyManager.
 *
 * Constructs instance of [OnionProxyContext] with the specified [torConfigFiles]. Typically
 * this constructor will be used when tor is currently installed on the system, with the
 * tor executable and config files in different locations.
 *
 * @param [torConfigFiles] [TorConfigFiles] Tor file/directory info used for running/installing Tor
 * @param [torInstaller] [TorInstaller]
 * @param [torSettings] [TorSettings] Basically your torrc file
 */
internal class OnionProxyContext(
    val torConfigFiles: TorConfigFiles,
    val torInstaller: TorInstaller,
    val torSettings: TorSettings
): CoreConsts() {

    // Gets initialized in OnionProxyManager _immediately_ after
    // OnionProxyContext is instantiated.
    private lateinit var broadcastLogger: BroadcastLogger
    fun initBroadcastLogger(onionProxyBroadcastLogger: BroadcastLogger) {
        if (!::broadcastLogger.isInitialized)
            broadcastLogger = onionProxyBroadcastLogger
    }

    private val controlPortFileLock = Object()
    private val cookieAuthFileLock = Object()
    private val dataDirLock = Object()
    private val resolvConfFileLock = Object()
    private val hostnameFileLock = Object()

    /**
     * Creates an observer for the file referenced. See [CoreConsts.ConfigFile] annotation class
     * for accepted arguments.
     *
     * @param [configFileReference] String that defines what file a [WriteObserver] is created for.
     * @return A WriteObserver for the appropriate file referenced.
     * @throws [IllegalArgumentException] If [configFileReference] is an invalid argument, or null file.
     * @throws [SecurityException] See [WriteObserver.checkExists]
     */
    @Throws(IllegalArgumentException::class, SecurityException::class)
    fun createFileObserver(@ConfigFile configFileReference: String): WriteObserver {
        broadcastLogger.debug("Creating FileObserver for $configFileReference")
        return when (configFileReference) {
            ConfigFile.CONTROL_PORT_FILE -> {
                synchronized(controlPortFileLock) {
                    WriteObserver(torConfigFiles.controlPortFile)
                }
            }
            ConfigFile.COOKIE_AUTH_FILE -> {
                synchronized(cookieAuthFileLock) {
                    WriteObserver(torConfigFiles.cookieAuthFile)
                }
            }
            ConfigFile.HOSTNAME_FILE -> {
                synchronized(hostnameFileLock) {
                    WriteObserver(torConfigFiles.hostnameFile)
                }
            }
            else -> {
                throw IllegalArgumentException("$configFileReference is not a valid argument")
            }
        }
    }

    /**
     * Creates an empty file if the provided one does not exist. See [CoreConsts.ConfigFile]
     * annotation class for accepted arguments.
     *
     * @param [configFileReference] String that defines what file is to be created.
     * @return True if file exists or is created successfully, otherwise false.
     * @throws [SecurityException] Unauthorized access to file/directory.
     * */
    @Throws(IllegalArgumentException::class, SecurityException::class)
    fun createNewFileIfDoesNotExist(@ConfigFile configFileReference: String): Boolean {
        broadcastLogger.debug("Creating $configFileReference if DNE")
        return when (configFileReference) {
            ConfigFile.CONTROL_PORT_FILE -> {
                synchronized(controlPortFileLock) {
                    createNewFileIfDoesNotExist(torConfigFiles.controlPortFile)
                }
            }
            ConfigFile.COOKIE_AUTH_FILE -> {
                synchronized(cookieAuthFileLock) {
                    createNewFileIfDoesNotExist(torConfigFiles.cookieAuthFile)
                }
            }
            ConfigFile.HOSTNAME_FILE -> {
                synchronized(hostnameFileLock) {
                    createNewFileIfDoesNotExist(torConfigFiles.hostnameFile)
                }
            }
            else -> {
                throw IllegalArgumentException("$configFileReference is not a valid argument")
            }
        }
    }

    /**
     * Deletes the referenced file. See [CoreConsts.ConfigFile] annotation class
     * for accepted arguments.
     *
     * @param [configFileReference] String that defines what file is to be delete.
     * @return True if it was deleted, false if it failed.
     * @throws [SecurityException] Unauthorized access to file/directory.
     * @throws [IllegalArgumentException]
     * */
    @Throws(SecurityException::class, IllegalArgumentException::class)
    fun deleteFile(@ConfigFile configFileReference: String): Boolean {
        broadcastLogger.debug("Deleting $configFileReference")
        return when (configFileReference) {
            ConfigFile.CONTROL_PORT_FILE -> {
                synchronized(controlPortFileLock) {
                    torConfigFiles.controlPortFile.delete()
                }
            }
            ConfigFile.COOKIE_AUTH_FILE -> {
                synchronized(cookieAuthFileLock) {
                    torConfigFiles.cookieAuthFile.delete()
                }
            }
            ConfigFile.HOSTNAME_FILE -> {
                synchronized(hostnameFileLock) {
                    torConfigFiles.hostnameFile.delete()
                }
            }
            else -> {
                throw IllegalArgumentException("$configFileReference is not a valid argument")
            }
        }
    }

    /**
     * Reads the referenced file. See [CoreConsts.ConfigFile] annotation class
     * for accepted arguments.
     *
     * @param [configFileReference] String that defines what file is to be read.
     * @return The contents of the file.
     * @throws [IOException] Errors while reading file.
     * @throws [EOFException] Errors while reading file.
     * @throws [SecurityException] Unauthorized access to file/directory.
     * */
    @Throws(IOException::class, EOFException::class, SecurityException::class)
    fun readFile(@ConfigFile configFileReference: String): ByteArray {
        broadcastLogger.debug("Reading $configFileReference")
        return when (configFileReference) {
            ConfigFile.CONTROL_PORT_FILE -> {
                synchronized(controlPortFileLock) {
                    FileUtilities.read(torConfigFiles.controlPortFile)
                }
            }
            ConfigFile.COOKIE_AUTH_FILE -> {
                synchronized(cookieAuthFileLock) {
                    FileUtilities.read(torConfigFiles.cookieAuthFile)
                }
            }
            ConfigFile.HOSTNAME_FILE -> {
                synchronized(hostnameFileLock) {
                    FileUtilities.read(torConfigFiles.hostnameFile)
                }
            }
            else -> {
                throw IllegalArgumentException("$configFileReference is not a valid argument")
            }
        }
    }

    ///////////////
    /// DataDir ///
    ///////////////
    /**
     * Creates the configured tor data directory
     *
     * @return True if directory exists or is created successfully, otherwise false.
     * @throws [SecurityException] Unauthorized access to file/directory.
     */
    @Throws(SecurityException::class)
    fun createDataDir(): Boolean =
        synchronized(dataDirLock) {
            return torConfigFiles.dataDir.exists() || torConfigFiles.dataDir.mkdirs()
        }

    /**
     * Deletes the configured tor data directory, except for hiddenservices.
     *
     * @throws [RuntimeException] Was unable to delete a file.
     * @throws [SecurityException] Unauthorized access to file/directory.
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

    ////////////////////
    /// HostnameFile ///
    ////////////////////
    @Throws(SecurityException::class)
    fun setHostnameDirPermissionsToReadOnly(): Boolean =
        synchronized(hostnameFileLock) {
            FileUtilities.setToReadOnlyPermissions(torConfigFiles.hostnameFile.parentFile)
        }

    ///////////
    /// DNS ///
    ///////////
    /**
     * Creates a default resolv.conf file using the Quad9 name server. This is a convenience
     * method.
     *
     * @return The resolveConf file with newly added ip addresses for Quad9
     * @throws [IOException] from FileWriter
     */
    @Throws(IOException::class)
    fun createQuad9NameserverFile(): File =
        synchronized(resolvConfFileLock) {
            val file = torConfigFiles.resolveConf
            val writer = BufferedWriter(FileWriter(file))
            writer.write("nameserver 9.9.9.9\n")
            writer.write("nameserver 149.112.112.112\n")
            writer.close()
            return file
        }

    /**
     * Returns the system process id of the process running this onion proxy
     *
     * @return process id
     */
    val processId: String
        get() = Process.myPid().toString()

    @Throws(SecurityException::class)
    private fun createNewFileIfDoesNotExist(file: File): Boolean {
        if (!file.parentFile.exists() && !file.parentFile.mkdirs()) {
            broadcastLogger.warn("Could not create ${file.nameWithoutExtension} parent directory")
            return false
        }

        return try {
            val exists = if (file.exists()) true else file.createNewFile()
            exists
        } catch (e: IOException) {
            broadcastLogger.warn("Could not create ${file.nameWithoutExtension}")
            false
        }
    }
}