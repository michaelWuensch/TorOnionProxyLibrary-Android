package io.matthewnelson.topl_service

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_service.service.ActionConsts.ServiceAction
import androidx.core.app.NotificationCompat.NotificationVisibility
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings
import net.freehaven.tor.control.EventListener

class TorServiceController private constructor() {

    /**
     * The [TorServiceController.Builder] is where you get to customize how [TorService] works
     * for your application. Call it in `Application.onCreate` and follow along.
     *
     * A note about the [TorSettings] you send this. Those are the default settings which
     * [TorService] will fall back on if [io.matthewnelson.topl_service.util.TorServicePrefs]
     * has nothing in it for that particular [io.matthewnelson.topl_service.util.PrefsKeys].
     * The settings get written to the `torrc` file every time Tor is started (I plan to make
     * this less sledgehammer-ish in the future).
     *
     * To update settings while your application is running you need only to instantiate
     * [io.matthewnelson.topl_service.util.TorServicePrefs] and save the data using the
     * appropriately annotated method and [io.matthewnelson.topl_service.util.PrefsKeys], then
     * restart Tor (for now... ;-D).
     *
     * I plan to implement a
     * [android.content.SharedPreferences.OnSharedPreferenceChangeListener] that will do this
     * immediately for the settings that don't require a restart, but a stable release comes first).
     *
     * You can see how the [TorSettings] sent here are used in [TorService] by looking at
     * [io.matthewnelson.topl_service.service.TorServiceSettings] and [TorService.onCreate].
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
        private lateinit var additionalEventListener: EventListener

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
         * This will require adding the `jtorctl` library:
         *
         *  - Add `implementation "info.guardianproject:jtorctl:0.4"` to your dependencies block
         *
         * Extend the [EventListener] class and implement the overridden methods. It will be
         * registered when Tor is started up so messages from Tor will be piped to it.
         *
         * This is limited to adding only 1 [EventListener], so calling this builder method
         * multiple times will only register the last one when Tor starts.
         *
         * See [io.matthewnelson.topl_service.onionproxy.OnionProxyEventListener] for an example
         * and what `CONTROL_COMMAND_EVENTS` will be registered to be listened for.
         *
         * TODO: Provide ability to add more CONTROL_COMMAND_EVENTS if desired by library user.
         *
         * @param [jtorctlEventListener] [EventListener]
         * */
        fun addTorEventListener(jtorctlEventListener: EventListener): Builder {
            this.additionalEventListener = jtorctlEventListener
            return this
        }

        /**
         * Customize the service notification to your application.
         *
         * See [Builder] for code samples.
         *
         * @param [channelName] Your notification channel's name (cannot be empty).
         * @param [channelID] Your notification channel's ID (cannot be empty).
         * @param [channelDescription] Your notification channel's description (cannot be empty).
         * @param [notificationID] Your foreground notification's ID.
         * @return [NotificationBuilder] to obtain methods specific to notification customization.
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
         * Calling [customizeNotification] will return this class to you which provides methods
         * specific to customization of notifications. Call [applyNotificationSettings] when done
         * to return to [Builder] to continue with it's methods for customization.
         *
         * See [Builder] for code samples.
         *
         * @param [builder] [Builder] To return to it when calling [applyNotificationSettings]
         * @param [channelName] Your notification channel's name.
         * @param [channelID] Your notification channel's ID.
         * @param [channelDescription] Your notification channel's description.
         * @param [notificationID] Your foreground notification's ID.
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
             * Defaults to Orbot/TorBrowser's icon [R.drawable.tor_stat_network_enabled].
             *
             * The small icon you wish to display when Tor's network state is
             * [io.matthewnelson.topl_core_base.TorStates.TorNetworkState.ENABLED].
             *
             * See [Builder] for code samples.
             *
             * @param [drawableRes] Drawable resource id.
             * @return [NotificationBuilder]
             * */
            fun setImageTorNetworkingEnabled(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageNetworkEnabled = drawableRes
                return this
            }

            /**
             * Defaults to Orbot/TorBrowser's icon [R.drawable.tor_stat_network_disabled].
             *
             * The small icon you wish to display when Tor's network state is
             * [io.matthewnelson.topl_core_base.TorStates.TorNetworkState.DISABLED].
             *
             * See [Builder] for code samples.
             *
             * @param [drawableRes] Drawable resource id.
             * @return [NotificationBuilder]
             * */
            fun setImageTorNetworkingDisabled(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageNetworkDisabled = drawableRes
                return this
            }

            /**
             * Defaults to Orbot/TorBrowser's icon [R.drawable.tor_stat_network_dataxfer].
             *
             * The small icon you wish to display when bandwidth is being used.
             *
             * See [Builder] for code samples.
             *
             * @param [drawableRes] Drawable resource id.
             * @return [NotificationBuilder]
             * */
            fun setImageTorDataTransfer(@DrawableRes drawableRes: Int): NotificationBuilder {
                serviceNotification.imageDataTransfer = drawableRes
                return this
            }

            /**
             * Defaults to Orbot/TorBrowser's icon [R.drawable.tor_stat_notifyerr].
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
             * Defaults to [R.color.tor_service_white]
             *
             * The color you wish to display when Tor's network state is
             * [io.matthewnelson.topl_core_base.TorStates.TorNetworkState.ENABLED].
             *
             * See [Builder] for code samples.
             *
             * @param [colorRes] Color resource id.
             * @param [colorizeBackground] true = background is colorized, false = icon is colorized
             * @return [NotificationBuilder]
             * */
            fun setColorWhenTorOn(@ColorRes colorRes: Int, colorizeBackground: Boolean): NotificationBuilder {
                serviceNotification.colorWhenConnected = colorRes
                serviceNotification.colorizeBackground = colorizeBackground
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
            val eventListener =
                if (::additionalEventListener.isInitialized)
                    this.additionalEventListener
                else
                    null

            TorService.initialize(
                torConfigFiles,
                torSettings,
                eventListener,
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

        private fun sendAction(@ServiceAction action: String) {
            if (!::appContext.isInitialized) return
            val torServiceIntent = Intent(appContext.applicationContext, TorService::class.java)
            torServiceIntent.action = action
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                appContext.startForegroundService(torServiceIntent)
            else
                appContext.startService(torServiceIntent)
        }

        /**
         * Starts [TorService]. Does nothing if called before:
         *
         *  - Have not initialized [TorServiceController.Builder] by calling [Builder.build]
         *  - Have not called [startTor].
         * */
        fun startTor() =
            sendAction(ServiceAction.ACTION_START)

        /**
         * Stops [TorService]. Does nothing if called before:
         *
         *  - Have not initialized [TorServiceController.Builder] by calling [Builder.build]
         *  - Have not called [startTor].
         * */
        fun stopTor() {
            if (TorService.isServiceStarted)
                sendAction(ServiceAction.ACTION_STOP)
        }

        /**
         * Restarts Tor. Does nothing if called before:
         *
         *  - Have not initialized [TorServiceController.Builder] by calling [Builder.build]
         *  - Have not called [startTor].
         * */
        fun restartTor() {
            if (TorService.isServiceStarted)
                sendAction(ServiceAction.ACTION_RESTART)
        }

        /**
         * Changes identities. Does nothing if called before:
         *
         *  - Have not initialized [TorServiceController.Builder] by calling [Builder.build]
         *  - Have not called [startTor].
         * */
        fun newIdentity() {
            if (TorService.isServiceStarted)
                sendAction(ServiceAction.ACTION_NEW_ID)
        }
    }
}