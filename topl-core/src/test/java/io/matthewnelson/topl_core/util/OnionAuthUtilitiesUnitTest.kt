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
*     needed to implement TorOnionProxyLibrary-Android, as listed below:
*
*      - From the `topl-core-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service-base` module:
*          - All Classes/methods/variables
*
*      - From the `topl-service` module:
*          - The TorServiceController class and it's contained classes/methods/variables
*          - The ServiceNotification.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Builder class and it's contained classes/methods/variables
*          - The BackgroundManager.Companion class and it's contained methods/variables
*
*     The following code is excluded from "The Interfaces":
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
* */
package io.matthewnelson.topl_core.util

import io.matthewnelson.topl_core_base.TorConfigFiles
import org.junit.*
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import kotlin.jvm.Throws

@Config(minSdk = 16, maxSdk = 28)
@RunWith(RobolectricTestRunner::class)
class OnionAuthUtilitiesUnitTest {

    @get:Rule
    val testDirectory = TemporaryFolder()

    private lateinit var torConfigFiles: TorConfigFiles
    private val validNickname = "test_file_name"
    private val validOnion = "pqojaffu4an36caasmwnkg3n5lxvf4h4nbnvsloqdloqqay5nbvrwjyd"
    private val validClientAuthKey = "KCTSNJBCYVJ6GK3WMVBVPP6LAWNFAIHN2VHCLZ75BDQA2SZGDBOQ"
    private val expectedFileContent = "${validOnion}:descriptor:x25519:$validClientAuthKey"

    @Before
    fun setup() {
        testDirectory.create()
        val installDir = File("/usr/bin")
        val configDir = testDirectory.newFolder("configDir")
        torConfigFiles = TorConfigFiles.Builder(installDir, configDir)
            .torExecutable(File(installDir, "tor"))
            .build()
    }

    @After
    fun tearDown() {
        testDirectory.delete()
    }

    /////////////////////////////////////
    /// addV3AuthenticationPrivateKey ///
    /////////////////////////////////////

