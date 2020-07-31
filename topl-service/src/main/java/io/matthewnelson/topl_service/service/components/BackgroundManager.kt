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
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.util.ServiceConsts
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
 *   - [onActivityDestroyed] <-- Where we shutdown Tor, and then stop [TorService]
 *
 * @param [torService] [BaseService]
 * */
class BackgroundManager internal constructor(
    private val torService: BaseService/*,
    private val application: Application,
    @BackgroundPolicy private val policy: String,
    private val executionDelay: Long*/
): ServiceConsts(), Application.ActivityLifecycleCallbacks {


    /**
     * When your application is sent to the background (the Recent App's tray), the chosen
     * [BackgroundManager.Builder.Policy] will be executed after the number of seconds you've
     * declared, unless brought *back* into the foreground by the user.
     *
     * While your application is in the foreground the only way to stop the service is by
     * calling [io.matthewnelson.topl_service.TorServiceController.stopTor], or via the
     * [io.matthewnelson.topl_service.notification.ServiceNotification] Action (if enabled);
     * The OS will not kill a service started using `Context.startService` &
     * `Context.bindService` (how [TorService] is started).
     *
     * When the user sends your application to the Recent App's tray though, the OS will kill
     * your app after being idle for a period of time (random AF... typically 0.5m to 1.25m)
     * to recoup resources. This is not an issue if the user removes the task before the OS
     * kills the app, as Tor will be able to shutdown properly and the service will stop.
     *
     * This is where Services get sketch AF (especially when trying to implement an always
     * running service for networking), and is the purpose of the [BackgroundManager] class.
     *
     * This [BackgroundManager.Builder] sets how you want the service to operate while your
     * app is in the background (the Recent App's tray), such that we don't hog resources
     * unnecessarily, and run reliably based off of your application's needs.
     *
     * @sample [io.matthewnelson.sampleapp.App.generateBackgroundManagerPolicy]
     * */
    class Builder {

        @BackgroundPolicy private lateinit var chosenPolicy: String
        private var executionDelay: Long = 30_000L

        /**
         * While your application is in the background (the Recent App's tray), run
         * [TorService] in the foreground to prevent going idle and being killed. A
         * Notification will be displayed no matter if you set
         * [io.matthewnelson.topl_service.notification.ServiceNotification.Builder.showNotification]
         * to `false` or not.
         *
         * @param [secondsFrom15To45]? Seconds before the [Policy] is executed after the
         *   Application goes to the background. Sending null will use the default (30s)
         * @return [BackgroundManager.Builder.Policy] To use when initializing
         *   [io.matthewnelson.topl_service.TorServiceController.Builder]
         * */
        fun switchServiceToForeground(secondsFrom15To45: Int? = null): Policy {
            chosenPolicy = BackgroundPolicy.FOREGROUND
            if (secondsFrom15To45 != null && secondsFrom15To45 in 15..45)
                executionDelay = (secondsFrom15To45 * 1000).toLong()
            return Policy(this)
        }

        /**
         * Stops [TorService], and then starts it up again if your application is brought back
         * into the foreground between when the [Policy] is executed and the application is
         * killed by the OS.
         *
         * Your application won't go through it's normal `Application.onCreate` process, but
         * [TorService] will have been stopped when the policy gets executed. Electing
         * this option ensures [TorService] gets restarted in a more reliable manner then
         * returning `Context.START_STICKY` in [TorService.onStartCommand]. It also allows for
         * a proper shutdown of Tor prior to the service being stopped instead of it being
         * killed along with your application (which causes problems sometimes).
         *
         * If killed by the OS then this class gets garbage collected such that in the event
         * the user brings your application back into the foreground (after it had been killed),
         * this will be re-instantiated when going through `Application.onCreate` again, and
         * [TorService] started by however you have it implemented.
         *
         * @param [secondsFrom15To45]? Seconds before the [Policy] is executed after the
         *   Application goes to the background. Sending null will use the default (30s)
         * @return [BackgroundManager.Builder.Policy] To use when initializing
         *   [io.matthewnelson.topl_service.TorServiceController.Builder]
         * */
        fun stopServiceThenStartIfBroughtBackIntoForeground(secondsFrom15To45: Int? = null): Policy {
            chosenPolicy = BackgroundPolicy.STOP_THEN_START
            if (secondsFrom15To45 != null && secondsFrom15To45 in 15..45)
                executionDelay = (secondsFrom15To45 * 1000).toLong()
            return Policy(this)
        }

        /**
         * Holds the chosen policy to be built in
         * [io.matthewnelson.topl_service.TorServiceController.Builder.build].
         *
         * @param [policyBuilder] The [BackgroundManager.Builder] to be built during initialization
         * */
        class Policy(private val policyBuilder: BackgroundManager.Builder) {

            internal fun build(application: Application) {
//                backgroundManager = BackgroundManager(
//                    application,
//                    policyBuilder.chosenPolicy,
//                    policyBuilder.executionDelay
//                )
            }
        }
    }

    internal companion object {
        lateinit var backgroundManager: BackgroundManager

//        @BackgroundPolicy private lateinit var backgroundPolicy: String
//        private var delayLength = 30_000L

        var heartbeatTime = 30_000L
            private set

        fun initialize(milliseconds: Long) {
            heartbeatTime = milliseconds
        }
    }

    private val broadcastLogger = torService.getBroadcastLogger(BackgroundManager::class.java)
    private val heartbeatJob: Job

    init {
        (torService.context.applicationContext as Application)
            .registerActivityLifecycleCallbacks(this)

        broadcastLogger.debug("Has been registered")

        heartbeatJob = torService.getScopeIO().launch {
            while (isActive) {
                delay(heartbeatTime)
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
        torService.unregisterBackgroundManager()
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {
        broadcastLCE(activity, "onActivityDestroyed")
        // TorService.onTaskRemoved will handle the swap to foreground
        torService.unregisterBackgroundManager()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        broadcastLCE(activity, "onActivitySaveInstanceState")
    }
}