package ai

import domain.NoteItem
import domain.ToolContext
import domain.ToolRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 대화 오케스트레이션(UI와 분리): 시스템 프롬프트 + 히스토리 구성 → NIM 호출 →
 * 도구 호출이면 registry로 실행 → 결과를 Outcome으로 반환.
 * registry는 매 요청 시 "켜진 도구"로 구성해 넘긴다.
 */
class ChatEngine {

    sealed interface Outcome {
        data class Reply(val text: String) : Outcome
        data class Applied(val text: String, val toolLabel: String) : Outcome
        data class Failed(val message: String) : Outcome
    }

    suspend fun send(
        key: String,
        model: String,
        targetNote: NoteItem?,
        history: List<ChatMsg>,
        registry: ToolRegistry,
        toolChoice: Any = "auto",
    ): Outcome = withContext(Dispatchers.IO) {
        val messages = buildList {
            add(ChatMsg("system", Prompt.system(targetNote)))
            addAll(history)
        }
        NimClient.chatTools(key, model, messages, registry.toJson(), toolChoice).fold(
            { res ->
                when (res) {
                    is ChatResult.Text -> Outcome.Reply(res.content.ifBlank { "(빈 응답)" })
                    is ChatResult.ToolCalls -> {
                        val ctx = ToolContext(targetNote)
                        var applied = 0
                        var last = ""
                        res.calls.forEach { call ->
                            registry.execute(call.name, ctx, call.args)?.let { applied++; last = it }
                        }
                        if (applied > 0 && targetNote != null) {
                            Outcome.Applied(last.ifBlank { "노트에 반영했어요." }, "노트에 작성함")
                        } else {
                            Outcome.Reply(last.ifBlank { "먼저 노트를 선택해 주세요." })
                        }
                    }
                }
            },
            { Outcome.Failed(it.message ?: "요청 실패") },
        )
    }
}
