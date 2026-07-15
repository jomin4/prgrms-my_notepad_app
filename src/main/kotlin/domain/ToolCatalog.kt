package domain

import domain.tools.AppendNoteTool
import domain.tools.RewriteNoteTool

/** 앱이 제공하는 모든 도구의 카탈로그. 설정 화면과 실행 registry가 여기서 목록을 얻는다. */
object ToolCatalog {
    fun all(): List<AiTool> = listOf(AppendNoteTool(), RewriteNoteTool())
}