    @Test(expected = IllegalArgumentException::class)
    fun `empty nickname throws exception`() {
        addV3ClientAuthenticationPrivateKey("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `nickname over 75 chars throws exception`() {
        addV3ClientAuthenticationPrivateKey("${validOnion}${validOnion}")
    }

    @Test
    fun `nickname with special chars throws exceptions`() {
        val unacceptableCharacters = charArrayOf(
            '`', '~', '!', '@', '#', '$',
            '%', '^', '&', '*', '(', ')',
            '-', '=', '+', '{', '}', '[',
            ']', '|', ';', ':', '\'', '"',
            ',', '<', '>', '?', '/', '\\',
            ' '
        )

        unacceptableCharacters.forEach { char ->
            val wasThrown = try {
                addV3ClientAuthenticationPrivateKey("$validNickname${char}p")
                false
            } catch (e: IllegalArgumentException) {
                e.message?.contains("at position ${validNickname.length}") == true
            }
            Assert.assertTrue(wasThrown)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `nickname last character cannot be a period`() {
        addV3ClientAuthenticationPrivateKey("${validNickname}.")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `nickname cannot contain consecutive periods`() {
        addV3ClientAuthenticationPrivateKey("${validNickname}..p")
    }

    @Test
    fun `nickname with period and character after is valid`() {
        val name = "${validNickname}.p"
        val file = addV3ClientAuthenticationPrivateKey(name)
        Assert.assertEquals(file?.name, "${name}.auth_private")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `nickname with spaces throws exception`() {
        addV3ClientAuthenticationPrivateKey("test file name")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `empty onion address throws exception`() {
        addV3ClientAuthenticationPrivateKey(validNickname, "")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `short onion address throws exception`() {
        addV3ClientAuthenticationPrivateKey(validNickname, validOnion.dropLast(1))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `long onion address throws exception`() {
        addV3ClientAuthenticationPrivateKey(validNickname, "${validOnion}asdfk")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `onion address with spaces throws exception`() {
        addV3ClientAuthenticationPrivateKey(validNickname, validOnion.dropLast(5) + " ldo3")
    }

    @Test(expected = IllegalStateException::class)
    fun `file already exists throws exception`() {
        addV3ClientAuthenticationPrivateKey()
        addV3ClientAuthenticationPrivateKey()
    }

    @Test(expected = IllegalStateException::class)
    fun `file with same content already exists throws exception`() {
        addV3ClientAuthenticationPrivateKey()
        addV3ClientAuthenticationPrivateKey(name = validNickname + "_diff")
    }

    @Test(expected = IllegalStateException::class)
    fun `file with same content already exists throws exception with onion appendage`() {
        addV3ClientAuthenticationPrivateKey()
        addV3ClientAuthenticationPrivateKey(name = validNickname + "_diff", "$validOnion.onion")
    }

    @Test(expected = AssertionError::class)
    fun `check assertion method throws exception if null`() {
        checkAddAuthPrivateAssertions(null)
    }

    @Test
    fun `file creation successful with proper parameters`() {
        val file = addV3ClientAuthenticationPrivateKey()

        checkAddAuthPrivateAssertions(file)
    }

    @Test
    fun `file creation successful with dot_onion appended to onionAddress field`() {
        val file = addV3ClientAuthenticationPrivateKey(validNickname, "${validOnion}.onion")

        checkAddAuthPrivateAssertions(file)
    }

    @Test
    fun `arguments are trimmed prior to being checked for compliance`() {
        var file = addV3ClientAuthenticationPrivateKey(
            "$validNickname ", "$validOnion .onion", "$validClientAuthKey "
        )

        checkAddAuthPrivateAssertions(file)

        file?.delete()
        file = addV3ClientAuthenticationPrivateKey(
            " $validNickname", " $validOnion", " $validClientAuthKey"
        )

        checkAddAuthPrivateAssertions(file)

        file?.delete()
        file = addV3ClientAuthenticationPrivateKey(
            validNickname, " $validOnion .onion  ", validClientAuthKey
        )

        checkAddAuthPrivateAssertions(file)
    }

    private fun addV3ClientAuthenticationPrivateKey(
        name: String = validNickname,
        onionAddress: String = validOnion,
        privateKey: String = validClientAuthKey,
        configFiles: TorConfigFiles = torConfigFiles
    ): File? {
        return OnionAuthUtilities.addV3ClientAuthenticationPrivateKey(name, onionAddress, privateKey, configFiles)
    }

    @Throws(AssertionError::class)
    private fun checkAddAuthPrivateAssertions(file: File?, expectedContent: String = expectedFileContent) {
        Assert.assertTrue(file?.exists() == true)
        Assert.assertEquals(file?.parentFile, torConfigFiles.v3AuthPrivateDir)
        Assert.assertEquals(expectedContent, file?.readText())
    }


    /////////////////
    /// Retrieval ///
    /////////////////
    @Test
    fun `getAllFiles returns null when none exist`() {
        val files = OnionAuthUtilities.getAllFiles(torConfigFiles)
        Assert.assertNull(files)
    }

    @Test
    fun `getAllFiles returns list of files`() {
        val name1 = "name1"
        val name2 = "name2"
        val name3 = "name3"
        val file1 = addV3ClientAuthenticationPrivateKey(name1, privateKey = validClientAuthKey.dropLast(1) + "A")
        val file2 = addV3ClientAuthenticationPrivateKey(name2, privateKey = validClientAuthKey.dropLast(1) + "B")
        val file3 = addV3ClientAuthenticationPrivateKey(name3, privateKey = validClientAuthKey.dropLast(1) + "C")
        checkAddAuthPrivateAssertions(file1, expectedFileContent.dropLast(1) + "A")
        checkAddAuthPrivateAssertions(file2, expectedFileContent.dropLast(1) + "B")
        checkAddAuthPrivateAssertions(file3, expectedFileContent.dropLast(1) + "C")

        val expectedArray = arrayOf(file1, file2, file3)

        val resultArray = OnionAuthUtilities.getAllFiles(torConfigFiles)
        Assert.assertNotNull(resultArray)

        expectedArray.forEach { file ->
            Assert.assertTrue(resultArray?.contains(file) == true)
        }
    }

    @Test
    fun `getFileByNickname returns file if exists`() {
        val file = addV3ClientAuthenticationPrivateKey()
        checkAddAuthPrivateAssertions(file)

        val fileResult = OnionAuthUtilities.getFileByNickname(validNickname, torConfigFiles)

        Assert.assertEquals(file, fileResult)
    }

    @Test
    fun `getFileByNickname returns null if does not exist`() {
        val file = addV3ClientAuthenticationPrivateKey()
        checkAddAuthPrivateAssertions(file)

        var fileResult = OnionAuthUtilities.getFileByNickname("${validNickname}_does_not_exist", torConfigFiles)
        Assert.assertNotEquals(file, fileResult)
        Assert.assertNull(fileResult)

        fileResult = OnionAuthUtilities.getFileByNickname(validNickname, torConfigFiles)
        Assert.assertEquals(file, fileResult)
    }

    @Test
    fun `getAllFileNicknames returns null if no files exist`() {
        val nicknames = OnionAuthUtilities.getAllFileNicknames(torConfigFiles)
        Assert.assertNull(nicknames)
    }

    @Test
    fun `getAllFileNicknames returns list of nicknames only without file extension`() {
        val name1 = "name1"
        val name2 = "name2"
        val name3 = "name3"
        val file1 = addV3ClientAuthenticationPrivateKey(name1, privateKey = validClientAuthKey.dropLast(1) + "A")
        val file2 = addV3ClientAuthenticationPrivateKey(name2, privateKey = validClientAuthKey.dropLast(1) + "B")
        val file3 = addV3ClientAuthenticationPrivateKey(name3, privateKey = validClientAuthKey.dropLast(1) + "C")
        checkAddAuthPrivateAssertions(file1, expectedFileContent.dropLast(1) + "A")
        checkAddAuthPrivateAssertions(file2, expectedFileContent.dropLast(1) + "B")
        checkAddAuthPrivateAssertions(file3, expectedFileContent.dropLast(1) + "C")

        val expectedNicknames = arrayOf(name1, name2, name3)

        val nicknames = OnionAuthUtilities.getAllFileNicknames(torConfigFiles)
        Assert.assertEquals(3, nicknames?.size)

        for (nickname in nicknames!!) {
            Assert.assertTrue(expectedNicknames.contains(nickname))
            Assert.assertFalse(expectedNicknames.contains(".auth_private"))
            Assert.assertFalse(nickname.contains(".auth_private"))
        }
    }


    ////////////////
    /// Deletion ///
    ////////////////
    @Test
    fun `deleteFile returns null if file does not exist`() {
        val result = OnionAuthUtilities.deleteFile(validNickname, torConfigFiles)
        Assert.assertNull(result)
    }

    @Test
    fun `deleteFile returns null if file is directory`() {
        val result = OnionAuthUtilities.deleteFile(torConfigFiles.v3AuthPrivateDir, torConfigFiles)
        Assert.assertNull(result)
    }

    @Test
    fun `deleteFile by sending file works properly`() {
        val file = addV3ClientAuthenticationPrivateKey()
        Assert.assertNotNull(file)

        val result = OnionAuthUtilities.deleteFile(file!!, torConfigFiles)
        Assert.assertTrue(result == true)
    }

    @Test
    fun `deleteFile by sending nickname with extension works properly`() {
        val file = addV3ClientAuthenticationPrivateKey()
        Assert.assertNotNull(file)

        val result = OnionAuthUtilities.deleteFile(file!!.name, torConfigFiles)
        Assert.assertTrue(result == true)
    }

    @Test
    fun `deleteFile by sending nickname without extension works properly`() {
        val file = addV3ClientAuthenticationPrivateKey()
        Assert.assertNotNull(file)

        val result = OnionAuthUtilities.deleteFile(file!!.nameWithoutExtension, torConfigFiles)
        Assert.assertTrue(result == true)
    }
}