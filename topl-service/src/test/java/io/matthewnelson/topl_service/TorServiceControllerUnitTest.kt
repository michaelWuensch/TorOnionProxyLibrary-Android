package io.matthewnelson.topl_service

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.test_helpers.TestTorSettings
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.notification.ServiceNotification
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

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
        TorConfigFiles.Builder(File("installDir"), File("configDir"))
            .build()
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
    fun `throw errors if called before build`() {
        TorServiceController.startTor()
    }

    @Test
    fun `ensure one-time initialization`() {
        builder.build()
        val initialHashCode = TorServiceController.getTorSettings().hashCode()

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