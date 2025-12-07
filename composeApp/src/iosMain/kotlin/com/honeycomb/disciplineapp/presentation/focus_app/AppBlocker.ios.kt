package com.honeycomb.disciplineapp.presentation.focus_app

import com.honeycomb.appblocker.AppBlockerSwift
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)

actual class AppBlocker {
    private val manager = AppBlockerSwift.shared()

    actual fun stopBlocking() {
        manager.stopBlocking()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun startBlocking(
        context: Any?,
        packageNames: List<String>,
        durationMinutes: Long,
        onFinished: () -> Unit
    ) {
        requestPermission(
            onSuccess = {
                manager.startBlocking()
            },
            onFailure = {

            }
        )
    }

    actual fun isBlocking(): Boolean {
        return true
    }

    actual fun requestPermission(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        manager.requestPermissionOnSuccess(
            onSuccess = {
                onSuccess()
            },
            onError = {
                onFailure()
            }
        )
    }

    actual fun selectApps(exclude: Boolean): List<AppInfo> {
        return manager.selectApps(exclude)
    }
}