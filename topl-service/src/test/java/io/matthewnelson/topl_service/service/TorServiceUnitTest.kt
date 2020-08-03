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

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.test_helpers.application_provided_classes.TestEventBroadcaster
import io.matthewnelson.test_helpers.application_provided_classes.TestTorSettings
import io.matthewnelson.test_helpers.service.TestTorService
import io.matthewnelson.topl_core_base.BaseConsts.TorNetworkState
import io.matthewnelson.topl_core_base.BaseConsts.TorState
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.BuildConfig
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.components.onionproxy.ServiceEventBroadcaster
import io.matthewnelson.topl_service.prefs.TorServicePrefs
import io.matthewnelson.topl_service.service.components.receiver.TorServiceReceiver
import io.matthewnelson.topl_service.lifecycle.BackgroundManager
import io.matthewnelson.topl_service.service.components.binding.TorServiceConnection
import io.matthewnelson.topl_service.util.ServiceConsts.PrefKeyBoolean
import io.matthewnelson.topl_service.util.ServiceConsts.ServiceActionName
import io.matthewnelson.topl_service.util.ServiceUtilities
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.io.File


/**
 * Tests the components that make [TorService] work.
 * */
@InternalCoroutinesApi
@Config(minSdk = 16, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
internal class TorServiceUnitTest {

    @get:Rule
    val testDirectory = TemporaryFolder()

    private val app: Application by lazy {
        ApplicationProvider.getApplicationContext() as Application
    }
    private val torSettings: TestTorSettings by lazy { TestTorSettings() }
    private lateinit var torConfigFiles: TorConfigFiles
    private lateinit var testEventBroadcaster: TestEventBroadcaster
    private lateinit var testTorService: TestTorService
    private val serviceEventBroadcaster: ServiceEventBroadcaster
        get() = testTorService.serviceEventBroadcaster
    private val serviceNotification: ServiceNotification
        get() = ServiceNotification.serviceNotification

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        testDirectory.create()
        val installDir = File("/usr/bin")
        torConfigFiles =
            TorConfigFiles
                .Builder(installDir, testDirectory.newFolder("configDir"))
                .torExecutable(File(installDir, "tor"))
                .build()
        val notificationBuilder = getNewServiceNotificationBuilder()
            .showNotification(true)
            .enableTorRestartButton(true)
            .enableTorStopButton(true)

        testEventBroadcaster = TestEventBroadcaster()

        getNewControllerBuilder(notificationBuilder)
            .setEventBroadcaster(testEventBroadcaster)
            .setBuildConfigDebug(BuildConfig.DEBUG)
            .build()

        Dispatchers.setMain(testDispatcher)

        // Setup TestTorService
        testTorService = TestTorService(app, testDispatcher)

        // Ensure everything's cleared
        shadowOf(app).clearStartedServices()
        assertNull(shadowOf(app).nextStartedService)

        // Ensure binder is being set when startService is called
        shadowOf(app).setComponentNameAndServiceForBindService(
            ComponentName(app.applicationContext, TestTorService::class.java),
            testTorService.testTorServiceBinder
        )
        BaseService.startService(app, TestTorService::class.java)
        assertEquals(shadowOf(app).nextStartedService.action, ServiceActionName.START)
        assertNotNull(TorServiceConnection.serviceBinder)

        // Simulate startup
        testTorService.onCreate()
        testTorService.onStartCommand(Intent(ServiceActionName.START), 0, 0)
    }

    private fun getNewServiceNotificationBuilder(): ServiceNotification.Builder =
        ServiceNotification.Builder(
            "Test Channel Name",
            "Test Channel ID",
            "Test Channel Description",
            615615
        )

    private fun getNewControllerBuilder(
        notificationBuilder: ServiceNotification.Builder
    ): TorServiceController.Builder {
        val backgroundPolicyBuilder = BackgroundManager.Builder()
            .respectResourcesWhileInBackground(5)

        // Build it prior to initializing TorServiceController with the builder
        // so we get our test classes initialized which won't be overwritten.
        backgroundPolicyBuilder.build(
            TestTorService::class.java
        )

        return TorServiceController.Builder(
            app,
            notificationBuilder,
            backgroundPolicyBuilder,
            BuildConfig.VERSION_CODE,
            torSettings,
            "common/geoip",
            "common/geoip6"
        )
            .useCustomTorConfigFiles(torConfigFiles)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        testDirectory.delete()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `validate startup state`() = runBlockingTest(testDispatcher) {
        // Check EventBroadcasters are working and notification is being updated
        var statePair = testTorService.getSimulatedTorStates()
        assertEquals(TorState.STARTING, statePair.first)
        assertEquals(TorNetworkState.DISABLED, statePair.second)
        assertEquals(TorState.STARTING, serviceNotification.currentContentTitle)
        delay(1000)

        statePair = testTorService.getSimulatedTorStates()
        assertEquals(TorState.ON, statePair.first)
        assertEquals(TorNetworkState.DISABLED, statePair.second)
        assertEquals(TorState.ON, serviceNotification.currentContentTitle)
        delay(1000)

        // Ensure ServiceActionProcessor started Tor, messages broadcast properly,
        // and notification is updating
        statePair = testTorService.getSimulatedTorStates()
        assertEquals(TorState.ON, statePair.first)
        assertEquals(TorNetworkState.ENABLED, statePair.second)
        assertEquals(TorState.ON, serviceNotification.currentContentTitle)

        // Waiting to Bootstrap
        assertEquals(true, serviceNotification.progressBarShown)
        assertEquals(serviceNotification.imageNetworkDisabled, serviceNotification.currentIcon)
        delay(1000)

        // Bootstrapped
        assertEquals("Bootstrapped 95%", serviceNotification.currentContentText)
        delay(1000)

        assertEquals("Bootstrapped 100%", serviceNotification.currentContentText)
        assertEquals(false, serviceNotification.progressBarShown)
        assertEquals(serviceNotification.imageNetworkEnabled, serviceNotification.currentIcon)
        delay(1000)

        // Data transfer
        val bandwidth = testTorService.bandwidth1000
        val contentTextString =
            ServiceUtilities.getFormattedBandwidthString(bandwidth.toLong(), bandwidth.toLong())
        assertEquals(contentTextString, serviceNotification.currentContentText)
        assertEquals(serviceNotification.imageDataTransfer, serviceNotification.currentIcon)
        delay(1000)

        assertEquals(serviceNotification.imageNetworkEnabled, serviceNotification.currentIcon)
        delay(1000)

        // Ensure Receivers were registered
        shadowOf(app).registeredReceivers.elementAtOrNull(0)?.let {
            // Registered with the system
            assertEquals(testTorService.torServiceReceiver, it.broadcastReceiver)
            // Boolean value is correct
            assertEquals(true, TorServiceReceiver.isRegistered)
        }

        // Test TorServicePrefsListener is working (will refresh Loggers if tor is _not_ off)
        val currentHasDebugLogsValue = testTorService.serviceTorSettings.hasDebugLogs
        val prefs = TorServicePrefs(app.applicationContext)
        assertTrue(!testTorService.refreshBroadcastLoggerWasCalled)
        prefs.putBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, !currentHasDebugLogsValue)
        assertTrue(testTorService.refreshBroadcastLoggerWasCalled)
        testTorService.refreshBroadcastLoggerWasCalled = false
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `calling stopTor cleans up`() = runBlockingTest(testDispatcher){
        delay(6000) // testTorService.simulateStart() takes 6000ms

        TorServiceController.stopTor()

        // Broadcaster and notification are working properly
        var statePair = testTorService.getSimulatedTorStates()
        assertEquals(TorState.STOPPING, statePair.first)
        assertEquals(TorNetworkState.ENABLED, statePair.second)
        assertEquals(TorState.STOPPING, serviceNotification.currentContentTitle)
        assertEquals("Stopping Service...", serviceNotification.currentContentText)
        delay(1000)

        statePair = testTorService.getSimulatedTorStates()
        assertEquals(TorState.STOPPING, statePair.first)
        assertEquals(TorNetworkState.DISABLED, statePair.second)
        assertEquals(TorState.STOPPING, serviceNotification.currentContentTitle)
        delay(1000)

        statePair = testTorService.getSimulatedTorStates()
        assertEquals(TorState.OFF, statePair.first)
        assertEquals(TorNetworkState.DISABLED, statePair.second)
        assertEquals(TorState.OFF, serviceNotification.currentContentTitle)
        delay(1000)

        // Ensure Receivers were unregistered
        shadowOf(app).registeredReceivers.elementAtOrNull(0)?.let {
            // Registered with the system
            assertNull(it.broadcastReceiver)
            // Boolean value is correct
            assertEquals(false, TorServiceReceiver.isRegistered)
        }

        // Test TorServicePrefsListener is unregistered
        val currentHasDebugLogsValue = testTorService.serviceTorSettings.hasDebugLogs
        val prefs = TorServicePrefs(app.applicationContext)
        assertFalse(testTorService.refreshBroadcastLoggerWasCalled)
        prefs.putBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, !currentHasDebugLogsValue)
        assertFalse(testTorService.refreshBroadcastLoggerWasCalled)
    }
}