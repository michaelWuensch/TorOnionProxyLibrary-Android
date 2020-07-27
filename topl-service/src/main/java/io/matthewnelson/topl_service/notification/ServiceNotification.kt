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
package io.matthewnelson.topl_service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.NotificationVisibility
import androidx.core.content.ContextCompat
import io.matthewnelson.topl_service.R
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.receiver.TorServiceReceiver
import io.matthewnelson.topl_service.util.ServiceConsts

/**
 * Everything to do with [TorService]'s notification.
 *
 * See [io.matthewnelson.topl_service.TorServiceController.Builder.NotificationBuilder]
 * */
class ServiceNotification internal constructor(
    private val channelName: String,
    private val channelID: String,
    private val channelDescription: String,
    private val notificationID: Int,

    var activityWhenTapped: Class<*>? = null,
    var activityIntentKey: String? = null,
    var activityIntentExtras: String? = null,
    var activityIntentRequestCode: Int = 0,

    @DrawableRes var imageNetworkEnabled: Int = R.drawable.tor_stat_network_enabled,
    @DrawableRes var imageNetworkDisabled: Int = R.drawable.tor_stat_network_disabled,
    @DrawableRes var imageDataTransfer: Int = R.drawable.tor_stat_network_dataxfer,
    @DrawableRes var imageError: Int = R.drawable.tor_stat_notifyerr,

    @ColorRes var colorWhenConnected: Int = R.color.tor_service_white,

    @NotificationVisibility var visibility: Int = NotificationCompat.VISIBILITY_SECRET,

    var enableRestartButton: Boolean = false,
    var enableStopButton: Boolean = false,

    var showNotification: Boolean = true
): ServiceConsts() {

    companion object {
        private lateinit var serviceNotification: ServiceNotification

        internal fun initialize(serviceNotificay: ServiceNotification) {
            if (!::serviceNotification.isInitialized)
                serviceNotification = serviceNotificay
        }

        internal fun get(): ServiceNotification {
            return if (::serviceNotification.isInitialized)
                serviceNotification
            else
                throw UninitializedPropertyAccessException("A Notification must be setup " +
                        "to properly shutdown Tor when your task is removed. If you wish to " +
                        "not show a notification while your Application is running, elect the " +
                        "showNotification(false) method when initializing the Builder")
        }
    }


    /////////////
    /// Setup ///
    /////////////
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var notificationManager: NotificationManager? = null

    internal fun buildNotification(torService: TorService): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(torService, channelID)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setContentText(currentContent)
            .setContentTitle(currentTitle)
            .setGroup("TorService")
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
            .setGroupSummary(false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(0, 100, true)
            .setSmallIcon(currentIcon)
            .setSound(null)
            .setVisibility(visibility)

        if (activityWhenTapped != null)
            builder.setContentIntent(getContentPendingIntent(torService))

        notificationBuilder = builder
        return builder
    }

    private fun getContentPendingIntent(torService: TorService): PendingIntent {
        val contentIntent = Intent(torService, activityWhenTapped)

        if (!activityIntentKey.isNullOrEmpty() && !activityIntentExtras.isNullOrEmpty())
            contentIntent.putExtra(activityIntentKey, activityIntentExtras)

        return PendingIntent.getActivity(
            torService,
            activityIntentRequestCode,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun notify(builder: NotificationCompat.Builder) {
        notificationBuilder = builder
        if (showNotification || inForeground)
            notificationManager?.notify(notificationID, builder.build())
    }

    @Synchronized
    internal fun remove() {
        if (!inForeground && showNotification)
            notificationManager?.cancel(notificationID)
    }

    /**
     * Called once per application start in
     * [io.matthewnelson.topl_service.TorServiceController.Builder.build]
     * */
    internal fun setupNotificationChannel(context: Context): ServiceNotification {
        val nm: NotificationManager? =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = channelDescription
            channel.setSound(null, null)
            nm?.let {
                notificationManager = it
                it.createNotificationChannel(channel)
            }
        }
        return serviceNotification
    }


    //////////////////////////
    /// Foreground Service ///
    //////////////////////////
    @Volatile
    private var inForeground = false

    @Synchronized
    internal fun startForeground(torService: TorService): ServiceNotification {
        if (!inForeground) {
            torService.startForeground(notificationID, notificationBuilder.build())
            inForeground = true
        }
        return serviceNotification
    }

    @Synchronized
    internal fun stopForeground(torService: TorService, removeNotification: Boolean = false): ServiceNotification {
        if (inForeground) {
            torService.stopForeground(if (removeNotification) true else !showNotification)
            inForeground = false
        }
        return serviceNotification
    }


    ///////////////
    /// Actions ///
    ///////////////
    @Synchronized
    internal fun addActions(torService: TorService) {
        val builder = notificationBuilder
        builder.addAction(
            imageNetworkEnabled,
            "New Identity",
            getActionPendingIntent(torService, ServiceAction.NEW_ID, 1)
        )

        if (enableRestartButton)
            builder.addAction(
                imageNetworkEnabled,
                "Restart Tor",
                getActionPendingIntent(torService, ServiceAction.RESTART_TOR, 2)
            )

        if (enableStopButton)
            builder.addAction(
                imageNetworkEnabled,
                "Stop Tor",
                getActionPendingIntent(torService, ServiceAction.STOP, 3)
            )
        notify(builder)
    }

    private fun getActionPendingIntent(
        torService: TorService,
        @ServiceAction action: String,
        requestCode: Int
    ): PendingIntent {
        val intent = Intent(TorServiceReceiver.SERVICE_INTENT_FILTER)
        intent.putExtra(TorServiceReceiver.SERVICE_INTENT_FILTER, action)
        intent.setPackage(torService.packageName)

        return PendingIntent.getBroadcast(
            torService,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @Synchronized
    internal fun removeActions(torService: TorService) {
        notify(buildNotification(torService))
    }


    ////////////////////
    /// Content Text ///
    ////////////////////
    @Volatile
    private var currentContent = "Waiting..."

    @Synchronized
    internal fun updateContentText(string: String) {
        if (currentContent == string) return
        currentContent = string
        val builder = notificationBuilder
        builder.setContentText(string)
        notify(builder)
    }


    /////////////////////
    /// Content Title ///
    /////////////////////
    @Volatile
    private var currentTitle = TorState.OFF

    @Synchronized
    internal fun updateContentTitle(title: String) {
        if (currentTitle == title) return
        currentTitle = title
        val builder = notificationBuilder
        builder.setContentTitle(title)
        notify(builder)
    }


    ////////////
    /// Icon ///
    ////////////
    @Volatile
    private var currentIcon = imageNetworkDisabled

    @Synchronized
    internal fun updateIcon(torService: TorService, @NotificationImage notificationImage: Int) {
        val builder = notificationBuilder
        when (notificationImage) {
            NotificationImage.ENABLED -> {
                if (currentIcon == imageNetworkEnabled) return
                currentIcon = imageNetworkEnabled
                builder.setSmallIcon(imageNetworkEnabled)
                builder.color = ContextCompat.getColor(torService, colorWhenConnected)
            }
            NotificationImage.DISABLED -> {
                if (currentIcon == imageNetworkDisabled) return
                currentIcon = imageNetworkDisabled
                builder.setSmallIcon(imageNetworkDisabled)
                builder.color = ContextCompat.getColor(torService, R.color.tor_service_white)
            }
            NotificationImage.DATA -> {
                if (currentIcon == imageDataTransfer) return
                currentIcon = imageDataTransfer
                builder.setSmallIcon(imageDataTransfer)
            }
            NotificationImage.ERROR -> {
                if (currentIcon == imageError) return
                currentIcon = imageError
                builder.setSmallIcon(imageError)
            }
            else -> {}
        }
        notify(builder)
    }


    ////////////////////
    /// Progress Bar ///
    ////////////////////
    @Synchronized
    internal fun updateProgress(show: Boolean, progress: Int? = null) {
        val builder = notificationBuilder
        when {
            progress != null -> {
                builder.setProgress(100, progress, false)
            }
            show -> {
                builder.setProgress(100, 0, true)
            }
            else -> {
                builder.setProgress(0, 0, false)
            }
        }
        notify(builder)
    }
}