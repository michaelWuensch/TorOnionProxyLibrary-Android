package io.matthewnelson.topl_service.util

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(minSdk = 16, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class TorServicePrefsUnitTest: PrefsKeys() {

    private val prefs by lazy {
        TorServicePrefs(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testBooleans() {
        prefs.putBoolean(BooleanKey.DISABLE_NETWORK, true)
        val result = prefs.getBoolean(BooleanKey.DISABLE_NETWORK, false)
        assertEquals(true, result)
    }

    @Test
    fun testInts_null() {
        prefs.putInt(IntKey.RELAY_PORT, null)
        val result = prefs.getInt(IntKey.RELAY_PORT, 9999)
        assertEquals(null, result)
    }

    @Test
    fun testInts() {
        prefs.putInt(IntKey.PROXY_PORT, 8188)
        val result = prefs.getInt(IntKey.PROXY_PORT, null)
        assertEquals(8188, result)
    }

    @Test
    fun testLists_empty() {
        val invalid = arrayListOf("INVALID")
        prefs.putList(ListKey.LIST_OF_SUPPORTED_BRIDGES, arrayListOf())
        val result = prefs.getList(ListKey.LIST_OF_SUPPORTED_BRIDGES, invalid)
        assertEquals(invalid, result)
    }

    @Test
    fun testLists() {
        val myList = arrayListOf("ONE", "TWO", "THREE")
        val invalid = arrayListOf("INVALID")
        prefs.putList(ListKey.LIST_OF_SUPPORTED_BRIDGES, myList)
        val result = prefs.getList(ListKey.LIST_OF_SUPPORTED_BRIDGES, invalid)
        assertEquals(myList, result)
    }

    @Test
    fun testStrings_null() {
        val invalid = "INVALID"
        prefs.putString(StringKey.ENTRY_NODES, null)
        val result = prefs.getString(StringKey.ENTRY_NODES, invalid)
        assertEquals(null, result)
    }

    @Test
    fun testStrings() {
        val invalid = "INVALID"
        prefs.putString(StringKey.PROXY_TYPE, "Socks")
        val result = prefs.getString(StringKey.PROXY_TYPE, invalid)
        assertEquals("Socks", result)
    }
}