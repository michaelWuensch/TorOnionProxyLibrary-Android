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
        addV3AuthenticationPrivateKey("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `nickname over 75 chars throws exception`() {
        addV3AuthenticationPrivateKey("${validOnion}${validOnion}")
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
                addV3AuthenticationPrivateKey("$validNickname${char}p")
                false
            } catch (e: IllegalArgumentException) {
                e.message?.contains("at position ${validNickname.length}") == true
            }
            Assert.assertTrue(wasThrown)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `nickname last character cannot be a period`() {
        addV3AuthenticationPrivateKey("${validNickname}.")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `nickname cannot contain consecutive periods`() {
        addV3AuthenticationPrivateKey("${validNickname}..p")
    }

    @Test
    fun `nickname with period and character after is valid`() {
        val name = "${validNickname}.p"
        val file = addV3AuthenticationPrivateKey(name)
        Assert.assertEquals(file?.name, "${name}.auth_private")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `nickname with spaces throws exception`() {
        addV3AuthenticationPrivateKey("test file name")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `empty onion address throws exception`() {
        addV3AuthenticationPrivateKey(validNickname, "")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `short onion address throws exception`() {
        addV3AuthenticationPrivateKey(validNickname, validOnion.dropLast(1))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `long onion address throws exception`() {
        addV3AuthenticationPrivateKey(validNickname, "${validOnion}asdfk")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `onion address with spaces throws exception`() {
        addV3AuthenticationPrivateKey(validNickname, validOnion.dropLast(5) + " ldo3")
    }

    @Test(expected = IllegalStateException::class)
    fun `file already exists throws exception`() {
        addV3AuthenticationPrivateKey()
        addV3AuthenticationPrivateKey()
    }

    @Test(expected = AssertionError::class)
    fun `check assertion method throws exception if null`() {
        checkAddAuthPrivateAssertions(null)
    }

    @Test
    fun `file creation successful with proper parameters`() {
        val file = addV3AuthenticationPrivateKey()

        checkAddAuthPrivateAssertions(file)
    }

    @Test
    fun `file creation successful with dot_onion appended to onionAddress field`() {
        val file = addV3AuthenticationPrivateKey(validNickname, "${validOnion}.onion")

        checkAddAuthPrivateAssertions(file)
    }

    @Test
    fun `arguments are trimmed prior to being checked for compliance`() {
        var file = addV3AuthenticationPrivateKey(
            "$validNickname ", "$validOnion .onion", "$validClientAuthKey "
        )

        checkAddAuthPrivateAssertions(file)

        file?.delete()
        file = addV3AuthenticationPrivateKey(
            " $validNickname", " $validOnion", " $validClientAuthKey"
        )

        checkAddAuthPrivateAssertions(file)

        file?.delete()
        file = addV3AuthenticationPrivateKey(
            validNickname, " $validOnion .onion  ", validClientAuthKey
        )

        checkAddAuthPrivateAssertions(file)
    }

    private fun addV3AuthenticationPrivateKey(
        name: String = validNickname,
        onionAddress: String = validOnion,
        privateKey: String = validClientAuthKey,
        configFiles: TorConfigFiles = torConfigFiles
    ): File? {
        return OnionAuthUtilities.addV3AuthenticationPrivateKey(name, onionAddress, privateKey, configFiles)
    }

    @Throws(AssertionError::class)
    private fun checkAddAuthPrivateAssertions(file: File?) {
        Assert.assertTrue(file?.exists() == true)
        Assert.assertEquals(file?.parentFile, torConfigFiles.v3AuthPrivateDir)
        Assert.assertEquals(expectedFileContent, file?.readText())
    }
}