package com.honeycomb.disciplineapp.presentation.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.data.dto.UserData
import com.honeycomb.disciplineapp.domain.repository.LoginRepository
import com.honeycomb.disciplineapp.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val repository: LoginRepository
): ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()


    private val _analysingState: MutableStateFlow<OnboardingAnalysingState> = MutableStateFlow(OnboardingAnalysingState(
        points = listOf()
    ))
    val analysingState: StateFlow<OnboardingAnalysingState> = _analysingState.asStateFlow()

    fun loginUser(
        signInResult: UserData?,
    ) = viewModelScope.launch(Dispatchers.IO) {
        _loginState.update {
            it.copy(isLoading = true)
        }
        signInResult?.let { user ->
            repository.loginUser(user).onSuccess { data ->
                _loginState.update { login ->
                    login.copy(isLoading = false, error = null, data = data)
                }
            }.onFailure { err ->
                _loginState.update { login ->
                    login.copy(isLoading = false, error = err)
                }
            }
        } ?: run {
            _loginState.update { login ->
                login.copy(isLoading = false, error = Exception("Something went wrong"))
            }
        }
    }


    fun startAnalysing() = viewModelScope.launch {
        val startDelay = 1000L
        val completeDelay = 1000L
        val points = listOf(
            "analysing your phone usage...",
            "identifying distractions...",
            "generating report....",
            "finalizing insights",
            "all done",
        )

        points.forEachIndexed { index, it ->
            delay(startDelay)
            val point = OnboardingAnalysingState.OnboardingAnalysingItem(
                title = it,
                completed = false
            )
            _analysingState.update { state ->
                state.copy(
                    points = state.points.toMutableList().apply {
                        add(point)
                    }
                )
            }
            if (index != points.lastIndex) delay(completeDelay + index * 300L)
            _analysingState.update { state ->
                state.copy(
                    points = state.points.toMutableList().apply {
                        this[state.points.lastIndex] = point.copy(completed = true)
                    },
                    completed = index == points.lastIndex
                )
            }
//            if (index == points.lastIndex) delay(1500)
        }
    }
}

data class OnboardingAnalysingState(
    val points: List<OnboardingAnalysingItem>,
    val completed: Boolean = false
) {
    data class OnboardingAnalysingItem(
        val title: String,
        val completed: Boolean
    )
}

data class LoginState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val data: UserData? = null
)