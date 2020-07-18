/*
* TorOnionProxyLibrary-Android (a.k.a. topl-android) is a derivation of
* work from the Tor_Onion_Proxy_Library project that started at commit
* hash 74407114cbfa8ea6f2ac51417dda8be98d8aba86. Contributions made after
* said commit hash are:
*
*     Copyright (C) 2020 Matthew Nelson
*
*     This program is free software: you can redistribute it and/or modify
*     it under the terms of the GNU General Public License as published by
*     the Free Software Foundation, either version 3 of the License, or
*     (at your option) any later version.
*
*     This program is distributed in the hope that it will be useful,
*     but WITHOUT ANY WARRANTY; without even the implied warranty of
*     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*     GNU General Public License for more details.
*
*     You should have received a copy of the GNU General Public License
*     along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
* ================================================================================
* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
* ================================================================================
*
* The original code, prior to commit hash 74407114cbfa8ea6f2ac51417dda8be98d8aba86,
* was:
*
*     Copyright (c) Microsoft Open Technologies, Inc.
*     All Rights Reserved
*
*     Licensed under the Apache License, Version 2.0 (the "License");
*     you may not use this file except in compliance with the License.
*     You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
*
*
*     THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR
*     CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING
*     WITHOUT LIMITATION ANY IMPLIED WARRANTIES OR CONDITIONS OF TITLE,
*     FITNESS FOR A PARTICULAR PURPOSE, MERCHANTABLITY OR NON-INFRINGEMENT.
*
*     See the Apache 2 License for the specific language governing permissions and
*     limitations under the License.
* */
package io.matthewnelson.topl_core_base

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
            File(sampleFile, BaseConsts.ConfigFileName.GEO_IP).path,
            config.geoIpFile.path
        )
    }
}