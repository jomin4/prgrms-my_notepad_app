package ai

import domain.NoteItem

/** 시스템 프롬프트 구성. AI가 보고/쓰는 "대상 노트"를 여기서 명시적으로 주입한다. */
object Prompt {
    fun system(targetNote: NoteItem?): String {
        val base =
            "당신은 사용자의 노트 작성을 돕는 조수입니다. 한국어로 답하세요. " +
                "사용자가 노트에 쓰거나 정리·요약·추가를 요청하면 반드시 도구(append_note 또는 rewrite_note)를 호출해 노트를 직접 수정하세요. " +
                "단순 질문에는 도구 없이 답하세요."
        val ctx = targetNote?.let { "\n\n[대상 노트]\n제목: ${it.title}\n본문:\n${it.body}" } ?: "\n\n(대상 노트 없음)"
        return base + ctx
    }
}
