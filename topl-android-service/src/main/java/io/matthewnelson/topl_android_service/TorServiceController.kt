package io.matthewnelson.topl_android_service

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.matthewnelson.topl_android_service.model.NotificationSettings
import io.matthewnelson.topl_android_settings.TorConfigFiles
import io.matthewnelson.topl_android_settings.TorSettings

class TorServiceController private constructor() {

    class Builder(
        private val context: Context,
        private val torSettings: TorSettings,
        private val geoipAssetPath: String,
        private val geoip6AssetPath: String
    ) {

        private lateinit var torConfig: TorConfigFiles
        private lateinit var startServiceAsap: String
        private lateinit var stopServiceOnTermination: String

        fun useCustomTorConfigFiles(torConfigFiles: TorConfigFiles): Builder {
            torConfig = torConfigFiles
            return this
        }

        fun startServiceAsSoonAsPossible(): Builder {
            startServiceAsap = ""
            return this
        }

        fun stopServiceWhenAppIsTerminated(): Builder {
            stopServiceOnTermination = ""
            return this
        }

        fun showNotification(channelDescription: String, notificationID: Short): NotificationBuilder {
            return NotificationBuilder(this, channelDescription, notificationID.toInt())
        }

        class NotificationBuilder(
            private val builder: Builder,
            channelDescription: String,
            notificationID: Int
        ) {

            private val notificationSettings = NotificationSettings(channelDescription, notificationID)

            fun setNotificationImageTorOn(@DrawableRes drawableRes: Int): NotificationBuilder {
                notificationSettings.imageOn = drawableRes
                return this
            }

            fun setNotificationImageTorOff(@DrawableRes drawableRes: Int): NotificationBuilder {
                notificationSettings.imageOff = drawableRes
                return this
            }

            fun setNotificationImageDataTransfer(@DrawableRes drawableRes: Int): NotificationBuilder {
                notificationSettings.imageData = drawableRes
                return this
            }

            fun setNotificationImageErrors(@DrawableRes drawableRes: Int): NotificationBuilder {
                notificationSettings.imageError = drawableRes
                return this
            }

            fun setNotificationColor(@ColorRes colorRes: Int): NotificationBuilder {
                notificationSettings.colorRes = colorRes
                return this
            }

            fun enableRestartButton(): NotificationBuilder {
                notificationSettings.enableRestartButton = true
                return this
            }

            fun enableStopButton(): NotificationBuilder {
                notificationSettings.enableStopButton = true
                return this
            }

            fun applyNotificationSettings(): Builder {
                notificationSettings.show = true
                TorServiceManager.setNotificationSettings(notificationSettings)
                return builder
            }

        }

        fun build() {
            if (!TorServiceManager.isNotificationSettingsInitialized()) {
                TorServiceManager.setNotificationSettings(
                    NotificationSettings(
                        "BSG is a national treasure. Separate Money and State. #BitcoinFixesThis",
                        615615
                    )
                )
            }

            TorServiceManager.initialize(
                context,
                torSettings,
                geoipAssetPath,
                geoip6AssetPath,
                if (::torConfig.isInitialized) torConfig else null,
                ::startServiceAsap.isInitialized,
                ::stopServiceOnTermination.isInitialized
            )
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