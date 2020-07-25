/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.topl_service.service

import android.content.Intent
import io.matthewnelson.topl_service.util.ServiceConsts.ActionCommand
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceAction

/**
 * Facilitates mapping of a [ServiceAction] to an object which allows for individual command
 * execution by [io.matthewnelson.topl_service.service.ServiceActionProcessor] in a repeatable
 * manner. This allows for structured execution depending on the [ServiceAction] passed to
 * [TorService] via Intent, while still maintaining an easy way to interrupt coroutine command
 * execution for quickly responding to user actions.
 *
 * Think, running machine code to grok.
 * */
internal sealed class ActionCommands {

    abstract class ServiceActionObject: ActionCommands() {

        abstract val serviceAction: @ServiceAction String

        /**
         * Individual [ActionCommand]'s to executed sequentially by
         * [io.matthewnelson.topl_service.service.ServiceActionProcessor].
         * */
        abstract val commands: Array<@ActionCommand String>

        /**
         * For every ActionCommand.DELAY called for, a value will be consumed
         * when executing the DELAY command.
         *
         * Override this to define the values for each DELAY call.
         * */
        protected open val delayLengthQueue: MutableList<Long> = mutableListOf()

        /**
         * Consumes the 0th element within [delayLengthQueue], removes, then returns it.
         * If [delayLengthQueue] is empty, returns 0L.
         *
         * @return The 0th element within [delayLengthQueue], or 0L if empty
         * */
        fun consumeDelayLength(): Long {
            return if (delayLengthQueue.isNotEmpty()) {
                val delayLength = delayLengthQueue[0]
                delayLengthQueue.removeAt(0)
                delayLength
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
            mutableListOf(700L)
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
                ActionCommand.STOP_SERVICE
            )
    }

    class ServiceActionObjectGetter {

        /**
         * Processes an Intent by it's contained action and returns a
         * [ServiceActionObject] for the passed [ServiceAction]
         *
         * @param [intent] The intent from [TorService.onStartCommand]
         * @return [ServiceActionObject] associated with the intent's action (a [ServiceAction])
         * @throws [IllegalArgumentException] if the intent's action isn't a [ServiceAction]
         * */
        @Throws(IllegalArgumentException::class)
        fun get(intent: Intent): ServiceActionObject {
            return when (val action = intent.action) {
                ServiceAction.NEW_ID -> {
                    NewId(action)
                }
                ServiceAction.RESTART_TOR -> {
                    RestartTor(action)
                }
                ServiceAction.START -> {
                    Start(action)
                }
                ServiceAction.STOP -> {
                    Stop(action)
                }
                else -> {
                    throw (IllegalArgumentException())
                }
            }
        }
    }
}