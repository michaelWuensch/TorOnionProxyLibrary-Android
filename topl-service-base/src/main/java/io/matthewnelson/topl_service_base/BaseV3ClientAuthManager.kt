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
*     needed to implement TorOnionProxyLibrary-Android, as listed below:
*
*      - From the `topl-core-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service` module:
*          - The TorServiceController class and it's contained classes/methods/variables
*          - The ServiceNotification.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Companion class and it's contained methods/variables
*
*     The following code is excluded from "The Interfaces":
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
package io.matthewnelson.topl_service_base

import androidx.annotation.WorkerThread
import java.io.File

abstract class BaseV3ClientAuthManager {


    ///////////////////////////////////
    /// Add v3 Client Authorization ///
    ///////////////////////////////////
    /**
     * Creates a file containing v3 Client Authorization for a Hidden Service in the format of:
     *  - Filename: [nickname].auth_private
     *  - File Contents:  (56-char-onion-addr-**without**.onion):descriptor:x25519:(x25519 private key in base32)
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
     * @throws [IllegalStateException] If the file already exists (and must be deleted before
     *   overwriting), or if a file exists with the same onion address & private key
     * @throws [SecurityException] If access is not authorized
     * */
    @WorkerThread
    @Throws(
        IllegalArgumentException::class,
        IllegalStateException::class,
        SecurityException::class
    )
    abstract fun addV3ClientAuthenticationPrivateKey(
        nickname: String,
        onionAddress: String,
        base32EncodedPrivateKey: String
    ): File?

    /////////////////
    /// Retrieval ///
    /////////////////
    /**
     * Retrieve a v3 client authentication file by the nickname, whether the file
     * extension ".auth_private" is included or not.
     *
     * @param [nickname] The pre file extension name
     * */
    @WorkerThread
    abstract fun getFileByNickname(nickname: String): File?

    /**
     * All files within the v3 Client Authentication directory are returned. If
     * the directory is empty, returns `null`.
     * */
    @WorkerThread
    abstract fun getAllFiles(): Array<File>?

    /**
     * From the v3 Client Authentication directory, all files that contain the
     * ".auth_private" extension will have their name w/o the extension returned
     * in an array. If the directory is empty, returns `null`.
     * */
    @WorkerThread
    abstract fun getAllFileNicknames(): Array<String>?

    @WorkerThread
    abstract fun getFileContent(nickname: String): V3ClientAuthContent?

    @WorkerThread
    abstract fun getFileContent(file: File): V3ClientAuthContent?

    ////////////////
    /// Deletion ///
    ////////////////
    @WorkerThread
    abstract fun deleteFile(nickname: String): Boolean?

    @WorkerThread
    abstract fun deleteFile(file: File): Boolean?
}