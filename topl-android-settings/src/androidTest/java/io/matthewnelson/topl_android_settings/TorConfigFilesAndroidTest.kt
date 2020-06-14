package io.matthewnelson.topl_android_settings

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class TorConfigFilesAndroidTest {

    private val appContext: Context by lazy {
        ApplicationProvider.getApplicationContext() as Context
    }
    private lateinit var torConfigFilesBuilder: TorConfigFiles.Builder
    private val sampleFile = File("sample")

    @Before
    fun setup() {
        torConfigFilesBuilder =
            TorConfigFiles.Builder(
                File(appContext.applicationInfo.nativeLibraryDir),
                sampleFile
            )
    }

    @Test
    fun defaultDataDir() {
        val config = torConfigFilesBuilder.build()
        assertEquals(
            File(sampleFile, "lib/tor").path,
            config.dataDir.path
        )
    }

    @Test
    fun defaultCookie() {
        val config = torConfigFilesBuilder.build()
        assertEquals(
            File(sampleFile, "lib/tor/control_auth_cookie").path,
            config.cookieAuthFile.path
        )
    }

    @Test
    fun defaultHostname() {
        val config = torConfigFilesBuilder.build()
        assertEquals(
            File(sampleFile, "lib/tor/hostname").path,
            config.hostnameFile.path
        )
    }

    @Test
    fun libraryPathRelativeToExecutable() {
        val config = torConfigFilesBuilder.torExecutable(File(sampleFile, "exedir/tor.real")).build()
        assertEquals(
            File(sampleFile, "exedir").path,
            config.libraryPath.path
        )
    }

    @Test
    fun libraryPathDefaultExecutableInstall() {
        val config = torConfigFilesBuilder.build()
        assertEquals(
            appContext.applicationInfo.nativeLibraryDir,
            config.libraryPath.path
        )
    }

    @Test
    fun defaultCookieWithDataDir() {
        val dataDir = File("sample/datadir")
        val config = torConfigFilesBuilder.dataDir(dataDir).build()
        assertEquals(
            File(dataDir, "control_auth_cookie").path,
            config.cookieAuthFile.path
        )
    }

    @Test
    fun geoip() {
        val config = torConfigFilesBuilder.build()
        assertEquals(
            File(sampleFile, TorConfigFiles.GEO_IP_NAME).path,
            config.geoIpFile.path
        )
    }
}