package com.honeycomb.disciplineapp.presentation.focus_app

actual class ScreenTimeManager {
    actual suspend fun requestPermission(): Boolean {
        return true
    }

    actual suspend fun startFocusSession(minutes: Int) {
    }

    actual suspend fun getBlockableApps(): List<AppInfo> {
        return emptyList()
    }

    actual suspend fun blockApps(
        appIds: List<String>,
        minutes: Int,
    ) {
    }

    actual suspend fun stopFocusSession() {
    }
}