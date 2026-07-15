import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 메모리 상의 노트 한 건. (M1.3에서 로컬 DB로 저장 예정) */
class Note(title: String, body: String) {
    val id: Long = nextId()
    var title by mutableStateOf(title)
    var body by mutableStateOf(body)
    var updatedAt by mutableStateOf(System.currentTimeMillis())
    fun touch() { updatedAt = System.currentTimeMillis() }
    companion object {
        private var counter = 0L
        private fun nextId(): Long = ++counter
    }
}

@Composable
fun App() {
    val c = if (isSystemInDarkTheme()) DarkInk else LightInk
    val notes = remember {
        mutableStateListOf(
            Note("환영합니다", "로컬 AI 노트에 오신 걸 환영해요.\n왼쪽에서 새 노트를 만들어 보세요."),
            Note("할 일", "- 우유 사기\n- 저녁 운동"),
        )
    }
    var selectedId by remember { mutableStateOf(notes.firstOrNull()?.id) }
    var query by remember { mutableStateOf("") }

    val selected = notes.firstOrNull { it.id == selectedId }
    val filtered = notes.filter {
        query.isBlank() || it.title.contains(query, true) || it.body.contains(query, true)
    }

    Row(Modifier.fillMaxSize().background(c.canvas)) {
        Sidebar(
            c = c, notes = filtered, selectedId = selectedId, query = query,
            onQuery = { query = it },
            onSelect = { selectedId = it },
            onNew = {
                val n = Note("", "")
                notes.add(0, n)
                selectedId = n.id
                query = ""
            },
        )
        Box(Modifier.weight(1f).fillMaxHeight().background(c.surface)) {
            if (selected != null) {
                Editor(c, selected, onDelete = {
                    notes.remove(selected)
                    selectedId = notes.firstOrNull()?.id
                })
            } else {
                BasicText(
                    "노트를 선택하거나 새로 만드세요",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(color = c.faint, fontSize = 14.sp),
                )
            }
        }
    }
}

@Composable
private fun Sidebar(
    c: Ink, notes: List<Note>, selectedId: Long?, query: String,
    onQuery: (String) -> Unit, onSelect: (Long) -> Unit, onNew: () -> Unit,
) {
    Column(Modifier.width(240.dp).fillMaxHeight().background(c.soft)) {
        Column(Modifier.padding(12.dp)) {
            Box(
                Modifier.fillMaxWidth()
                    .background(c.inset, RoundedCornerShape(8.dp))
                    .border(0.5.dp, c.line, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
            ) {
                if (query.isEmpty()) {
                    BasicText("검색", style = TextStyle(color = c.faint, fontSize = 13.sp))
                }
                BasicTextField(
                    value = query, onValueChange = onQuery,
                    textStyle = TextStyle(color = c.ink, fontSize = 13.sp),
                    cursorBrush = SolidColor(c.primary),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier.fillMaxWidth()
                    .background(c.primary, RoundedCornerShape(8.dp))
                    .clickable { onNew() }
                    .padding(vertical = 9.dp),
                contentAlignment = Alignment.Center,
            ) {
                BasicText("+ 새 노트", style = TextStyle(color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium))
            }
        }
        LazyColumn(Modifier.weight(1f)) {
            items(notes, key = { it.id }) { note ->
                val sel = note.id == selectedId
                Row(
                    Modifier.fillMaxWidth()
                        .clickable { onSelect(note.id) }
                        .background(if (sel) c.surface else Color.Transparent),
                ) {
                    Box(Modifier.width(2.dp).fillMaxHeight().background(if (sel) c.line2 else Color.Transparent))
                    Column(Modifier.padding(horizontal = 12.dp, vertical = 9.dp)) {
                        BasicText(
                            note.title.ifBlank { "제목 없음" },
                            style = TextStyle(color = c.ink, fontSize = 14.sp, fontWeight = if (sel) FontWeight.Medium else FontWeight.Normal),
                            maxLines = 1, overflow = TextOverflow.Ellipsis,
                        )
                        val sub = note.body.lineSequence().firstOrNull { it.isNotBlank() }.orEmpty()
                        if (sub.isNotBlank()) {
                            BasicText(
                                sub, style = TextStyle(color = c.faint, fontSize = 12.sp),
                                maxLines = 1, overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Editor(c: Ink, note: Note, onDelete: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.weight(1f))
            Box(Modifier.clickable { onDelete() }.padding(6.dp)) {
                BasicText("삭제", style = TextStyle(color = c.muted, fontSize = 13.sp))
            }
        }
        Box(Modifier.height(0.5.dp).fillMaxWidth().background(c.line))
        Column(Modifier.fillMaxWidth().weight(1f).padding(horizontal = 30.dp, vertical = 22.dp)) {
            Box(Modifier.fillMaxWidth()) {
                if (note.title.isEmpty()) {
                    BasicText("제목", style = TextStyle(color = c.faint, fontFamily = FontFamily.Serif, fontSize = 26.sp, fontWeight = FontWeight.Medium))
                }
                BasicTextField(
                    value = note.title, onValueChange = { note.title = it; note.touch() },
                    textStyle = TextStyle(color = c.ink, fontFamily = FontFamily.Serif, fontSize = 26.sp, fontWeight = FontWeight.Medium),
                    cursorBrush = SolidColor(c.primary), modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth().weight(1f)) {
                if (note.body.isEmpty()) {
                    BasicText("여기에 노트를 작성하세요…", style = TextStyle(color = c.faint, fontFamily = FontFamily.Serif, fontSize = 16.sp))
                }
                BasicTextField(
                    value = note.body, onValueChange = { note.body = it; note.touch() },
                    textStyle = TextStyle(color = c.ink, fontFamily = FontFamily.Serif, fontSize = 16.sp, lineHeight = 26.sp),
                    cursorBrush = SolidColor(c.primary), modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
