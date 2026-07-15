package data

import com.sun.jna.platform.win32.Crypt32Util
import java.io.File
import java.util.Properties

/**
 * API 키를 Windows DPAPI로 암호화해 저장(현재 사용자만 복호화). 그 외 설정은 평문 properties.
 */
object SecureStore {
    private val dir = File(System.getProperty("user.home"), ".local-ai-note").apply { mkdirs() }
    private val keyFile = File(dir, "nim.key")
    private val cfgFile = File(dir, "config.properties")

    const val DEFAULT_MODEL = "meta/llama-3.1-8b-instruct"

    /** 선택 가능한 NIM 모델(빠른 순). */
    val MODELS = listOf(
        "meta/llama-3.1-8b-instruct",
        "meta/llama-3.3-70b-instruct",
        "meta/llama-3.1-70b-instruct",
    )

    // ── API 키 (DPAPI) ──
    fun hasKey(): Boolean = keyFile.exists()
    fun saveKey(key: String) = keyFile.writeBytes(Crypt32Util.cryptProtectData(key.toByteArray(Charsets.UTF_8)))
    fun loadKey(): String? =
        if (!keyFile.exists()) null
        else try { String(Crypt32Util.cryptUnprotectData(keyFile.readBytes()), Charsets.UTF_8) } catch (e: Exception) { null }
    fun clearKey() { keyFile.delete() }

    // ── 설정(평문) ──
    private fun load(): Properties = Properties().apply { if (cfgFile.exists()) cfgFile.inputStream().use { load(it) } }
    private fun store(p: Properties) = cfgFile.outputStream().use { p.store(it, null) }

    fun saveModel(model: String) = store(load().apply { setProperty("model", model) })
    fun loadModel(): String = load().getProperty("model", DEFAULT_MODEL)

    /** 도구 사용 on/off (커넥터식). 기본값 켜짐. */
    fun isToolEnabled(name: String): Boolean = load().getProperty("tool.$name", "true").toBoolean()
    fun setToolEnabled(name: String, enabled: Boolean) = store(load().apply { setProperty("tool.$name", enabled.toString()) })
}
