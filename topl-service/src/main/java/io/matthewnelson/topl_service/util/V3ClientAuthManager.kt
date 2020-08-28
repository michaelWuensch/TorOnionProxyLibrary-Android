package io.matthewnelson.topl_service.util

import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core.util.OnionAuthUtilities
import io.matthewnelson.topl_core_base.TorConfigFiles
import java.io.File

class V3ClientAuthManager internal constructor(
    private val torConfigFiles: TorConfigFiles
) {

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
    fun addV3ClientAuthenticationPrivateKey(
        nickname: String,
        onionAddress: String,
        base32EncodedPrivateKey: String
    ): File? =
        OnionAuthUtilities.addV3ClientAuthenticationPrivateKey(
            nickname, onionAddress, base32EncodedPrivateKey, torConfigFiles
        )

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
    fun getFileByNickname(nickname: String): File? =
        OnionAuthUtilities.getFileByNickname(nickname, torConfigFiles)

    /**
     * All files within the v3 Client Authentication directory are returned. If
     * the directory is empty, returns `null`.
     * */
    @WorkerThread
    fun getAllFiles(): Array<File>? =
        OnionAuthUtilities.getAllFiles(torConfigFiles)

    /**
     * From the v3 Client Authentication directory, all files that contain the
     * ".auth_private" extension will have their name w/o the extension returned
     * in an array. If the directory is empty, returns `null`.
     * */
    @WorkerThread
    fun getAllFileNicknames(): Array<String>? =
        OnionAuthUtilities.getAllFileNicknames(torConfigFiles)

    class V3ClientAuthContent internal constructor(val address: String, val privateKey: String)

    @WorkerThread
    fun getFileContent(nickname: String): V3ClientAuthContent? {
        val v3ClientAuthFile = getFileByNickname(nickname) ?: return null
        val content = v3ClientAuthFile.readText().split(':')
        return V3ClientAuthContent(content[0], content[3])
    }

    @WorkerThread
    fun getFileContent(file: File): V3ClientAuthContent? {
        val v3ClientAuthFile = getFileByNickname(file.nameWithoutExtension) ?: return null
        val content = v3ClientAuthFile.readText().split(':')
        return V3ClientAuthContent(content[0], content[3])
    }


    ////////////////
    /// Deletion ///
    ////////////////
    @WorkerThread
    fun deleteFile(nickname: String): Boolean? =
        OnionAuthUtilities.deleteFile(nickname, torConfigFiles)

    @WorkerThread
    fun deleteFile(file: File): Boolean? =
        OnionAuthUtilities.deleteFile(file, torConfigFiles)
}