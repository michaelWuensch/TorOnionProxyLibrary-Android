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
package io.matthewnelson.topl_service.prefs

import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.topl_service.util.ServiceConsts
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(minSdk = 16, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class TorServicePrefsUnitTest: ServiceConsts() {

    private val prefs by lazy {
        TorServicePrefs(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testBooleans() {
        prefs.putBoolean(PrefKeyBoolean.DISABLE_NETWORK, true)
        val result = prefs.getBoolean(PrefKeyBoolean.DISABLE_NETWORK, false)
        assertEquals(true, result)
    }

    @Test
    fun testInts_null() {
        prefs.putInt(PrefKeyInt.PROXY_SOCKS5_SERVER_PORT, null)
        val result = prefs.getInt(PrefKeyInt.PROXY_SOCKS5_SERVER_PORT, 9999)
        assertEquals(null, result)
    }

    @Test
    fun testInts() {
        prefs.putInt(PrefKeyInt.PROXY_PORT, 8188)
        val result = prefs.getInt(PrefKeyInt.PROXY_PORT, null)
        assertEquals(8188, result)
    }

    @Test
    fun testLists_empty() {
        val invalid = arrayListOf("INVALID")
        prefs.putList(PrefKeyList.DNS_PORT_ISOLATION_FLAGS, arrayListOf())
        val result = prefs.getList(PrefKeyList.DNS_PORT_ISOLATION_FLAGS, invalid)
        assertEquals(invalid, result)
    }

    @Test
    fun testLists() {
        val myList = arrayListOf("ONE", "TWO", "THREE")
        val invalid = arrayListOf<String>()
        prefs.putList(PrefKeyList.DNS_PORT_ISOLATION_FLAGS, myList)
        val result = prefs.getList(PrefKeyList.DNS_PORT_ISOLATION_FLAGS, invalid)
        assertEquals(myList, result)
    }

    @Test
    fun testStrings_null() {
        val invalid = "INVALID"
        prefs.putString(PrefKeyString.ENTRY_NODES, null)
        val result = prefs.getString(PrefKeyString.ENTRY_NODES, invalid)
        assertEquals(null, result)
    }

    @Test
    fun testStrings() {
        val invalid = "INVALID"
        prefs.putString(PrefKeyString.PROXY_TYPE, "Socks")
        val result = prefs.getString(PrefKeyString.PROXY_TYPE, invalid)
        assertEquals("Socks", result)
    }
}