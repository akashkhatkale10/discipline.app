package com.honeycomb.disciplineapp.presentation.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.data.dto.UserData
import com.honeycomb.disciplineapp.domain.repository.LoginRepository
import com.honeycomb.disciplineapp.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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

}

data class LoginState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val data: UserData? = null
)