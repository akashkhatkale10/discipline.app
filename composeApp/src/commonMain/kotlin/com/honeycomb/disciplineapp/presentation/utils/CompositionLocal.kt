package com.honeycomb.disciplineapp.presentation.utils

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.honeycomb.disciplineapp.presentation.ui.AppTheme
import com.honeycomb.disciplineapp.presentation.ui.Theme

val LocalPlatformContext: ProvidableCompositionLocal<Any?> = compositionLocalOf { null }
val LocalTheme: ProvidableCompositionLocal<AppTheme> = compositionLocalOf { Theme.getTheme() }