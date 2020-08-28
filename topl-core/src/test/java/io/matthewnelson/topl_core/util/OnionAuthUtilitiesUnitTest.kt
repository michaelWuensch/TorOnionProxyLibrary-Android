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
    private fun checkAddAuthPrivateAssertions(file: File?) {
        Assert.assertTrue(file?.exists() == true)
        Assert.assertEquals(file?.parentFile, torConfigFiles.v3AuthPrivateDir)
        Assert.assertEquals(expectedFileContent, file?.readText())
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
        val file1 = addV3ClientAuthenticationPrivateKey(name1)
        val file2 = addV3ClientAuthenticationPrivateKey(name2)
        val file3 = addV3ClientAuthenticationPrivateKey(name3)
        checkAddAuthPrivateAssertions(file1)
        checkAddAuthPrivateAssertions(file2)
        checkAddAuthPrivateAssertions(file3)

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
        val file1 = addV3ClientAuthenticationPrivateKey(name1)
        val file2 = addV3ClientAuthenticationPrivateKey(name2)
        val file3 = addV3ClientAuthenticationPrivateKey(name3)
        checkAddAuthPrivateAssertions(file1)
        checkAddAuthPrivateAssertions(file2)
        checkAddAuthPrivateAssertions(file3)

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