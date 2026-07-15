import androidx.compose.ui.graphics.Color

/** Quiet Ink 디자인 시스템 색 토큰 (DESIGN.md 기준). */
data class Ink(
    val canvas: Color, val surface: Color, val soft: Color, val inset: Color,
    val ink: Color, val body: Color, val muted: Color, val faint: Color,
    val line: Color, val line2: Color, val primary: Color, val primarySoft: Color,
)

val LightInk = Ink(
    canvas = Color(0xFFF6F6F4), surface = Color(0xFFFFFFFF), soft = Color(0xFFF1F1EE), inset = Color(0xFFECEBE7),
    ink = Color(0xFF1A1A1E), body = Color(0xFF3A3A40), muted = Color(0xFF6F6E75), faint = Color(0xFFA3A2A9),
    line = Color(0xFFE7E6E2), line2 = Color(0xFFD9D8D3), primary = Color(0xFF5B4BD6), primarySoft = Color(0xFFECEBFB),
)

val DarkInk = Ink(
    canvas = Color(0xFF151418), surface = Color(0xFF1D1C22), soft = Color(0xFF212028), inset = Color(0xFF26252E),
    ink = Color(0xFFEDECF0), body = Color(0xFFC8C7CE), muted = Color(0xFF918F99), faint = Color(0xFF6A6872),
    line = Color(0xFF2B2A33), line2 = Color(0xFF3A3944), primary = Color(0xFF8F82F2), primarySoft = Color(0xFF272348),
)
