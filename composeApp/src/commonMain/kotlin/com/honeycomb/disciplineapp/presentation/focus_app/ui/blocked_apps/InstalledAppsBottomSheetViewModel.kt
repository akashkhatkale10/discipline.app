package com.honeycomb.disciplineapp.presentation.focus_app.ui.blocked_apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppCategory
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InstalledAppsBottomSheetViewModel: ViewModel() {

    private val _state = MutableStateFlow(InstalledAppBottomSheetState())
    val state: StateFlow<InstalledAppBottomSheetState> = _state

    fun initData(
        installedApps: List<AppInfo>,
        selectedApps: List<AppInfo>,
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                installedApp = installedApps.map { app ->
                    InstalledAppState(
                        app = app,
                        isSelected = selectedApps.find { it.packageName == app.packageName } != null
                    )
                }.toMutableList()
            )
        }
    }

    fun selectAnApp(
        isSelected: Boolean,
        packageName: String,
    ) = viewModelScope.launch {
        _state.value = _state.value.copy(
            installedApp = _state.value.installedApp.map {
                if (it.app.packageName == packageName) {
                    it.copy(isSelected = isSelected)
                } else {
                    it
                }
            }
        )
    }

    fun selectCategory(
        category: AppCategory,
        select: Boolean
    ) = viewModelScope.launch {
        _state.value = _state.value.copy(
            installedApp = _state.value.installedApp.map {
                if (it.app.category == category) {
                    it.copy(isSelected = select)
                } else {
                    it
                }
            }
        )
    }

}

data class InstalledAppBottomSheetState(
    val installedApp: List<InstalledAppState> = mutableListOf()
)

data class InstalledAppState(
    val app: AppInfo,
    val isSelected: Boolean
)