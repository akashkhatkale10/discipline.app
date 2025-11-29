package com.honeycomb.disciplineapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.stopForeground
import com.honeycomb.disciplineapp.presentation.focus_app.BreakPolicy
import com.honeycomb.disciplineapp.presentation.focus_app.FocusSessionConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicBoolean

class FocusMonitoringService : Service() {

    companion object {
        const val TAG = "FocusMonitoringService"
        const val ACTION_START = "com.honeycomb.disciplineapp.ACTION_START"
        const val ACTION_STOP = "com.honeycomb.disciplineapp.ACTION_STOP"
        const val EXTRA_CONFIG_JSON = "com.honeycomb.disciplineapp.EXTRA_CONFIG_JSON"

        private val running = AtomicBoolean(false)
        fun isRunning(): Boolean = running.get()
    }

    private var monitorJob: Job? = null
    private var config: FocusSessionConfig? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AKASH_LOG", "inside service onStartCommand")

        when (intent?.action) {
            ACTION_START -> {
                intent.getStringExtra(EXTRA_CONFIG_JSON)?.let { json ->
                    try {
                        config = Json.decodeFromString<FocusSessionConfig>(json)
                        startForegroundServiceWithNotification()
                        startMonitoringLoop()
                        running.set(true)
                    } catch (t: Throwable) {
                        Log.d("AKASH_LOG", "Failed parse config: $t")
                        stopSelf()
                    }
                }
            }
            ACTION_STOP -> {
                stopMonitoring()
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun startForegroundServiceWithNotification() {
        val notification = buildNotification("Focus session active")
        startForeground(1001, notification)
    }

    private fun buildNotification(text: String): Notification {
        val intent = Intent(this, FocusMonitoringService::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, "focus_channel")
            .setContentTitle("Focus")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel("focus_channel", "Focus", NotificationManager.IMPORTANCE_LOW)
        nm.createNotificationChannel(channel)
    }

    private fun startMonitoringLoop() {
        monitorJob?.cancel()
        monitorJob = scope.launch {
            config?.let { cfg ->
                val startTime = System.currentTimeMillis()
                val endTime = startTime + cfg.durationMinutes * 60_000L

                when (cfg.breaksAllowed) {
                    BreakPolicy.UNLIMITED -> Int.MAX_VALUE
                    BreakPolicy.TWO_BREAKS -> 2
                    BreakPolicy.NO_BREAKS -> 0
                }

                while (isActive && System.currentTimeMillis() < endTime) {
                    // Preferred: AccessibilityService will notify on window change.
                    // Fallback: poll UsageStats for foreground app
                    val fg = getForegroundPackageUsageStats()
                    if (fg != null) {
                        if (cfg.blockedApps.contains(fg)) {
                            Log.i(TAG, "Blocked app in foreground: $fg")
                            // Launch block UI
                            launchBlockActivity(fg)
                            // Penalty: if break detected and penaltyEnabled -> log to console
                            if (cfg.penaltyEnabled) {
                                Log.i(TAG, "PENALTY: ${cfg.penaltyDescription} (printed only, not charged)")
                            }
                            // Wait until app is no longer foreground
                            // Simple approach: sleep and re-check
                            delay(1000)
                            continue
                        }
                    }
                    delay(700) // polling interval
                }

                // session finished
                Log.i(TAG, "Session ended")
                stopSelf()
            }
        }
    }

    private fun getForegroundPackageUsageStats(): String? {
        // Attempt to get package via UsageEvents
        try {
            val usm = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val now = System.currentTimeMillis()
            val events = usm.queryEvents(now - 2000, now)
            var lastPackage: String? = null
            val ev = UsageEvents.Event()
            while (events.hasNextEvent()) {
                events.getNextEvent(ev)
                Log.d("AKASH_LOG", "getForegroundPackageUsageStats: package name : ${ev.packageName}")
                if (ev.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    lastPackage = ev.packageName
                }
            }
            return lastPackage
        } catch (t: Throwable) {
            Log.e(TAG, "UsageStats failure: $t")
            return null
        }
    }

    private fun launchBlockActivity(packageName: String) {
        val i = Intent(this, BlockActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(BlockActivity.EXTRA_PACKAGE_NAME, packageName)
        }
        startActivity(i)
    }

    private fun stopMonitoring() {
        monitorJob?.cancel()
        running.set(false)
        stopForeground(true)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}

class AppAccessibilityService : AccessibilityService() {

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
                val packages = listOf(
                    "com.google.android.youtube",
                    "com.google.android.gm"
                )
                if (rootNode.packageName in packages) {
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