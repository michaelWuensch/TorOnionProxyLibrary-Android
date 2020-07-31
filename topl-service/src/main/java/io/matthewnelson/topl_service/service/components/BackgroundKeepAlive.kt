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
package io.matthewnelson.topl_service.service.components

import android.app.Activity
import android.app.Application
import android.os.Bundle
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.service.TorService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.freehaven.tor.control.TorControlCommands

/**
 * This gets instantiated when the user sends the application to the recent apps's tray.
 * It starts a heartbeat for Tor and cycles [TorService] between foreground and background
 * to inhibit the OS from killing it due to being idle.
 *
 * When a user sends the application to the recent app's tray:
 *
 *   - [TorService.onTrimMemory] - TRIM_MEMORY_UI_HIDDEN (when this gets instantiated)
 *   - [onActivityStopped]
 *   - [onActivitySaveInstanceState] <-- Where we pickup the activity in question.
 *
 * If the user returns the application to the foreground:
 *
 *   - [onActivityStarted] <-- Where we unregister and continue with our day.
 *   - [onActivityResumed]
 *
 * When a user removes the application from the recent app's tray:
 *
 *   - [onActivityDestroyed] <-- Where we shutdown Tor, and then [TorService]
 *
 * @param [torService] [BaseService]
 * */
internal class BackgroundKeepAlive(
    private val torService: BaseService
): Application.ActivityLifecycleCallbacks {

    private val broadcastLogger = torService.getBroadcastLogger(BackgroundKeepAlive::class.java)
    private val heartbeatJob: Job

    init {
        (torService.context.applicationContext as Application)
            .registerActivityLifecycleCallbacks(this)

        broadcastLogger.debug("Has been registered")

        heartbeatJob = torService.getScopeIO().launch {
            while (isActive) {
                delay(TorServiceController.backgroundHeartbeatTime)
                if (isActive) {
                    torService.startForegroundService().stopForeground(torService)
                    if (torService.signalControlConnection(TorControlCommands.SIGNAL_HEARTBEAT)) {
                        torService.registerReceiver()
                        torService.addNotificationActions()
                    }
                }
            }
        }
    }

    fun unregister() {
        try {
            (torService.context.applicationContext as Application)
                .unregisterActivityLifecycleCallbacks(this)
            heartbeatJob.cancel()
            broadcastLogger.debug("Has been unregistered")
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }

    private fun broadcastLCE(activity: Activity, event: String) =
        broadcastLogger.debug("${activity.javaClass.simpleName}@${activity.hashCode()} - $event")

    // If destroyed -> task removed from recent apps tray
    // If started/resumed -> task back in foreground
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        broadcastLCE(activity, "onActivityStarted")
        torService.unregisterBackgroundKeepAlive()
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {
        broadcastLCE(activity, "onActivityDestroyed")
        // TorService.onTaskRemoved will handle the swap to foreground
        torService.unregisterBackgroundKeepAlive()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        broadcastLCE(activity, "onActivitySaveInstanceState")
    }
}