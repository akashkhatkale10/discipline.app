package com.honeycomb.disciplineapp.presentation.focus_app

import android.content.Context
import android.content.Intent
import android.os.Build
import com.honeycomb.disciplineapp.presentation.focus_app.data_store.AppBlockingService

actual class AppBlocker {
    actual fun startBlocking(
        context: Any?,
        packageNames: List<String>,
        durationMinutes: Long,
        onFinished: () -> Unit
    ) {
        if (context !is Context) return

        val intent = Intent(context, AppBlockingService::class.java).apply {
            putStringArrayListExtra("packages", ArrayList(packageNames))
            putExtra("durationMs", durationMinutes)
        }
        context.startForegroundService(intent)
        AppBlockingService.onSessionEnd = onFinished
    }

    actual fun stopBlocking() {
//        context.stopService(Intent(context, AppBlockingService::class.java))
    }

    actual fun isBlocking(): Boolean = AppBlockingService.isRunning
    actual fun requestPermission(onSuccess: () -> Unit, onFailure: () -> Unit) {
        onSuccess()
    }
}