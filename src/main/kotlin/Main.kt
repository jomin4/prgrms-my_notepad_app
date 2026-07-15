import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState(size = DpSize(1040.dp, 680.dp))
    Window(onCloseRequest = ::exitApplication, state = state, title = "로컬 노트") {
        App()
    }
}
