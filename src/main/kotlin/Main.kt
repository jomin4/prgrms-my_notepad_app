import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "로컬 노트") {
        MaterialTheme {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("로컬 AI 노트 — 첫 실행 OK")
            }
        }
    }
}
