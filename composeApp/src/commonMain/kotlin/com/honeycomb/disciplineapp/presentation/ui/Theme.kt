package com.honeycomb.disciplineapp.presentation.ui

import androidx.compose.ui.graphics.Color

object Theme {
    fun getTheme(): AppTheme {
        return blackTheme
    }
}



class AppTheme(
    val backgroundColorGradient: List<Color>,
    val primaryButtonColorGradient: List<Color>,
    val primaryButtonStrokeColorGradient: List<Color>,
    val secondaryButtonColor: Color,
    val secondaryButtonStrokeColor: Color,
    val subtitleColor: Color,
    val titleColor: Color,
    val tertiaryColor: Color,
    val quaternaryColor: Color
)

val blackTheme = AppTheme(
    backgroundColorGradient = listOf(
        Color(0xFF0D0D0D),
        Color(0xFF0D0D0D),
        Color(0xFF0D0D0D),
    ),

    primaryButtonColorGradient = listOf(
        Color(0xFFCF414B),
        Color(0xFFC62D2D),
    ),
    primaryButtonStrokeColorGradient = listOf(
        Color(0xFFE5727F),
        Color(0xFFC74545),
    ),

    secondaryButtonColor = Color(0x05FFFFFF),
    secondaryButtonStrokeColor = Color(0x10FFFFFF),

    titleColor = Color(0xFFFFFFFF),
    subtitleColor = Color(0x02FFFFFF),
    tertiaryColor = Color(0x10FFFFFF),
    quaternaryColor = Color(0x30FFFFFF),
)

val redTheme = AppTheme(
    backgroundColorGradient = listOf(
        Color(0xFF2F1517),
        Color(0xFF0F0505),
        Color(0xFF121212),
    ),

    primaryButtonColorGradient = listOf(
        Color(0xFFCF414B),
        Color(0xFFC62D2D),
    ),
    primaryButtonStrokeColorGradient = listOf(
        Color(0xFFE5727F),
        Color(0xFFC74545),
    ),

    secondaryButtonColor = Color(0x02FFFFFF),
    secondaryButtonStrokeColor = Color(0x10FFFFFF),

    subtitleColor = Color(0x02FFFFFF),
    tertiaryColor = Color(0x10FFFFFF),
    titleColor = Color(0xFFFFFFFF),
    quaternaryColor = Color(0x30FFFFFF),
)