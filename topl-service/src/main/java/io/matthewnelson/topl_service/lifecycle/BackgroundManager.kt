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
package io.matthewnelson.topl_service.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.service.components.binding.BaseServiceConnection
import io.matthewnelson.topl_service.util.ServiceConsts

/**
 * When your application is sent to the background (the Recent App's tray or lock screen), the
 * chosen [BackgroundManager.Builder.Policy] will be executed after the number of seconds you've
 * declared.
 *
 * If brought back into the foreground by the user:
 *
 *   - **Before Policy execution**: Execution is canceled. If [BaseService.lastAcceptedServiceAction]
 *   was **not** [ServiceConsts.ServiceActionName.STOP], a startService call is made to ensure it's
 *   started.
 *
 *   - **After Policy execution**: If [BaseService.lastAcceptedServiceAction]
 *   was **not** [ServiceConsts.ServiceActionName.STOP], a startService call is made to ensure it's
 *   started.
 *
 *   - See [BaseService.updateLastAcceptedServiceAction] and [TorService.onTaskRemoved] for
 *   more information.
 *
 * While your application is in the foreground the only way to stop the service is by
 * calling [io.matthewnelson.topl_service.TorServiceController.stopTor], or via the
 * [io.matthewnelson.topl_service.notification.ServiceNotification] Action (if enabled);
 * The OS will not kill a service started using `Context.startService` &
 * `Context.bindService` (how [TorService] is started) while in the foreground.
 *
 * When the user sends your application to the Recent App's tray though, to recoup resources
 * the OS will kill your app after being idle for a period of time (random AF... typically
 * 0.75m to 1.25m). This is not an issue if the user removes the task before the OS
 * kills the app, as Tor will be able to shutdown properly and the service will stop.
 *
 * This is where Services get sketchy (especially when trying to implement an always
 * running service for networking), and is the purpose of the [BackgroundManager] class.
 *
 * This class starts your chosen [BackgroundManager.Builder.Policy] as soon as your
 * application is sent to the background, waits for the time you declared, and then executes.
 *
 * See the [BackgroundManager.Builder] for more detail.
 *
 * @param [policy] The chosen [ServiceConsts.BackgroundPolicy] to be executed.
 * @param [executionDelay] Length of time before the policy gets executed *after* the application
 *   is sent to the background.
 * @param [serviceClass] The Service class being managed
 * @param [serviceConnection] The ServiceConnection being used to bind with
 * @see [io.matthewnelson.topl_service.service.components.binding.TorServiceBinder.executeBackgroundPolicyJob]
 * @see [io.matthewnelson.topl_service.service.components.binding.TorServiceBinder.cancelExecuteBackgroundPolicyJob]
 * */
