package com.msopentech.toronionproxy

import com.msopentech.thali.toronionproxy.TorConfig
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File

class TorConfigUnitTest {

    private val sampleFile = File("sample")
    private lateinit var torConfigBuilder: TorConfig.Builder

    @Before
    fun setup() {
        torConfigBuilder = TorConfig.Builder(sampleFile, sampleFile)
    }

    @Test
    fun defaultDataDir() {
        val config = torConfigBuilder.build()
        Assert.assertEquals(
            File(sampleFile, "lib/tor").path, config.dataDir.path
        )
    }

    @Test
    fun defaultCookie() {
        val config = torConfigBuilder.build()
        Assert.assertEquals(
            File(sampleFile, "lib/tor/control_auth_cookie").path, config.cookieAuthFile.path
        )
    }

    @Test
    fun defaultHostname() {
        val config = torConfigBuilder.build()
        Assert.assertEquals(
            File(sampleFile, "lib/tor/hostname").path, config.hostnameFile.path
        )
    }

    @Test
    fun libraryPathRelativeToExecutable() {
        val config = torConfigBuilder.torExecutable(File(sampleFile, "exedir/tor.real")).build()
        Assert.assertEquals(
            File(sampleFile, "exedir").path,
            config.libraryPath.path
        )
    }

    @Test
    fun defaultCookieWithDataDir() {
        val dataDir = File("sample/datadir")
        val config = torConfigBuilder.dataDir(dataDir).build()
        Assert.assertEquals(
            File(dataDir, "control_auth_cookie").path,
            config.cookieAuthFile.path
        )
    }

    @Test
    fun geoip() {
        val config = torConfigBuilder.build()
        Assert.assertEquals(
            File(sampleFile, TorConfig.GEO_IP_NAME).path,
            config.geoIpFile.path
        )
    }
}