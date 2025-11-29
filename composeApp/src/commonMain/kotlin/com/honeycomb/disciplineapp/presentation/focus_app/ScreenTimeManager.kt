package com.honeycomb.disciplineapp.presentation.focus_app

expect class ScreenTimeManager() {
    suspend fun requestPermission(): Boolean
    suspend fun startFocusSession(minutes: Int)
    suspend fun getBlockableApps(): List<AppInfo>
    suspend fun blockApps(appIds: List<String>, minutes: Int)
    suspend fun stopFocusSession()
}

data class AppInfo(
    val id: String,          // bundle ID on iOS, package name on Android
    val name: String,
    val iconUrl: String? = null // not used on iOS yet
)