package io.matthewnelson.topl_service_settings

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.topl_service_prefs.PrefsKeys
import io.matthewnelson.topl_service_prefs.TorServicePrefs
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(minSdk = 16, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class TorServiceSettingsUnitTest: PrefsKeys() {

    private val appContext: Context by lazy {
        ApplicationProvider.getApplicationContext() as Context
    }
    private val defaults = TestTorSettings()
    private lateinit var settings: TorServiceSettings
    private lateinit var prefs: TorServicePrefs

    @Before
    fun setup() {
        settings = TorServiceSettings(defaults, appContext)
        prefs = TorServicePrefs(appContext)
    }

    @Test
    fun testInts() {
        assertEquals(defaults.proxyPort, settings.proxyPort)

        prefs.putInt(IntKey.PROXY_PORT, -9999)
        assertEquals(-9999, settings.proxyPort)

        prefs.remove(IntKey.PROXY_PORT)
        assertEquals(defaults.proxyPort, settings.proxyPort)
    }

    @Test
    fun testLists() {
        assertEquals(defaults.listOfSupportedBridges, settings.listOfSupportedBridges)

        prefs.putList(ListKey.LIST_OF_SUPPORTED_BRIDGES, arrayListOf("meek"))
        assertEquals(arrayListOf("meek"), settings.listOfSupportedBridges)

        prefs.remove(ListKey.LIST_OF_SUPPORTED_BRIDGES)
        assertEquals(defaults.listOfSupportedBridges, settings.listOfSupportedBridges)
    }

    @Test
    fun testStrings() {
        assertEquals(defaults.proxyUser, settings.proxyUser)

        prefs.putString(StringKey.PROXY_USER, "tor")
        assertEquals("tor", settings.proxyUser)

        prefs.remove(StringKey.PROXY_USER)
        assertEquals(defaults.proxyUser, settings.proxyUser)
    }

}