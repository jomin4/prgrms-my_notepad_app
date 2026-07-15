import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.net.URI

private val MODELS = listOf(
    "meta/llama-3.1-8b-instruct",
    "meta/llama-3.3-70b-instruct",
    "meta/llama-3.1-70b-instruct",
)

@Composable
fun Settings(c: Ink, onBack: () -> Unit) {
    var keyInput by remember { mutableStateOf(SecureStore.loadKey() ?: "") }
    var showKey by remember { mutableStateOf(false) }
    var model by remember { mutableStateOf(SecureStore.loadModel()) }
    var status by remember { mutableStateOf<Pair<String, Color>?>(null) }
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().background(c.surface)) {
        // 상단 바
        Row(Modifier.fillMaxWidth().background(c.soft).padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.clickable { onBack() }.padding(4.dp)) {
                BasicText("←", style = TextStyle(color = c.body, fontSize = 18.sp))
            }
            Spacer(Modifier.width(10.dp))
            BasicText("설정", style = TextStyle(color = c.ink, fontSize = 15.sp, fontWeight = FontWeight.Medium))
        }
        Box(Modifier.height(0.5.dp).fillMaxWidth().background(c.line))

        Column(Modifier.fillMaxWidth().padding(horizontal = 26.dp, vertical = 22.dp).widthIn(max = 560.dp)) {
            // ── AI 연결 그룹 ──
            Column(
                Modifier.fillMaxWidth().border(0.5.dp, c.line, RoundedCornerShape(12.dp)).padding(18.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicText("AI 연결", style = TextStyle(color = c.ink, fontSize = 14.sp, fontWeight = FontWeight.Medium))
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.background(c.inset, RoundedCornerShape(999.dp)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                        BasicText("NVIDIA NIM", style = TextStyle(color = c.muted, fontFamily = FontFamily.Monospace, fontSize = 11.sp))
                    }
                }

                Spacer(Modifier.height(14.dp))
                BasicText("API 키", style = TextStyle(color = c.muted, fontSize = 11.sp, fontWeight = FontWeight.Medium))
                Spacer(Modifier.height(6.dp))
                Row(
                    Modifier.fillMaxWidth().background(c.inset, RoundedCornerShape(8.dp)).border(0.5.dp, c.line, RoundedCornerShape(8.dp)).padding(horizontal = 11.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.weight(1f)) {
                        if (keyInput.isEmpty()) {
                            BasicText("nvapi-…", style = TextStyle(color = c.faint, fontFamily = FontFamily.Monospace, fontSize = 13.sp))
                        }
                        BasicTextField(
                            value = keyInput,
                            onValueChange = { keyInput = it; status = null },
                            textStyle = TextStyle(color = c.ink, fontFamily = FontFamily.Monospace, fontSize = 13.sp),
                            visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
                            cursorBrush = SolidColor(c.primary),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.clickable { showKey = !showKey }.padding(2.dp)) {
                        BasicText(if (showKey) "숨김" else "표시", style = TextStyle(color = c.muted, fontSize = 12.sp))
                    }
                }
                Spacer(Modifier.height(7.dp))
                Row {
                    Box(Modifier.clickable { openUrl("https://build.nvidia.com") }) {
                        BasicText("무료 키 발급 build.nvidia.com →", style = TextStyle(color = c.primary, fontSize = 11.5.sp, fontWeight = FontWeight.Medium))
                    }
                    Spacer(Modifier.width(12.dp))
                    BasicText("이 기기에 암호화 저장", style = TextStyle(color = c.muted, fontSize = 11.5.sp))
                }

                Spacer(Modifier.height(16.dp))
                BasicText("모델", style = TextStyle(color = c.muted, fontSize = 11.sp, fontWeight = FontWeight.Medium))
                Spacer(Modifier.height(3.dp))
                BasicText("8b가 무료 티어에서 가장 빠릅니다", style = TextStyle(color = c.faint, fontSize = 11.sp))
                Spacer(Modifier.height(6.dp))
                MODELS.forEach { m ->
                    val sel = m == model
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 2.dp)
                            .background(if (sel) c.inset else Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable { model = m }
                            .padding(horizontal = 11.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(Modifier.width(9.dp).height(9.dp).background(if (sel) c.primary else Color.Transparent, RoundedCornerShape(999.dp)).border(0.5.dp, if (sel) c.primary else c.line2, RoundedCornerShape(999.dp)))
                        Spacer(Modifier.width(10.dp))
                        BasicText(m, style = TextStyle(color = if (sel) c.ink else c.body, fontFamily = FontFamily.Monospace, fontSize = 12.5.sp))
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.background(c.surface, RoundedCornerShape(8.dp)).border(0.5.dp, c.line2, RoundedCornerShape(8.dp))
                            .clickable {
                                if (keyInput.isBlank()) {
                                    status = "키를 먼저 입력하세요" to c.muted
                                } else {
                                    status = "테스트 중…" to c.muted
                                    scope.launch {
                                        val r = withContext(Dispatchers.IO) { NimClient.testConnection(keyInput) }
                                        status = r.fold({ "● $it" to c.primary }, { (it.message ?: "실패") to Color(0xFFD64545) })
                                    }
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                    ) {
                        BasicText("연결 테스트", style = TextStyle(color = c.ink, fontSize = 12.5.sp, fontWeight = FontWeight.Medium))
                    }
                    Spacer(Modifier.width(12.dp))
                    status?.let { (msg, col) ->
                        BasicText(msg, style = TextStyle(color = col, fontSize = 12.sp, fontWeight = FontWeight.Medium))
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Box(
                    Modifier.background(c.primary, RoundedCornerShape(8.dp))
                        .clickable {
                            try {
                                if (keyInput.isNotBlank()) SecureStore.saveKey(keyInput.trim())
                                SecureStore.saveModel(model)
                                onBack()
                            } catch (e: Exception) {
                                status = "저장 실패: ${e.message}" to Color(0xFFD64545)
                            }
                        }
                        .padding(horizontal = 22.dp, vertical = 9.dp),
                ) {
                    BasicText("저장", style = TextStyle(color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium))
                }
            }
        }
    }
}

private fun openUrl(url: String) {
    try {
        if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(URI(url))
    } catch (_: Exception) {
    }
}
