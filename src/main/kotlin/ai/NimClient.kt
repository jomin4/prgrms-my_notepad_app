package ai

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

/** 대화 결과: 일반 답변(Text) 또는 도구 호출(ToolCalls). */
sealed interface ChatResult {
    data class Text(val content: String) : ChatResult
    data class ToolCalls(val calls: List<ToolCall>) : ChatResult
}

/** 모델이 호출한 도구 1건. */
data class ToolCall(val name: String, val args: JSONObject)

/** NVIDIA NIM(OpenAI 호환) 저수준 통신 계층. 오케스트레이션은 ChatEngine이 담당. */
object NimClient {
    private const val BASE = "https://integrate.api.nvidia.com/v1"

    private val client: HttpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(15))
        .build()

    /** 키가 유효한지 /models 로 확인. (블로킹) */
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

    /** 도구를 포함해 대화. 모델이 도구를 호출하면 ToolCalls, 아니면 Text. (블로킹) */
    fun chatTools(key: String, model: String, messages: List<ChatMsg>, tools: JSONArray, toolChoice: Any = "auto"): Result<ChatResult> {
        return try {
            val payload = JSONObject()
                .put("model", model)
                .put("messages", JSONArray(messages.map { JSONObject().put("role", it.role).put("content", it.content) }))
                .put("tools", tools)
                .put("tool_choice", toolChoice)
                .put("temperature", 0.4)
                .put("max_tokens", 1024)
            val req = HttpRequest.newBuilder(URI.create("$BASE/chat/completions"))
                .header("Authorization", "Bearer $key")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString(), StandardCharsets.UTF_8))
                .build()
            val resp = client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
            if (resp.statusCode() !in 200..299) {
                return Result.failure(RuntimeException("오류 (HTTP ${resp.statusCode()})"))
            }
            val msg = JSONObject(resp.body()).getJSONArray("choices").getJSONObject(0).getJSONObject("message")
            val toolCalls = msg.optJSONArray("tool_calls")
            if (toolCalls != null && toolCalls.length() > 0) {
                val calls = (0 until toolCalls.length()).map { i ->
                    val fn = toolCalls.getJSONObject(i).getJSONObject("function")
                    val args = try { JSONObject(fn.optString("arguments", "{}")) } catch (e: Exception) { JSONObject() }
                    ToolCall(fn.getString("name"), args)
                }
                Result.success(ChatResult.ToolCalls(calls))
            } else {
                Result.success(ChatResult.Text(msg.optString("content", "").trim()))
            }
        } catch (e: Exception) {
            Result.failure(RuntimeException(e.message ?: "요청 실패"))
        }
    }
}
