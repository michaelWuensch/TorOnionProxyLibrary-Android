/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.topl_service.util

import java.text.NumberFormat
import java.util.*

object ServiceUtilities {

    /**
     * Formats the supplied values to look like: `20kbps ↓ / 85kbps ↑`
     *
     * @param [download] Long value associated with download (bytesRead)
     * @param [upload] Long value associated with upload (bytesWritten)
     * */
    fun getFormattedBandwidthString(download: Long, upload: Long): String =
        "${formatBandwidth(download)} ↓ / ${formatBandwidth(upload)} ↑"

    /**
     * Obtained from: https://gitweb.torproject.org/tor-android-service.git/tree/service/
     *                src/main/java/org/torproject/android/service/TorEventHandler.java
     *
     * Original method name: formatCount()
     * */
    private fun formatBandwidth(value: Long): String {
        val numberFormat = NumberFormat.getInstance(Locale.getDefault())

        return if (value < 1e6) {
            numberFormat.format(
                Math.round((((value * 10 / 1024).toInt()) / 10).toFloat())
            ) + "kbps"
        } else {
            numberFormat.format(
                Math.round((((value * 100 / 1024 / 1024).toInt()) / 100).toFloat())
            ) + "mbps"
        }
    }
}