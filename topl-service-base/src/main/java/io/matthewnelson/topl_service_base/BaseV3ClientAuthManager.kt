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