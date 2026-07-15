import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

/** NVIDIA NIM (OpenAI 호환) 클라이언트. M2.1은 연결 테스트만. */
object NimClient {
    private const val BASE = "https://integrate.api.nvidia.com/v1"

    private val client: HttpClient = HttpClient.newBuilder()
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
}
