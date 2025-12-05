package com.honeycomb.disciplineapp.presentation.focus_app

import com.honeycomb.appblocker.AppBlockerSwift
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)

actual class AppBlocker {
    private val manager = AppBlockerSwift.shared()

    actual fun stopBlocking() {
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun startBlocking(
        context: Any?,
        packageNames: List<String>,
        durationMinutes: Long,
        onFinished: () -> Unit
    ) {
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
//                manager.getSocialMediaUsageWithDays(
//                    days = -7,
//                    completionHandler = {
//
//                    },
//                    completion = {
//
//                    }
//                )
            },
            onError = {
                onFailure()
            }
        )
    }
}