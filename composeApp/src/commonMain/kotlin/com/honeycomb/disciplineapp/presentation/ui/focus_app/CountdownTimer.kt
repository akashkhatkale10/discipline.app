package com.honeycomb.disciplineapp.presentation.ui.focus_app

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountdownTimer(
    private val scope: CoroutineScope
) {
    private var timerJob: Job? = null
    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds
    private var isPaused = false

    private val _minutes = MutableStateFlow(0)
    val minutes: StateFlow<Int> = _minutes

    private val _seconds = MutableStateFlow(0)
    val seconds: StateFlow<Int> = _seconds

    fun start(totalMinutes: Int) {
        timerJob?.cancel()


        isPaused = false

        timerJob = scope.launch {
            _remainingSeconds.value = totalMinutes * 60
            runTimer()
        }
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        if (!isPaused) return
        isPaused = false

        timerJob = scope.launch {
            runTimer()
        }
    }

    fun stop() {
        timerJob?.cancel()
        scope.launch {
            _remainingSeconds.value = 0
        }
        updateState(0)
    }

    private suspend fun runTimer() {
        while (_remainingSeconds.value >= 0 && !isPaused) {
            updateState(_remainingSeconds.value)
            delay(1000)
            _remainingSeconds.value = _remainingSeconds.value - 1
        }
    }

    private fun updateState(seconds: Int) {
        _minutes.value = seconds / 60
        _seconds.value = seconds % 60
    }
}
