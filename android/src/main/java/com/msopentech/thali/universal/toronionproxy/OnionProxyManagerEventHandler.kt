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

import net.freehaven.tor.control.EventHandler
import org.slf4j.LoggerFactory

/**
 * Logs the data we get from notifications from the Tor OP. This is really just meant for debugging.
 */
class OnionProxyManagerEventHandler : EventHandler {
    override fun circuitStatus(
        status: String,
        id: String,
        path: String
    ) {
        LOG.info("circuitStatus: status:$status, id: $id, path: $path")
    }

    override fun streamStatus(
        status: String,
        id: String,
        target: String
    ) {
        LOG.info("streamStatus: status: $status, id: $id, target: $target")
    }

    override fun orConnStatus(status: String, orName: String) {
        LOG.info("OR connection: status: $status, orName: $orName")
    }

    override fun bandwidthUsed(read: Long, written: Long) {
        LOG.info("bandwidthUsed: read: $read, written: $written")
    }

    override fun newDescriptors(orList: List<String>) {
        val iterator = orList.iterator()
        val stringBuilder = StringBuilder()
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next())
        }
        LOG.info("newDescriptors: $stringBuilder")
    }

    override fun message(severity: String, msg: String) {
        LOG.info("message: severity: $severity, msg: $msg")
    }

    override fun unrecognized(type: String, msg: String) {
        LOG.info("unrecognized: type: $type, msg: $msg")
    }

    private fun shortenPath(path: List<String>): String {
        val s = StringBuilder()
        for (id in path) {
            if (s.length > 0) s.append(',')
            s.append(id.substring(1, 7))
        }
        return s.toString()
    }

    companion object {
        private val LOG =
            LoggerFactory.getLogger(OnionProxyManagerEventHandler::class.java)
    }
}