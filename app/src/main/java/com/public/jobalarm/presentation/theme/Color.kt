package com.jobalarm.presentation.theme

import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF1B5E9B)
val Secondary = Color(0xFF2E7D32)
val BackgroundLight = Color(0xFFFAFAFA)
val BackgroundDark = Color(0xFF121212)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1E1E1E)
val OnPrimary = Color.White
val OnBackgroundLight = Color(0xFF1B1B1B)
val OnBackgroundDark = Color(0xFFECECEC)

val CategoryAColor = Color(0xFF1565C0)
val CategoryBColor = Color(0xFF2E7D32)
val CategoryCColor = Color(0xFF616161)
val CategoryDColor = Color(0xFF6A1B9A)

val HireRegular = Color(0xFF1565C0)
val HireUnlimited = Color(0xFFE65100)
val HireTerm = Color(0xFF616161)

val DeadlineRed = Color(0xFFD32F2F)

fun categoryColor(code: String): Color = when (code) {
    "A" -> CategoryAColor
    "B" -> CategoryBColor
    "C" -> CategoryCColor
    "D" -> CategoryDColor
    else -> CategoryCColor
}

fun hireColor(name: String): Color = when {
    name.contains("정규") -> HireRegular
    name.contains("무기") -> HireUnlimited
    name.contains("기간") -> HireTerm
    else -> HireTerm
}
