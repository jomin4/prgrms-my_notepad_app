package domain

import org.json.JSONObject

/**
 * LLM에게 노출할 도구 하나. "무슨 도구가 있고, 무엇을 하고, 어떻게 실행되는지"를 한 곳에 캡슐화한다.
 * (기존엔 스키마 JSON과 실행 로직이 흩어져 있어 도구 선택이 불투명했다.)
 */
interface AiTool {
    val name: String
    val description: String

    /** function-calling 파라미터 스키마(JSON). */
    fun parametersSchema(): JSONObject

    /** 도구 실행: 맥락(대상 노트)에 작용. 사용자에게 보일 짧은 결과 문구 반환. */
    fun execute(ctx: ToolContext, args: JSONObject): String
}

/** 도구가 작동하는 맥락. 지금은 "AI가 보고/쓰는 대상 노트". */
class ToolContext(val targetNote: NoteItem?)
