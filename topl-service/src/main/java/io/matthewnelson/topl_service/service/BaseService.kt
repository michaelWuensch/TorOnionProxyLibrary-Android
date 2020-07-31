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
 */
package io.matthewnelson.topl_service.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.components.BaseServiceConnection
import io.matthewnelson.topl_service.util.ServiceConsts
import io.matthewnelson.topl_service.util.ServiceConsts.NotificationImage
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.IOException

/**
 * Contains all methods that are called from classes external to, and instantiated by
 * [TorService]. It acts as the glue and helps with testing such that intercepting the
 * calls are easier when testing the individual components.
 * */
internal abstract class BaseService: Service() {

    companion object {
        var buildConfigVersionCode: Int = -1
            private set
        var buildConfigDebug: Boolean? = null
            private set
        lateinit var geoipAssetPath: String
            private set
        lateinit var geoip6AssetPath: String
            private set

        fun initialize(
            buildConfigVersionCode: Int,
            buildConfigDebug: Boolean,
            geoipAssetPath: String,
            geoip6AssetPath: String
        ) {
            this.buildConfigVersionCode = buildConfigVersionCode
            this.buildConfigDebug = buildConfigDebug
            this.geoipAssetPath = geoipAssetPath
            this.geoip6AssetPath = geoip6AssetPath
        }

        // For things that can't be saved to TorServicePrefs, such as BuildConfig.VERSION_CODE
        fun getLocalPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences("TorServiceLocalPrefs", Context.MODE_PRIVATE)

        //////////////////////
        /// ServiceStartup ///
        //////////////////////
        fun startService(context: Context, clazz: Class<*>, serviceConn: BaseServiceConnection) {
            val startServiceIntent = Intent(context.applicationContext, clazz)
            startServiceIntent.action = ServiceConsts.ServiceAction.START
            context.applicationContext.startService(startServiceIntent)
            bindService(context.applicationContext, serviceConn, clazz)
        }
        /**
         * Binds to the provided [Service] class using the provided [BaseServiceConnection]
         *
         * @param [context] [Context]
         * @param [serviceConn] The [BaseServiceConnection] to bind
         * @param [clazz] The [Service]'s class you wish to unbind
         * */
        private fun bindService(context: Context, serviceConn: BaseServiceConnection, clazz: Class<*>) {
            val bindingIntent = Intent(context.applicationContext, clazz)
            bindingIntent.action = ServiceConsts.ServiceAction.START

            context.applicationContext.bindService(
                bindingIntent,
                serviceConn,
                Context.BIND_AUTO_CREATE
            )
        }

        /**
         * Unbinds [TorService] from the Application and clears the
         * [BaseServiceConnection.serviceBinder] reference.
         *
         * @param [context] [Context]
         * @param [serviceConn] The [BaseServiceConnection] to unbind
         * @throws [IllegalArgumentException] If no binding exists for the provided [serviceConn]
         * */
        @Throws(IllegalArgumentException::class)
        fun unbindService(context: Context, serviceConn: BaseServiceConnection) {
            serviceConn.clearServiceBinderReference()
            context.applicationContext.unbindService(serviceConn)
        }
    }

    // All classes that interact with System APIs which require context to do something
    // call this in production (torService.context), such that in testing we can easily
    // swap it out without needing to start the Service and still get functionality for
    // the components that make TorService work.
    abstract val context: Context


    ///////////////////////////
    /// BackgroundKeepAlive ///
    ///////////////////////////
    abstract fun registerBackgroundKeepAlive()
    abstract fun unregisterBackgroundKeepAlive()


    ///////////////
    /// Binding ///
    ///////////////
    abstract fun unbindService()


    /////////////////////////
    /// BroadcastReceiver ///
    /////////////////////////
    abstract fun registerReceiver()
    abstract fun unregisterReceiver()


    //////////////////
    /// Coroutines ///
    //////////////////
    abstract fun getScopeIO(): CoroutineScope
    abstract fun getScopeMain(): CoroutineScope


    //////////////////////////////
    /// ServiceActionProcessor ///
    //////////////////////////////
    abstract fun processIntent(serviceActionIntent: Intent)
    abstract fun stopService()


    ///////////////////////////
    /// ServiceNotification ///
    ///////////////////////////
    abstract fun addNotificationActions()
    abstract fun removeNotification()
    abstract fun removeNotificationActions()
    abstract fun startForegroundService(): ServiceNotification
    abstract fun stopForegroundService(): ServiceNotification
    abstract fun updateNotificationContentText(string: String)
    abstract fun updateNotificationContentTitle(title: String)
    abstract fun updateNotificationIcon(@NotificationImage notificationImage: Int)
    abstract fun updateNotificationProgress(show: Boolean, progress: Int?)


    /////////////////
    /// TOPL-Core ///
    /////////////////
    @WorkerThread
    @Throws(IOException::class)
    abstract fun copyAsset(assetPath: String, file: File)
    abstract fun getBroadcastLogger(clazz: Class<*>): BroadcastLogger
    abstract fun hasControlConnection(): Boolean
    abstract fun isTorOff(): Boolean
    abstract fun refreshBroadcastLoggersHasDebugLogsVar()
    @WorkerThread
    abstract fun signalControlConnection(torControlCommand: String): Boolean
    @WorkerThread
    abstract suspend fun signalNewNym()
    @WorkerThread
    abstract fun startTor()
    @WorkerThread
    abstract fun stopTor()


    ///////////////////////////////
    /// TorServicePrefsListener ///
    ///////////////////////////////
    abstract fun registerPrefsListener()
    abstract fun unregisterPrefsListener()
}