package com.honeycomb.disciplineapp.presentation.ui.focus_app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.max

class CountdownTimer(
    private val scope: CoroutineScope,
    private val onCompleted: () -> Job
) {
    private var timerJob: Job? = null
    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds
    private var isPaused = false
    private var startTimestamp: Instant? = null
    var totalDurationSeconds: Int = 0

    private val _minutes = MutableStateFlow(0)
    val minutes: StateFlow<Int> = _minutes

    private val _seconds = MutableStateFlow(0)
    val seconds: StateFlow<Int> = _seconds

    fun start(totalMinutes: Int) {
        timerJob?.cancel()
        totalDurationSeconds = totalMinutes * 60
        startTimestamp = Clock.System.now()
        isPaused = false
        val remaining = calculateRemaining()
        _remainingSeconds.value = remaining
        _minutes.value = remaining / 60
        _seconds.value = remaining % 60
        launchUpdateJob()
    }

    fun pause() {
        if (isPaused) return
        timerJob?.cancel()
        isPaused = true
        val remaining = calculateRemaining()
        _remainingSeconds.value = remaining
        _minutes.value = remaining / 60
        _seconds.value = remaining % 60
    }

    fun resume() {
        if (!isPaused) return
        isPaused = false
        startTimestamp = Clock.System.now()
        totalDurationSeconds = _remainingSeconds.value
        launchUpdateJob()
    }

    fun stop() {
        timerJob?.cancel()
        isPaused = false
        startTimestamp = null
        totalDurationSeconds = 0
        _remainingSeconds.value = 0
        _minutes.value = 0
        _seconds.value = 0
    }

    private fun launchUpdateJob() {
        timerJob = scope.launch {
            while (true) {
                val remaining = calculateRemaining()
                _remainingSeconds.value = remaining
                _minutes.value = remaining / 60
                _seconds.value = remaining % 60
                if (remaining <= 0 && !isPaused) {
                    onCompleted()
                    stop()
                    break
                }
                delay(1000)
            }
        }
    }

    private fun calculateRemaining(): Int {
        if (isPaused || startTimestamp == null) {
            return _remainingSeconds.value
        }
        val elapsed = (Clock.System.now() - startTimestamp!!).inWholeSeconds.toInt()
        return max(0, totalDurationSeconds - elapsed)
    }

    fun restoreRunning(start: Instant, durationSeconds: Int) {
        timerJob?.cancel()
        startTimestamp = start
        totalDurationSeconds = durationSeconds
        isPaused = false
        launchUpdateJob()
    }

    fun restorePaused(remaining: Int) {
        timerJob?.cancel()
        _remainingSeconds.value = remaining
        _minutes.value = remaining / 60
        _seconds.value = remaining % 60
        isPaused = true
    }
}
