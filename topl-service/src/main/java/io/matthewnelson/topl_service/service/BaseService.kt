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
*     needed to implement TorOnionProxyLibrary-Android, as listed below:
*
*      - From the `topl-core-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service` module:
*          - The TorServiceController class and it's contained classes/methods/variables
*          - The ServiceNotification.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Companion class and it's contained methods/variables
*
*     The following code is excluded from "The Interfaces":
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
package io.matthewnelson.topl_service.service

import android.app.Application
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.BuildConfig
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.lifecycle.BackgroundManager
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.prefs.TorServicePrefsListener
import io.matthewnelson.topl_service.service.components.actions.ServiceActionProcessor
import io.matthewnelson.topl_service.service.components.actions.ServiceAction
import io.matthewnelson.topl_service.service.components.binding.TorServiceConnection
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceActionName
import io.matthewnelson.topl_service.util.ServiceConsts.NotificationImage
import io.matthewnelson.topl_service_base.ApplicationDefaultTorSettings
import io.matthewnelson.topl_service_base.BaseServiceConsts.ServiceLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.io.IOException

/**
 * Contains all methods that are called from classes external to, and instantiated by
 * [TorService]. It acts as the glue and helps with integration testing of the individual
 * components that make [TorService] work.
 * */
internal abstract class BaseService: Service() {

    companion object {
        private var application: Application? = null
        var buildConfigVersionCode: Int = -1
            private set
        var buildConfigDebug: Boolean = BuildConfig.DEBUG
            private set
        var geoipAssetPath: String = ""
            private set
        var geoip6AssetPath: String = ""
            private set
        lateinit var torConfigFiles: TorConfigFiles
            private set
        lateinit var defaultTorSettings: ApplicationDefaultTorSettings
            private set
        var stopServiceOnTaskRemoved: Boolean = true
            private set

        fun initialize(
            application: Application,
            buildConfigVersionCode: Int,
            torSettings: ApplicationDefaultTorSettings,
            buildConfigDebug: Boolean,
            geoipAssetPath: String,
            geoip6AssetPath: String,
            torConfigFiles: TorConfigFiles,
            stopServiceOnTaskRemoved: Boolean
        ) {
            if (this.application == null) {
                this.application = application
                this.buildConfigVersionCode = buildConfigVersionCode
                this.defaultTorSettings = torSettings
                this.buildConfigDebug = buildConfigDebug
                this.geoipAssetPath = geoipAssetPath
                this.geoip6AssetPath = geoip6AssetPath
                this.torConfigFiles = torConfigFiles
                this.stopServiceOnTaskRemoved = stopServiceOnTaskRemoved
            }
        }

        @Throws(RuntimeException::class)
        fun getAppContext(): Context =
            application?.applicationContext ?: throw RuntimeException(
                "Builder.build has not been called yet"
            )

        // For things that can't be saved to TorServicePrefs, such as BuildConfig.VERSION_CODE
        fun getLocalPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences("TorServiceLocalPrefs", Context.MODE_PRIVATE)


        ///////////////////////////////////
        /// Last Accepted ServiceAction ///
        ///////////////////////////////////
        @Volatile
        @ServiceActionName
        private var lastAcceptedServiceAction: String = ServiceActionName.STOP

        /**
         * Updates [lastAcceptedServiceAction] in several key places so that we can keep
         * [TorService]'s state in sync with the latest calls coming from the Application using
         * the Library. It is used in [TorService.onStartCommand] and
         * [io.matthewnelson.topl_service.service.components.binding.BaseServiceBinder.submitServiceAction]
         *
         * @param [serviceAction] The [ServiceActionName] to update [lastAcceptedServiceAction] to
         * */
        fun updateLastAcceptedServiceAction(@ServiceActionName serviceAction: String) {
            lastAcceptedServiceAction = serviceAction
        }


        //////////////////////
        /// ServiceStartup ///
        //////////////////////

        /**
         * Starts the Service. Setting [includeIntentActionStart] to `false`, will not include
         * [ServiceActionName.START] in the Intent as an action so that [onStartCommand] knows
         * to set the [ServiceAction.Start.updateLastAction] to false. This allows for
         * distinguishing what is coming from the application (either by user input, or how
         * the application has the library implemented), and what is coming from this library.
         * It makes keeping the state of the service in sync with the application's desires.
         *
         * @param [context]
         * @param [serviceClass] The Service's class wanting to be started
         * @param [includeIntentActionStart] Boolean for including [ServiceActionName.START] as
         *   the Intent's Action.
         * @param [bindServiceFlag] The flag to use when binding to [TorService]
         * @return true if startService didn't throw an exception, false if it did.
         * */
        fun startService(
            context: Context,
            serviceClass: Class<*>,
            includeIntentActionStart: Boolean = true,
            bindServiceFlag: Int = Context.BIND_AUTO_CREATE
        ): Boolean {
            val intent = Intent(context.applicationContext, serviceClass)
            if (includeIntentActionStart) {
                intent.action = ServiceActionName.START
            }

            // A RuntimeException is thrown if Context.startService is called while
            // the application is in the background. In that case, we do not want to
            // start/bind the service b/c we are most likely getting a restart from
            // the system via START_STICKY
            return try {
                context.applicationContext.startService(intent)
                bindService(context, serviceClass, bindServiceFlag)
                true
            } catch (e: RuntimeException) {
                false
            }
        }

        /**
         * Binds the Service.
         *
         * @param [context]
         * @param [serviceClass] The Service's class wanting to be started
         * @param [bindServiceFlag] The flag to use when binding to [TorService]
         * @return true if startService didn't throw an exception, false if it did.
         * */
        fun bindService(
            context: Context,
            serviceClass: Class<*>,
            bindServiceFlag: Int = Context.BIND_AUTO_CREATE
        ) {
            context.applicationContext.bindService(
                Intent(context.applicationContext, serviceClass),
                TorServiceConnection.torServiceConnection,
                bindServiceFlag
            )
        }


        /**
         * Unbinds [TorService] from the Application and clears the reference to
         * [TorServiceConnection.serviceBinder].
         *
         * @param [context] [Context]
         * @throws [IllegalArgumentException] If no binding exists
         * */
        @Throws(IllegalArgumentException::class)
        fun unbindService(context: Context) {
            TorServiceConnection.torServiceConnection.clearServiceBinderReference()
            context.applicationContext.unbindService(TorServiceConnection.torServiceConnection)
        }
    }

