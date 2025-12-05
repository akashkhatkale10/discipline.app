package com.honeycomb.disciplineapp.presentation.focus_app.models

import androidx.compose.ui.graphics.painter.Painter

actual suspend fun getInstalledApps(
    context: Any?,
    includeSystemApps: Boolean
): List<AppInfo> {
    return emptyList()
}

actual class AppIcon

actual fun AppIcon.toPainter(): Painter? {
    return null
}