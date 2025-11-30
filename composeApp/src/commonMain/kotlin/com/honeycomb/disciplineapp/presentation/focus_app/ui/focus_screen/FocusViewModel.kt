package com.honeycomb.disciplineapp.presentation.focus_app.ui.focus_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FocusViewModel(): ViewModel() {
    private val _state = MutableStateFlow(FocusState())
    val state: StateFlow<FocusState> = _state

    fun addAppsBlocked(
        apps: List<AppInfo>
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                selectedApps = apps
            )
        }
    }

}

data class FocusState(
    val permissionGranted: Boolean = false,
    val availableApps: List<AppInfo> = emptyList(),
    val selectedApps: List<AppInfo> = emptyList(),
    val isSessionRunning: Boolean = false,
    val remainingSeconds: Int = 0
)