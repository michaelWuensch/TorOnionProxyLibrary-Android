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
package io.matthewnelson.topl_service.onionproxy

import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core_base.EventBroadcaster
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.ServiceActionProcessor
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceAction
import io.matthewnelson.topl_service.util.ServiceConsts.NotificationImage
import io.matthewnelson.topl_service.util.ServiceUtilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.freehaven.tor.control.TorControlCommands

/**
 * [io.matthewnelson.topl_core.OnionProxyManager] utilizes this customized class for
 * broadcasting things while it is operating (such as Tor's State, operation errors,
 * debugging, etc).
 *
 * [ServiceEventListener] utilizes this class by sending it what Tor is spitting out
 * (selectively curated, ofc).
 *
 * @param [torService] [TorService] for context.
 * */
internal class ServiceEventBroadcaster(private val torService: TorService): EventBroadcaster() {

    private val serviceNotification: ServiceNotification
        get() = torService.serviceNotification
    private val scopeMain: CoroutineScope
        get() = torService.scopeMain

    /////////////////
    /// Bandwidth ///
    /////////////////
    @Volatile
    private var bytesRead = 0L
    @Volatile
    private var bytesWritten = 0L

    override fun broadcastBandwidth(bytesRead: String, bytesWritten: String) {
        val read =
            try {
                bytesRead.toLong()
            } catch (e: NumberFormatException) {
                this.bytesRead
            }
        val written =
            try {
                bytesWritten.toLong()
            } catch (e: NumberFormatException) {
                this.bytesWritten
            }

        // Only update the notification if proper State is had & we're bootstrapped.
        if (torState == TorState.ON &&
            torNetworkState == TorNetworkState.ENABLED &&
            isBootstrappingComplete()
        ) {
            if (read != this.bytesRead || written != this.bytesWritten) {
                this.bytesRead = read
                this.bytesWritten = written

                updateBandwidth(read, written)

                if (read == 0L && written == 0L)
                    serviceNotification.updateIcon(torService, NotificationImage.ENABLED)
                else
                    serviceNotification.updateIcon(torService, NotificationImage.DATA)
            }
        }

        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastBandwidth(bytesRead, bytesWritten) }
        }
    }

    /**
     * Do a check for if a message is being displayed in the contentText of the
     * notification, allowing it to remain there unabated until the coroutine
     * finishes.
     * */
    private fun updateBandwidth(download: Long, upload: Long) {
        if (::noticeMsgToContentTextJob.isInitialized && noticeMsgToContentTextJob.isActive) return
        serviceNotification.updateContentText(
            ServiceUtilities.getFormattedBandwidthString(download, upload)
        )
    }


    /////////////
    /// Debug ///
    /////////////
    override fun broadcastDebug(msg: String) {
        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastDebug(msg) }
        }
    }


    //////////////////
    /// Exceptions ///
    //////////////////
    override fun broadcastException(msg: String?, e: Exception) {
        if (!msg.isNullOrEmpty()) {
            if (msg.contains(TorService::class.java.simpleName)) {
                serviceNotification.updateIcon(torService, NotificationImage.ERROR)
                val splitMsg = msg.split("|")
                if (splitMsg.size > 2 && splitMsg[2].isNotEmpty()) {
                    serviceNotification.updateContentText(splitMsg[2])
                    serviceNotification.updateProgress(show = false)
                }
            }
        }

        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastException(msg, e) }
        }
    }


    ///////////////////
    /// LogMessages ///
    ///////////////////
    override fun broadcastLogMessage(logMessage: String?) {
        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastLogMessage(logMessage) }
        }
    }


    ///////////////
    /// Notices ///
    ///////////////
    private lateinit var noticeMsgToContentTextJob: Job

    @Volatile
    private var bootstrapProgress = ""
    private fun isBootstrappingComplete(): Boolean =
        bootstrapProgress == "Bootstrapped 100%"

    override fun broadcastNotice(msg: String) {

        // BOOTSTRAPPED
        if (msg.contains("Bootstrapped")) {
            val msgSplit = msg.split(" ")
            if (msgSplit.size > 2) {
                val bootstrapped = "${msgSplit[0]} ${msgSplit[1]}".split("|")[2]

                if (bootstrapped != bootstrapProgress) {
                    serviceNotification.updateContentText(bootstrapped)

                    if (bootstrapped == "Bootstrapped 100%") {
                        serviceNotification.updateIcon(torService, NotificationImage.ENABLED)
                        serviceNotification.updateProgress(show = true, progress = 100)
                        serviceNotification.updateProgress(show = false)
                        serviceNotification.addActions(torService)
                    } else {
                        val progress: Int? = try {
                            bootstrapped.split(" ")[1].split("%")[0].toInt()
                        } catch (e: Exception) {
                            null
                        }
                        progress?.let {
                            serviceNotification.updateProgress(show = true, progress = progress)
                        }
                    }

                    bootstrapProgress = bootstrapped
                }
            }

        // NEWNYM
        } else if (msg.contains(TorControlCommands.SIGNAL_NEWNYM)) {
            val msgToShow: String? =
                when {
                    msg.contains(OnionProxyManager.NEWNYM_SUCCESS_MESSAGE) -> {
                        OnionProxyManager.NEWNYM_SUCCESS_MESSAGE
                    }
                    msg.contains(OnionProxyManager.NEWNYM_NO_NETWORK) -> {
                        OnionProxyManager.NEWNYM_NO_NETWORK
                    }
                    else -> {
                        val msgSplit = msg.split("|")
                        if (msgSplit.size > 2) {
                            msgSplit[2]
                        } else {
                            null
                        }
                    }
                }

            if (::noticeMsgToContentTextJob.isInitialized && noticeMsgToContentTextJob.isActive)
                noticeMsgToContentTextJob.cancel()

            msgToShow?.let {
                displayMessageToContentText(it, 3500L)
            }


        } else if (msg.contains(ServiceActionProcessor::class.java.simpleName)) {
            val msgSplit = msg.split("|")
            if (msgSplit.size > 2) {
                val msgToShow: String? = when (msgSplit[2]) {
                    ServiceAction.RESTART_TOR -> {
                        "Restarting Tor..."
                    }
                    ServiceAction.START -> {
                        "Waiting..."
                    }
                    ServiceAction.STOP -> {
                        "Stopping Service..."
                    }
                    else -> {
                        null
                    }
                }
                msgToShow?.let {
                    serviceNotification.updateContentText(it)
                    serviceNotification.updateIcon(torService, NotificationImage.DISABLED)
                }
            }
        }

        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastNotice(msg) }
        }
    }

    /**
     * Display a message in the notification's ContentText space for the defined
     * [delayMilliSeconds], after which (if Tor is connected), publish to the Notification's
     * ContentText the most recently broadcast bandwidth via [bytesRead] && [bytesWritten].
     * */
    private fun displayMessageToContentText(message: String, delayMilliSeconds: Long) {
        noticeMsgToContentTextJob = scopeMain.launch {
            serviceNotification.updateContentText(message)
            delay(delayMilliSeconds)

            // Publish the last bandwidth broadcast to overwrite the message.
            if (torNetworkState == TorNetworkState.ENABLED) {
                serviceNotification.updateContentText(
                    ServiceUtilities.getFormattedBandwidthString(bytesRead, bytesWritten)
                )
            } else if (isBootstrappingComplete()){
                serviceNotification.updateContentText(
                    ServiceUtilities.getFormattedBandwidthString(0L, 0L)
                )
            }
        }
    }


    ////////////////
    /// TorState ///
    ////////////////
    @Volatile
    private var torState = TorState.OFF
    @Volatile
    private var torNetworkState = TorNetworkState.DISABLED

    override fun broadcastTorState(@TorState state: String, @TorNetworkState networkState: String) {
        if (torState == TorState.ON && state != torState) {
            bootstrapProgress = ""
            serviceNotification.removeActions(torService)
        }

        if (state != TorState.ON)
            serviceNotification.updateProgress(show = true)

        serviceNotification.updateContentTitle(state)
        torState = state

        if (networkState == TorNetworkState.DISABLED) {
            // Update torNetworkState _before_ setting the icon to `disabled` so bandwidth won't
            // overwrite the icon with an update
            torNetworkState = networkState
            serviceNotification.updateIcon(torService, NotificationImage.DISABLED)
        } else {
            if (isBootstrappingComplete())
                serviceNotification.updateIcon(torService, NotificationImage.ENABLED)

            // Update torNetworkState _after_ setting the icon to `enabled` so bandwidth changes
            // occur afterwards and this won't overwrite ImageState.DATA
            torNetworkState = networkState
        }

        TorServiceController.appEventBroadcaster?.let {
            scopeMain.launch { it.broadcastTorState(state, networkState) }
        }
    }
}