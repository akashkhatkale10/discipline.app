package com.honeycomb.disciplineapp.presentation.ui.focus_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.data.repository.TimerRepository
import com.honeycomb.disciplineapp.presentation.focus_app.AppBlocker
import com.honeycomb.disciplineapp.presentation.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateFocusViewModel : ViewModel(), KoinComponent {

    private val timerRepository: TimerRepository by inject()

    private val _state: MutableStateFlow<CreateFocusState> = MutableStateFlow(CreateFocusState())
    val state: StateFlow<CreateFocusState> = _state.asStateFlow()

    val onCompleted = {
        _state.update {
            it.copy(
                timerState = TimerState.COMPLETED,
            )
        }
        viewModelScope.launch {
            timerRepository.clearTimerState()
        }
    }

    val timer = CountdownTimer(viewModelScope, onCompleted)

    val minutes: StateFlow<Int> = timer.minutes
    val seconds: StateFlow<Int> = timer.seconds

    fun initVm() {
        viewModelScope.launch {
            val persisted = timerRepository.loadTimerState() ?: return@launch

            _state.update {
                it.copy(
                    timerState = persisted.state,
                    time = persisted.initialDuration
                )
            }
            when (persisted.state) {
                TimerState.RUNNING -> {
                    persisted.startTimestamp?.let { start ->
                        timer.restoreRunning(start, persisted.initialDuration * 60)
                    }
                }

                TimerState.PAUSED -> {
                    persisted.pausedRemainingSeconds?.let { remaining ->
                        timer.restorePaused(remaining)
                    }
                }

                else -> {}
            }
        }
    }

    fun updateTime(time: Int) {
        _state.update {
            it.copy(
                time = time
            )
        }
    }

    fun startTimer(context: Any?, time: Int) {
        _state.update {
            it.copy(
                timerState = TimerState.RUNNING,
                time = time
            )
        }
        timer.start(time) // 15 minutes
        viewModelScope.launch {
            timerRepository.saveTimerState(
                TimerState.RUNNING,
                time,
                Clock.System.now(),
                null
            )
        }
    }

    fun stopTimer() {
        _state.update {
            it.copy(
                timerState = TimerState.STOPPED
            )
        }
        timer.stop()
        viewModelScope.launch {
            timerRepository.clearTimerState()
        }
    }

    fun resetTimer() {
        _state.update {
            it.copy(
                timerState = TimerState.IDLE,
                time = Constants.getMinimumTime()
            )
        }
        viewModelScope.launch {
            timerRepository.clearTimerState()
        }
    }

    fun pauseTimer() {
        _state.update {
            it.copy(
                timerState = TimerState.PAUSED
            )
        }
        timer.pause()
        viewModelScope.launch {
            timerRepository.saveTimerState(
                TimerState.PAUSED,
                state.value.time,
                null,
                timer.remainingSeconds.value
            )
        }
    }

    fun resumeTimer() {
        _state.update {
            it.copy(
                timerState = TimerState.RUNNING
            )
        }
        timer.resume()
        viewModelScope.launch {
            timerRepository.saveTimerState(
                TimerState.RUNNING,
                state.value.time,
                Clock.System.now(),
                null
            )
        }
    }
}

@Serializable
enum class TimerState {
    PAUSED, STOPPED, RUNNING, IDLE, COMPLETED;

    companion object {
        fun find(value: String) = entries.firstOrNull { it.name == value } ?: IDLE
    }
}

@Serializable
data class CreateFocusState(
    val timerState: TimerState = TimerState.IDLE,
    val time: Int = Constants.getMinimumTime(),
)

