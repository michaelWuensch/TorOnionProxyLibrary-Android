package io.matthewnelson.topl_service

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.matthewnelson.topl_service.model.ServiceNotification
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.service.ActionConsts.ServiceAction
import androidx.core.app.NotificationCompat.NotificationVisibility
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings

class TorServiceController private constructor() {

    /**
     * The [TorServiceController.Builder] is where you get to customize how [TorService] works
     * for your application. Call it in `Application.onCreate` and follow along.
     *
     * A note about the [TorSettings] you send this. Those are the default settings which
     * [TorService] will fall back on if [io.matthewnelson.topl_service_prefs.TorServicePrefs]
     * has nothing in it for that particular [io.matthewnelson.topl_service_prefs.PrefsKeys].
     * The settings get written to the `torrc` file everytime Tor is started (I plan to make this
     * less sledgehammer-ish in the future).
     *
     * To update settings while your application is running you need only to instantiate
     * [io.matthewnelson.topl_service_prefs.TorServicePrefs] and save the data using the
     * appropriately annotated method and [io.matthewnelson.topl_service_prefs.PrefsKeys], then
     * restart Tor (for now... ;-D).
     *
     * I plan to implement a
     * [android.content.SharedPreferences.OnSharedPreferenceChangeListener] that will do this
     * immediately for the settings that don't require a restart, but a stable release comes first).
     *
     * You can see how the [TorSettings] sent here are used in [TorService] by looking at
     * [io.matthewnelson.topl_service_settings.TorServiceSettings] and [TorService.onCreate].
     *
     * @param [context] Context
     * @param [buildConfigVersion] send [BuildConfig.VERSION_CODE]. Mitigates copying of geoip
     *   files to app updates only.
     * @param [torSettings] [TorSettings] used to create your torrc file on startup.
     * @param [geoipAssetPath] The path to where you have your geoip file located (ex: in
     *   assets/common directory, send this variable "common/geoip").
     * @param [geoip6AssetPath] The path to where you have your geoip6 file located (ex: in
     *   assets/common directory, send this variable "common/geoip6").
     *
     * @sample [io.matthewnelson.sampleapp.App.setupTorServices]
     * */
    class Builder(
        private val context: Context,
        private val buildConfigVersion: Int,
        private val torSettings: TorSettings,
        private val geoipAssetPath: String,
        private val geoip6AssetPath: String
    ) {

        private lateinit var torConfigFiles: TorConfigFiles

        /**
         * If you wish to customize the file structure of how Tor is installed in your app,
         * you can do so by instantiating your own [TorConfigFiles] and customizing it via
         * the [TorConfigFiles.Builder], or overridden method [TorConfigFiles.createConfig].
         *
         * By default, [TorService] will call [TorConfigFiles.createConfig] using your
         * [Context.getApplicationContext] to set up a standard directory hierarchy for Tor
         * to operate with.
         *
         * See [Builder] for code samples.
         *
         * @return [Builder]
         * @see [Builder.build]
         * */
        fun useCustomTorConfigFiles(torConfigFiles: TorConfigFiles): Builder {
            this.torConfigFiles = torConfigFiles
            return this
        }

        /**
         * Customize the service notification to your application.
         *
         * See [Builder] for code samples.
         *
         * @param [channelName] Your notification channel's name.
         * @param [channelID] Your notification channel's ID.
         * @param [channelDescription] Your notification channel's description.
         * @param [notificationID] Your foreground notification's ID
         * @return [NotificationBuilder]
         * */
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
            ) { "channelName, channelID, & channelDescription must not be empty." }

