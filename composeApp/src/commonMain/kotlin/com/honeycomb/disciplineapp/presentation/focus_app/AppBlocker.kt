package com.honeycomb.disciplineapp.presentation.focus_app

expect class AppBlocker() {
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