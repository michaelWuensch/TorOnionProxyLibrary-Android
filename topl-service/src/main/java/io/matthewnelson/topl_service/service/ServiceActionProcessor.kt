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

internal class ServiceActionProcessor(private val torService: TorService): ServiceConsts() {

    companion object {

        // Needed to inhibit all TorServiceController methods except for startTor()
        // from sending such that startService isn't called without properly
        // starting Tor first.
        @Volatile
        var isAcceptingActions = false
            private set
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

    fun processActionObject(serviceActionObject: ActionCommands.ServiceActionObject) {
        addActionToQueue(serviceActionObject)

        if (serviceActionObject is ActionCommands.Stop) {
            isAcceptingActions = false

            // If there are actions queued previous to Stop, clear them.
            if (actionQueue.size > 1)
                launchClearQueueJob()

        } else if (serviceActionObject is ActionCommands.Start) {
            isAcceptingActions = true
        }

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

    private fun addActionToQueue(serviceActionObject: ActionCommands.ServiceActionObject) =
        synchronized(actionQueueLock) {
            if (actionQueue.add(serviceActionObject))
                broadcastDebugObjectDetailsMsg(
                    "Added to queue: ServiceActionObject.", serviceActionObject
                )
        }

    private fun removeActionFromQueue(serviceActionObject: ActionCommands.ServiceActionObject) {
        synchronized(actionQueueLock) {
            if (actionQueue.remove(serviceActionObject))
                broadcastDebugObjectDetailsMsg(
                    "Removed from queue: ServiceActionObject.", serviceActionObject
                )
        }
    }


    //////////////////
    /// Coroutines ///
    //////////////////
    private val scopeMain: CoroutineScope
        get() = torService.scopeMain
    private lateinit var clearQueueJob: Job
    private lateinit var processQueueJob: Job

    /**
     * Will clear [actionQueue] up to the [ActionCommands.Stop] insertion
     * */
    private fun launchClearQueueJob() {
        synchronized(actionQueueLock) {
            if (!::clearQueueJob.isInitialized || !clearQueueJob.isActive) {
                clearQueueJob = scopeMain.launch(Dispatchers.Default) {
                    val iterator = actionQueue.listIterator()
                    while (iterator.hasNext() && isActive) {
                        val serviceActionObject = iterator.next()
                        if (serviceActionObject is ActionCommands.Stop) {
                            return@launch
                        }
                        iterator.remove()
                        broadcastDebugObjectDetailsMsg(
                            "Removed from queue: ServiceActionObject.", serviceActionObject
                        )
                    }
                }
            }
        }
    }

    /**
     * Processes the [actionQueue]. Checks to see if [clearQueueJob] is active before executing
     * every [ServiceConsts.ActionCommand], will finish processing the current command and then
     * wait for completion of [clearQueueJob] before continuing to process [actionQueue].
     * */
    private fun launchProcessQueueJob() {
        if (!::processQueueJob.isInitialized || !processQueueJob.isActive) {
            processQueueJob = scopeMain.launch(Dispatchers.IO) {

                while (actionQueue.size > 0 && isActive) {

                    // Hold processing the queue until after clearQueueJob is done.
                    // Happens more frequently if the queue is big enough and the timing
                    // is just right.
                    if (::clearQueueJob.isInitialized && clearQueueJob.isActive) {
                        clearQueueJob.join()
                        delay(25L)
                    }

                    val actionObject = actionQueue[0]
                    actionObject.commands.forEachIndexed { index, command ->

                        // Check if the current actionObject being executed has been
                        // removed from the queue by clearQueueJob before executing the
                        // next command.
                        if (actionQueue[0] != actionObject) {
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
                            }
                            ActionCommand.STOP_SERVICE -> {
                                stopService()
                            }
                            ActionCommand.STOP_TOR -> {
                                stopTor()
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