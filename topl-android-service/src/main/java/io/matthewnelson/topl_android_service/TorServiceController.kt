package io.matthewnelson.topl_android_service

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.matthewnelson.topl_android_settings.TorConfigFiles
import io.matthewnelson.topl_android_settings.TorSettings

class TorServiceController private constructor() {

    class Builder(
        private val context: Context,
        private val torSettings: TorSettings,
        private val geoipAssetPath: String,
        private val geoip6AssetPath: String
    ) {

        private lateinit var torConfigFiles: TorConfigFiles
        private var startServiceAsap = false
        private var stopTorOnTermination = true

        fun useCustomTorConfigFiles(torConfigFiles: TorConfigFiles): Builder {
            // TODO: Implement
            return this
        }

        fun startServiceAsSoonAsPossible(): Builder {
            // TODO: Implement
            return this
        }

        fun stopServiceWhenAppIsTerminated(): Builder {
            // TODO: Implement
            return this
        }

        fun showNotification(channelDescription: String): NotificationBuilder {
            // TODO: Implement
            return NotificationBuilder(this)
        }

        class NotificationBuilder(private val builder: Builder) {

            fun setNotificationImageTorOn(@DrawableRes drawableRes: Int): NotificationBuilder {
                // TODO: Implement
                return this
            }

            fun setNotificationImageTorOff(@DrawableRes drawableRes: Int): NotificationBuilder {
                // TODO: Implement
                return this
            }

            fun setNotificationImageDataTransfer(@DrawableRes drawableRes: Int): NotificationBuilder {
                // TODO: Implement
                return this
            }

            fun setNotificationImageErrors(@DrawableRes drawableRes: Int): NotificationBuilder {
                // TODO: Implement
                return this
            }

            fun setNotificationColor(@ColorRes colorRes: Int): NotificationBuilder {
                // TODO: Implement
                return this
            }

            fun enableRestartButton(): NotificationBuilder {
                // TODO: Implement
                return this
            }

            fun enableStopButton(): NotificationBuilder {
                // TODO: Implement
                return this
            }

            fun applyNotificationSettings(): Builder {
                // TODO: Implement
                return builder
            }

        }

        fun build() {
            // TODO: Implement
        }
    }

    companion object {

        /**
         * Starts the TorService
         *
         * @param [torSettings] If you wish to apply new settings before tor starts.
         * */
        fun startTorService(torSettings: TorSettings?) {
            // TODO: Implement
        }

        /**
         * Stops the TorService
         *
         * @param [torSettings] If you wish to apply new settings after tor stops.
         * */
        fun stopTorService(torSettings: TorSettings?) {
            // TODO: Implement
        }

        /**
         * Restarts the TorService
         *
         * @param [torSettings] If you wish to apply new settings between stop and start.
         * */
        fun restartTorService(torSettings: TorSettings?) {
            // TODO: Implement
        }
    }
}