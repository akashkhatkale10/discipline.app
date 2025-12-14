package com.honeycomb.disciplineapp.presentation.focus_app

import com.honeycomb.appblocker.AppBlockerSwift
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppIcon
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIImage
import platform.UIKit.removeImage

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
        onPermissionSuccess: () -> Unit
    ) {
        requestPermission(
            onSuccess = {
                onPermissionSuccess()
                manager.startBlockingWithTime(
                    time = durationMinutes
                )
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

    actual fun selectApps(exclude: Boolean, onAppsSelected: (apps: List<AppInfo>) -> Unit) {
        manager.selectAppsWithExclude(
            exclude = exclude,
            selectApps = { apps ->
                onAppsSelected(
                    apps?.mapNotNull {
                        AppInfo(
                            icon = null,
                            packageName = "",
                            name = "",
                            token = (it as? String)
                        )
                    } as List<AppInfo>
                )
            },
        )
    }
}