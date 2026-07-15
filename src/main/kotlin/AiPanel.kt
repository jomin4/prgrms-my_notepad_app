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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AiPanel(c: Ink, note: NoteItem?, onOpenSettings: () -> Unit) {
    val key = remember { SecureStore.loadKey() }
    val model = remember { SecureStore.loadModel() }
    val messages = remember { mutableStateListOf<ChatMsg>() }
    var input by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, sending) {
        val target = messages.size
        if (target > 0) listState.animateScrollToItem(target)
    }

    Column(Modifier.width(320.dp).fillMaxHeight().background(c.soft)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(24.dp).background(c.primarySoft, RoundedCornerShape(7.dp)), contentAlignment = Alignment.Center) {
                BasicText("✦", style = TextStyle(color = c.primary, fontSize = 13.sp))
            }
            Spacer(Modifier.width(8.dp))
            Column {
                BasicText("AI", style = TextStyle(color = c.ink, fontSize = 13.sp, fontWeight = FontWeight.Medium))
                BasicText(model.substringAfterLast('/'), style = TextStyle(color = c.faint, fontFamily = FontFamily.Monospace, fontSize = 10.sp))
            }
        }
        Box(Modifier.height(0.5.dp).fillMaxWidth().background(c.line))

        if (key == null) {
            Column(
                Modifier.fillMaxWidth().weight(1f).padding(18.dp),
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BasicText("API 키를 등록하면 AI와 대화할 수 있어요.", style = TextStyle(color = c.faint, fontSize = 13.sp, lineHeight = 20.sp))
                Spacer(Modifier.height(12.dp))
                Box(
                    Modifier.background(c.primary, RoundedCornerShape(8.dp)).clickable { onOpenSettings() }.padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
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
                if (sending) {
                    item { BasicText("생각 중…", style = TextStyle(color = c.faint, fontSize = 12.sp)) }
                }
            }
            Box(Modifier.padding(11.dp)) {
                Row(
                    Modifier.fillMaxWidth().background(c.surface, RoundedCornerShape(10.dp)).border(0.5.dp, c.line2, RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.weight(1f)) {
                        if (input.isEmpty()) BasicText("물어보기", style = TextStyle(color = c.faint, fontSize = 13.sp))
                        BasicTextField(
                            value = input, onValueChange = { input = it },
                            textStyle = TextStyle(color = c.ink, fontSize = 13.sp),
                            cursorBrush = SolidColor(c.primary), modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    val canSend = input.isNotBlank() && !sending
                    Box(
                        Modifier.size(28.dp).background(if (canSend) c.primary else c.line2, RoundedCornerShape(7.dp))
                            .clickable(enabled = canSend) {
                                val text = input.trim()
                                input = ""
                                messages.add(ChatMsg("user", text))
                                sending = true
                                scope.launch {
                                    val convo = buildList {
                                        add(ChatMsg("system", systemPrompt(note)))
                                        addAll(messages)
                                    }
                                    val r = withContext(Dispatchers.IO) { NimClient.chat(key, model, convo) }
                                    sending = false
                                    messages.add(ChatMsg("assistant", r.getOrElse { "⚠ ${it.message}" }))
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

private fun systemPrompt(note: NoteItem?): String =
    "당신은 사용자의 노트 작성을 돕는 조수입니다. 한국어로 간결하게 답하세요." +
        (note?.let { "\n\n[현재 노트]\n제목: ${it.title}\n본문:\n${it.body}" } ?: "")

@Composable
private fun Bubble(c: Ink, m: ChatMsg) {
    val user = m.role == "user"
    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (user) Arrangement.End else Arrangement.Start) {
        Box(
            Modifier.widthIn(max = 244.dp)
                .background(if (user) c.primary else Color.Transparent, RoundedCornerShape(12.dp))
                .padding(horizontal = if (user) 11.dp else 0.dp, vertical = if (user) 8.dp else 0.dp),
        ) {
            BasicText(
                m.content,
                style = TextStyle(color = if (user) Color.White else c.ink, fontSize = 12.5.sp, lineHeight = 18.sp),
            )
        }
    }
}
