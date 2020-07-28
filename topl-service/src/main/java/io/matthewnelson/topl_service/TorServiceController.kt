/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash `74407114cbfa8ea6f2ac51417dda8be98d8aba86`. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify it
*     under the terms of the GNU General Public License as published by the
*     Free Software Foundation, either version 3 of the License, or (at your
*     option) any later version.
*
*     This program is distributed in the hope that it will be useful, but
*     WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*     or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
*     for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program. If not, see <https://www.gnu.org/licenses/>.
*
* `===========================================================================`
* `+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++`
* `===========================================================================`
*
* The following exception is an additional permission under section 7 of the
* GNU General Public License, version 3 (“GPLv3”).
*
*     "The Interfaces" is henceforth defined as Application Programming Interfaces
*     that are publicly available classes/functions/etc (ie: do not contain the
*     visibility modifiers `internal`, `private`, `protected`, or are within
*     classes/functions/etc that contain the aforementioned visibility modifiers)
*     to TorOnionProxyLibrary-Android users that are needed to implement
*     TorOnionProxyLibrary-Android and reside in ONLY the following modules:
*
*      - topl-core-base
*      - topl-service
*
*     The following are excluded from "The Interfaces":
*
*       - All other code
*
*     Linking TorOnionProxyLibrary-Android statically or dynamically with other
*     modules is making a combined work based on TorOnionProxyLibrary-Android.
*     Thus, the terms and conditions of the GNU General Public License cover the
*     whole combination.
*
*     As a special exception, the copyright holder of TorOnionProxyLibrary-Android
*     gives you permission to combine TorOnionProxyLibrary-Android program with free
*     software programs or libraries that are released under the GNU LGPL and with
*     independent modules that communicate with TorOnionProxyLibrary-Android solely
*     through "The Interfaces". You may copy and distribute such a system following
*     the terms of the GNU GPL for TorOnionProxyLibrary-Android and the licenses of
*     the other code concerned, provided that you include the source code of that
*     other code when and as the GNU GPL requires distribution of source code and
*     provided that you do not modify "The Interfaces".
*
*     Note that people who make modified versions of TorOnionProxyLibrary-Android
*     are not obligated to grant this special exception for their modified versions;
*     it is their choice whether to do so. The GNU General Public License gives
*     permission to release a modified version without this exception; this exception
*     also makes it possible to release a modified version which carries forward this
*     exception. If you modify "The Interfaces", this exception does not apply to your
*     modified version of TorOnionProxyLibrary-Android, and you must remove this
*     exception when you distribute your modified version.
* */
package io.matthewnelson.topl_service

