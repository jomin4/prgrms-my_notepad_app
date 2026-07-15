package ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import ai.ChatEngine
import ai.ChatMsg
import data.SecureStore
import domain.NoteItem
import domain.ToolRegistry
import kotlinx.coroutines.launch
import ui.theme.Ink

/** 패널에 표시할 메시지. tool != null이면 도구 실행 칩을 함께 보여준다. */
private data class UiMsg(val role: String, val content: String, val tool: String? = null)

@Composable
fun AiPanel(c: Ink, targetNote: NoteItem?, onEdit: (NoteItem) -> Unit, onOpenSettings: () -> Unit) {
    val key = remember { SecureStore.loadKey() }
    val model = remember { SecureStore.loadModel() }
    val engine = remember { ChatEngine(ToolRegistry.default()) }
    val messages = remember { mutableStateListOf<UiMsg>() }
    var input by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, sending) {
        if (messages.size > 0) listState.animateScrollToItem(messages.size)
    }

    Column(Modifier.width(320.dp).fillMaxHeight().background(c.soft)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(24.dp).background(c.primarySoft, RoundedCornerShape(7.dp)), contentAlignment = Alignment.Center) {
                BasicText("✦", style = TextStyle(color = c.primary, fontSize = 13.sp))
            }
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                BasicText("AI", style = TextStyle(color = c.ink, fontSize = 13.sp, fontWeight = FontWeight.Medium))
                BasicText(model.substringAfterLast('/'), style = TextStyle(color = c.faint, fontFamily = FontFamily.Monospace, fontSize = 10.sp))
            }
        }
        // 대상 노트 명시 — AI가 무엇을 보고/쓰는지 항상 드러낸다.
        Row(Modifier.fillMaxWidth().background(c.inset).padding(horizontal = 13.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            BasicText("대상  ", style = TextStyle(color = c.faint, fontSize = 11.sp))
            BasicText(
                targetNote?.title?.ifBlank { "제목 없음" } ?: "노트 없음",
                style = TextStyle(color = c.body, fontSize = 11.5.sp, fontWeight = FontWeight.Medium),
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
        }
        Box(Modifier.height(0.5.dp).fillMaxWidth().background(c.line))

        if (key == null) {
            Column(Modifier.fillMaxWidth().weight(1f).padding(18.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                BasicText("API 키를 등록하면 AI와 대화할 수 있어요.", style = TextStyle(color = c.faint, fontSize = 13.sp, lineHeight = 20.sp))
                Spacer(Modifier.height(12.dp))
                Box(Modifier.background(c.primary, RoundedCornerShape(8.dp)).clickable { onOpenSettings() }.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    BasicText("설정 열기", style = TextStyle(color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Medium))
                }
            }
        } else {
            LazyColumn(Modifier.weight(1f).fillMaxWidth().padding(horizontal = 13.dp), state = listState) {
                item { Spacer(Modifier.height(10.dp)) }
                items(messages.size) { i ->
                    Bubble(c, messages[i])
                    Spacer(Modifier.height(10.dp))
                }
                if (sending) item { BasicText("생각 중…", style = TextStyle(color = c.faint, fontSize = 12.sp)) }
            }
            Box(Modifier.padding(11.dp)) {
                Row(
                    Modifier.fillMaxWidth().background(c.surface, RoundedCornerShape(10.dp)).border(0.5.dp, c.line2, RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.weight(1f)) {
                        if (input.isEmpty()) BasicText("물어보기 · 노트에 써달라고 해보세요", style = TextStyle(color = c.faint, fontSize = 12.5.sp))
                        BasicTextField(input, { input = it }, textStyle = TextStyle(color = c.ink, fontSize = 13.sp), cursorBrush = SolidColor(c.primary), modifier = Modifier.fillMaxWidth())
                    }
                    Spacer(Modifier.width(8.dp))
                    val canSend = input.isNotBlank() && !sending
                    Box(
                        Modifier.size(28.dp).background(if (canSend) c.primary else c.line2, RoundedCornerShape(7.dp)).clickable(enabled = canSend) {
                            val text = input.trim()
                            input = ""
                            val note = targetNote
                            messages.add(UiMsg("user", text))
                            sending = true
                            scope.launch {
                                val history = messages.map { ChatMsg(it.role, it.content) }
                                val outcome = engine.send(key, model, note, history)
                                sending = false
                                when (outcome) {
                                    is ChatEngine.Outcome.Reply -> messages.add(UiMsg("assistant", outcome.text))
                                    is ChatEngine.Outcome.Applied -> {
                                        if (note != null) onEdit(note)
                                        messages.add(UiMsg("assistant", outcome.text, tool = outcome.toolLabel))
                                    }
                                    is ChatEngine.Outcome.Failed -> messages.add(UiMsg("assistant", "⚠ ${outcome.message}"))
                                }
                            }
                        },
                        contentAlignment = Alignment.Center,
                    ) {
                        BasicText("↑", style = TextStyle(color = Color.White, fontSize = 14.sp))
                    }
                }
            }
        }
    }
}

@Composable
private fun Bubble(c: Ink, m: UiMsg) {
    val user = m.role == "user"
    Column(Modifier.fillMaxWidth(), horizontalAlignment = if (user) Alignment.End else Alignment.Start) {
        Box(
            Modifier.widthIn(max = 244.dp).background(if (user) c.primary else Color.Transparent, RoundedCornerShape(12.dp)).padding(horizontal = if (user) 11.dp else 0.dp, vertical = if (user) 8.dp else 0.dp),
        ) {
            BasicText(m.content, style = TextStyle(color = if (user) Color.White else c.ink, fontSize = 12.5.sp, lineHeight = 18.sp))
        }
        if (m.tool != null) {
            Spacer(Modifier.height(6.dp))
            Row(
                Modifier.background(c.localSoft, RoundedCornerShape(7.dp)).border(0.5.dp, c.local, RoundedCornerShape(7.dp)).padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicText("✎ ${m.tool}", style = TextStyle(color = c.localInk, fontSize = 10.5.sp, fontWeight = FontWeight.Medium))
            }
        }
    }
}
