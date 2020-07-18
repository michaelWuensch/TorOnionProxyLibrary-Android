/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash 74407114cbfa8ea6f2ac51417dda8be98d8aba86. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     This program is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
* ================================================================================
* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
* ================================================================================
*
* The original code, prior to commit hash 74407114cbfa8ea6f2ac51417dda8be98d8aba86,
* was:
*
*     Copyright (c) Microsoft Open Technologies, Inc.
*     All Rights Reserved
*
*     Licensed under the Apache License, Version 2.0 (the "License");
*     you may not use this file except in compliance with the License.
*     You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*
*
*     THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR
*     CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING
*     WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE,
*     FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABLITY OR NON-INFRINGEMENT.
*
*     See the Apache 2 License for the specific language governing permissions and
*     limitations under the License.
*
* ================================================================================
* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
* ================================================================================
*
* This code took the Socks4a logic from SocksProxyClientConnOperator in NetCipher
* which was then modified to meet our needs. That original code was licensed as:
*
* This file contains the license for Orlib, a free software project to
* provide anonymity on the Internet from a Google Android smartphone.
*
*     For more information about Orlib, see https://guardianproject.info/
*
* If you got this file as a part of a larger bundle, there may be other
* license terms that you should be aware of.
*
*
*     Orlib is distributed under this license (aka the 3-clause BSD license)
*     Copyright (c) 2009-2010, Nathan Freitas, The Guardian Project
*
*     Redistribution and use in source and binary forms, with or without
*     modification, are permitted provided that the following conditions are
*     met:
*
*         Redistributions of source code must retain the above copyright
*         notice, this list of conditions and the following disclaimer.
*
*         Redistributions in binary form must reproduce the above
*         copyright notice, this list of conditions and the following
*         disclaimer in the documentation and/or other materials provided
*         with the distribution.
*
*         Neither the names of the copyright owners nor the names of its
*         contributors may be used to endorse or promote products derived from
*         this software without specific prior written permission.
*
*         THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
*         "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
*         LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
*         A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
*         OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
*         SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
*         LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
*         DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
*         THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
*         (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
*         OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
* */
package io.matthewnelson.topl_core.util

import java.io.*
import java.util.logging.Logger
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object FileUtilities {

    // TODO: search commit history to see why this method name does not
    //  properly represent what it is doing.
    fun setToReadOnlyPermissions(file: File): Boolean {
        return file.setReadable(false, false) &&
                file.setWritable(false, false) &&
                file.setExecutable(false, false) &&
                file.setReadable(true, true) &&
                file.setWritable(true, true) &&
                file.setExecutable(true, true)
    }

    /**
     * Sets readable/executable for all users and writable by owner
     *
     * @param file the file to set the permissions on
     */
    fun setPerms(file: File) {
        file.setReadable(true)
        file.setExecutable(true)
        file.setWritable(false)
        file.setWritable(true, true)
    }

    /**
     * Closes both input and output streams when done.
     * @param in Stream to read from
     * @param out Stream to write to
     * @throws java.io.IOException - If close on input or output fails
     */
    @Throws(IOException::class)
    fun copy(`in`: InputStream, out: OutputStream) =
        `in`.use { inputStream ->
            copyDoNotCloseInput(inputStream, out)
        }

    /**
     * Won't close the input stream when it's done, needed to handle ZipInputStreams
     * @param in Won't be closed
     * @param out Will be closed
     * @throws java.io.IOException If close on output fails
     */
    @Throws(IOException::class)
    fun copyDoNotCloseInput(`in`: InputStream, out: OutputStream) =
        out.use { outputStream ->
            val buf = ByteArray(4096)
            while (true) {
                val read = `in`.read(buf)
                if (read == -1) break
                outputStream.write(buf, 0, read)
            }
        }

    @Throws(SecurityException::class)
    fun listFilesToLog(f: File) {
        if (f.isDirectory) {
            val listFiles = f.listFiles() ?: return
            for (child in listFiles)
                listFilesToLog(child)
        }
    }

    @Throws(IOException::class, EOFException::class, SecurityException::class)
    fun read(f: File): ByteArray {
        val b = ByteArray(f.length().toInt())
        val `in` = FileInputStream(f)

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

    /**
     * Reads the input stream, deletes fileToWriteTo if it exists and over writes it with the stream.
     * @param readFrom Stream to read from
     * @param fileToWriteTo File to write to
     *
     * @throws java.io.IOException - If any of the file operations fail
     */
    @Throws(IOException::class, SecurityException::class)
    fun cleanInstallOneFile(readFrom: InputStream, fileToWriteTo: File) {
        if (fileToWriteTo.exists() && !fileToWriteTo.delete())
            throw RuntimeException("Could not remove existing file ${fileToWriteTo.name}")

        val out: OutputStream = FileOutputStream(fileToWriteTo)
        copy(readFrom, out)
    }

    @Throws(SecurityException::class)
    fun recursiveFileDelete(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            val listFiles = fileOrDirectory.listFiles() ?: return
            for (child in listFiles)
                recursiveFileDelete(child)
        }

        if (fileOrDirectory.exists() && !fileOrDirectory.delete())
            throw RuntimeException("Could not delete directory ${fileOrDirectory.absolutePath}")
    }

    /**
     * This has to exist somewhere! Why isn't it a part of the standard Java library?
     * @param destinationDirectory Directory files are to be extracted to
     * @param zipFileInputStream Stream to unzip
     * @throws java.io.IOException - If there are any file errors
     */
    @Throws(IOException::class, RuntimeException::class)
    fun extractContentFromZip(destinationDirectory: File, zipFileInputStream: InputStream) {
        val zipInputStream: ZipInputStream
        try {

            zipInputStream = ZipInputStream(zipFileInputStream)
            var zipEntry: ZipEntry

            while (zipInputStream.nextEntry.also { zipEntry = it } != null) {
                val file = File(destinationDirectory, zipEntry.name)
                if (zipEntry.isDirectory)
                    if (!file.exists() && !file.mkdirs())
                        throw RuntimeException("Could not create directory $file")

                else
                    if (file.exists() && !file.delete())
                        throw RuntimeException("Could not delete file in preparation " +
                                "for overwriting it. File - ${file.absolutePath}"
                        )

                    if (!file.createNewFile())
                        throw RuntimeException("Could not create file $file")

                    val fileOutputStream: OutputStream = FileOutputStream(file)
                    copyDoNotCloseInput(zipInputStream, fileOutputStream)
            }

        } finally {
            zipFileInputStream.close()
        }
    }
}