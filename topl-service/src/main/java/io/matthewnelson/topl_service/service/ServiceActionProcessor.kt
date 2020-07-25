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
import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core.OnionProxyManager
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
internal class ServiceActionProcessor(private val torService: TorService): ServiceConsts() {

    companion object {

        // Needed to inhibit all TorServiceController methods except for startTor()
        // from sending such that startService isn't called without properly
        // starting Tor first.
        @Volatile
        var isAcceptingActions = false
            private set
    }

    fun setIsAcceptingActions(value: Boolean) {
        isAcceptingActions = value
    }

    private val onionProxyManager: OnionProxyManager
        get() = torService.onionProxyManager

    private val broadcastLogger = onionProxyManager.getBroadcastLogger(ServiceActionProcessor::class.java)
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
        if (serviceActionObject is ActionCommands.Stop) {
            isAcceptingActions = false
            clearActionQueue()
            broadcastLogger.notice(ServiceAction.STOP)
        } else if (serviceActionObject is ActionCommands.Start) {
            clearActionQueue()
            isAcceptingActions = true
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
    private val scopeMain: CoroutineScope
        get() = torService.scopeMain
    private lateinit var processQueueJob: Job

    /**
     * Processes the [actionQueue].
     * */
    private fun launchProcessQueueJob() {
        if (::processQueueJob.isInitialized && processQueueJob.isActive) return
        processQueueJob = scopeMain.launch(Dispatchers.IO) {
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

                        when (command) {
                            ActionCommand.DELAY -> {
                                delay(actionObject.consumeDelayLength())
                            }
                            ActionCommand.NEW_ID -> {
                                onionProxyManager.signalNewNym()
                            }
                            ActionCommand.START_TOR -> {
                                startTor()
                                delay(300L)
                            }
                            ActionCommand.STOP_SERVICE -> {
                                stopService()
                            }
                            ActionCommand.STOP_TOR -> {
                                stopTor()
                                delay(300L)
                            }
                        }
                        if (index == actionObject.commands.lastIndex) {
                            removeActionFromQueue(actionObject)
                        }
                    }
                }
            }
        }
    }


    /////////////////////////
    /// Execution Methods ///
    /////////////////////////
    private fun stopService() {
        if (!isAcceptingActions) {
            broadcastDebugObjectDetailsMsg("Stopping: ", torService)
            torService.stopSelf()
        }
    }

    @WorkerThread
    private fun startTor() {
        if (onionProxyManager.hasControlConnection) return
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