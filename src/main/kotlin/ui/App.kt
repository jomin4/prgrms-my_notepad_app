package ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import data.SecureStore
import ui.notes.NotesScreen
import ui.settings.Settings
import ui.theme.DarkInk
import ui.theme.LightInk

/** 최상위 화면 라우터: 노트 화면 ↔ 설정 화면. 키가 없으면 설정부터. */
@Composable
fun App() {
    val c = if (isSystemInDarkTheme()) DarkInk else LightInk
    var screen by remember { mutableStateOf(if (SecureStore.hasKey()) "notes" else "settings") }
    when (screen) {
        "settings" -> Settings(c, onBack = { screen = "notes" })
        else -> NotesScreen(c, onOpenSettings = { screen = "settings" })
    }
}
