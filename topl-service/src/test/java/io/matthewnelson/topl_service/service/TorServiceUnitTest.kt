package io.matthewnelson.topl_service.service

import android.app.Application
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        get() = testTorService.serviceNotification

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
        testTorService = TestTorService(app, testDispatcher)
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
            TestTorService::class.java,
            TorServiceConnection.torServiceConnection
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
        simulateStartService()

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

        // Bootstrapped
        serviceEventBroadcaster.broadcastNotice("NOTICE|BaseEventListener|Bootstrapped 95% (")
        assertEquals("Bootstrapped 95%", serviceNotification.currentContentText)
        serviceEventBroadcaster.broadcastNotice("NOTICE|BaseEventListener|Bootstrapped 100% (")
        assertEquals("Bootstrapped 100%", serviceNotification.currentContentText)
        assertEquals(false, serviceNotification.progressBarShown)
        assertEquals(serviceNotification.imageNetworkEnabled, serviceNotification.currentIcon)

        // Data transfer
        val bytesRead = "1000"
        val bytesWritten = "1050"
        serviceEventBroadcaster.broadcastBandwidth(bytesRead, bytesWritten)
        val contentTextString =
            ServiceUtilities.getFormattedBandwidthString(bytesRead.toLong(), bytesWritten.toLong())
        assertEquals(contentTextString, serviceNotification.currentContentText)
        assertEquals(serviceNotification.imageDataTransfer, serviceNotification.currentIcon)
        serviceEventBroadcaster.broadcastBandwidth("0", "0")
        assertEquals(serviceNotification.imageNetworkEnabled, serviceNotification.currentIcon)


        // Ensure Receivers were registered
        shadowOf(app).registeredReceivers.elementAtOrNull(0)?.let {
            // Registered with the system
            assertEquals(testTorService.torServiceReceiver, it.broadcastReceiver)
            // Boolean value is correct
            assertEquals(true, TorServiceReceiver.isRegistered)
        }

        // Test TorServicePrefsListener is working (will refresh Loggers if tor is _not_ off)
        assertEquals(true, testTorService.torServicePrefsListenerIsRegistered)
        val currentHasDebugLogsValue = testTorService.serviceTorSettings.hasDebugLogs
        val prefs = TorServicePrefs(app.applicationContext)
        assertTrue(!testTorService.refreshBroadcastLoggerWasCalled)
        prefs.putBoolean(PrefKeyBoolean.HAS_DEBUG_LOGS, !currentHasDebugLogsValue)
        assertTrue(testTorService.refreshBroadcastLoggerWasCalled)
        testTorService.refreshBroadcastLoggerWasCalled = false
    }

    private fun simulateStartService() {
        testTorService.onCreate()
        testTorService.onStartCommand(Intent(ServiceActionName.START), 0, 0)
        testTorService.onBind(null)
    }
}