    // All classes that interact with APIs which require Context to do something
    // call this in production (torService.context). This allows for easily
    // swapping it out with what we want to use when testing.
    abstract val context: Context


    ///////////////
    /// Binding ///
    ///////////////
    open fun unbindTorService() {
        TorServiceController.appEventBroadcaster?.broadcastServiceLifecycleEvent(
            ServiceLifecycleEvent.ON_UNBIND, this.hashCode()
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        TorServiceController.appEventBroadcaster?.broadcastServiceLifecycleEvent(
            ServiceLifecycleEvent.ON_BIND, this.hashCode()
        )
        return null
    }


    /////////////////////////
    /// BroadcastReceiver ///
    /////////////////////////
    abstract fun registerReceiver()
    abstract fun setIsDeviceLocked()
    abstract fun unregisterReceiver()


    //////////////////
    /// Coroutines ///
    //////////////////
    abstract fun getScopeIO(): CoroutineScope
    abstract fun getScopeMain(): CoroutineScope


    //////////////////////////////
    /// ServiceActionProcessor ///
    //////////////////////////////
    private val serviceActionProcessor by lazy {
        ServiceActionProcessor(this)
    }

    fun processServiceAction(serviceAction: ServiceAction) {
        serviceActionProcessor.processServiceAction(serviceAction)
    }
    open fun stopService() {
        stopSelf()
    }


    ///////////////////////////
    /// ServiceNotification ///
    ///////////////////////////
    private val serviceNotification: ServiceNotification
        get() = ServiceNotification.serviceNotification

    fun addNotificationActions() {
        serviceNotification.addActions(this)
    }
    fun doesReceiverNeedToListenForLockScreen(): Boolean {
        return when {
            serviceNotification.visibility == NotificationCompat.VISIBILITY_SECRET -> {
                false
            }
            serviceNotification.enableRestartButton || serviceNotification.enableStopButton -> {
                true
            }
            else -> {
                false
            }
        }
    }
    open fun refreshNotificationActions(): Boolean {
        return serviceNotification.refreshActions(this)
    }
    fun removeNotification() {
        serviceNotification.remove()
    }
    fun removeNotificationActions() {
        serviceNotification.removeActions(this)
    }
    open fun startForegroundService(): Boolean {
        return serviceNotification.startForeground(this)
    }
    open fun stopForegroundService(): Boolean {
        return serviceNotification.stopForeground(this)
    }
    fun updateNotificationContentText(string: String) {
        serviceNotification.updateContentText(this, string)
    }
    fun updateNotificationContentTitle(title: String) {
        serviceNotification.updateContentTitle(this, title)
    }
    fun updateNotificationIcon(@NotificationImage notificationImage: Int) {
        serviceNotification.updateIcon(this, notificationImage)
    }
    fun updateNotificationProgress(show: Boolean, progress: Int?) {
        serviceNotification.updateProgress(this, show, progress)
    }


    /////////////////
    /// TOPL-Core ///
    /////////////////
    @WorkerThread
    @Throws(IOException::class)
    abstract fun copyAsset(assetPath: String, file: File)
    @WorkerThread
    @Throws(IOException::class, NullPointerException::class)
    abstract fun disableNetwork(disable: Boolean)
    abstract fun getBroadcastLogger(clazz: Class<*>): BroadcastLogger
    abstract fun hasNetworkConnectivity(): Boolean
    abstract fun hasControlConnection(): Boolean
    abstract fun isTorOff(): Boolean
    abstract fun isTorOn(): Boolean
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
    private var torServicePrefsListener: TorServicePrefsListener? = null

    // TODO: register and unregister based on background/foreground state using
    //  Background manager
    private fun registerPrefsListener() {
        torServicePrefsListener?.unregister()
        torServicePrefsListener = TorServicePrefsListener(this)
    }
    private fun unregisterPrefsListener() {
        torServicePrefsListener?.unregister()
        torServicePrefsListener = null
    }




    override fun onCreate() {
        TorServiceController.appEventBroadcaster?.broadcastServiceLifecycleEvent(
            ServiceLifecycleEvent.CREATED, this.hashCode()
        )
        serviceNotification.buildNotification(this, setStartTime = true)
        registerPrefsListener()
        setIsDeviceLocked()
    }

    override fun onDestroy() {
        TorServiceController.appEventBroadcaster?.broadcastServiceLifecycleEvent(
            ServiceLifecycleEvent.DESTROYED, this.hashCode()
        )
        unregisterPrefsListener()
    }

    /**
     * No matter what Intent comes in, it starts Tor. If the Intent comes with no Action,
     * it will not update [ServiceActionProcessor.lastServiceAction].
     *
     * @see [Companion.startService]
     * */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ServiceActionName.START) {
            processServiceAction(ServiceAction.Start())
        } else {
            processServiceAction(ServiceAction.Start(updateLastServiceAction = false))
        }

        return START_NOT_STICKY
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        TorServiceController.appEventBroadcaster?.broadcastServiceLifecycleEvent(
            ServiceLifecycleEvent.TASK_REMOVED, this.hashCode()
        )
        BackgroundManager.taskIsRemovedFromRecentApps(true)
        // Move to the foreground so we can properly shutdown w/o interrupting the
        // application's normal lifecycle (Context.startServiceForeground does... thus,
        // the complexity)
        startForegroundService()

        // Shutdown Tor and stop the Service.
        if (stopServiceOnTaskRemoved) {
            processServiceAction(ServiceAction.Stop(updateLastServiceAction = false))
        }
    }
}