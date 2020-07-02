package io.matthewnelson.topl_service.util

import java.text.NumberFormat
import java.util.*

object ServiceUtilities {

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