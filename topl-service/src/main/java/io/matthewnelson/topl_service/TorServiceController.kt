package io.matthewnelson.topl_service

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.matthewnelson.topl_service.model.ServiceNotification
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.service.ServiceActions.ServiceAction
import androidx.core.app.NotificationCompat.NotificationVisibility
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

        private lateinit var torConfigFiles: TorConfigFiles

        fun useCustomTorConfigFiles(torConfigFiles: TorConfigFiles): Builder {
            this.torConfigFiles = torConfigFiles
            return this
        }

        @Throws(IllegalArgumentException::class)
        fun customizeNotification(
            channelName: String,
            channelID: String,
            channelDescription: String,
            notificationID: Short
        ): NotificationBuilder {
            require(
                channelName.isNotEmpty() &&
                channelID.isNotEmpty() &&
                channelDescription.isNotEmpty()
            ) { "channelName & channelID must not be empty." }

            return NotificationBuilder(
                this,
                channelName,
                channelID,
                channelDescription,
                notificationID.toInt()
            )
        }

        class NotificationBuilder(
            private val builder: Builder,
            channelName: String,
            channelID: String,
            channelDescription: String,
            notificationID: Int
        ) {

            private val serviceNotification =
                ServiceNotification(
                    channelName,
                    channelID,
                    channelDescription,
                    notificationID
                )

            fun setActivityToBeOpenedOnTap(
                clazz: Class<*>,
                intentExtrasKey: String?,
                intentExtras: String?,
                intentRequestCode: Int?
            ): NotificationBuilder {
                serviceNotification.activityWhenTapped = clazz
                serviceNotification.activityIntentKey = intentExtrasKey
                serviceNotification.activityIntentExtras = intentExtras
                intentRequestCode?.let { serviceNotification.activityIntentRequestCode = it }
                return this
            }

            fun setImageTorOn(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageOn = drawableRes
                return this
            }

            fun setImageTorOff(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageOff = drawableRes
                return this
            }

            fun setImageTorDataTransfer(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageData = drawableRes
                return this
            }

            fun setImageTorErrors(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageError = drawableRes
                return this
            }

            fun setColorWhenTorOn(@ColorRes colorRes: Int): NotificationBuilder {
                serviceNotification.colorRes = colorRes
                return this
            }

            fun setVisibility(@NotificationVisibility visibility: Int): NotificationBuilder {
                if (visibility in -1..1)
                    serviceNotification.visibility = visibility
                return this
            }

            fun enableTorRestartButton(): NotificationBuilder {
                serviceNotification.enableRestartButton = true
                return this
            }

            fun enableTorStopButton(): NotificationBuilder {
                serviceNotification.enableStopButton = true
                return this
            }

            fun applySettings(): Builder {
                ServiceNotification.initialize(serviceNotification)
                return builder
            }

        }

        fun build() {
            val torConfigFiles =
                if (::torConfigFiles.isInitialized) {
                    this.torConfigFiles
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

            ServiceNotification.get().setupNotificationChannel(context.applicationContext)

            appContext = context.applicationContext
        }
    }

    companion object {

        private lateinit var appContext: Context

        private fun broadcastAction(@ServiceAction actions: String) {
            if (!::appContext.isInitialized) return
            val broadcastIntent = Intent(TorService.FILTER)
            broadcastIntent.putExtra(TorService.ACTION_EXTRAS_KEY, actions)
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(broadcastIntent)
        }

        /**
         * Starts the TorService. Does nothing if called before the builder has gone off.
         * */
        fun startTor() {
            if (!::appContext.isInitialized) return

            val torServiceIntent = Intent(appContext.applicationContext, TorService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                appContext.startForegroundService(torServiceIntent)
            else
                appContext.startService(torServiceIntent)
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