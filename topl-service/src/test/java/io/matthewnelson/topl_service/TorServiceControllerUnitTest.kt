package io.matthewnelson.topl_service

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.test_helpers.TestEventBroadcaster
import io.matthewnelson.test_helpers.TestTorSettings
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.TorService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Config(minSdk = 16, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
internal class TorServiceControllerUnitTest {

    private val app: Application by lazy {
        ApplicationProvider.getApplicationContext() as Application
    }
    private val notificationBuilder: ServiceNotification.Builder by lazy {
        ServiceNotification.Builder(
            "Test Channel Name",
            "Test Channel ID",
            "Test Channel Description",
            615615
        )
    }
    private val torConfigFiles: TorConfigFiles by lazy {
        // Cannot use factory methods used by default b/c not running on a device.
        TorConfigFiles.Builder(File("installDir"), File("configDir")).build()
    }
    private val torSettings: TestTorSettings by lazy {
        TestTorSettings()
    }

    @Test(expected = RuntimeException::class)
    fun `_throw errors if startTor called before build`() {
        TorServiceController.startTor()
    }

    @Test(expected = RuntimeException::class)
    fun `_throw errors if sendBroadcast called before build`() {
        // sendBroadcast method used in newIdentity, restartTor, and stopTor
        // which is what will throw the RuntimeException.
        TorServiceController.newIdentity()
    }

    @Test(expected = RuntimeException::class)
    fun `_throw errors if getTorSettings called before build`() {
        TorServiceController.getTorSettings()
    }

    @Test(expected = RuntimeException::class)
    fun `_throw errors if getTorConfigFiles called before build`() {
        TorServiceController.getTorConfigFiles()
    }

    @Test
    fun `_z_ensure builder methods properly initialize variables when build called`() {
        // Hasn't been initialized yet
        assertNull(TorService.buildConfigDebug)
        assertNull(TorServiceController.appEventBroadcaster)
        val initialRestartTorDelay = TorServiceController.restartTorDelayTime
        val initialStopServiceDelay = TorServiceController.stopServiceDelayTime

        val timeToAdd = 300L

        getNewBuilder(torSettings)
            .addTimeToRestartTorDelay(timeToAdd)
            .addTimeToStopServiceDelay(timeToAdd)
            .setBuildConfigDebug(BuildConfig.DEBUG)
            .setEventBroadcaster(TestEventBroadcaster())
            .build()

        assertEquals(TorServiceController.restartTorDelayTime, initialRestartTorDelay + timeToAdd)
        assertEquals(TorServiceController.stopServiceDelayTime, initialStopServiceDelay + timeToAdd)
        assertEquals(TorService.buildConfigDebug, BuildConfig.DEBUG)
        assertNotNull(TorServiceController.appEventBroadcaster)
    }

    @Test
    fun `_zz_ensure one-time initialization if build called more than once`() {
        // build has already been called in previous test
        val initialHashCode = TorServiceController.getTorSettings().hashCode()

        // Instantiate new TorSettings and try to overwrite things via the builder
        val newTorSettings = TestTorSettings()
        getNewBuilder(newTorSettings)
            .useCustomTorConfigFiles(torConfigFiles)
            .build()

        val hashCodeAfterSecondBuildCall = TorServiceController.getTorSettings().hashCode()

        assertNotEquals(newTorSettings.hashCode(), hashCodeAfterSecondBuildCall)
        assertEquals(initialHashCode, hashCodeAfterSecondBuildCall)
    }

    private fun getNewBuilder(torSettings: TorSettings): TorServiceController.Builder {
        return TorServiceController.Builder(
            app,
            notificationBuilder,
            BuildConfig.VERSION_CODE,
            torSettings,
            "common/geoip",
            "common/geoip6"
        )
            .useCustomTorConfigFiles(torConfigFiles)
    }
}