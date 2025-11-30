package com.honeycomb.disciplineapp.presentation.focus_app

import kotlinx.serialization.Serializable

@Serializable
data class FocusSessionConfig(
    val blockedApps: List<String>,
    val penaltyEnabled: Boolean,
    val penaltyDescription: String? = null,
    val breaksAllowed: BreakPolicy,
    val durationMinutes: Int,
)

enum class BreakPolicy {
    UNLIMITED,
    TWO_BREAKS,
    NO_BREAKS
}

expect class PlatformScreenTimeManager() {

    fun setup(activity: Any?)

    fun isSupported(): Boolean

    suspend fun requestPermission(): Boolean

    suspend fun startMonitoring(config: FocusSessionConfig)

    fun stopMonitoring()

    fun isMonitoringActive(): Boolean
}