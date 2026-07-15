package domain.tools

import domain.AiTool
import domain.ToolContext
import org.json.JSONObject

/** 노트 본문 전체 교체(요약·정리·개요 작성 등). */
class RewriteNoteTool : AiTool {
    override val name = "rewrite_note"
    override val description = "현재 노트 본문 전체를 새 텍스트로 교체한다(요약·정리·개요 작성 등)."
    override fun parametersSchema(): JSONObject = textSchema("새 본문 전체")

    override fun execute(ctx: ToolContext, args: JSONObject): String {
        val note = ctx.targetNote ?: return "대상 노트가 없습니다."
        val text = args.optString("text", "")
        if (text.isBlank()) return "적용할 내용이 없습니다."
        note.body = text
        note.aiRanges.clear()
        note.aiRanges.add(0 to text.length)
        note.touch()
        return "노트를 새로 정리했어요."
    }
}
