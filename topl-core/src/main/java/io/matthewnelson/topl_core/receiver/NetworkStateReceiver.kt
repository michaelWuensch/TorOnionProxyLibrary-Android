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
* */
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