import org.json.JSONArray
import org.json.JSONObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration

/** NIM 대화 메시지 한 건. */
data class ChatMsg(val role: String, val content: String)

/** NVIDIA NIM (OpenAI 호환) 클라이언트. M2.1은 연결 테스트만. */
object NimClient {
    private const val BASE = "https://integrate.api.nvidia.com/v1"

    private val client: HttpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(15))
        .build()

    /** 키가 유효한지 /models 호출로 확인. (블로킹 — IO 스레드에서 호출할 것) */
    fun testConnection(key: String): Result<String> {
        return try {
            val req = HttpRequest.newBuilder(URI.create("$BASE/models"))
                .header("Authorization", "Bearer $key")
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build()
            val resp = client.send(req, HttpResponse.BodyHandlers.ofString())
            when (resp.statusCode()) {
                in 200..299 -> Result.success("연결됨")
                401, 403 -> Result.failure(RuntimeException("키가 올바르지 않습니다"))
                else -> Result.failure(RuntimeException("오류 (HTTP ${resp.statusCode()})"))
            }
        } catch (e: Exception) {
            Result.failure(RuntimeException("네트워크 오류"))
        }
    }

    /** 대화 요청 → 모델의 답변 텍스트. (블로킹 — IO 스레드에서 호출할 것) */
    fun chat(key: String, model: String, messages: List<ChatMsg>): Result<String> {
        return try {
            val payload = JSONObject()
                .put("model", model)
                .put("messages", JSONArray(messages.map { JSONObject().put("role", it.role).put("content", it.content) }))
                .put("temperature", 0.5)
                .put("max_tokens", 1024)
            val req = HttpRequest.newBuilder(URI.create("$BASE/chat/completions"))
                .header("Authorization", "Bearer $key")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(90))
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                .build()
            val resp = client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
            if (resp.statusCode() in 200..299) {
                val content = JSONObject(resp.body())
                    .getJSONArray("choices").getJSONObject(0)
                    .getJSONObject("message").getString("content")
                Result.success(content.trim())
            } else {
                Result.failure(RuntimeException("오류 (HTTP ${resp.statusCode()})"))
            }
        } catch (e: Exception) {
            Result.failure(RuntimeException(e.message ?: "요청 실패"))
        }
    }
}
