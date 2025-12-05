package com.honeycomb.disciplineapp.presentation.focus_app

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.honeycomb.disciplineapp.BlockActivity
import com.honeycomb.disciplineapp.presentation.focus_app.data_store.BlockedAppsStore
import com.honeycomb.disciplineapp.presentation.focus_app.data_store.BlockedWebsitesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.collections.contains


class AppAccessibilityService : AccessibilityService() {
    private var job: Job? = null
    private var blockedApps = emptySet<String>()
    private var blockedWebsites = emptySet<String>()

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
        job = CoroutineScope(Dispatchers.Main).launch {
            combine(
                BlockedAppsStore.getBlockedAppsFlow(),
                BlockedWebsitesStore.getBlockedWebsitesFlow()
            ) { apps, sites -> apps to sites }
                .collect { (apps, sites) ->
                    blockedApps = apps
                    blockedWebsites = sites
                }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val pkg = event.packageName?.toString()
            if (!pkg.isNullOrBlank()) {
                detectAndBlockApps()

                if (isBrowser(pkg)) {
                    val currentUrl = extractUrlFromBrowser(rootInActiveWindow) ?: return
                    val cleanUrl = currentUrl.lowercase()
                        .removePrefix("http://").removePrefix("https://").removePrefix("www.")

                    val b = listOf("www.google.com")
                    if (b.any { blocked -> blocked.contains(cleanUrl) }) {
                        performGlobalAction(GLOBAL_ACTION_BACK)
                        Toast.makeText(this, "Website blocked: $currentUrl", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private val handler = Handler(Looper.getMainLooper())
    private fun detectAndBlockApps() {
        val maxRetries = 3
        var retryCount = 0
        val checkRunnable = object : Runnable {
            override fun run() {
                val rootNode = rootInActiveWindow ?: return
                if (rootNode.packageName in blockedApps) {
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

    private fun isBrowser(packageName: String) = packageName.run {
        startsWith("com.android.chrome") ||
                startsWith("com.brave.browser") ||
                startsWith("com.duckduckgo.mobile") ||
                startsWith("org.mozilla") ||
                startsWith("com.sec.android.app.sbrowser") || // Samsung
                startsWith("com.vivaldi.browser") ||
                startsWith("com.microsoft.emmx") // Edge
    }

    private fun extractUrlFromBrowser(root: AccessibilityNodeInfo?): String? {
        root ?: return null

        // Chrome / most Chromium browsers
        root.findAccessibilityNodeInfosByViewId("com.android.chrome:id/url_bar")
            ?.firstOrNull()
            ?.text?.toString()
            ?.takeIf { it.isNotBlank() }
            ?.let { return it }

        // Samsung Internet
        root.findAccessibilityNodeInfosByViewId("com.sec.android.app.sbrowser:id/location_bar")
            ?.firstOrNull()
            ?.text?.toString()
            ?.takeIf { it.isNotBlank() }
            ?.let { return it }

        // Fallback: search any EditText containing "." and "/"
        return root.findAccessibilityNodeInfosByViewId("android:id/input")
            .firstOrNull { it.text?.contains(".") == true && it.text?.contains("/") == true }
            ?.text?.toString()
    }
}
