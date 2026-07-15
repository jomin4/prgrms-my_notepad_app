package domain.tools

import domain.AiTool
import domain.ToolContext
import org.json.JSONArray
import org.json.JSONObject

/** 텍스트 파라미터 하나짜리 스키마 헬퍼. */
internal fun textSchema(desc: String): JSONObject =
    JSONObject().put("type", "object")
        .put("properties", JSONObject().put("text", JSONObject().put("type", "string").put("description", desc)))
        .put("required", JSONArray().put("text"))

/** 노트 본문 끝에 이어쓰기. */
class AppendNoteTool : AiTool {
    override val name = "append_note"
    override val description = "현재 노트 본문 끝에 텍스트를 이어 붙인다."
    override fun parametersSchema(): JSONObject = textSchema("추가할 텍스트(마크다운 허용)")

    override fun execute(ctx: ToolContext, args: JSONObject): String {
        val note = ctx.targetNote ?: return "대상 노트가 없습니다."
        val text = args.optString("text", "")
        if (text.isBlank()) return "추가할 내용이 없습니다."
        val base = note.body
        val gap = if (base.isEmpty() || base.endsWith("\n")) "" else "\n"
        val start = base.length + gap.length
        note.body = base + gap + text
        note.aiRanges.add(start to note.body.length)
        note.touch()
        return "노트에 이어 썼어요."
    }
}
