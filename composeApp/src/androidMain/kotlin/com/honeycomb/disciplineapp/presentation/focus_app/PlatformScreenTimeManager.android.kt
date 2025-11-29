package com.honeycomb.disciplineapp.presentation.focus_app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.honeycomb.disciplineapp.FocusMonitoringService
import com.honeycomb.disciplineapp.MainApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

actual class PlatformScreenTimeManager {
    private var monitoring = false

    private var activity: Activity? = null

    actual fun setup(activity: Any?) {
        this.activity = activity as? Activity
    }

    private val context: Context
        get() = MainApp.context  // Use a global/application context reference

    actual fun isSupported(): Boolean {
        // Using API level >= 33 (Android 13) for screen time APIs
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    actual suspend fun requestPermission(): Boolean = withContext(Dispatchers.Main) {
        val accEnabled = android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK // placeholder
        // We'll return true if user has either Accessibility enabled (recommended) or usage access granted.
        // Launch settings for user
        val usageIntent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(usageIntent)

        val accIntent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(accIntent)

        // Can't know immediately; return true optimistically. Caller should check separately using helper checks below.
        true
    }

    private fun requestForegroundPermission(onResult: (Boolean) -> Unit) {
        lastCallback = onResult
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val permission = Manifest.permission.FOREGROUND_SERVICE_SPECIAL_USE

            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d("AKASH_LOG", "showing permissiong")
                ActivityCompat.requestPermissions(activity!!, arrayOf(
                    permission,
                ), 100)
                // Result will be received via Activity callback
                // You’ll need to forward it to KMP
            } else {
                onResult(true)
            }
        } else {
            onResult(true)
        }
    }

    actual fun startMonitoring(config: FocusSessionConfig) {
        // Serialize config to JSON and start a ForegroundService
        requestForegroundPermission {
            if (it) {
                val json = Json.encodeToString(config)
                val intent = Intent(context, FocusMonitoringService::class.java).apply {
                    putExtra(FocusMonitoringService.EXTRA_CONFIG_JSON, json)
                    action = FocusMonitoringService.ACTION_START
                }

                context.startForegroundService(intent)
            } else {
                Log.d("AKASH_LOG", "startMonitoring: denied permission")
            }
        }
//        val json = Json.encodeToString(config)
//        val intent = Intent(context, FocusMonitoringService::class.java).apply {
//            putExtra(FocusMonitoringService.EXTRA_CONFIG_JSON, json)
//            action = FocusMonitoringService.ACTION_START
//        }
//
//        context.startForegroundService(intent)
    }

    actual fun stopMonitoring() {
        Log.d("AKASH_LOG", "stopMonitoring: called")
        monitoring = false
    }

    actual fun isMonitoringActive(): Boolean {
        return monitoring
    }

    private var lastCallback: ((Boolean) -> Unit)? = null

    fun onPermissionResult(granted: Boolean) {
        lastCallback?.invoke(granted)
    }
}