package com.honeycomb.disciplineapp.presentation.ui.focus_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.data.repository.TimerRepository
import com.honeycomb.disciplineapp.presentation.focus_app.AppBlocker
import com.honeycomb.disciplineapp.presentation.utils.Constants
import com.honeycomb.disciplineapp.data.repository.FocusRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateFocusViewModel : ViewModel(), KoinComponent {

    private val timerRepository: TimerRepository by inject()
    private val focusRepository: FocusRepository by inject()

    private val _state: MutableStateFlow<CreateFocusState?> = MutableStateFlow(null)
    val state: StateFlow<CreateFocusState?> = _state.asStateFlow()

    val onCompleted: (Long) -> Job = { endTime ->
        _state.update {
            it?.copy(
                timerState = TimerState.COMPLETED,
                endTimestamp = Instant.fromEpochMilliseconds(endTime)
            )
        }
        viewModelScope.launch {
            timerRepository.clearTimerState()
            focusRepository.saveFocusSession(
                _state.value!!.startTimestamp!!.toEpochMilliseconds(),
                endTime,
                _state.value!!.time * 60
            )
        }
    }

    val timer = CountdownTimer(viewModelScope, onCompleted)

    val minutes: StateFlow<Int> = timer.minutes
    val seconds: StateFlow<Int> = timer.seconds

    fun initVm() {
        viewModelScope.launch {
            val persisted = timerRepository.loadTimerState() ?: run {
                _state.value = CreateFocusState()
                return@launch
            }

            _state.update {
                it?.copy(
                    timerState = persisted.state,
                    time = persisted.initialDuration,
                    startTimestamp = persisted.startTimestamp
                ) ?: CreateFocusState(
                    timerState = persisted.state,
                    time = persisted.initialDuration,
                    startTimestamp = persisted.startTimestamp
                )
            }

            println("state is: ${persisted.state}")
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

                else -> {
                    if (_state.value == null) {
                        _state.value = CreateFocusState()
                    }
                }
            }
        }
    }

    fun updateTime(time: Int) {
        _state.update {
            it?.copy(
                time = time
            )
        }
    }

    fun startTimer(context: Any?, time: Int) {
        val startTimestamp = Clock.System.now()
        _state.update {
            it?.copy(
                timerState = TimerState.RUNNING,
                time = time,
                startTimestamp = startTimestamp
            )
        }
        timer.start(time) // 15 minutes
        viewModelScope.launch {
            timerRepository.saveTimerState(
                TimerState.RUNNING,
                time,
                startTimestamp,
                null
            )
        }
    }

    fun stopTimer() {
        _state.update {
            it?.copy(
                timerState = TimerState.STOPPED,
                endTimestamp = Clock.System.now()
            )
        }
        timer.stop()
        viewModelScope.launch {
            timerRepository.clearTimerState()
            focusRepository.saveFocusSession(
                _state.value!!.startTimestamp!!.toEpochMilliseconds(),
                Clock.System.now().toEpochMilliseconds(),
                _state.value!!.time * 60
            )
        }
    }

    fun resetTimer() {
        _state.update {
            it?.copy(
                timerState = TimerState.IDLE,
                time = Constants.getMinimumTime(),
                startTimestamp = null,
                endTimestamp = null
            )
        }
        viewModelScope.launch {
            timerRepository.clearTimerState()
        }
    }

    fun pauseTimer() {
        _state.update {
            it?.copy(
                timerState = TimerState.PAUSED
            )
        }
        timer.pause()
        viewModelScope.launch {
            timerRepository.saveTimerState(
                TimerState.PAUSED,
                state.value?.time  ?: Constants.getMinimumTime(),
                null,
                timer.remainingSeconds.value
            )
        }
    }

    fun resumeTimer() {
        _state.update {
            it?.copy(
                timerState = TimerState.RUNNING
            )
        }
        timer.resume()
        viewModelScope.launch {
            timerRepository.saveTimerState(
                TimerState.RUNNING,
                state.value?.time ?: Constants.getMinimumTime(),
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
    val startTimestamp: Instant? = null,
    val endTimestamp: Instant? = null,
)

