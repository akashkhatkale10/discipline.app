package com.honeycomb.disciplineapp.presentation.ui.start_routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.data.dto.SetRoutineDto
import com.honeycomb.disciplineapp.data.dto.StartRoutineDto
import com.honeycomb.disciplineapp.domain.repository.StartRoutineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartRoutineViewModel(
    private val repository: StartRoutineRepository
): ViewModel() {

    private val _state: MutableStateFlow<StartRoutineState> =
        MutableStateFlow(StartRoutineState(
            routine = SetRoutineDto(
                habits = emptyList()
            )
        ))
    val state = _state.asStateFlow()

    init {
        getStartRoutine()
    }

    fun addHabit(
        habitDto: SetRoutineDto.SetHabitDto
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                routine = it.routine?.copy(
                    habits = it.routine.habits.toMutableList().apply {
                        add(habitDto)
                    }
                )
            )
        }
    }


    fun getStartRoutine() = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        repository.getStartRoutineData().onSuccess { data ->
            _state.update {
                it.copy(
                    isLoading = false,
                    data = data,
                    error = null
                )
            }
        }.onFailure { err ->
            _state.update {
                it.copy(
                    isLoading = false,
                    data = null,
                    error = err.message
                )
            }
        }
    }
}

data class StartRoutineState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: StartRoutineDto? = null,
    val routine: SetRoutineDto? = null
)