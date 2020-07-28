package io.matthewnelson.topl_service

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.test_helpers.TestEventBroadcaster
import io.matthewnelson.test_helpers.TestTorSettings
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.TorService
import org.junit.Before
import org.junit.Assert.assertEquals
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
    private lateinit var builder: TorServiceController.Builder
    private lateinit var torSettings: TestTorSettings

    @Before
    fun setup() {
        torSettings = TestTorSettings()
        builder = TorServiceController.Builder(
            app,
            notificationBuilder,
            BuildConfig.VERSION_CODE,
            torSettings,
            "common/geoip",
            "common/geoip6"
        )
            .useCustomTorConfigFiles(torConfigFiles)
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
    fun `_z_ensure setBuildConfigDebug is properly initialized`() {
        // Hasn't been initialized yet
        assertNull(TorService.buildConfigDebug)

        builder.setBuildConfigDebug(BuildConfig.DEBUG).build()

        assertEquals(TorService.buildConfigDebug, BuildConfig.DEBUG)
    }

    @Test
    fun `ensure one-time initialization if build called more than once`() {
        builder.build()
        val initialHashCode = TorServiceController.getTorSettings().hashCode()

        // Instantiate new TorSettings and try to overwrite things via the builder
        torSettings = TestTorSettings()
        builder = TorServiceController.Builder(
            app,
            notificationBuilder,
            BuildConfig.VERSION_CODE,
            torSettings,
            "common/geoip",
            "common/geoip6"
        )
            .useCustomTorConfigFiles(torConfigFiles)

        builder.build()

        val hashCodeAfterSecondBuildCall = TorServiceController.getTorSettings().hashCode()

        assertEquals(initialHashCode, hashCodeAfterSecondBuildCall)
    }
}