import android.app.Application
import android.content.Context
import android.content.Intent
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.TorService
import io.matthewnelson.topl_core_base.EventBroadcaster
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.receiver.TorServiceReceiver
import io.matthewnelson.topl_service.service.TorServiceConnection
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
     * @param [application] [Application], for obtaining context
     * @param [torServiceNotificationBuilder] The [ServiceNotification.Builder] for
     *   customizing [TorService]'s notification
     * @param [buildConfigVersionCode] send [BuildConfig.VERSION_CODE]. Mitigates copying of geoip
     *   files to app updates only
     * @param [torSettings] [TorSettings] used to create your torrc file on start of Tor
     * @param [geoipAssetPath] The path to where you have your geoip file located (ex: in
     *   assets/common directory, send this variable "common/geoip")
     * @param [geoip6AssetPath] The path to where you have your geoip6 file located (ex: in
     *   assets/common directory, send this variable "common/geoip6")
     *
     * @sample [io.matthewnelson.sampleapp.App.generateTorServiceNotificationBuilder]
     * @sample [io.matthewnelson.sampleapp.App.setupTorServices]
     * */
    class Builder(
        private val application: Application,
        private val torServiceNotificationBuilder: ServiceNotification.Builder,
        private val buildConfigVersionCode: Int,
        private val torSettings: TorSettings,
        private val geoipAssetPath: String,
        private val geoip6AssetPath: String
    ) {

        private lateinit var appEventBroadcaster: EventBroadcaster
        private var restartTorDelayTime = Companion.restartTorDelayTime
        private var stopServiceDelayTime = Companion.stopServiceDelayTime
        private lateinit var torConfigFiles: TorConfigFiles

        // On published releases of this Library, this value will **always** be `false`.
        private var buildConfigDebug = BuildConfig.DEBUG

        /**
         * Default is set to 500ms, (what this method adds time to).
         *
         * A slight delay is required when starting and stopping Tor to allow the [Process]
         * for which it is running in to settle. This method adds time to the the cautionary
         * delay between execution of stopTor and startTor, which are the individual calls
         * executed when using the [restartTor] method.
         *
         * The call to [restartTor] executes individual commands to:
         *
         *   - stop tor + stop tor delay (300ms)
         *   - delay <---------------------- what this method will modify
         *   - start tor + start tor delay (300ms)
         *
         * @param [milliseconds] A value greater than 0
         * @see [io.matthewnelson.topl_service.service.ActionCommands.RestartTor]
         * @see [io.matthewnelson.topl_service.service.ServiceActionProcessor.processActionCommand]
         * */
        fun addTimeToRestartTorDelay(milliseconds: Long): Builder {
            if (milliseconds > 0L)
                this.restartTorDelayTime += milliseconds
            return this
        }

        /**
         * Default is set to 100ms (what this method adds time to).
         *
         * A slight delay is required when starting and stopping Tor to allow the [Process]
         * for which it is running in to settle. This method adds time to the the cautionary
         * delay between execution of stopping Tor and stopping [TorService].
         *
         * The call to [stopTor] executes individual commands to:
         *
         *   - stop tor + stop tor delay (300ms)
         *   - delay <---------------------- what this method will modify
         *   - stop service
         *
         * @param [milliseconds] A value greater than 0
         * @see [io.matthewnelson.topl_service.service.ActionCommands.Stop]
         * @see [io.matthewnelson.topl_service.service.ServiceActionProcessor.processActionCommand]
         * */
        fun addTimeToStopServiceDelay(milliseconds: Long): Builder {
            if (milliseconds > 0L)
                this.stopServiceDelayTime += milliseconds
            return this
        }

        /**
         * This makes it such that on your Application's **Debug** builds, the `topl-core` and
         * `topl-service` modules will provide you with Logcat messages (when
         * [TorSettings.hasDebugLogs] is enabled).
         *
         * For your **Release** builds no Logcat messaging will be provided, but you
         * will still get the same messages sent to your [EventBroadcaster] if you set it
         * via [Builder.setEventBroadcaster].
         *
         * @param [buildConfigDebug] Send [BuildConfig.DEBUG]
         * @see [io.matthewnelson.topl_core.broadcaster.BroadcastLogger]
         *
         * TODO: Provide a link to gh-pages that discusses logging and how it work, it's pretty
         *  complex with everything that is going on.
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
            this.appEventBroadcaster = eventBroadcaster
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
         * @sample [io.matthewnelson.sampleapp.App.customTorConfigFilesSetup]
         * @see [Builder.build]
         * */
        fun useCustomTorConfigFiles(torConfigFiles: TorConfigFiles): Builder {
            this.torConfigFiles = torConfigFiles
            return this
        }

        /**
         * Initializes [TorService] setup and enables the ability to call methods from the
         * [Companion] object w/o throwing exceptions.
         *
         * See [Builder] for code samples.
         * */
        fun build() {

            appContext = application.applicationContext

            // If TorSettings has been initialized already, return as to not
            // overwrite things.
            try {
                getTorSettings()
                return
            } catch (e: RuntimeException) {}

            torServiceNotificationBuilder.build()

            Companion.restartTorDelayTime = this.restartTorDelayTime
            Companion.stopServiceDelayTime = this.stopServiceDelayTime

            if (::appEventBroadcaster.isInitialized)
                Companion.appEventBroadcaster = this.appEventBroadcaster

            Companion.torSettings = torSettings
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
        }
    }

    /**
     * Where everything needed to interact with [TorService] resides.
     * */
    companion object {

        private lateinit var appContext: Context
        var appEventBroadcaster: EventBroadcaster? = null
            private set
        internal var restartTorDelayTime = 500L
        internal var stopServiceDelayTime = 100L
        private lateinit var torConfigFiles: TorConfigFiles
        private lateinit var torSettings: TorSettings

        private fun builderDotBuildNotCalledException(): RuntimeException =
            RuntimeException("${Builder::class.java.simpleName}.build has yet been called")

        /**
         * Get the [TorConfigFiles] that have been set after calling [Builder.build]
         *
         * @return Instance of [TorConfigFiles] that are being used throughout TOPL-Android
         * @throws [RuntimeException] if called before [Builder.build]
         * */
        @Throws(RuntimeException::class)
        fun getTorConfigFiles(): TorConfigFiles =
            if (::torConfigFiles.isInitialized)
                torConfigFiles
            else
                throw builderDotBuildNotCalledException()

        /**
         * Get the [TorSettings] that have been set after calling [Builder.build]. These are
         * the [TorSettings] you initialized [TorServiceController.Builder] with.
         *
         * @return Instance of [TorSettings] that are being used throughout TOPL-Android
         * @throws [RuntimeException] if called before [Builder.build]
         * */
        @Throws(RuntimeException::class)
        fun getTorSettings(): TorSettings =
            if (::torSettings.isInitialized)
                torSettings
            else
                throw builderDotBuildNotCalledException()

        /**
         * Starts [TorService] and then Tor. You can call this as much as you want. If the Tor
         * Process is already running, it will do nothing.
         *
         * @throws [RuntimeException] if called before [Builder.build]
         * */
        @Throws(RuntimeException::class)
        fun startTor() {
            if (!::appContext.isInitialized)
                throw builderDotBuildNotCalledException()

            val startServiceIntent = Intent(appContext, TorService::class.java)
            startServiceIntent.action = ServiceAction.START
            appContext.startService(startServiceIntent)
            bindTorService(appContext)
        }

        /**
         * Stops [TorService]. Does nothing if called prior to calling [startTor]
         *
         * @throws [RuntimeException] if called before [Builder.build]
         * */
        @Throws(RuntimeException::class)
        fun stopTor() =
            sendBroadcast(ServiceAction.STOP)

        /**
         * Restarts Tor. Does nothing if called prior to calling [startTor]
         *
         * @throws [RuntimeException] if called before [Builder.build]
         * */
        @Throws(RuntimeException::class)
        fun restartTor() =
            sendBroadcast(ServiceAction.RESTART_TOR)

        /**
         * Changes identities. Does nothing if called prior to calling [startTor]
         *
         * @throws [RuntimeException] if called before [Builder.build]
         * */
        @Throws(RuntimeException::class)
        fun newIdentity() =
            sendBroadcast(ServiceAction.NEW_ID)

        /**
         * Adding a StringExtra to the Intent by passing a value for [extrasString] will
         * always use the [action] as the key for retrieving it.
         *
         * @param [action] A [ServiceConsts.ServiceAction] to be processed by [TorService]
         * @param [extrasString] To be included in the intent.
         * @throws [RuntimeException] if called before [Builder.build]
         * */
        @Throws(RuntimeException::class)
        private fun sendBroadcast(@ServiceAction action: String, extrasString: String? = null) {
            if (!::appContext.isInitialized)
                throw builderDotBuildNotCalledException()

            val broadcastIntent = Intent(TorServiceReceiver.SERVICE_INTENT_FILTER)
            broadcastIntent.putExtra(TorServiceReceiver.SERVICE_INTENT_FILTER, action)
            broadcastIntent.setPackage(appContext.packageName)

            if (extrasString != null)
                broadcastIntent.putExtra(action, extrasString)

            appContext.sendBroadcast(broadcastIntent)
        }


        /////////////////////
        /// ServiceBinder ///
        /////////////////////
        private val torServiceConnection = TorServiceConnection()

        private fun bindTorService(context: Context) {
            val bindingIntent = Intent(context.applicationContext, TorService::class.java)
            bindingIntent.action = ServiceAction.START

            context.applicationContext.bindService(
                bindingIntent,
                torServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }

        internal fun unbindTorService(context: Context) {
            torServiceConnection.clearServiceBinderReference()
            context.applicationContext.unbindService(torServiceConnection)
        }
    }
}