            return NotificationBuilder(
                this,
                channelName,
                channelID,
                channelDescription,
                notificationID.toInt()
            )
        }

        /**
         * Where you get to customize how your foreground notification will look/function.
         *
         * See [Builder] for code samples.
         * */
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

            /**
             * For when your user taps the TorService notification.
             *
             * See [Builder] for code samples.
             *
             * @param [clazz] The Activity to be opened when tapped.
             * @param [intentExtrasKey]? The key for if you with to add extras in the PendingIntent.
             * @param [intentExtras]? The extras that will be sent in the PendingIntent.
             * @param [intentRequestCode]? The request code - Defaults to 0 if not set.
             * */
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

            /**
             * Defaults to Orbot/TorBrowser's icon.
             *
             * The small icon you wish to display when Tor's State is
             * [io.matthewnelson.topl_core_base.TorStates.TorState.ON].
             *
             * See [Builder] for code samples.
             *
             * @param [drawableRes] Drawable resource id.
             * @return [NotificationBuilder]
             * */
            fun setImageTorOn(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageOn = drawableRes
                return this
            }

            /**
             * Defaults to Orbot/TorBrowser's icon.
             *
             * The small icon you wish to display when Tor's State is
             * [io.matthewnelson.topl_core_base.TorStates.TorState.OFF].
             *
             * See [Builder] for code samples.
             *
             * @param [drawableRes] Drawable resource id.
             * @return [NotificationBuilder]
             * */
            fun setImageTorOff(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageOff = drawableRes
                return this
            }

            /**
             * Defaults to Orbot/TorBrowser's icon.
             *
             * The small icon you wish to display when bandwidth is being used.
             *
             * See [Builder] for code samples.
             *
             * @param [drawableRes] Drawable resource id.
             * @return [NotificationBuilder]
             * */
            fun setImageTorDataTransfer(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageData = drawableRes
                return this
            }

            /**
             * Defaults to Orbot/TorBrowser's icon.
             *
             * The small icon you wish to display when Tor is having problems.
             *
             * See [Builder] for code samples.
             *
             * @param [drawableRes] Drawable resource id.
             * @return [NotificationBuilder]
             * */
            fun setImageTorErrors(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageError = drawableRes
                return this
            }

            /**
             * Defaults to white
             *
             * The color you wish to display when Tor's State is
             * [io.matthewnelson.topl_core_base.TorStates.TorState.ON].
             *
             * See [Builder] for code samples.
             *
             * @param [colorRes] Color resource id.
             * @return [NotificationBuilder]
             * */
            fun setColorWhenTorOn(@ColorRes colorRes: Int): NotificationBuilder {
                serviceNotification.colorWhenOn = colorRes
                return this
            }

            /**
             * Defaults to NotificationVisibility.VISIBILITY_SECRET
             *
             * The visibility of your notification on the user's lock screen.
             *
             * See [Builder] for code samples.
             *
             * @return [NotificationBuilder]
             * */
            fun setVisibility(@NotificationVisibility visibility: Int): NotificationBuilder {
                if (visibility in -1..1)
                    serviceNotification.visibility = visibility
                return this
            }

            /**
             * Disabled by Default
             *
             * Enable on the notification the ability to *restart* Tor.
             *
             * See [Builder] for code samples.
             *
             * @return [NotificationBuilder]
             * */
            fun enableTorRestartButton(): NotificationBuilder {
                serviceNotification.enableRestartButton = true
                return this
            }

            /**
             * Disabled by Default
             *
             * Enable on the notification the ability to *stop* Tor.
             *
             * See [Builder] for code samples.
             *
             * @return [NotificationBuilder]
             * */
            fun enableTorStopButton(): NotificationBuilder {
                serviceNotification.enableStopButton = true
                return this
            }

            /**
             * Initialize settings.
             *
             * See [Builder] for code samples.
             *
             * @return [Builder]
             * */
            fun applyNotificationSettings(): Builder {
                ServiceNotification.initialize(serviceNotification)
                return builder
            }

        }

        /**
         * Initializes [TorService] setup and enables the ability to call methods in the
         * [Companion] object.
         *
         * See [Builder] for code samples.
         * */
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

    /**
     * Where everything needed to interact with [TorService] resides.
     * */
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