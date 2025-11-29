package com.honeycomb.disciplineapp.presentation.focus_app.ui.focus_screen

import androidx.lifecycle.ViewModel
import com.honeycomb.disciplineapp.presentation.focus_app.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FocusViewModel(): ViewModel() {
    private val _state = MutableStateFlow(FocusState())
    val state: StateFlow<FocusState> = _state

}

data class FocusState(
    val permissionGranted: Boolean = false,
    val availableApps: List<AppInfo> = emptyList(),
    val selectedApps: List<AppInfo> = emptyList(),
    val isSessionRunning: Boolean = false,
    val remainingSeconds: Int = 0
)