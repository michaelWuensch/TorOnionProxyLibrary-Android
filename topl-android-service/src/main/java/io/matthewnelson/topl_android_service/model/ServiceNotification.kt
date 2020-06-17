package io.matthewnelson.topl_android_service.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.matthewnelson.topl_android_service.R

internal class ServiceNotification(
    private val channelDescription: String,
    private val id: Int,
    private val activity: Class<*>?,
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
                notification = ServiceNotification("BSG is a national treasure", 615615, null)

            return notification
        }
    }
}