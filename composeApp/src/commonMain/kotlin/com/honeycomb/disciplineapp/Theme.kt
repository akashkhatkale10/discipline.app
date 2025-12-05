package com.honeycomb.disciplineapp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import disciplineapp.composeapp.generated.resources.Res
import disciplineapp.composeapp.generated.resources.montserrat_bold
import disciplineapp.composeapp.generated.resources.montserrat_extraBold
import disciplineapp.composeapp.generated.resources.montserrat_light
import disciplineapp.composeapp.generated.resources.montserrat_medium
import disciplineapp.composeapp.generated.resources.montserrat_regular
import disciplineapp.composeapp.generated.resources.montserrat_semiBold
import disciplineapp.composeapp.generated.resources.nunito_bold
import disciplineapp.composeapp.generated.resources.nunito_extrabold
import disciplineapp.composeapp.generated.resources.nunito_extralight
import disciplineapp.composeapp.generated.resources.nunito_medium
import disciplineapp.composeapp.generated.resources.nunito_regular
import disciplineapp.composeapp.generated.resources.nunito_semibold
import org.jetbrains.compose.resources.Font


val nunitoFontFamily
    @Composable get() = FontFamily(
        Font(Res.font.montserrat_light, FontWeight.Light),
        Font(Res.font.montserrat_regular, FontWeight.Normal),
        Font(Res.font.montserrat_medium, FontWeight.Medium),
        Font(Res.font.montserrat_semiBold, FontWeight.SemiBold),
        Font(Res.font.montserrat_bold, FontWeight.Bold),
        Font(Res.font.montserrat_extraBold, FontWeight.ExtraBold)
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