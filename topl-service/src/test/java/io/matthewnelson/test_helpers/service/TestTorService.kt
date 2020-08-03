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
package io.matthewnelson.test_helpers.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.annotation.WorkerThread
import io.matthewnelson.test_helpers.application_provided_classes.TestEventBroadcaster
import io.matthewnelson.test_helpers.service.components.binding.TestTorServiceBinder
import io.matthewnelson.topl_core.OnionProxyManager
import io.matthewnelson.topl_core.broadcaster.BroadcastLogger
import io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState
import io.matthewnelson.topl_core_base.BaseConsts.TorState
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.service.components.onionproxy.ServiceEventBroadcaster
import io.matthewnelson.topl_service.service.components.onionproxy.ServiceEventListener
import io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorInstaller
import io.matthewnelson.topl_service.service.components.onionproxy.ServiceTorSettings
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.service.components.actions.ServiceActionProcessor
import io.matthewnelson.topl_service.service.components.binding.TorServiceConnection
import io.matthewnelson.topl_service.service.components.receiver.TorServiceReceiver
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import net.freehaven.tor.control.TorControlCommands
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException

internal class TestTorService(
    override val context: Context,
    dispatcher: CoroutineDispatcher
): BaseService() {


    ///////////////
    /// Binding ///
    ///////////////
    val testTorServiceBinder: TestTorServiceBinder by lazy {
            TestTorServiceBinder(this)
    }
    var serviceIsBound = false
        private set

    override fun unbindTorService() {
        try {
            unbindService(context, TorServiceConnection.torServiceConnection)
            serviceIsBound = false
        } catch (e: IllegalArgumentException) {}
    }
    override fun onBind(intent: Intent?): IBinder? {
        serviceIsBound = true
        return testTorServiceBinder
    }


    /////////////////////////
    /// BroadcastReceiver ///
    /////////////////////////
    val torServiceReceiver by lazy {
        TorServiceReceiver(this)
    }
    override fun registerReceiver() {
        torServiceReceiver.register()
    }
    override fun unregisterReceiver() {
        torServiceReceiver.unregister()
    }


    //////////////////
    /// Coroutines ///
    //////////////////
    val supervisorJob = SupervisorJob()
    @ExperimentalCoroutinesApi
    val testCoroutineScope = TestCoroutineScope(dispatcher + supervisorJob)

    @ExperimentalCoroutinesApi
    override fun getScopeIO(): CoroutineScope {
        return testCoroutineScope
    }
    @ExperimentalCoroutinesApi
    override fun getScopeMain(): CoroutineScope {
        return testCoroutineScope
    }


    //////////////////////////////
    /// ServiceActionProcessor ///
    //////////////////////////////
    var stopSelfCalled = false
        private set

    /**
     * In production, this is where `stopSelf()` is called. Need to decouple from
     * the thread it is called on so that functionality of the [ServiceActionProcessor]
     * is simulated properly in that it will stop processing the queue and the Coroutine
     * Job will move to `complete`.
     * */
    @ExperimentalCoroutinesApi
    override fun stopService() {
        stopSelfCalled = true
        getScopeMain().launch { onDestroy() }
    }


    /////////////////
    /// TOPL-Core ///
    /////////////////
    private val broadcastLogger: BroadcastLogger by lazy {
        getBroadcastLogger(TestTorService::class.java)
    }
    val serviceEventBroadcaster: ServiceEventBroadcaster by lazy {
        ServiceEventBroadcaster(this)
    }
    val serviceTorSettings: ServiceTorSettings by lazy {
        ServiceTorSettings(this, TorServiceController.getTorSettings())
    }
    val onionProxyManager: OnionProxyManager by lazy {
        OnionProxyManager(
            context,
            TorServiceController.getTorConfigFiles(),
            ServiceTorInstaller(this),
            serviceTorSettings,
            ServiceEventListener(),
            serviceEventBroadcaster,
            buildConfigDebug
        )
    }

    /**
     * Requires the use of [TorServiceController.Builder.setEventBroadcaster] to set
     * [TestEventBroadcaster] so that [TorServiceController.appEventBroadcaster] is
     * assuredly not null.
     * */
    fun getSimulatedTorStates(): Pair<@TorState String, @TorNetworkState String> {
        val appEventBroadcaster = TorServiceController.appEventBroadcaster!! as TestEventBroadcaster
        return Pair(appEventBroadcaster.torState, appEventBroadcaster.torNetworkState)
    }

    @WorkerThread
    @Throws(IOException::class)
    override fun copyAsset(assetPath: String, file: File) {
        try {
//            FileUtilities.copy(context.assets.open(assetPath), file.outputStream())
        } catch (e: Exception) {
            throw IOException("Failed copying asset from $assetPath", e)
        }
    }
    override fun getBroadcastLogger(clazz: Class<*>): BroadcastLogger {
        return onionProxyManager.getBroadcastLogger(clazz)
    }
    override fun hasControlConnection(): Boolean {
        return getSimulatedTorStates().first == TorState.ON
    }
    override fun isTorOff(): Boolean {
        return getSimulatedTorStates().first == TorState.OFF
    }

    var refreshBroadcastLoggerWasCalled = false
    override fun refreshBroadcastLoggersHasDebugLogsVar() {
        onionProxyManager.refreshBroadcastLoggersHasDebugLogsVar()
        refreshBroadcastLoggerWasCalled = true
    }
    override suspend fun signalNewNym() {
        serviceEventBroadcaster.broadcastNotice(
            "${TorControlCommands.SIGNAL_NEWNYM}: ${OnionProxyManager.NEWNYM_SUCCESS_MESSAGE}"
        )
        delay(1000L)
    }
    override fun signalControlConnection(torControlCommand: String): Boolean {
        return true
    }
    @WorkerThread
    override fun startTor() {
        simulateStart()
    }
    @ExperimentalCoroutinesApi
    private fun simulateStart() = getScopeIO().launch {
        try {
            onionProxyManager.setup()
            generateTorrcFile()

            serviceEventBroadcaster.broadcastTorState(TorState.STARTING, TorNetworkState.DISABLED)
            delay(1000)
            serviceEventBroadcaster.broadcastTorState(TorState.ON, TorNetworkState.DISABLED)
            delay(1000)
            serviceEventBroadcaster.broadcastTorState(TorState.ON, TorNetworkState.ENABLED)
        } catch (e: Exception) {
            broadcastLogger.exception(e)
        }
    }
    @WorkerThread
    override fun stopTor() {
        try {
            serviceEventBroadcaster.broadcastTorState(TorState.STOPPING, TorNetworkState.ENABLED)
            serviceEventBroadcaster.broadcastTorState(TorState.STOPPING, TorNetworkState.DISABLED)
            serviceEventBroadcaster.broadcastTorState(TorState.OFF, TorNetworkState.DISABLED)
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
//            .setGeoIpFiles()
            .finishAndWriteToTorrcFile()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        // Cancel the BackgroundManager's coroutine if it's active so it doesn't execute
        testTorServiceBinder.cancelExecuteBackgroundPolicyJob()
        super.onTaskRemoved(rootIntent)
    }
}