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
package com.msopentech.thali.toronionproxy.util

import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeoutException

abstract class TorInstaller {
    /**
     * Sets up and installs the tor environment. If the tor environment is already setup, this does not need to be invoked.
     */
    @Throws(IOException::class)
    abstract fun setup()

    @Throws(IOException::class, TimeoutException::class)
    abstract fun updateTorConfigCustom(content: String?)

    fun getAssetOrResourceByName(fileName: String): InputStream? =
        javaClass.getResourceAsStream("/$fileName")

    /**
     * If first byte of stream is 0, then the following stream will have the form
     *
     * `
     * ($bridge_type $bridge_info \r\n)*
    ` *
     *
     * if first byte is 1, the the stream will have the form
     * `
     * ($bridge_info \r\n)*
    ` *
     *
     * The second form is used for custom bridges from the user.
     *
     */
    @Throws(IOException::class)
    abstract fun openBridgesStream(): InputStream?
}