package com.honeycomb.disciplineapp.presentation.focus_app

actual class PlatformScreenTimeManager actual constructor() {
    actual fun isSupported(): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun requestPermission(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun startMonitoring(config: FocusSessionConfig) {
    }

    actual fun stopMonitoring() {
    }

    actual fun isMonitoringActive(): Boolean {
        TODO("Not yet implemented")
    }
}