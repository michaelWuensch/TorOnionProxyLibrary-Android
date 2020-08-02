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
package io.matthewnelson.topl_service.service.components.binding

import android.content.Intent
import android.os.Binder
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.lifecycle.BackgroundManager
import io.matthewnelson.topl_service.util.ServiceConsts.BackgroundPolicy
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceActionName
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


internal class TorServiceBinder(private val torService: BaseService): Binder() {

    private val broadcastLogger = torService.getBroadcastLogger(TorServiceBinder::class.java)

    fun submitServiceActionIntent(serviceActionIntent: Intent) {
        val action = serviceActionIntent.action
        if (action != null && action.contains(ServiceActionName.ACTION_NAME)) {

            when (action) {
                ServiceActionName.START -> {
                    // Do not accept the above ServiceActions through use of this method.
                    // START = to start TorService
                    broadcastLogger.warn("$action is not an accepted intent action for this class")
                }
                else -> {
                    BaseService.updateLastAcceptedServiceAction(action)
                    torService.processIntent(serviceActionIntent)
                }
            }
        }
    }


    //////////////////////////////////////////
    /// BackgroundManager Policy Execution ///
    //////////////////////////////////////////

    private val bgMgrBroadcastLogger = torService.getBroadcastLogger(BackgroundManager::class.java)
    private var backgroundPolicyExecutionJob: Job? = null

    /**
     * Execution of a [BackgroundPolicy] takes place here in order to stay within the lifecycle
     * of [io.matthewnelson.topl_service.service.TorService] so that we prevent any potential
     * leaks from occurring.
     *
     * @param [policy] The [BackgroundPolicy] to be executed
     * @param [executionDelay] the time expressed in your [BackgroundManager.Builder.Policy]
     * */
    fun executeBackgroundPolicyJob(@BackgroundPolicy policy: String, executionDelay: Long) {
        cancelExecuteBackgroundPolicyJob(policy)
        backgroundPolicyExecutionJob = torService.getScopeMain().launch {
            when (policy) {
                BackgroundPolicy.KEEP_ALIVE -> {
                    while (isActive && BaseServiceConnection.serviceBinder != null) {
                        delay(executionDelay)
                        if (isActive && BaseServiceConnection.serviceBinder != null) {
                            bgMgrBroadcastLogger.debug("Executing background management policy")
                            torService.startForegroundService()
                            torService.stopForegroundService()
                        }
                    }
                }
                BackgroundPolicy.RESPECT_RESOURCES -> {
                    delay(executionDelay)
                    bgMgrBroadcastLogger.debug("Executing background management policy")
                    torService.processIntent(Intent(ServiceActionName.STOP))
                }
            }
        }
    }

    /**
     * Cancels the coroutine executing the [BackgroundPolicy] if it is active.
     *
     * @param [policy] the [BackgroundPolicy] being cancelled
     * */
    fun cancelExecuteBackgroundPolicyJob(@BackgroundPolicy policy: String) {
        if (backgroundPolicyExecutionJob?.isActive == true) {
            backgroundPolicyExecutionJob?.let {
                it.cancel()
                bgMgrBroadcastLogger.debug("Execution of $policy has been cancelled")
            }
        }
    }
}