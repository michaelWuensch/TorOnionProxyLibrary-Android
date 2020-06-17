package io.matthewnelson.topl_android_service

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.matthewnelson.topl_android_service.util.ServiceAction
import io.matthewnelson.topl_android_service.model.ServiceNotification
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

        fun useCustomTorConfigFiles(torConfigFiles: TorConfigFiles): Builder {
            torConfig = torConfigFiles
            return this
        }

        fun customizeNotification(
            channelDescription: String,
            notificationID: Short,
            activityToOpenWhenClicked: Class<*>?
        ): NotificationBuilder {
            return NotificationBuilder(
                this,
                channelDescription,
                notificationID.toInt(),
                activityToOpenWhenClicked
            )
        }

        class NotificationBuilder(
            private val builder: Builder,
            channelDescription: String,
            notificationID: Int,
            activityToOpenWhenClicked: Class<*>?
        ) {

            private val serviceNotification =
                ServiceNotification(channelDescription, notificationID, activityToOpenWhenClicked)

            fun setNotificationImageTorOn(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageOn = drawableRes
                return this
            }

            fun setNotificationImageTorOff(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageOff = drawableRes
                return this
            }

            fun setNotificationImageDataTransfer(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageData = drawableRes
                return this
            }

            fun setNotificationImageErrors(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageError = drawableRes
                return this
            }

            fun setNotificationColor(@ColorRes colorRes: Int): NotificationBuilder {
                serviceNotification.colorRes = colorRes
                return this
            }

            fun enableRestartButton(): NotificationBuilder {
                serviceNotification.enableRestartButton = true
                return this
            }

            fun enableStopButton(): NotificationBuilder {
                serviceNotification.enableStopButton = true
                return this
            }

            fun applyNotificationSettings(): Builder {
                ServiceNotification.initialize(serviceNotification)
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

            TorService.initialize(
                torConfigFiles,
                torSettings,
                buildConfigVersion,
                geoipAssetPath,
                geoip6AssetPath
            )

            contxt = context.applicationContext
        }
    }

    companion object {

        private lateinit var contxt: Context

        private fun broadcastAction(action: @ServiceAction.Action String) {
            if (!::contxt.isInitialized) return
            val broadcastIntent = Intent(TorService.FILTER)
            broadcastIntent.putExtra(TorService.ACTION_EXTRAS_KEY, action)
            LocalBroadcastManager.getInstance(contxt).sendBroadcast(broadcastIntent)
        }

        /**
         * Starts the TorService. Does nothing if called before the builder has gone off.
         * */
        fun startTor() {
            if (!::contxt.isInitialized) return

            val torServiceIntent = Intent(contxt.applicationContext, TorService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                contxt.startForegroundService(torServiceIntent)
            } else {
                contxt.startService(torServiceIntent)
            }
        }

        /**
         * Stops the TorService. Does nothing if called before the builder has gone off.
         * */
        fun stopTor() =
            broadcastAction(ServiceAction.ACTION_STOP)

        /**
         * Restarts the TorService. Does nothing if called before the builder has gone off.
         * */
        fun restartTor() =
            broadcastAction(ServiceAction.ACTION_RESTART)

        /**
         * Renews the identity. Does nothing if called before the builder has gone off.
         * */
        fun newIdentity() =
            broadcastAction(ServiceAction.ACTION_NEW_ID)
    }
}