package io.matthewnelson.topl_core.util

import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.createNewFileIfDoesNotExist
import java.io.File

object OnionAuthUtilities {


    ///////////////////////////////////
    /// Add v3 Client Authorization ///
    ///////////////////////////////////
    /**
     * Creates a file containing v3 Client Authorization for a Hidden Service in the format of:
     *  - Filename: [nickName].auth_private
     *  - File Contents:  <56-char-onion-addr-without-.onion-part>:descriptor:x25519:<x25519 private key in base32>
     *
     * Exceptions are thrown for you with adequate messages if the values passed
     * are non-compliant.
     *
     * **Docs:** https://2019.www.torproject.org/docs/tor-onion-service.html.en#ClientAuthorization
     *
     * @param [nickName] The nickname for the file. Is appended with `.auth_private` and used as the File name
     *   [nickName] requirements are:
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
    fun addV3AuthenticationPrivateKey(
        nickName: String,
        onionAddress: String,
        base32EncodedPrivateKey: String,
        torConfigFiles: TorConfigFiles
    ): File? {
        val name = nickName.trim()
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

        val file = File(torConfigFiles.v3AuthPrivateDir, "${name}.auth_private")

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

    private val FILE_NAME_CHARS = charArrayOf(
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

    private val BASE32_ED25519_CHARS = charArrayOf(
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

    private val BASE32_X25519_CHARS = charArrayOf(
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
}