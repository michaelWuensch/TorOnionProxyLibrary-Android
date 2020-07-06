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
* */
package io.matthewnelson.topl_core.util

import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeoutException

/**
 * Extend this class and implement the need methods.
 *
 * [setup] is called from [io.matthewnelson.topl_core.OnionProxyManager.setup] after
 * instantiation, and [openBridgesStream] is called from
 * [io.matthewnelson.topl_core.settings.TorSettingsBuilder.addBridgesFromResources]
 * when configuring bridge support.
 * */
abstract class TorInstaller: CoreConsts() {

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
     * @sample [io.matthewnelson.topl_service.onionproxy.ServiceTorInstaller.setup]
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
     *
     * @sample [io.matthewnelson.topl_service.onionproxy.ServiceTorInstaller.openBridgesStream]
     */
    @Throws(IOException::class)
    abstract fun openBridgesStream(): InputStream?
}