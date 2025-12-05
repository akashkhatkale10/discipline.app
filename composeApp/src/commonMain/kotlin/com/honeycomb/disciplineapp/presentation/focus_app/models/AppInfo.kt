package com.honeycomb.disciplineapp.presentation.focus_app.models

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter

expect class AppIcon {
    // Platform-specific representation of the app icon
}

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: AppIcon,
    val category: AppCategory,
    val isSystemApp: Boolean = false
)

expect suspend fun getInstalledApps(
    context: Any?,
    includeSystemApps: Boolean = false
): List<AppInfo>

expect fun AppIcon.toPainter(): Painter?

sealed class AppCategory(
    val title: String,
    val emojie: String
) {
    data object AUDIO: AppCategory("Audio", "\uD83C\uDFA7")
    data object GAMES: AppCategory("Games", "\uD83C\uDFAE")
    data object NEWS: AppCategory("News", "🗞️")
    data object PRODUCTIVITY: AppCategory("Productivity", "💻")
    data object SOCIAL: AppCategory("Social", "\uD83D\uDCAC")
    data object VIDEO: AppCategory("Video", "\uD83D\uDCFA")
    data object IMAGE: AppCategory("Image", "\uD83D\uDDBC\uFE0F")
    data object UNKNOWN: AppCategory("Others", "")
}