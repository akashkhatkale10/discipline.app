package com.honeycomb.disciplineapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.data.dto.FocusDto
import com.honeycomb.disciplineapp.data.dto.HomeDto
import com.honeycomb.disciplineapp.domain.repository.HomeRepository
import com.honeycomb.disciplineapp.presentation.ui.splash.SplashState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        homeRepository.getHome().onSuccess { focus ->
            _state.update {
                it.copy(
                    isLoading = false,
                    data = focus,
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
    val data: List<FocusDto>? = null
)