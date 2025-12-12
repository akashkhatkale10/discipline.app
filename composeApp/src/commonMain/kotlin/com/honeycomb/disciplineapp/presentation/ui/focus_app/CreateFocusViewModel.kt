package com.honeycomb.disciplineapp.presentation.ui.focus_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.presentation.focus_app.AppBlocker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class CreateFocusViewModel : ViewModel() {

    val appBlocker = AppBlocker()

    private val _state: MutableStateFlow<CreateFocusState> = MutableStateFlow(CreateFocusState())
    val state: StateFlow<CreateFocusState> = _state.asStateFlow()

    val timer = CountdownTimer(viewModelScope)

    val minutes: StateFlow<Int> = timer.minutes
    val seconds: StateFlow<Int> = timer.seconds

    fun updateTime(time: Int) {
        _state.update {
            it.copy(
                time = time
            )
        }
    }

    fun startTimer(time: Int) {
        _state.update {
            it.copy(
                timerState = TimerState.RUNNING
            )
        }
        timer.start(time) // 15 minutes
    }

    fun stopTimer() {
        _state.update {
            it.copy(
                timerState = TimerState.STOPPED
            )
        }
        timer.stop()
    }

    fun pauseTimer() {
        _state.update {
            it.copy(
                timerState = TimerState.PAUSED
            )
        }
        timer.pause()
    }

    fun resumeTimer() {
        _state.update {
            it.copy(
                timerState = TimerState.RUNNING
            )
        }
        timer.resume()
    }
}

enum class TimerState {
    PAUSED, STOPPED, RUNNING, IDLE
}
data class CreateFocusState(
    val timerState: TimerState = TimerState.IDLE,
    val time: Int = 15
)

