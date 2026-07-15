package domain

import domain.tools.AppendNoteTool
import domain.tools.RewriteNoteTool
import org.json.JSONArray
import org.json.JSONObject

/** 앱이 제공하는 도구 목록을 한 곳에서 정의·조회·실행. (도구가 늘면 여기만 보면 된다.) */
class ToolRegistry(private val tools: List<AiTool>) {

    /** NIM에 보낼 function-calling tools 배열. */
    fun toJson(): JSONArray {
        val arr = JSONArray()
        tools.forEach { t ->
            arr.put(
                JSONObject().put("type", "function").put(
                    "function",
                    JSONObject().put("name", t.name).put("description", t.description).put("parameters", t.parametersSchema()),
                ),
            )
        }
        return arr
    }

    /** 이름으로 도구를 찾아 실행. 있으면 결과 문구, 없으면 null. */
    fun execute(name: String, ctx: ToolContext, args: JSONObject): String? =
        tools.firstOrNull { it.name == name }?.execute(ctx, args)

    companion object {
        /** 기본 도구 묶음(1차: 텍스트). 새 도구는 여기 추가. */
        fun default(): ToolRegistry = ToolRegistry(listOf(AppendNoteTool(), RewriteNoteTool()))
    }
}
