package io.matthewnelson.topl_service.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.matthewnelson.topl_service.service.ServiceActionProcessor
import io.matthewnelson.topl_service.service.TorService
import java.math.BigInteger
import java.security.SecureRandom

/**
 * Is registered at startup of [TorService], and unregistered when it is stopped.
 * Sending an intent here to start [TorService] will do nothing as all intents are piped
 * to [ServiceActionProcessor] directly. To start the service (and Tor), call the
 * [io.matthewnelson.topl_service.TorServiceController.startTor] method.
 *
 * @param [torService]
 * */
internal class TorServiceReceiver(private val torService: TorService): BroadcastReceiver() {

    companion object {
        // Secures the intent filter at each application startup.
        // Also serves as the key to string extras containing the ServiceAction to be executed.
        val SERVICE_INTENT_FILTER: String = BigInteger(130, SecureRandom()).toString(32)

        @Volatile
        var isRegistered = false
            private set
    }

    private val serviceActionProcessor: ServiceActionProcessor
        get() = torService.serviceActionProcessor
    private val broadcastLogger =
        torService.onionProxyManager.getBroadcastLogger(TorServiceReceiver::class.java)

    fun register() {
        if (!isRegistered) {
            torService.applicationContext.registerReceiver(this, IntentFilter(SERVICE_INTENT_FILTER))
            isRegistered = true
            broadcastLogger.debug("Receiver registered")
        }
    }

    fun unregister() {
        if (isRegistered) {
            try {
                torService.applicationContext.unregisterReceiver(this)
                isRegistered = false
                broadcastLogger.debug("Receiver unregistered")
            } catch (e: IllegalArgumentException) {
                broadcastLogger.exception(e)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            if (intent.`package` != torService.packageName) return
            val serviceAction = intent.getStringExtra(SERVICE_INTENT_FILTER)
            val newIntent = Intent(serviceAction)

            // If the broadcast intent has any string extras, their key will be the
            // ServiceAction that was included.
            intent.getStringExtra(serviceAction)?.let {
                newIntent.putExtra(serviceAction, it)
            }
            serviceActionProcessor.processIntent(newIntent)
        }
    }
}