class BackgroundManager internal constructor(
    @BackgroundPolicy private val policy: String,
    private val executionDelay: Long,
    private val serviceClass: Class<*>,
    private val serviceConnection: BaseServiceConnection
): ServiceConsts(), LifecycleObserver {


    /**
     * This [BackgroundManager.Builder] sets how you want the service to operate while your
     * app is in the background (the Recent App's tray or lock screen), such that things run
     * reliably based off of your application's needs.
     *
     * When your application is brought back into the foreground your [Policy] is canceled
     * and, if [BaseService.lastAcceptedServiceAction] was **not** to Stop the service, a
     * startup command is issued to bring it back to the started state no matter if it is still
     * running or not.
     *
     * @sample [io.matthewnelson.sampleapp.App.generateBackgroundManagerPolicy]
     * */
    class Builder {

        @BackgroundPolicy
        private lateinit var chosenPolicy: String
        private var executionDelay: Long = 30_000L

        // TODO: Needs more work... running in the foreground is inhibiting the Application from
        //  performing it's normal lifecycle after user swipes it away such that it's not going
        //  through Application.onCreate, but is holding onto references. (same problem when
        //  starting the service using Context.startForegroundService), which is bullshit.
        /**
         * While your application is in the background (the Recent App's tray or lock screen),
         * this [Policy] periodically switches [TorService] to the foreground then immediately
         * back the background. Doing do prevents your application from going idle and being
         * killed by the OS. It is much more resource intensive than choosing
         * [respectResourcesWhileInBackground].
         *
         * @param [secondsFrom20To40]? Seconds between the events of cycling from background to
         * foreground to background. Sending null will use the default (30s)
         * @return [BackgroundManager.Builder.Policy] To use when initializing
         *   [io.matthewnelson.topl_service.TorServiceController.Builder]
         * */
        fun keepAliveWhileInBackground(secondsFrom20To40: Int? = null): Policy {
            chosenPolicy = BackgroundPolicy.KEEP_ALIVE
            if (secondsFrom20To40 != null && secondsFrom20To40 in 20..40)
                executionDelay = (secondsFrom20To40 * 1000).toLong()
            return Policy(this)
        }

        /**
         * Stops [TorService] after being in the background for the declared [secondsFrom5To45].
         *
         * Your application won't go through it's normal `Application.onCreate` process unless
         * it was killed, but [TorService] may have been stopped when the policy gets executed.
         *
         * Electing this option ensures [TorService] gets restarted in a more reliable manner then
         * returning `Context.START_STICKY` in [TorService.onStartCommand]. It also allows for
         * a proper shutdown of Tor prior to the service being stopped instead of it being
         * killed along with your application (which causes problems sometimes).
         *
         * If killed by the OS then this gets garbage collected such that in the event
         * the user brings your application back into the foreground (after it had been killed),
         * this will be re-instantiated when going through `Application.onCreate` again, and
         * [TorService] started by however you have it implemented.
         *
         * @param [secondsFrom5To45]? Seconds before the [Policy] is executed after the
         *   Application goes to the background. Sending null will use the default (30s)
         * @return [BackgroundManager.Builder.Policy] To use when initializing
         *   [io.matthewnelson.topl_service.TorServiceController.Builder]
         * */
        fun respectResourcesWhileInBackground(secondsFrom5To45: Int? = null): Policy {
            chosenPolicy = BackgroundPolicy.RESPECT_RESOURCES
            if (secondsFrom5To45 != null && secondsFrom5To45 in 5..45)
                executionDelay = (secondsFrom5To45 * 1000).toLong()
            return Policy(this)
        }

        /**
         * Holds the chosen policy to be built in
         * [io.matthewnelson.topl_service.TorServiceController.Builder.build].
         *
         * @param [policyBuilder] The [BackgroundManager.Builder] to be built during initialization
         * */
        class Policy(private val policyBuilder: Builder) {

            /**
             * Only available internally, so this is where we intercept for integration testing.
             * Calling [Policy.build] before
             * [io.matthewnelson.topl_service.TorServiceController.Builder.build] ensures our
             * test classes get initialized so they aren't overwritten by production classes.
             * */
            internal fun build(
                serviceClass: Class<*>,
                serviceConnection: BaseServiceConnection
            ) {

                // Only initialize it once. Reflection has issues here
                // as it's in a Companion object.
                try {
                    backgroundManager.hashCode()
                } catch (e: UninitializedPropertyAccessException) {
                    backgroundManager =
                        BackgroundManager(
                            policyBuilder.chosenPolicy,
                            policyBuilder.executionDelay,
                            serviceClass,
                            serviceConnection
                        )
                }
            }
        }
    }

    internal companion object {
        private lateinit var backgroundManager: BackgroundManager

        // TODO: re-implement in BaseService as a monitor for Tor's state so it can automatically
        //  handle hiccups (such as network getting stuck b/c Android is sometimes unreliable,
        //  or Bootstrapping stalling).
//        var heartbeatTime = 30_000L
//            private set
//
//        fun initialize(milliseconds: Long) {
//            heartbeatTime = milliseconds
//        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun applicationMovedToForeground() {
        // if the last _accepted_ ServiceAction to be issued by the Application was not to STOP
        // the service, then we want to put it back in the state it was in
        if (!BaseService.wasLastAcceptedServiceActionStop()) {
            BaseServiceConnection.serviceBinder?.cancelExecuteBackgroundPolicyJob()
            BaseService.startService(BaseService.getAppContext(), serviceClass, serviceConnection)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun applicationMovedToBackground() {
        if (!BaseService.wasLastAcceptedServiceActionStop())
            BaseServiceConnection.serviceBinder?.executeBackgroundPolicyJob(policy, executionDelay)
    }
}