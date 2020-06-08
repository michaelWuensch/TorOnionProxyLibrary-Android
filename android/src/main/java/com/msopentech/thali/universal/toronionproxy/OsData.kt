/*
Copyright (C) 2011-2014 Sublime Software Ltd

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
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
package com.msopentech.thali.universal.toronionproxy

import java.io.IOException
import java.util.*

object OsData {
    private var detectedType: OsType? = null
    val osType: OsType?
        get() {
            if (detectedType == null) {
                detectedType = actualGetOsType()
            }
            return detectedType
        }

    /**
     * Yes, I should use a proper memoization abstract class but, um, next time.
     * @return Type of OS we are running on
     */
    internal fun actualGetOsType(): OsType {
        if (System.getProperty("java.vm.name").contains("Dalvik")) {
            return OsType.ANDROID
        }
        val osName = System.getProperty("os.name")
        if (osName.contains("Windows")) {
            return OsType.WINDOWS
        } else if (osName.contains("Mac")) {
            return OsType.MAC
        } else if (osName.contains("Linux")) {
            return linuxType
        }
        return OsType.UNSUPPORTED
    }

    internal val linuxType: OsType
        internal get() {
            val cmd = arrayOf("uname", "-m")
            var unameProcess: Process? = null
            var scanner: Scanner? = null
            return try {
                val unameOutput: String
                unameProcess = Runtime.getRuntime().exec(cmd)
                scanner = Scanner(unameProcess.inputStream)
                unameOutput = if (scanner.hasNextLine()) {
                    scanner.nextLine()
                } else {
                    throw RuntimeException("Couldn't get output from uname call")
                }
                val exit = unameProcess.waitFor()
                if (exit != 0) {
                    throw RuntimeException("Uname returned error code $exit")
                }
                if (unameOutput.compareTo("i686") == 0) {
                    return OsType.LINUX_32
                }
                if (unameOutput.compareTo("x86_64") == 0) {
                    OsType.LINUX_64
                } else OsType.UNSUPPORTED
            } catch (e: IOException) {
                throw RuntimeException("Uname failure", e)
            } catch (e: InterruptedException) {
                throw RuntimeException("Uname failure", e)
            } finally {
                if (unameProcess != null) {
                    scanner!!.close()
                    unameProcess.destroy()
                }
            }
        }

    enum class OsType {
        WINDOWS, LINUX_32, LINUX_64, MAC, ANDROID, UNSUPPORTED
    }
}