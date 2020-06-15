package io.matthewnelson.topl_android_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import java.math.BigInteger
import java.security.SecureRandom

internal class TorServiceReceiver: BroadcastReceiver() {

    companion object {
        val TOR_SERVICE_START: String = BigInteger(130, SecureRandom()).toString(32)
        val TOR_SERVICE_STOP: String = BigInteger(130, SecureRandom()).toString(32)
        val TOR_SERVICE_RESTART: String = BigInteger(130, SecureRandom()).toString(32)
        val TOR_SERVICE_RENEW: String = BigInteger(130, SecureRandom()).toString(32)

        const val EXTRAS = "EXTRAS"
        const val UPDATE_SETTINGS = "UPDATE_SETTINGS"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            when (val action = intent.action) {
                TOR_SERVICE_START, TOR_SERVICE_STOP, TOR_SERVICE_RESTART, TOR_SERVICE_RENEW -> {
                    val torServiceIntent = Intent(context.applicationContext, TorService::class.java)
                    torServiceIntent.putExtra(EXTRAS, intent.getBooleanExtra(UPDATE_SETTINGS, false))
                    torServiceIntent.action = action

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(torServiceIntent)
                    } else {
                        context.startService(torServiceIntent)
                    }
                }
                else -> { }
            }
        }
    }

}