package domain

import org.json.JSONArray
import org.json.JSONObject

/** 이번 요청에 실제로 쓸 도구 묶음. (설정에서 켠 도구만 담아 만든다.) */
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
}
