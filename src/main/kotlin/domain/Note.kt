package domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** 노트 한 건(화면 편집용 상태 보유). id는 DB 행 id. aiRanges = AI가 쓴 범위(ai-mark). */
class NoteItem(val id: Long, title: String, body: String, updatedAt: Long) {
    var title by mutableStateOf(title)
    var body by mutableStateOf(body)
    var updatedAt by mutableStateOf(updatedAt)
    val aiRanges = mutableStateListOf<Pair<Int, Int>>()
    fun touch() { updatedAt = System.currentTimeMillis() }
}
