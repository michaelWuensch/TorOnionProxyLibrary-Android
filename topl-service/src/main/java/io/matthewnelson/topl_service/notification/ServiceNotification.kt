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
import io.matthewnelson.topl_service.util.ServiceConsts

/**
 * Everything to do with [TorService]'s notification.
 *
 * See [io.matthewnelson.topl_service.TorServiceController.Builder.NotificationBuilder]
 * */
internal class ServiceNotification(
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
    var colorizeBackground: Boolean = false,

    @ColorRes var colorWhenConnected: Int = R.color.tor_service_white,

    @NotificationVisibility var visibility: Int = NotificationCompat.VISIBILITY_SECRET,

    var enableRestartButton: Boolean = false,
    var enableStopButton: Boolean = false
): ServiceConsts() {

    companion object {
        private lateinit var serviceNotification: ServiceNotification

        fun initialize(serviceNotificay: ServiceNotification) {
            if (!::serviceNotification.isInitialized)
                serviceNotification = serviceNotificay
        }

        fun get(): ServiceNotification {
            if (!::serviceNotification.isInitialized)
                serviceNotification = ServiceNotification(
                    "CHANGE ME",
                    "TorService Channel",
                    "BSG is a national treasure",
                    615615
                )

            return serviceNotification
        }
    }

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    private fun notify(builder: NotificationCompat.Builder) {
        notificationBuilder = builder
        if (::notificationManager.isInitialized)
            notificationManager.notify(notificationID, builder.build())
    }


    ////////////////////////////
    /// Notification Channel ///
    ////////////////////////////
    /**
     * Called once per application start in
     * [io.matthewnelson.topl_service.TorServiceController.Builder.build]
     * */
    fun setupNotificationChannel(context: Context) {
        val nm: NotificationManager? = context.applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm?.let { notificationManager = it }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = channelDescription
            channel.setSound(null, null)
            nm?.createNotificationChannel(channel)
        }
    }


    //////////////////////////
    /// Foreground Service ///
    //////////////////////////
    /**
     * Called at [TorService.onCreate] and sets the [notificationBuilder] variable such that
     * it can be re-used/updated throughout the life of [TorService].
     * */
    fun startForegroundNotification(torService: TorService) {
        val builder = buildInitialNotification(torService, TorState.OFF)
        torService.startForeground(notificationID, builder.build())
    }

    private fun buildInitialNotification(
        torService: TorService,
        title: String
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(torService.applicationContext, channelID)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setColorized(colorizeBackground)
            .setContentText("Waiting...")
            .setContentTitle(title)
            .setGroup("TorService")
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
            .setGroupSummary(false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSmallIcon(imageNetworkDisabled)
            .setSound(null)
            .setVisibility(visibility)

        if (colorizeBackground)
            builder.color = ContextCompat.getColor(torService, colorWhenConnected)

        if (activityWhenTapped != null)
            builder.setContentIntent(getContentPendingIntent(torService))

        notificationBuilder = builder
        return builder
    }

    private fun getContentPendingIntent(torService: TorService): PendingIntent {
        val contentIntent = Intent(torService.applicationContext, activityWhenTapped)

        if (!activityIntentKey.isNullOrEmpty() && !activityIntentExtras.isNullOrEmpty())
            contentIntent.putExtra(activityIntentKey, activityIntentExtras)

        return PendingIntent.getActivity(
            torService.applicationContext,
            activityIntentRequestCode,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    ///////////////
    /// Actions ///
    ///////////////
    @Synchronized
    fun addActions(torService: TorService) {
        val builder = notificationBuilder
        builder.addAction(
            imageNetworkEnabled,
            "New Identity",
            getActionPendingIntent(torService, ServiceAction.ACTION_NEW_ID)
        )

        if (enableRestartButton)
            builder.addAction(
                imageNetworkEnabled,
                "Restart Tor",
                getActionPendingIntent(torService, ServiceAction.ACTION_RESTART)
            )

        if (enableStopButton)
            builder.addAction(
                imageNetworkEnabled,
                "Stop Tor",
                getActionPendingIntent(torService, ServiceAction.ACTION_STOP)
            )
        notify(builder)
    }

    private fun getActionPendingIntent(
        torService: TorService,
        @ServiceAction action: String
    ): PendingIntent {
        val intent = Intent(torService.applicationContext, TorService::class.java)
        intent.action = action

        return PendingIntent.getService(
            torService.applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @Synchronized
    fun removeActions(torService: TorService, @TorState state: String) {
        val builder = buildInitialNotification(torService, state)
        notify(builder)
    }


    /////////////////////
    /// Content Text ///
    /////////////////////
    @Synchronized
    fun updateContentText(string: String) {
        val builder = notificationBuilder
        builder.setContentText(string)
        notify(builder)
    }


    /////////////////////
    /// Content Title ///
    /////////////////////
    @Synchronized
    fun updateContentTitle(title: String) {
        val builder = notificationBuilder
        builder.setContentTitle(title)
        notify(builder)
    }


    ////////////
    /// Icon ///
    ////////////
    @Synchronized
    fun updateIcon(torService: TorService, @NotificationImage notificationImage: Int) {
        val builder = notificationBuilder
        when (notificationImage) {
            NotificationImage.ENABLED -> {
                builder.setSmallIcon(imageNetworkEnabled)
                if (!colorizeBackground)
                    builder.color = ContextCompat.getColor(torService, colorWhenConnected)
            }
            NotificationImage.DISABLED -> {
                builder.setSmallIcon(imageNetworkDisabled)
                if (!colorizeBackground)
                    builder.color = ContextCompat.getColor(torService, R.color.tor_service_white)
            }
            NotificationImage.DATA -> {
                builder.setSmallIcon(imageDataTransfer)
            }
            NotificationImage.ERROR -> {
                builder.setSmallIcon(imageError)
            }
            else -> {}
        }
        notify(builder)
    }
}