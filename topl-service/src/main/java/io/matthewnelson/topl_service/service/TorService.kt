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
package io.matthewnelson.topl_service.service

import android.content.*
import android.os.IBinder
import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_core.util.FileUtilities
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.onionproxy.ServiceEventBroadcaster
import io.matthewnelson.topl_service.onionproxy.ServiceEventListener
import io.matthewnelson.topl_service.onionproxy.ServiceTorInstaller
import io.matthewnelson.topl_service.onionproxy.ServiceTorSettings
import io.matthewnelson.topl_service.prefs.TorServicePrefsListener
import io.matthewnelson.topl_service.receiver.TorServiceReceiver
import io.matthewnelson.topl_service.service.components.BackgroundKeepAlive
import io.matthewnelson.topl_service.service.components.ServiceActionProcessor
import io.matthewnelson.topl_service.service.components.TorServiceBinder
import io.matthewnelson.topl_service.util.ServiceConsts.NotificationImage
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceAction
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException

internal class TorService: BaseService() {

    override val context: Context
        get() = this


    ///////////////////////////
    /// BackgroundKeepAlive ///
    ///////////////////////////
    private var backgroundKeepAlive: BackgroundKeepAlive? = null

    override fun registerBackgroundKeepAlive() {
        backgroundKeepAlive?.unregister()
        backgroundKeepAlive = BackgroundKeepAlive(this)
    }
    override fun unregisterBackgroundKeepAlive() {
        backgroundKeepAlive?.unregister()
        backgroundKeepAlive = null
    }
    override fun onTrimMemory(level: Int) {
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE -> {
            }
            ComponentCallbacks2.TRIM_MEMORY_MODERATE -> {
                startForegroundService().stopForeground(this)
            }
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> {
                broadcastLogger.debug("Application sent to background")
                registerBackgroundKeepAlive()
            }
            else -> {}
        }
    }


    ///////////////
    /// Binding ///
    ///////////////
    private val torServiceBinder: TorServiceBinder by lazy { TorServiceBinder(this) }

    override fun unbindService() {
        try {
            TorServiceController.unbindTorService(context)
            broadcastLogger.debug("Has been unbound")
        } catch (e: IllegalArgumentException) {}
    }
    override fun onBind(intent: Intent?): IBinder? {
        broadcastLogger.debug("Has been bound")
        return torServiceBinder
    }


    /////////////////////////
    /// BroadcastReceiver ///
    /////////////////////////
    private val torServiceReceiver by lazy { TorServiceReceiver(this) }

    override fun registerReceiver() {
        torServiceReceiver.register()
    }
    override fun unregisterReceiver() {
        torServiceReceiver.unregister()
    }


    //////////////////
    /// Coroutines ///
    //////////////////
    private val supervisorJob = SupervisorJob()
    private val scopeIO = CoroutineScope(Dispatchers.IO + supervisorJob)
    private val scopeMain = CoroutineScope(Dispatchers.Main + supervisorJob)

    override fun getScopeIO(): CoroutineScope {
        return scopeIO
    }
    override fun getScopeMain(): CoroutineScope {
        return scopeMain
    }


    //////////////////////////////
    /// ServiceActionProcessor ///
    //////////////////////////////
    private val serviceActionProcessor by lazy { ServiceActionProcessor(this) }

    override fun processIntent(serviceActionIntent: Intent) {
        serviceActionProcessor.processIntent(serviceActionIntent)
    }
    override fun stopService() {
        stopSelf()
    }


    ///////////////////////////
    /// ServiceNotification ///
    ///////////////////////////
    private val serviceNotification = ServiceNotification.get()

    override fun addNotificationActions() {
        serviceNotification.addActions(this)
    }
    override fun removeNotification() {
        serviceNotification.remove()
    }
    override fun removeNotificationActions() {
        serviceNotification.removeActions(this)
    }
    override fun startForegroundService(): ServiceNotification {
        return serviceNotification.startForeground(this)
    }
    override fun stopForegroundService(): ServiceNotification {
        return serviceNotification.stopForeground(this)
    }
    override fun updateNotificationContentText(string: String) {
        serviceNotification.updateContentText(string)
    }
    override fun updateNotificationContentTitle(title: String) {
        serviceNotification.updateContentTitle(title)
    }
    override fun updateNotificationIcon(@NotificationImage notificationImage: Int) {
        serviceNotification.updateIcon(this, notificationImage)
    }
    override fun updateNotificationProgress(show: Boolean, progress: Int?) {
        serviceNotification.updateProgress(show, progress)
    }



    /////////////////
    /// TOPL-Core ///
    /////////////////
    private val broadcastLogger: BroadcastLogger by lazy {
        getBroadcastLogger(TorService::class.java)
    }
    private val onionProxyManager: OnionProxyManager by lazy {
        OnionProxyManager(
            context,
            TorServiceController.getTorConfigFiles(),
            ServiceTorInstaller(this),
            ServiceTorSettings(this, TorServiceController.getTorSettings()),
            ServiceEventListener(),
            ServiceEventBroadcaster(this),
            buildConfigDebug
        )
    }

    @WorkerThread
    @Throws(IOException::class)
    override fun copyAsset(assetPath: String, file: File) {
        try {
            FileUtilities.copy(context.assets.open(assetPath), file.outputStream())
        } catch (e: Exception) {
            throw IOException("Failed copying asset from $assetPath", e)
        }
    }
    override fun getBroadcastLogger(clazz: Class<*>): BroadcastLogger {
        return onionProxyManager.getBroadcastLogger(clazz)
    }
    override fun hasControlConnection(): Boolean {
        return onionProxyManager.hasControlConnection
    }
    override fun isTorOff(): Boolean {
        return onionProxyManager.torStateMachine.isOff
    }
    override fun refreshBroadcastLoggersHasDebugLogsVar() {
        onionProxyManager.refreshBroadcastLoggersHasDebugLogsVar()
    }

    @WorkerThread
    override fun signalControlConnection(torControlCommand: String): Boolean {
        return onionProxyManager.signalControlConnection(torControlCommand)
    }
    @WorkerThread
    override suspend fun signalNewNym() {
        onionProxyManager.signalNewNym()
    }
    @WorkerThread
    override fun startTor() {
        try {
            onionProxyManager.setup()
            generateTorrcFile()

            onionProxyManager.start()
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }
    @WorkerThread
    override fun stopTor() {
        try {
            onionProxyManager.stop()
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }
    @WorkerThread
    @Throws(
        SecurityException::class,
        IllegalAccessException::class,
        IllegalArgumentException::class,
        InvocationTargetException::class,
        NullPointerException::class,
        ExceptionInInitializerError::class,
        IOException::class
    )
    private fun generateTorrcFile() {
        onionProxyManager.getNewSettingsBuilder()
            .updateTorSettings()
            .setGeoIpFiles()
            .finishAndWriteToTorrcFile()
    }


    ///////////////////////////////
    /// TorServicePrefsListener ///
    ///////////////////////////////
    private var torServicePrefsListener: TorServicePrefsListener? = null

    override fun registerPrefsListener() {
        torServicePrefsListener?.unregister()
        torServicePrefsListener = TorServicePrefsListener(this)
    }
    override fun unregisterPrefsListener() {
        torServicePrefsListener?.unregister()
        torServicePrefsListener = null
    }


    override fun onCreate() {
        serviceNotification.buildNotification(this)
        broadcastLogger.notice("BuildConfig.DEBUG set to: $buildConfigDebug")
        registerPrefsListener()
    }

    override fun onDestroy() {
        unregisterPrefsListener()
        unregisterBackgroundKeepAlive()
        removeNotification()
        supervisorJob.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {

                if (intent.action == ServiceAction.START) {
                    processIntent(intent)
                } else {
                    broadcastLogger.warn(
                        "${intent.action} was used with startService. Use only " +
                                "${ServiceAction.START} to ensure proper startup of Tor"
                    )
                    processIntent(Intent(ServiceAction.START))
                    processIntent(intent)
                }

        }/* else {
            // If it's null, we're getting a re-start from the system via START_STICKY
            // and need to relink everything.
            TorServiceController.startTor()
        }*/
        return /*START_STICKY*/START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        startForegroundService()
        broadcastLogger.debug("Task has been removed")
        processIntent(Intent(ServiceAction.STOP))
    }
}