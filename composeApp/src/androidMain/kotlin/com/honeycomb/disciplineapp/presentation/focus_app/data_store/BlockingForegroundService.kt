package com.honeycomb.disciplineapp.presentation.focus_app.data_store

import android.R
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppBlockingService : Service() {

    companion object {
        var onSessionEnd: (() -> Unit)? = null
        var isRunning = false; private set

        private const val NOTIF_ID = 1001
        private const val CHANNEL_ID = "focus_blocker"
    }

    private var blockedPackages = emptyList<String>()
    private var endTimeMs = 0L
    private var timerJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startForeground(NOTIF_ID, createInitialNotification())

        isRunning = true
    }

    private fun createInitialNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Starting Focus Mode...")
            .setContentText("Preparing to block apps")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_menu_compass)
            .setOngoing(true)
            .build()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            blockedPackages = it.getStringArrayListExtra("packages") ?: emptyList()
            val durationMs = it.getLongExtra("durationMs", 0)
            if (blockedPackages.isEmpty() || durationMs <= 0) {
                stopSelf()
                return START_NOT_STICKY
            }

            endTimeMs = System.currentTimeMillis() + durationMs
            startForeground(NOTIF_ID, buildNotification(durationMs / 1000))
            updateBlockedAppsInService(blockedPackages)
            startCountdownTimer(durationMs)
        }
        return START_STICKY
    }

    private fun startCountdownTimer(durationMs: Long) {
        BlockerManager.state.start(durationMs / 1000) { sec ->
            updateNotification(sec)
        }

        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (System.currentTimeMillis() < endTimeMs) {
                delay(1000)
                BlockerManager.state.tick()
            }
            stopBlockingSession()
        }
    }

    private fun stopBlockingSession() {
        removelockedAppsInService(blockedPackages) // unblock all
        BlockerManager.state.finish()
        stopForeground(true)
        stopSelf()
        isRunning = false
        onSessionEnd?.invoke()
        onSessionEnd = null
    }

    private fun updateBlockedAppsInService(packages: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            packages.forEach {
                BlockedAppsStore
                    .addBlockedApp(it)
            }
        }
    }

    private fun removelockedAppsInService(packages: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            packages.forEach {
                BlockedAppsStore
                    .removeBlockedApp(it)
            }
        }
    }

    private fun buildNotification(seconds: Long): Notification {
        val mins = seconds / 60
        val secs = seconds % 60
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Focus Mode Active")
            .setContentText("$mins:${secs.toString().padStart(2, '0')} • ${blockedPackages.size} apps blocked")
            .setSmallIcon(R.drawable.ic_menu_compass)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun updateNotification(seconds: Long) {
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(NOTIF_ID, buildNotification(seconds))
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Focus Blocker", NotificationManager.IMPORTANCE_HIGH)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        timerJob?.cancel()
        isRunning = false
        super.onDestroy()
    }
}