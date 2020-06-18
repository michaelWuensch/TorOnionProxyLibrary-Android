package io.matthewnelson.topl_service.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import io.matthewnelson.topl_service.R
import io.matthewnelson.topl_service.service.TorService

internal class ServiceNotification(
    private val channelName: String,
    private val channelID: String,
    private val channelDescription: String,
    private val notificationID: Int,


    var activityWhenTapped: Class<*>? = null,
    var activityIntentKey: String? = null,
    var activityIntentExtras: String? = null,

    @DrawableRes var imageOn: Int = R.drawable.tor_stat_on,
    @DrawableRes var imageOff: Int = R.drawable.tor_stat_off,
    @DrawableRes var imageData: Int = R.drawable.tor_stat_dataxfer,
    @DrawableRes var imageError: Int = R.drawable.tor_stat_notifyerr,

    @ColorRes var colorRes: Int = R.color.tor_channel_color_white,

    var enableRestartButton: Boolean = false,
    var enableStopButton: Boolean = false
) {
    companion object {
        private lateinit var notification: ServiceNotification

        fun initialize(serviceNotification: ServiceNotification) {
            if (!::notification.isInitialized)
                notification = serviceNotification
        }

        fun get(): ServiceNotification {
            if (!::notification.isInitialized)
                notification = ServiceNotification(
                    "CHANGE ME",
                    "TorService Channel",
                    "BSG is a national treasure",
                    615615
                )

            return notification
        }
    }

    private var contentTitle = "Tor"

    fun setupNotificationChannel(context: Context) {
        val nm: NotificationManager? = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

    fun startForegroundNotification(torService: TorService) {
        val notificationBuilder = NotificationCompat.Builder(torService, channelID)
            .setContentTitle(contentTitle)
            .setContentText("Waiting...")
            .setOngoing(true)
            .setSound(null)
            .setOnlyAlertOnce(true)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
            .setGroup("Tor")
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setGroupSummary(false)
            .setSmallIcon(imageOff)

        if (activityWhenTapped != null) {
            val contentIntent = Intent(torService, activityWhenTapped)

            if (!activityIntentKey.isNullOrEmpty() && !activityIntentExtras.isNullOrEmpty())
                contentIntent.putExtra(activityIntentKey, activityIntentExtras)

            val pendingIntent = PendingIntent
                .getActivity(torService, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            notificationBuilder.setContentIntent(pendingIntent)
        }

        torService.startForeground(notificationID, notificationBuilder.build())
    }
}