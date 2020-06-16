package io.matthewnelson.topl_android_service

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.matthewnelson.topl_android_service.receiver.IntentExtra
import io.matthewnelson.topl_android_service.model.NotificationSettings
import io.matthewnelson.topl_android_service.receiver.TorServiceReceiver
import io.matthewnelson.topl_android_service.onionproxy.OnionProxyInstaller
import io.matthewnelson.topl_android_settings.TorConfigFiles
import io.matthewnelson.topl_android_settings.TorSettings

class TorServiceController private constructor() {

    class Builder(
        private val context: Context,
        private val buildConfigVersion: Int,
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
                NotificationSettings.initialize(notificationSettings)
                return builder
            }

        }

        fun build() {
            val torConfigFiles =
                if (::torConfig.isInitialized) {
                    torConfig
                } else {
                    TorConfigFiles.createConfig(context.applicationContext)
                }

            TorService.setOnionProxyContext(
                torConfigFiles,
                OnionProxyInstaller(
                    context.applicationContext,
                    torConfigFiles,
                    buildConfigVersion,
                    geoipAssetPath,
                    geoip6AssetPath
                ),
                torSettings
            )

            context.applicationContext.registerReceiver(
                TorServiceReceiver(), IntentFilter(TorServiceReceiver.INTENT_FILTER_ACTION)
            )

            contxt = context.applicationContext
        }
    }

    companion object {

        private lateinit var contxt: Context

        private fun sendBroadcast(extras: String) {
            val broadcastIntent = Intent(TorServiceReceiver.INTENT_FILTER_ACTION)
            broadcastIntent.putExtra(TorServiceReceiver.EXTRAS_KEY, extras)
            contxt.sendBroadcast(broadcastIntent)
        }

        /**
         * Starts the TorService. Does nothing if called before the builder has gone off.
         * */
        fun startTor() {
            if (::contxt.isInitialized)
                sendBroadcast(IntentExtra.ACTION_START)
        }

        /**
         * Stops the TorService. Does nothing if called before the builder has gone off.
         * */
        fun stopTor() {
            if (::contxt.isInitialized)
                sendBroadcast(IntentExtra.ACTION_STOP)
        }

        /**
         * Restarts the TorService. Does nothing if called before the builder has gone off.
         * */
        fun restartTor() {
            if (::contxt.isInitialized)
                sendBroadcast(IntentExtra.ACTION_RESTART)
        }

        /**
         * Renews the identity. Does nothing if called before the builder has gone off.
         * */
        fun newIdentity() {
            if (::contxt.isInitialized)
                sendBroadcast(IntentExtra.ACTION_RENEW)
        }
    }
}