import com.sun.jna.platform.win32.Crypt32Util
import java.io.File
import java.util.Properties

/**
 * API 키를 Windows DPAPI로 암호화해 기기에 저장한다(현재 사용자 계정만 복호화 가능).
 * 모델명 등 비민감 설정은 평문 properties에 저장.
 */
object SecureStore {
    private val dir = File(System.getProperty("user.home"), ".local-ai-note").apply { mkdirs() }
    private val keyFile = File(dir, "nim.key")
    private val cfgFile = File(dir, "config.properties")

    const val DEFAULT_MODEL = "meta/llama-3.3-70b-instruct"

    fun hasKey(): Boolean = keyFile.exists()

    fun saveKey(key: String) {
        val encrypted = Crypt32Util.cryptProtectData(key.toByteArray(Charsets.UTF_8))
        keyFile.writeBytes(encrypted)
    }

    fun loadKey(): String? {
        if (!keyFile.exists()) return null
        return try {
            String(Crypt32Util.cryptUnprotectData(keyFile.readBytes()), Charsets.UTF_8)
        } catch (e: Exception) {
            null
        }
    }

    fun clearKey() { keyFile.delete() }

    fun saveModel(model: String) {
        val p = Properties()
        if (cfgFile.exists()) cfgFile.inputStream().use { p.load(it) }
        p.setProperty("model", model)
        cfgFile.outputStream().use { p.store(it, null) }
    }

    fun loadModel(): String {
        val p = Properties()
        if (cfgFile.exists()) cfgFile.inputStream().use { p.load(it) }
        return p.getProperty("model", DEFAULT_MODEL)
    }
}
