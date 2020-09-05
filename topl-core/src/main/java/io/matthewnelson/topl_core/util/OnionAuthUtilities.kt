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
package io.matthewnelson.topl_core.util

import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.createNewFileIfDoesNotExist
import java.io.File

object OnionAuthUtilities {

    private const val FILE_EXTENSION = ".auth_private"


    ///////////////////////////////////
    /// Add v3 Client Authorization ///
    ///////////////////////////////////
    /**
     * Creates a file containing v3 Client Authorization for a Hidden Service in the format of:
     *  - Filename: [nickname].auth_private
     *  - File Contents:  <56-char-onion-addr-without-.onion-part>:descriptor:x25519:<x25519 private key in base32>
     *
     * Exceptions are thrown for you with adequate messages if the values passed
     * are non-compliant.
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-onion-service.html.en#ClientAuthorization
     *
     * @param [nickname] The nickname for the file. Is appended with `.auth_private` and used as the File name
     *   [nickname] requirements are:
     *   - between 1 and 75 characters
     *   - alpha-numeric values (0-9, a-z, A-Z) (capitalization does not matter)
     *   - may contain periods (.), **but** cannot be fist/last character or be used consecutively (..)
     *   - may contain underscores (_)
     * @param [onionAddress] The .onion address for which this Private Key will exist for
     *   - may or may not contain the `.onion` (it gets stripped off if it's present)
     *   - 56 characters (not including the `.onion` appendage)
     *   - Base32 encoded ed25519 (a-z, 2-7, **uncapitalized**)
     * @param [base32EncodedPrivateKey] The private key for authenticating to the Hidden Service
     *   - 52 characters
     *   - Base32 encoded x25519 (A-Z, 2-7, **capitalized**)
     *
     * @return The File if it was created properly, `null` if it was not
     * @throws [IllegalArgumentException] If passed arguments are not compliant with the spec
     * @throws [IllegalStateException] If the file already exists (and must be deleted before overwriting)
     * @throws [SecurityException] If access is not authorized
     * */
    @WorkerThread
    @JvmStatic
    @Throws(
        IllegalArgumentException::class,
        IllegalStateException::class,
        SecurityException::class
    )
    fun addV3ClientAuthenticationPrivateKey(
        nickname: String,
        onionAddress: String,
        base32EncodedPrivateKey: String,
        torConfigFiles: TorConfigFiles
    ): File? {
        val name = nickname.trim()
        val onion = if (onionAddress.contains(".onion"))
            onionAddress.split(".onion")[0].trim()
        else
            onionAddress.trim()
        val key = base32EncodedPrivateKey.trim()

        try {
            isV3ClientAuthNicknameCompliant(name)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Nickname Exception:\n${e.message}")
        }

        try {
            isV3OnionAddressBase32ed25519Compliant(onion)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Onion address Exception:\n${e.message}")
        }

        try {
            isV3ClientAuthKeyBase32x25519Compliant(key)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Client Authorization Key Exception:\n${e.message}")
        }

        val file = File(torConfigFiles.v3AuthPrivateDir, "$name$FILE_EXTENSION")

        if (file.exists())
            throw IllegalStateException(
                "A File with $name already exists and must be deleted first"
            )

        synchronized(torConfigFiles.v3AuthPrivateDirLock) {
            if (file.createNewFileIfDoesNotExist() != true)
                return null

            val string = "${onion}:descriptor:x25519:$key"

            file.writeText(string)
            val goodContent = file.readText() == string

            return if (!goodContent) {
                if (file.exists())
                    file.delete()
                null
            } else {
                file
            }
        }
    }

    private val FILE_NAME_CHARS: CharArray
        get() = charArrayOf(
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '_', '.',
            'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z'
        )

    private fun isV3ClientAuthNicknameCompliant(nickname: String): String =
        checkStringCompliance(nickname, FILE_NAME_CHARS, 1, 75)

    private val BASE32_ED25519_CHARS: CharArray
        get() = charArrayOf(
            '2', '3', '4', '5', '6', '7',
            'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'
        )

