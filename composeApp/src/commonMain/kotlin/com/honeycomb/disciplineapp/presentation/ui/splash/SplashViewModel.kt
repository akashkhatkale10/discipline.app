package com.honeycomb.disciplineapp.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import com.honeycomb.disciplineapp.data.dto.UserData
import com.honeycomb.disciplineapp.domain.repository.LoginRepository
import com.honeycomb.disciplineapp.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val loginRepository: LoginRepository
): ViewModel() {

    private val _state: MutableStateFlow<SplashState> =
        MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    init {
        getOnboarding()
    }

    private fun getOnboarding() = viewModelScope.launch {
        loginRepository.getCurrentUser().onSuccess { userData ->
            if (userData != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        user = userData,
                        onboarding = null
                    )
                }
            } else {
                onboardingRepository.getOnboarding().onFailure { err ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = err.message,
                            onboarding = null
                        )
                    }
                }.onSuccess { data ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            onboarding = data
                        )
                    }
                }
            }
        }.onFailure { err ->
            _state.update {
                it.copy(
                    isLoading = false,
                    error = err.message,
                    onboarding = null
                )
            }
        }
    }
}

data class SplashState(
    val onboarding: OnboardingDto? = null,
    val user: UserData? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)