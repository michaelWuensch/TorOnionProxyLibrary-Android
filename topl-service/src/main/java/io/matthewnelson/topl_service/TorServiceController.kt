/*
* Copyright (C) 2020 Matthew Nelson
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
* */
package io.matthewnelson.topl_service

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.TorService
import androidx.core.app.NotificationCompat.NotificationVisibility
import io.matthewnelson.topl_core_base.EventBroadcaster
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.service.ServiceActionProcessor
import io.matthewnelson.topl_service.util.ServiceConsts

class TorServiceController private constructor(): ServiceConsts() {

    /**
     * The [TorServiceController.Builder] is where you get to customize how [TorService] works
     * for your application. Call it in `Application.onCreate` and follow along.
     *
     * A note about the [TorSettings] you send this. Those are the default settings which
     * [TorService] will fall back on if [io.matthewnelson.topl_service.prefs.TorServicePrefs]
     * has nothing in it for that particular [ServiceConsts].PrefKey.
     *
     * The settings get written to the `torrc` file every time Tor is started (I plan to make
     * this less sledgehammer-ish in the future).
     *
     * To update settings while your application is running you need only to instantiate
     * [io.matthewnelson.topl_service.prefs.TorServicePrefs] and save the data using the
     * appropriately annotated method and [ServiceConsts].PrefKey, then
     * restart Tor (for now... ;-D).
     *
     * I plan to implement a
     * [android.content.SharedPreferences.OnSharedPreferenceChangeListener] that will do this
     * immediately for the settings that don't require a restart, but a stable release comes first).
     *
     * You can see how the [TorSettings] sent here are used in [TorService] by looking at
     * [io.matthewnelson.topl_service.onionproxy.ServiceTorSettings] and
     * [TorService.initTOPLCore].
     *
     * @param [application] [Application], for obtaining context.
     * @param [buildConfigVersionCode] send [BuildConfig.VERSION_CODE]. Mitigates copying of geoip
     *   files to app updates only.
     * @param [torSettings] [TorSettings] used to create your torrc file on start of Tor.
     * @param [geoipAssetPath] The path to where you have your geoip file located (ex: in
     *   assets/common directory, send this variable "common/geoip").
     * @param [geoip6AssetPath] The path to where you have your geoip6 file located (ex: in
     *   assets/common directory, send this variable "common/geoip6").
     *
     * @sample [io.matthewnelson.sampleapp.samplecode.SampleCode.setupTorServices]
     * */
    class Builder(
        private val application: Application,
        private val buildConfigVersionCode: Int,
        torSettings: TorSettings,
        private val geoipAssetPath: String,
        private val geoip6AssetPath: String
    ) {

        init {
            Companion.torSettings = torSettings
        }

        private lateinit var torConfigFiles: TorConfigFiles

        // On published releases of this Library, this value will **always** be `false`.
        private var buildConfigDebug = BuildConfig.DEBUG

        /**
         * This makes it such that on your Application's **Debug** builds, the `topl-core` and
         * `topl-service` modules will provide you with Logcat messages (when
         * [TorSettings.hasDebugLogs] is enabled).
         *
         * For your **Release** builds no Logcat messaging will be provided, but you
         * will still get the same messages sent to your [EventBroadcaster] if you set it
         * via [Builder.setEventBroadcaster].
         *
         * TODO: Provide a link to gh-pages that discusses logging and how it work, it's pretty
         *  complex with everything that is going on.
         *
         * @param [buildConfigDebug] Send [BuildConfig.DEBUG]
         * @see [io.matthewnelson.topl_core.broadcaster.BroadcastLogger]
         * */
        fun setBuildConfigDebug(buildConfigDebug: Boolean): Builder {
            this.buildConfigDebug = buildConfigDebug
            return this
        }

        /**
         * Get broadcasts piped to your Application to do with them what you desire. What
         * you send this will live at [Companion.appEventBroadcaster] for the remainder of
         * your application's lifecycle to refer to elsewhere in your App.
         *
         * NOTE: You will, ofc, have to cast [Companion.appEventBroadcaster] as whatever your
         * class actually is.
         * */
        fun setEventBroadcaster(eventBroadcaster: EventBroadcaster): Builder {
            appEventBroadcaster = eventBroadcaster
            return this
        }

        /**
         * If you wish to customize the file structure of how Tor is installed in your app,
         * you can do so by instantiating your own [TorConfigFiles] and customizing it via
         * the [TorConfigFiles.Builder], or overridden method [TorConfigFiles.createConfig].
         *
         * By default, [TorService] will call [TorConfigFiles.createConfig] using your
         * [Context.getApplicationContext] to set up a standard directory hierarchy for Tor
         * to operate with.
         *
         * @return [Builder]
         * @sample [io.matthewnelson.sampleapp.samplecode.SampleCode.customTorConfigFilesSetup]
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
         * @param [channelName] Your notification channel's name (cannot be empty).
         * @param [channelID] Your notification channel's ID (cannot be empty).
         * @param [channelDescription] Your notification channel's description (cannot be empty).
         * @param [notificationID] Your foreground notification's ID.
         * @return [NotificationBuilder] To obtain methods specific to notification customization.
         * @throws [IllegalArgumentException] If String fields are empty.
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
             *
             * TODO: Check if this conflicts with multi-activity apps such that if the user is
             *  on a certain activity, and clicks the notification it will bring them to the
             *  declared activity instead of back to the activity they were on.
             *  + Include an optional Bundle? to be set for creating the pending intent.
             *  + Think about overriding and providing another option to rotate the ContentIntent
             *  to open up/resume current activity?
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
             * [io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState.ENABLED].
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
             * [io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState.DISABLED].
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
             * [io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState.ENABLED].
             *
             * See [Builder] for code samples.
             *
             * @param [colorRes] Color resource id.
             * @return [NotificationBuilder]
             * */
            fun setCustomColor(@ColorRes colorRes: Int): NotificationBuilder {
                serviceNotification.colorWhenConnected = colorRes
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
             * @param [enable] Boolean, automatically set to true but provides cleaner option
             *   for implementor to query SharedPreferences for user's settings (if desired).
             * @return [NotificationBuilder]
             * */
            fun enableTorRestartButton(enable: Boolean = true): NotificationBuilder {
                serviceNotification.enableRestartButton = enable
                return this
            }

            /**
             * Disabled by Default
             *
             * Enable on the notification the ability to *stop* Tor.
             *
             * See [Builder] for code samples.
             *
             * @param [enable] Boolean, automatically set to true but provides cleaner option
             *   for implementor to query SharedPreferences for user's settings (if desired).
             * @return [NotificationBuilder]
             * */
            fun enableTorStopButton(enable: Boolean = true): NotificationBuilder {
                serviceNotification.enableStopButton = enable
                return this
            }

            /**
             * Enabled by Default.
             *
             * Setting it to false will only show a notification when your task is removed
             * in order to properly shutdown the Service.
             * */
            fun showNotification(show: Boolean = false): NotificationBuilder {
                serviceNotification.showNotification = show
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

            Companion.torConfigFiles =
                if (::torConfigFiles.isInitialized)
                    torConfigFiles
                else
                    TorConfigFiles.createConfig(application.applicationContext)

            TorService.initialize(
                buildConfigVersionCode,
                buildConfigDebug,
                geoipAssetPath,
                geoip6AssetPath
            )

            ServiceNotification.get().setupNotificationChannel(application.applicationContext)

            appContext = application.applicationContext
        }
    }

    /**
     * Where everything needed to interact with [TorService] resides.
     * */
    companion object {

        private lateinit var appContext: Context
        var appEventBroadcaster: EventBroadcaster? = null
            private set
        private lateinit var torConfigFiles: TorConfigFiles
        private lateinit var torSettings: TorSettings

        /**
         * Get the [TorConfigFiles] that have been set after calling [Builder.build]
         *
         * @return Instance of [TorConfigFiles] that are being used throughout TOPL-Android
         * @throws [UninitializedPropertyAccessException] if called before [Builder.build]
         * */
        @Throws(UninitializedPropertyAccessException::class)
        fun getTorConfigFiles(): TorConfigFiles =
            if (::torConfigFiles.isInitialized)
                torConfigFiles
            else
                throw UninitializedPropertyAccessException("TorConfigFiles hasn't been initialized")

        /**
         * Get the [TorSettings] that have been set after calling [Builder.build]. These are
         * the [TorSettings] you initialized [TorServiceController.Builder] with.
         *
         * @return Instance of [TorSettings] that are being used throughout TOPL-Android
         * @throws [UninitializedPropertyAccessException] if called before [Builder.build]
         * */
        @Throws(UninitializedPropertyAccessException::class)
        fun getTorSettings(): TorSettings =
            if (::torSettings.isInitialized)
                torSettings
            else
                throw UninitializedPropertyAccessException("TorSettings hasn't been initialized")

        /**
         * Adding a StringExtra to the Intent by passing a value for [extrasString] will
         * always use the [action] as the key for retrieving it.
         *
         * @param [action] A [ServiceConsts.ServiceAction] to be processed by [TorService]
         * @param [extrasString] To be included in the intent.
         * */
        private fun sendAction(@ServiceAction action: String, extrasString: String? = null) {
            if (!::appContext.isInitialized) return
            val torServiceIntent = Intent(appContext, TorService::class.java)
            torServiceIntent.action = action

            if (extrasString != null) {
                torServiceIntent.putExtra(action, extrasString)
            }
            appContext.startService(torServiceIntent)
        }

        /**
         * Starts [TorService]. Does nothing if called prior to:
         *
         *  - Initializing [TorServiceController.Builder] by calling [Builder.build]
         *
         * You can call this as much as you want. If Tor is already on, it will do nothing.
         * */
        fun startTor() {
            if (!ServiceActionProcessor.isAcceptingActions)
                sendAction(ServiceAction.START)
        }

        /**
         * Stops [TorService]. Does nothing if called prior to:
         *
         *  - Initializing [TorServiceController.Builder] by calling [Builder.build]
         *  - Calling [startTor]
         * */
        fun stopTor() {
            if (ServiceActionProcessor.isAcceptingActions)
                sendAction(ServiceAction.STOP)
        }

        /**
         * Restarts Tor. Does nothing if called prior to:
         *
         *  - Initializing [TorServiceController.Builder] by calling [Builder.build]
         *  - Calling [startTor]
         * */
        fun restartTor() {
            if (ServiceActionProcessor.isAcceptingActions)
                sendAction(ServiceAction.RESTART_TOR)
        }

        /**
         * Changes identities. Does nothing if called prior to:
         *
         *  - Initializing [TorServiceController.Builder] by calling [Builder.build]
         *  - Calling [startTor]
         * */
        fun newIdentity() {
            if (ServiceActionProcessor.isAcceptingActions)
                sendAction(ServiceAction.NEW_ID)
        }
    }
}