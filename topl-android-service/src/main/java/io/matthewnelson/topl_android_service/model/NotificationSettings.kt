package io.matthewnelson.topl_android_service.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.matthewnelson.topl_android_service.R

internal class NotificationSettings(
    val channelDescription: String,
    val id: Int,
    var show: Boolean = false,
    @DrawableRes var imageOn: Int = R.drawable.tor_stat_on,
    @DrawableRes var imageOff: Int = R.drawable.tor_stat_off,
    @DrawableRes var imageData: Int = R.drawable.tor_stat_dataxfer,
    @DrawableRes var imageError: Int = R.drawable.tor_stat_notifyerr,
    @ColorRes var colorRes: Int = R.color.tor_channel_color_white,
    var enableRestartButton: Boolean = false,
    var enableStopButton: Boolean = false
) {
    companion object {
        const val DEFAULT_CHAN_DESC = ""
        const val DEFAULT_CHAN_ID = 615615

        private lateinit var notificationSettings: NotificationSettings

        fun initialize(settings: NotificationSettings) {
            if (!::notificationSettings.isInitialized) {
                notificationSettings = settings
            }
        }

        fun getNotificationSettings(): NotificationSettings =
            if (::notificationSettings.isInitialized)
                notificationSettings
            else
                NotificationSettings(DEFAULT_CHAN_DESC, DEFAULT_CHAN_ID)
    }
}