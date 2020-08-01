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
 */
package io.matthewnelson.topl_service

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.test_helpers.application_provided_classes.TestEventBroadcaster
import io.matthewnelson.test_helpers.application_provided_classes.TestTorSettings
import io.matthewnelson.topl_core_base.TorConfigFiles
import io.matthewnelson.topl_service.notification.ServiceNotification
import io.matthewnelson.topl_service.service.BaseService
import io.matthewnelson.topl_service.service.components.BackgroundManager
import io.matthewnelson.topl_service.service.components.ServiceActionProcessor
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Config(minSdk = 16, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
internal class TorServiceControllerUnitTest {

    @get:Rule
    val testDirectory = TemporaryFolder()

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
    private val torSettings: TestTorSettings by lazy { TestTorSettings() }
    private lateinit var installDir: File
    private lateinit var configDir: File
    private lateinit var torConfigFiles: TorConfigFiles

    @Before
    fun setup() {
        testDirectory.create()
        installDir = File("/usr/bin")
        configDir = testDirectory.newFolder("configDir")
        torConfigFiles = TorConfigFiles.Builder(installDir, configDir)
            .torExecutable(File(installDir, "tor"))
            .build()
    }

    @After
    fun tearDown() {
        testDirectory.delete()
    }

    @Test(expected = RuntimeException::class)
    fun `_throw errors if startTor called before build`() {
        TorServiceController.startTor()
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
        assertEquals(BuildConfig.DEBUG, BaseService.buildConfigDebug)
        assertNull(TorServiceController.appEventBroadcaster)
        val initialRestartTorDelay = ServiceActionProcessor.restartTorDelayTime
        val initialStopServiceDelay = ServiceActionProcessor.stopServiceDelayTime

        val timeToAdd = 300L

        getNewControllerBuilder()
            .addTimeToRestartTorDelay(timeToAdd)
            .addTimeToStopServiceDelay(timeToAdd)
//            .setBackgroundHeartbeatTime(40_000)
            .setBuildConfigDebug(!BuildConfig.DEBUG)
            .setEventBroadcaster(TestEventBroadcaster())
            .build()

        assertEquals(ServiceActionProcessor.restartTorDelayTime, initialRestartTorDelay + timeToAdd)
        assertEquals(ServiceActionProcessor.stopServiceDelayTime, initialStopServiceDelay + timeToAdd)
        assertEquals(BaseService.buildConfigDebug, !BuildConfig.DEBUG)
        assertNotNull(TorServiceController.appEventBroadcaster)
    }

    @Test
    fun `_zz_ensure one-time initialization if build called more than once`() {
        try {
            TorServiceController.getTorSettings()
            assertNotNull(BaseService.buildConfigDebug)
            // build has been called in a previous test
        } catch (e: RuntimeException) {
            assertNull(BaseService.buildConfigDebug)
            getNewControllerBuilder().build()
            assertNotNull(BaseService.buildConfigDebug)
        }

        val initialHashCode = TorServiceController.getTorSettings().hashCode()

        // Instantiate new TorSettings and try to overwrite things via the builder
        val newTorSettings =
            TestTorSettings()
        getNewControllerBuilder()
            .useCustomTorConfigFiles(torConfigFiles)
            .build()

        val hashCodeAfterSecondBuildCall = TorServiceController.getTorSettings().hashCode()

        assertNotEquals(newTorSettings.hashCode(), hashCodeAfterSecondBuildCall)
        assertEquals(initialHashCode, hashCodeAfterSecondBuildCall)
    }

    private fun getNewControllerBuilder(): TorServiceController.Builder {
        return TorServiceController.Builder(
            app,
            notificationBuilder,
            BackgroundManager.Builder().respectResourcesWhileInBackground(30),
            BuildConfig.VERSION_CODE,
            torSettings,
            "common/geoip",
            "common/geoip6"
        )
            .useCustomTorConfigFiles(torConfigFiles)
    }
}