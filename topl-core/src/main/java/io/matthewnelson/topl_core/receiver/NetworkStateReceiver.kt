package io.matthewnelson.topl_core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import io.matthewnelson.topl_core.OnionProxyManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class NetworkStateReceiver(
    private val onionProxyManager: OnionProxyManager
): BroadcastReceiver() {

    private val broadcastLogger =
        onionProxyManager.getBroadcastLogger(NetworkStateReceiver::class.java)

    private lateinit var connectivityManager: ConnectivityManager
    var networkConnectivity = true
        private set

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            if (!initializeConnectivityManager(context)) return
            if (onionProxyManager.torStateMachine.isOff) return

            val info = connectivityManager.activeNetworkInfo
            networkConnectivity = info != null && info.isConnected

            broadcastLogger.debug("Network connectivity: $networkConnectivity")

            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                try {
                    onionProxyManager.disableNetwork(!networkConnectivity)
                } catch (e: Exception) {
                    broadcastLogger.exception(e)
                }
            }
        }
    }

    /**
     * Will initialize [connectivityManager] if it hasn't been already.
     *
     * @param [context]
     * @return `true` if [connectivityManager] was or already was initialized, `false` if
     *   `context.getSystemService(...) ...` returned `null`
     * */
    private fun initializeConnectivityManager(context: Context): Boolean =
        if (!::connectivityManager.isInitialized) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            if (cm != null) {
                connectivityManager = cm
                true
            } else {
                false
            }

        } else {
            true
        }

}