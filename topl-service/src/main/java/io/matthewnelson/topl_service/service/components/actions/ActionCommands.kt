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
package io.matthewnelson.topl_service.service.components.actions

import android.content.Intent
import io.matthewnelson.topl_service.util.ServiceConsts.ActionCommand
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceAction

/**
 * Facilitates mapping of a [ServiceAction] to an object which allows for individual command
 * execution by [io.matthewnelson.topl_service.service.components.ServiceActionProcessor] in a
 * repeatable manner. This allows for structured execution depending on the [ServiceAction] passed
 * to [io.matthewnelson.topl_service.service.TorService] via Intent, while still maintaining an
 * easy way to interrupt coroutine command execution for quickly responding to user actions.
 *
 * Think, running machine code to grok.
 * */
internal sealed class ActionCommands {

    abstract class ServiceActionObject: ActionCommands() {

        abstract val serviceAction: @ServiceAction String

        /**
         * Individual [ActionCommand]'s to executed sequentially by
         * [io.matthewnelson.topl_service.service.components.ServiceActionProcessor].
         * */
        abstract val commands: Array<@ActionCommand String>

        /**
         * For every [ActionCommand.DELAY] within [commands], a value will be consumed
         * when executing it.
         *
         * Override this to define the values for each DELAY call.
         * */
        protected open val delayLengthQueue: MutableList<Long> = mutableListOf()

        /**
         * Removes the 0th element within [delayLengthQueue] then returns it.
         * If [delayLengthQueue] is empty, returns 0L.
         *
         * @return The 0th element within [delayLengthQueue], or 0L if empty
         * */
        fun consumeDelayLength(): Long {
            return if (delayLengthQueue.isNotEmpty()) {
                delayLengthQueue.removeAt(0)
            } else {
                0L
            }
        }
    }

    class NewId(override val serviceAction: String) : ServiceActionObject() {
        override val commands: Array<String>
            get() = arrayOf(
                ActionCommand.NEW_ID
            )
    }

    class RestartTor(override val serviceAction: String) : ServiceActionObject() {
        override val commands: Array<String>
            get() = arrayOf(
                ActionCommand.STOP_TOR,
                ActionCommand.DELAY,
                ActionCommand.START_TOR
            )
        override val delayLengthQueue =
            mutableListOf(ServiceActionProcessor.restartTorDelayTime)
    }

    class Start(override val serviceAction: String) : ServiceActionObject() {
        override val commands: Array<String>
            get() = arrayOf(
                ActionCommand.START_TOR
            )
    }

    class Stop(override val serviceAction: String) : ServiceActionObject() {
        override val commands: Array<String>
            get() = arrayOf(
                ActionCommand.STOP_TOR,
                ActionCommand.DELAY,
                ActionCommand.STOP_SERVICE
            )
        override val delayLengthQueue =
            mutableListOf(ServiceActionProcessor.stopServiceDelayTime)
    }

    class ServiceActionObjectGetter {

        /**
         * Processes an Intent by it's contained action and returns a [ServiceActionObject]
         * for the passed [ServiceAction]
         *
         * @param [intent] The intent containing an appropriate [ServiceAction]
         * @return [ServiceActionObject] associated with the intent's action (a [ServiceAction])
         * @throws [IllegalArgumentException] if the intent's action isn't a [ServiceAction]
         * */
        @Throws(IllegalArgumentException::class)
        fun get(intent: Intent): ServiceActionObject {
            return when (val action = intent.action) {
                ServiceAction.NEW_ID -> {
                    NewId(
                        action
                    )
                }
                ServiceAction.RESTART_TOR -> {
                    RestartTor(
                        action
                    )
                }
                ServiceAction.START -> {
                    Start(
                        action
                    )
                }
                ServiceAction.STOP -> {
                    Stop(
                        action
                    )
                }
                else -> {
                    throw (IllegalArgumentException())
                }
            }
        }
    }
}