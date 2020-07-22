package io.matthewnelson.topl_core_base

import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Reads a [File]
 *
 * @return a [ByteArray] of the contents of the [File]
 * @throws [IOException] File errors
 * @throws [EOFException] File errors
 * @throws [SecurityException] Unauthorized access to file/directory.
 * */
@Throws(IOException::class, EOFException::class, SecurityException::class)
fun File.readTorConfigFile(): ByteArray {
    val b = ByteArray(this.length().toInt())
    val `in` = FileInputStream(this)

    return `in`.use { inputStream ->
        var offset = 0

        while (offset < b.size) {
            val read = inputStream.read(b, offset, b.size - offset)
            if (read == -1) throw EOFException()
            offset += read
        }

        b
    }
}