    @Throws(IllegalArgumentException::class)
    private fun isV3OnionAddressBase32ed25519Compliant(onionAddress: String): String =
        checkStringCompliance(onionAddress, BASE32_ED25519_CHARS, 56, 56)

    private val BASE32_X25519_CHARS: CharArray
        get() = charArrayOf(
            '2', '3', '4', '5', '6', '7',
            'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z'
        )

    @Throws(IllegalArgumentException::class)
    private fun isV3ClientAuthKeyBase32x25519Compliant(v3ClientAuthorizationKey: String): String =
        checkStringCompliance(v3ClientAuthorizationKey, BASE32_X25519_CHARS, 52, 52)

    @Throws(IllegalArgumentException::class)
    private fun checkStringCompliance(
        string: String,
        charArray: CharArray,
        minLength: Int,
        maxLength: Int
    ): String {
        var length = 0
        string.forEachIndexed { index, c ->
            val exceptionMessage: String? = when {
                !charArray.contains(c) -> {
                    "Character '$c' at position $index is invalid"
                }
                c == '.' && index == 0 -> {
                    "The first character cannot be '.'"
                }
                c == '.' && string[index - 1] == '.' -> {
                    "Consecutive periods '..' are not allowed"
                }
                c == '.' && index == string.lastIndex-> {
                    "The last character cannot be '.'"
                }
                else -> {
                    null
                }
            }

            exceptionMessage?.let {
                throw IllegalArgumentException(it)
            }

            length++
        }

        return if (length in minLength..maxLength)
            string
        else
            throw  IllegalArgumentException(
                "Length was $length but must be between $minLength and $maxLength characters"
            )
    }


    /////////////////
    /// Retrieval ///
    /////////////////
    /**
     * Retrieve a v3 client authentication file by the nickname, whether the file
     * extension ".auth_private" is included or not.
     *
     * @param [nickname] The pre file extension name
     * @param [torConfigFiles]
     * */
    @WorkerThread
    @JvmStatic
    fun getFileByNickname(nickname: String, torConfigFiles: TorConfigFiles): File? {
        val file = if (nickname.contains(FILE_EXTENSION))
            File(torConfigFiles.v3AuthPrivateDir, nickname)
        else
            File(torConfigFiles.v3AuthPrivateDir, "$nickname$FILE_EXTENSION")

        return if (file.exists())
            file
        else
            null
    }

    /**
     * All files within the v3 Client Authentication directory are returned. If
     * the directory is empty, returns `null`.
     *
     * @param [torConfigFiles]
     * */
    @WorkerThread
    @JvmStatic
    fun getAllFiles(torConfigFiles: TorConfigFiles): Array<File>? =
        synchronized(torConfigFiles.v3AuthPrivateDirLock) {
            torConfigFiles.v3AuthPrivateDir.listFiles()
        }

    /**
     * From the v3 Client Authentication directory, all files that contain the
     * ".auth_private" extension will have their name w/o the extension returned
     * in an array. If the directory is empty, returns `null`.
     *
     * @param [torConfigFiles]
     * */
    @WorkerThread
    @JvmStatic
    fun getAllFileNicknames(torConfigFiles: TorConfigFiles): Array<String>? {
        val fileNames = mutableListOf<String>()

        synchronized(torConfigFiles.v3AuthPrivateDirLock) {
            for (file in torConfigFiles.v3AuthPrivateDir.listFiles() ?: return null) {
                if (!file.isDirectory && file.name.contains(FILE_EXTENSION))
                    fileNames.add(file.nameWithoutExtension)
            }
        }
        return fileNames.toTypedArray()
    }


    ////////////////
    /// Deletion ///
    ////////////////
    @WorkerThread
    @JvmStatic
    fun deleteFile(nickname: String, torConfigFiles: TorConfigFiles): Boolean? {
        val file = getFileByNickname(nickname, torConfigFiles) ?: return null
        synchronized(torConfigFiles.v3AuthPrivateDirLock) {
            return if (!file.isDirectory)
                file.delete()
            else
                null
        }
    }

    @WorkerThread
    @JvmStatic
    fun deleteFile(file: File, torConfigFiles: TorConfigFiles): Boolean? =
        deleteFile(file.nameWithoutExtension, torConfigFiles)
}