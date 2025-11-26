package com.honeycomb.disciplineapp.presentation.ui.start_routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        MutableStateFlow(StartRoutineState())
    val state = _state.asStateFlow()


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
    val data: StartRoutineDto? = null
)