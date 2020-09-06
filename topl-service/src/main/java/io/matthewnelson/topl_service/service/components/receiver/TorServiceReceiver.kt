/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash `74407114cbfa8ea6f2ac51417dda8be98d8aba86`. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify it
*     under the terms of the GNU General Public License as published by the
*     Free Software Foundation, either version 3 of the License, or (at your
*     option) any later version.
*
*     This program is distributed in the hope that it will be useful, but
*     WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*     or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
*     for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program. If not, see <https://www.gnu.org/licenses/>.
*
* `===========================================================================`
* `+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++`
* `===========================================================================`
*
* The following exception is an additional permission under section 7 of the
* GNU General Public License, version 3 (“GPLv3”).
*
*     "The Interfaces" is henceforth defined as Application Programming Interfaces
*     that are publicly available classes/functions/etc (ie: do not contain the
*     visibility modifiers `internal`, `private`, `protected`, or are within
*     classes/functions/etc that contain the aforementioned visibility modifiers)
*     to TorOnionProxyLibrary-Android users that are needed to implement
*     TorOnionProxyLibrary-Android and reside in ONLY the following modules:
*
*      - topl-core-base
*      - topl-service
*
*     The following are excluded from "The Interfaces":
*
*       - All other code
*
*     Linking TorOnionProxyLibrary-Android statically or dynamically with other
*     modules is making a combined work based on TorOnionProxyLibrary-Android.
*     Thus, the terms and conditions of the GNU General Public License cover the
*     whole combination.
*
*     As a special exception, the copyright holder of TorOnionProxyLibrary-Android
*     gives you permission to combine TorOnionProxyLibrary-Android program with free
*     software programs or libraries that are released under the GNU LGPL and with
*     independent modules that communicate with TorOnionProxyLibrary-Android solely
*     through "The Interfaces". You may copy and distribute such a system following
*     the terms of the GNU GPL for TorOnionProxyLibrary-Android and the licenses of
*     the other code concerned, provided that you include the source code of that
*     other code when and as the GNU GPL requires distribution of source code and
*     provided that you do not modify "The Interfaces".
*
*     Note that people who make modified versions of TorOnionProxyLibrary-Android
*     are not obligated to grant this special exception for their modified versions;
*     it is their choice whether to do so. The GNU General Public License gives
*     permission to release a modified version without this exception; this exception
*     also makes it possible to release a modified version which carries forward this
*     exception. If you modify "The Interfaces", this exception does not apply to your
*     modified version of TorOnionProxyLibrary-Android, and you must remove this
*     exception when you distribute your modified version.
* */
package io.matthewnelson.topl_service.service.components.receiver

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.service.components.actions.ServiceActions
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceActionName
import java.math.BigInteger
import java.security.SecureRandom

/**
 * Is registered at startup of [TorService], and unregistered when it is stopped.
 * Sending an intent here to start [TorService] will do nothing. To start the service (and Tor),
 * call the [io.matthewnelson.topl_service.TorServiceController.startTor] method.
 *
 * @param [torService]
 * */
internal class TorServiceReceiver(private val torService: BaseService): BroadcastReceiver() {

    companion object {
        // Secures the intent filter at each application startup.
        // Also serves as the key to string extras containing the ServiceAction to be executed.
        val SERVICE_INTENT_FILTER: String = BigInteger(130, SecureRandom()).toString(32)

        @Volatile
        var isRegistered = false
            private set

        @Volatile
        var deviceIsLocked: Boolean? = null
            private set
    }

    private val broadcastLogger = torService.getBroadcastLogger(TorServiceReceiver::class.java)

    fun register() {
        val filter = IntentFilter(SERVICE_INTENT_FILTER)
        @Suppress("DEPRECATION")
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)

        if (torService.doesReceiverNeedToListenForLockScreen()) {
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_SCREEN_ON)
            filter.addAction(Intent.ACTION_USER_PRESENT)
        }
        torService.context.applicationContext.registerReceiver(this, filter)
        if (!isRegistered)
            broadcastLogger.debug("Has been registered")
        isRegistered = true
    }

    /**
     * Sets [deviceIsLocked] to the value sent. If [value] is null, it will check
     * KeyguardManager.
     * */
    fun setDeviceIsLocked(value: Boolean? = null) {
        deviceIsLocked = value ?: checkIfDeviceIsLocked()
    }

    fun unregister() {
        if (isRegistered) {
            try {
                torService.context.applicationContext.unregisterReceiver(this)
                isRegistered = false
                setDeviceIsLocked()
                broadcastLogger.debug("Has been unregistered")
            } catch (e: IllegalArgumentException) {
                broadcastLogger.exception(e)
            }
        }
    }

    private fun checkIfDeviceIsLocked(): Boolean? {
        val keyguardManager = torService.context.applicationContext
            .getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager?
        return keyguardManager?.isKeyguardLocked
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            when (intent.action) {
                @Suppress("DEPRECATION")
                ConnectivityManager.CONNECTIVITY_ACTION -> {
                    if (!torService.isTorOn()) return

                    val connectivity = torService.hasNetworkConnectivity()
                    broadcastLogger.notice("Network connectivity: $connectivity")

                    val actionObject = if (connectivity)
                        ServiceActions.SetDisableNetwork(ServiceActionName.ENABLE_NETWORK)
                    else
                        ServiceActions.SetDisableNetwork(ServiceActionName.DISABLE_NETWORK)

                    torService.processServiceAction(actionObject)
                }
                Intent.ACTION_SCREEN_OFF,
                Intent.ACTION_SCREEN_ON,
                Intent.ACTION_USER_PRESENT -> {
                    val locked = checkIfDeviceIsLocked()
                    if (locked != deviceIsLocked) {
                        setDeviceIsLocked(locked)
                        broadcastLogger.notice("Device is locked: $deviceIsLocked")
                        torService.refreshNotificationActions()
                    }
                }
                SERVICE_INTENT_FILTER -> {

                    when (val serviceAction = intent.getStringExtra(SERVICE_INTENT_FILTER)) {
                        ServiceActionName.NEW_ID -> {
                            torService.processServiceAction(ServiceActions.NewId())
                        }
                        ServiceActionName.RESTART_TOR -> {
                            torService.processServiceAction(ServiceActions.RestartTor())
                        }
                        ServiceActionName.STOP -> {
                            torService.processServiceAction(ServiceActions.Stop())
                        }
                        else -> {
                            broadcastLogger.warn(
                                "This class does not accept $serviceAction as an argument."
                            )
                        }
                    }

                }
            }
        }
    }
}