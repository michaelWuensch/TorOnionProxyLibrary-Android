package io.matthewnelson.test_helpers

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_core_base.TorSettings
import io.matthewnelson.topl_service.BuildConfig
import io.matthewnelson.topl_service.TorServiceController
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.util.ServiceConsts
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File

internal abstract class TestBase: ServiceConsts() {

    @get:Rule
    val testDirectory = TemporaryFolder()

    private val installDir = File("/usr/bin")
    lateinit var configDir: File

    private val notificationBuilder: ServiceNotification.Builder by lazy {
        ServiceNotification.Builder(
            "Test Channel Name",
            "Test Channel ID",
            "Test Channel Description",
            615615
        )
    }
    lateinit var torConfigFiles: TorConfigFiles
    val torSettings: TestTorSettings by lazy {
        TestTorSettings()
    }

    fun setupTorConfigFiles() {
        testDirectory.create()
        configDir = testDirectory.newFolder("configDir")
        torConfigFiles = TorConfigFiles.Builder(installDir, configDir)
            .torExecutable(File(installDir, "tor"))
            .build()
    }

    fun getNewBuilder(app: Application, torSettings: TorSettings): TorServiceController.Builder {
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