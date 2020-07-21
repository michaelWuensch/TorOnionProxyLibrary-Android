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
package io.matthewnelson.topl_service.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import androidx.annotation.WorkerThread
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.BuildConfig
import io.matthewnelson.topl_service.onionproxy.ServiceEventBroadcaster
import io.matthewnelson.topl_service.onionproxy.ServiceEventListener
import io.matthewnelson.topl_service.onionproxy.ServiceTorInstaller
import io.matthewnelson.topl_service.onionproxy.ServiceTorSettings
import io.matthewnelson.topl_service.prefs.TorServicePrefsListener
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceAction
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.reflect.InvocationTargetException

internal class TorService: Service() {

    companion object {
        private lateinit var torConfigFiles: TorConfigFiles
        private lateinit var torSettings: TorSettings
        private var buildConfigVersionCode: Int = -1
        private var buildConfigDebug: Boolean? = null
        private lateinit var geoipAssetPath: String
        private lateinit var geoip6AssetPath: String

        fun initialize(
            torConfigFiles: TorConfigFiles,
            torSettings: TorSettings,
            buildConfigVersionCode: Int,
            buildConfigDebug: Boolean,
            geoipAssetPath: String,
            geoip6AssetPath: String
        ) {
            this.torConfigFiles = torConfigFiles
            this.torSettings = torSettings
            this.buildConfigVersionCode = buildConfigVersionCode
            this.buildConfigDebug = buildConfigDebug
            this.geoipAssetPath = geoipAssetPath
            this.geoip6AssetPath = geoip6AssetPath
        }

        // Needed to inhibit all TorServiceController methods except for startTor()
        // from sending such that startService isn't called and Tor isn't properly
        // started up.
        @Volatile
        var isTorStarted = false
            private set

        // For things that can't be saved to TorServicePrefs, such as BuildConfig.VERSION_CODE
        fun getLocalPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences("TorServiceLocalPrefs", Context.MODE_PRIVATE)
    }

    private lateinit var broadcastLogger: BroadcastLogger
    private lateinit var torServicePrefsListener: TorServicePrefsListener

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        ServiceNotification.get().startForegroundNotification(this)
        initTOPLCore(this)
        broadcastLogger = onionProxyManager.getBroadcastLogger(TorService::class.java)
        broadcastLogger.notice("BuildConfig.DEBUG set to: $buildConfigDebug")
        torServicePrefsListener = TorServicePrefsListener(this, onionProxyManager)
    }

    override fun onDestroy() {
        torServicePrefsListener.unregister()
        supervisorJob.cancel()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        broadcastLogger.warn("Low memory!!!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let {
            broadcastLogger.debug("Received ServiceAction: $it")

            // Set isTorStarted immediately before sending action off to be executed on
            // a different thread. This inhibits issuance of actions after stop is called,
            // unless that action is ACTION_START which will "save" the service instance
            // and start Tor and allow for queuing of other ServiceActions.
            when (it) {
                ServiceAction.ACTION_START -> {
                    isTorStarted = true
                }
                ServiceAction.ACTION_STOP -> {
                    isTorStarted = false
                }
            }

            executeAction(it)
        }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        // TODO: add to debug message if Controller option for disabling stop on task removed
        //  when the feature gets implemented.
        broadcastLogger.debug("Task has been removed")
        executeAction(ServiceAction.ACTION_STOP)
    }

    /////////////////
    /// TOPL-Core ///
    /////////////////
    private lateinit var onionProxyManager: OnionProxyManager

    private fun initTOPLCore(torService: TorService) {
        val serviceTorInstaller = ServiceTorInstaller(
            torService,
            torConfigFiles,
            buildConfigVersionCode,
            buildConfigDebug ?: BuildConfig.DEBUG,
            geoipAssetPath,
            geoip6AssetPath
        )
        onionProxyManager = OnionProxyManager(
            torService,
            torConfigFiles,
            serviceTorInstaller,
            ServiceTorSettings(torSettings, torService),
            ServiceEventListener(),
            ServiceEventBroadcaster(torService),
            buildConfigDebug
        )
    }

    /**
     * Do not call directly, use [executeAction].
     * */
    @WorkerThread
    private fun startTor() {
        if (onionProxyManager.hasControlConnection) return
        try {
            onionProxyManager.setup()
            generateTorrcFile()

            onionProxyManager.start()
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }

    /**
     * Do not call directly, use [executeAction].
     * */
    @WorkerThread
    private fun stopTor() {
        try {
            onionProxyManager.stop()
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }

    /**
     * Called from [startTor].
     * */
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

    ///////////////
    /// Actions ///
    ///////////////
    private val supervisorJob = SupervisorJob()
    val scopeMain = CoroutineScope(Dispatchers.Main + supervisorJob)
    private lateinit var executeActionJob: Job

    /**
     * Route all [ServiceAction]s here for execution.
     *
     * @param [action] A [ServiceAction]
     * */
    private fun executeAction(@ServiceAction action: String) {
        scopeMain.launch {
            // TODO: Need to think about solution for if start() or stop() hangs due to
            //  jtorctl's lack of interruption timeout
            if (::executeActionJob.isInitialized && executeActionJob.isActive) {
                executeActionJob.join()
                delay(100L)
            }

            executeActionJob = launch(Dispatchers.IO) {
                when (action) {
                    ServiceAction.ACTION_START -> {
                        startTor()
                    }
                    ServiceAction.ACTION_STOP -> {
                        stopTor()

                        // Need a delay before calling stopSelf so that the coroutine which
                        // removes notification actions isn't cancelled via the supervisorJob
                        // being cancelled in onDestroy.
                        delay(200L)

                        // if ACTION_START was called while stopTor was executing, do not stop
                        // the service and allow startTor to be executed.
                        if (!isTorStarted)
                            stopSelf()
                    }
                    ServiceAction.ACTION_RESTART -> {
                        stopTor()
                        delay(1000L)
                        startTor()
                    }
                    ServiceAction.ACTION_NEW_ID -> {
                        onionProxyManager.signalNewNym()
                    }
                }
            }
        }
    }
}