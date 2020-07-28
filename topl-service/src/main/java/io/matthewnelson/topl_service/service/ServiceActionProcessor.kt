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
package io.matthewnelson.topl_service.service

import android.content.Intent
import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.util.ServiceConsts
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.reflect.InvocationTargetException

/**
 * [ServiceConsts.ServiceAction]'s are translated to [ActionCommands.ServiceActionObject]'s,
 * submitted to a queue, and then processed. This allows for sequential execution of
 * individual [ServiceConsts.ActionCommand]'s for each [ActionCommands.ServiceActionObject]
 * and the ability to quickly interrupt execution for reacting to User actions (such as
 * stopping, or clearing the task).
 *
 * @param [torService] For accessing internally public values
 * @see [ActionCommands]
 * */
internal class ServiceActionProcessor(private val torService: BaseService): ServiceConsts() {

    private val onionProxyManager: OnionProxyManager
        get() = torService.onionProxyManager

    private val broadcastLogger =
        onionProxyManager.getBroadcastLogger(ServiceActionProcessor::class.java)
    private val serviceActionObjectGetter = ActionCommands.ServiceActionObjectGetter()

    fun processIntent(intent: Intent) {
        val actionObject = try {
            serviceActionObjectGetter.get(intent)
        } catch (e: IllegalArgumentException) {
            broadcastLogger.exception(e)
            return
        }
        processActionObject(actionObject)
    }

    private fun processActionObject(serviceActionObject: ActionCommands.ServiceActionObject) {
        when (serviceActionObject) {
            is ActionCommands.Destroy -> {
                torService.unregisterReceiver()
                clearActionQueue()
            }
            is ActionCommands.Stop -> {
                torService.unregisterReceiver()
                clearActionQueue()
                broadcastLogger.notice(ServiceAction.STOP)
            }
            is ActionCommands.Start -> {
                clearActionQueue()
                torService.registerReceiver()
                torService.stopForegroundService()
            }
        }

        if (addActionToQueue(serviceActionObject))
            launchProcessQueueJob()
    }

    private fun broadcastDebugObjectDetailsMsg(prefix: String, something: Any) {
        broadcastLogger.debug(
            "$prefix${something.javaClass.simpleName}@${something.hashCode()}"
        )
    }


    ////////////////////
    /// Action Queue ///
    ////////////////////
    private val actionQueueLock = Object()
    private val actionQueue = mutableListOf<ActionCommands.ServiceActionObject>()

    private fun addActionToQueue(serviceActionObject: ActionCommands.ServiceActionObject): Boolean =
        synchronized(actionQueueLock) {
            return if (actionQueue.add(serviceActionObject)) {
                broadcastDebugObjectDetailsMsg(
                    "Added to queue: ServiceActionObject.", serviceActionObject
                )
                true
            } else {
                false
            }
        }

    private fun removeActionFromQueue(serviceActionObject: ActionCommands.ServiceActionObject) =
        synchronized(actionQueueLock) {
            if (actionQueue.remove(serviceActionObject))
                broadcastDebugObjectDetailsMsg(
                    "Removed from queue: ServiceActionObject.", serviceActionObject
                )
        }

    /**
     * Clears the [actionQueue]
     * */
    private fun clearActionQueue() =
        synchronized(actionQueueLock) {
            if (!actionQueue.isNullOrEmpty()) {
                actionQueue.clear()
                broadcastLogger.debug("Queue cleared")
            }
        }


    ////////////////////////
    /// Queue Processing ///
    ////////////////////////
    private lateinit var processQueueJob: Job

    /**
     * Processes the [actionQueue].
     * */
    private fun launchProcessQueueJob() {
        if (::processQueueJob.isInitialized && processQueueJob.isActive) return
        processQueueJob = torService.getScopeMain().launch(Dispatchers.IO) {
            broadcastDebugObjectDetailsMsg("Processing Queue: ", this)

            while (actionQueue.size > 0 && isActive) {
                val actionObject = actionQueue.elementAtOrNull(0)
                if (actionObject == null) {
                    return@launch
                } else {
                    broadcastLogger.notice(actionObject.serviceAction)
                    actionObject.commands.forEachIndexed { index, command ->

                        // Check if the current actionObject being executed has been
                        // removed from the queue before executing it's next command.
                        if (actionQueue.elementAtOrNull(0) != actionObject) {
                            broadcastDebugObjectDetailsMsg(
                                "Interrupting execution of: ServiceActionObject.", actionObject
                            )
                            return@forEachIndexed
                        }

                        var delayLength = 0L
                        if (command == ActionCommand.DELAY)
                            delayLength = actionObject.consumeDelayLength()
                        processActionCommand(command, delayLength)

                        if (index == actionObject.commands.lastIndex) {
                            removeActionFromQueue(actionObject)
                        }
                    }
                }
            }
        }
    }

    private suspend fun processActionCommand(@ActionCommand command: String, delayLength: Long) {
        when (command) {
            ActionCommand.DELAY -> {
                if (delayLength > 0L)
                    delay(delayLength)
            }
            ActionCommand.DESTROY -> {
                torService.unregisterPrefsListener()
                torService.removeNotification()
                delay(300L)
                torService.cancelSupervisorJob()
            }
            ActionCommand.NEW_ID -> {
                onionProxyManager.signalNewNym()
            }
            ActionCommand.START_TOR -> {
                if (!onionProxyManager.hasControlConnection) {
                    startTor()
                    delay(300L)
                }
            }
            ActionCommand.STOP_SERVICE -> {
                stopService()
            }
            ActionCommand.STOP_TOR -> {
                if (onionProxyManager.hasControlConnection) {
                    stopTor()
                    delay(300L)
                }
            }
        }
    }


    /////////////////////////
    /// Execution Methods ///
    /////////////////////////
    private fun stopService() {
        broadcastDebugObjectDetailsMsg("Stopping: ", torService)
        TorServiceController.unbindTorService(torService.context.applicationContext)
        torService.stopSelf()
    }

    @WorkerThread
    private fun startTor() {
        try {
            onionProxyManager.setup()
            generateTorrcFile()

            onionProxyManager.start()
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }

    @WorkerThread
    private fun stopTor() {
        try {
            onionProxyManager.stop()
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }

    @WorkerThread
    @Throws(
        SecurityException::class,
        IllegalAccessException::class,
        IllegalArgumentException::class,
        InvocationTargetException::class,
        NullPointerException::class,
        ExceptionInInitializerError::class,
        IOException::class
    )
    private fun generateTorrcFile() {
        onionProxyManager.getNewSettingsBuilder()
            .updateTorSettings()
            .setGeoIpFiles()
            .finishAndWriteToTorrcFile()
    }
}