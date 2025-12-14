package com.honeycomb.disciplineapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.data.dto.FocusDto
import com.honeycomb.disciplineapp.data.dto.FocusSessionDto
import com.honeycomb.disciplineapp.data.dto.HomeDto
import com.honeycomb.disciplineapp.domain.repository.HomeRepository
import com.honeycomb.disciplineapp.presentation.ui.splash.SplashState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeViewModel(
    private val homeRepository: HomeRepository
): ViewModel() {

    private val _state: MutableStateFlow<HomeState> =
        MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun getHomeData() = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        homeRepository.getHome().onSuccess { sessions: List<FocusSessionDto> ->
            val grouped: Map<String, List<FocusSessionDto>> = sessions.groupBy {
                Instant.fromEpochMilliseconds(it.startTimestamp)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date
                    .toString()
            }
            println("AKASH_LOG: grouped: $grouped")
            _state.update {
                it.copy(
                    isLoading = false,
                    data = grouped,
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

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: Map<String, List<FocusSessionDto>>? = null,
)