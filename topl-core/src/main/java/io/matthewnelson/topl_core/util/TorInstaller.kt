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
package io.matthewnelson.topl_core.util

import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeoutException

abstract class TorInstaller {

    /**
     * This gets set as soon as [io.matthewnelson.topl_core.OnionProxyManager] is instantiated,
     * and can be used to broadcast messages in your class which extends [TorInstaller].
     * */
    var broadcastLogger: BroadcastLogger? = null
        private set
    internal fun initBroadcastLogger(torInstallerBroadcastLogger: BroadcastLogger) {
        if (broadcastLogger == null)
            broadcastLogger = torInstallerBroadcastLogger
    }

    /**
     * Sets up and installs any files needed to run tor. If the tor files are already on
     * the system this does not need to be invoked.
     *
     * @return true if tor installation is successful, otherwise false.
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
     * `($bridge_type $bridge_info \r\n)*`
     *
     *
     * if first byte is 1, the the stream will have the form
     *
     * `($bridge_info \r\n)*`
     *
     * The second form is used for custom bridges from the user.
     */
    @Throws(IOException::class)
    abstract fun openBridgesStream(): InputStream?
}