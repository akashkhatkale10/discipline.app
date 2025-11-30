package com.honeycomb.disciplineapp.presentation.focus_app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.honeycomb.disciplineapp.BlockActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.collections.contains


class AppAccessibilityService : AccessibilityService() {
    private var job: Job? = null
    private var blockedApps = emptySet<String>()

    companion object { const val TAG = "AppAccessibilitySvc" }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = serviceInfo
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or
                AccessibilityEvent.TYPE_VIEW_SCROLLED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.packageNames = null // listen for all packages
        info.flags = AccessibilityServiceInfo.DEFAULT
        serviceInfo = info
        Log.d("AKASH_LOG", "Accessibility Service connected")
        job = CoroutineScope(Dispatchers.Main).launch {
            BlockedAppsStore.getBlockedAppsFlow().collect { newSet ->
                blockedApps = newSet
                Log.d("Blocker", "Updated blocked apps: $blockedApps")
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val pkg = event.packageName?.toString()
            if (!pkg.isNullOrBlank()) {
                // Broadcast to the service, or directly start block activity if needed
                Log.d(TAG, "onAccessibilityEvent: package: $pkg")
//                val i = Intent(this, FocusMonitoringService::class.java).apply {
//                    action = "com.honeycomb.disciplineapp.ACTION_FOREGROUND_PKG"
//                    putExtra("foreground_pkg", pkg)
//                }
//                startService(i)

                detectAndBlockShorts()
            }
        }
    }
    private val handler = Handler(Looper.getMainLooper())
    private fun detectAndBlockShorts() {
        val maxRetries = 3
        var retryCount = 0
        val checkRunnable = object : Runnable {
            override fun run() {
                val rootNode = rootInActiveWindow ?: return
                if (rootNode.packageName in blockedApps) {
                    Log.d(TAG, "detected Blocking...")
                    val i = Intent(this@AppAccessibilityService, BlockActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra(BlockActivity.EXTRA_PACKAGE_NAME, packageName)
                    }
                    startActivity(i)
                } else if (retryCount < maxRetries) {
                    retryCount++
                    handler.postDelayed(this, 300)  // Retry every 300ms
                }
            }
        }
        handler.post(checkRunnable)
    }

    override fun onInterrupt() { }
}