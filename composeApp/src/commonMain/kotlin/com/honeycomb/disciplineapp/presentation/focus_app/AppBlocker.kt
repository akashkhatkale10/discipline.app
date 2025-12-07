package com.honeycomb.disciplineapp.presentation.focus_app

import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo

expect class AppBlocker() {

    fun selectApps(
        exclude: Boolean = false
    ): List<AppInfo>

    fun requestPermission(
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    )
    fun startBlocking(
        context: Any?,
        packageNames: List<String>,
        durationMinutes: Long,
        onFinished: () -> Unit = {}
    )

    fun stopBlocking()

    fun isBlocking(): Boolean
}