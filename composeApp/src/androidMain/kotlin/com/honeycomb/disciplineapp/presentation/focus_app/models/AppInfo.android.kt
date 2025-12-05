package com.honeycomb.disciplineapp.presentation.focus_app.models

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ApplicationInfo.*
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.graphics.drawable.toBitmap

actual suspend fun getInstalledApps(
    context: Any?,
    includeSystemApps: Boolean
): List<AppInfo> {
    if (context !is Context) return emptyList()

    val pm = context.packageManager
    val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    return pm.queryIntentActivities(mainIntent, 0)
        .mapNotNull { resolveInfo ->
            val activityInfo = resolveInfo.activityInfo ?: return@mapNotNull null
            val packageName = activityInfo.packageName
            val appName = resolveInfo.loadLabel(pm).toString()
            val iconDrawable = resolveInfo.loadIcon(pm)
            val flags = activityInfo.applicationInfo.flags
            val appInfo: ApplicationInfo = try {
                pm.getApplicationInfo(packageName, 0)
            } catch (e: Exception) {
                return@mapNotNull null
            }
            val isSystem = (flags and FLAG_SYSTEM) != 0

            if (!includeSystemApps && isSystem) return@mapNotNull null

            Log.d("AKASH_LOG", "getInstalledApps: category ${appInfo.category}")

            AppInfo(
                name = appName,
                packageName = packageName,
                icon = AppIcon(iconDrawable),
                isSystemApp = isSystem,
                category = appInfo.category.toAppCategory()
            )
        }
        .sortedBy { it.name.lowercase() }
}

actual class AppIcon internal constructor(val drawable: Drawable)

fun AppIcon.toImageBitmap(): ImageBitmap {
    return drawable.toBitmap().asImageBitmap()
}

actual fun AppIcon.toPainter(): Painter? {
    return androidx.compose.ui.graphics.painter.BitmapPainter(drawable.toBitmap().asImageBitmap())
}

private fun Int.toAppCategory(): AppCategory = when (this) {
    CATEGORY_AUDIO -> AppCategory.AUDIO
    CATEGORY_GAME -> AppCategory.GAMES
    CATEGORY_NEWS -> AppCategory.NEWS
    CATEGORY_PRODUCTIVITY -> AppCategory.PRODUCTIVITY
    CATEGORY_SOCIAL -> AppCategory.SOCIAL
    CATEGORY_VIDEO -> AppCategory.VIDEO
    CATEGORY_IMAGE -> AppCategory.IMAGE
    else -> AppCategory.UNKNOWN
}