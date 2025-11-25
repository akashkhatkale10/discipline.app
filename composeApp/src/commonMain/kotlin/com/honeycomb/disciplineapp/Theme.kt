package com.honeycomb.disciplineapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import disciplineapp.composeapp.generated.resources.Res
import disciplineapp.composeapp.generated.resources.nunito_bold
import disciplineapp.composeapp.generated.resources.nunito_extrabold
import disciplineapp.composeapp.generated.resources.nunito_extralight
import disciplineapp.composeapp.generated.resources.nunito_medium
import disciplineapp.composeapp.generated.resources.nunito_regular
import disciplineapp.composeapp.generated.resources.nunito_semibold
import org.jetbrains.compose.resources.Font


val nunitoFontFamily
    @Composable get() = FontFamily(
        Font(Res.font.nunito_extralight, FontWeight.ExtraLight),
        Font(Res.font.nunito_regular, FontWeight.Normal),
        Font(Res.font.nunito_medium, FontWeight.Medium),
        Font(Res.font.nunito_semibold, FontWeight.SemiBold),
        Font(Res.font.nunito_bold, FontWeight.Bold),
        Font(Res.font.nunito_extrabold, FontWeight.ExtraBold)
    )

val CustomTextStyle
    @Composable get() = TextStyle(
        fontFamily = nunitoFontFamily
    )

@Composable
fun DisciplineAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}