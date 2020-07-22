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
import kotlinx.coroutines.*

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

        // For things that can't be saved to TorServicePrefs, such as BuildConfig.VERSION_CODE
        fun getLocalPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences("TorServiceLocalPrefs", Context.MODE_PRIVATE)
    }

    private val supervisorJob = SupervisorJob()
    val scopeMain = CoroutineScope(Dispatchers.Main + supervisorJob)
    lateinit var serviceActionProcessor: ServiceActionProcessor
        private set
    private lateinit var torServicePrefsListener: TorServicePrefsListener

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        ServiceNotification.get().startForegroundNotification(this)
        initTOPLCore(this)
        serviceActionProcessor = ServiceActionProcessor(this)
        torServicePrefsListener = TorServicePrefsListener(this)
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
            serviceActionProcessor.processIntent(intent)
        }
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        // TODO: add to debug message if Controller option for disabling stop on task removed
        //  when the feature gets implemented.
        broadcastLogger.debug("Task has been removed")
        serviceActionProcessor.processActionObject(ActionCommands.Stop())
    }

    /////////////////
    /// TOPL-Core ///
    /////////////////
    private lateinit var broadcastLogger: BroadcastLogger
    lateinit var onionProxyManager: OnionProxyManager
        private set

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
        broadcastLogger = onionProxyManager.getBroadcastLogger(TorService::class.java)
        broadcastLogger.notice("BuildConfig.DEBUG set to: $buildConfigDebug")
    }
}