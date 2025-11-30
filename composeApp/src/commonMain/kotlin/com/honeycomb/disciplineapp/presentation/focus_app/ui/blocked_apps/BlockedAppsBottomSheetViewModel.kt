package com.honeycomb.disciplineapp.presentation.focus_app.ui.blocked_apps


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo
import com.honeycomb.disciplineapp.presentation.focus_app.models.getInstalledApps
import com.honeycomb.disciplineapp.presentation.focus_app.ui.focus_screen.FocusState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlockedAppsBottomSheetViewModel: ViewModel() {

    private val _state = MutableStateFlow(BlockedAppsBottomSheetState())
    val state: StateFlow<BlockedAppsBottomSheetState> = _state

    fun initData(
        context: Any?,
        selectedApps: List<AppInfo>
    ) = viewModelScope.launch {
        val installedApps = getInstalledApps(context = context, true)
        println(installedApps.map { it.name })
        _state.update {
            it.copy(
                blockedApp = it.blockedApp.copy(
                    title = it.blockedApp.title,
                    totalApps = installedApps.size,
                    blockedApps = selectedApps
                ),
                installedApps = installedApps
            )
        }
    }

    fun addApps(
        apps: List<AppInfo>
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
               blockedApp = it.blockedApp.copy(
                   blockedApps = apps
               )
            )
        }
    }

    fun removeApp(
        packageName: String
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                blockedApp = it.blockedApp.copy(
                    blockedApps = it.blockedApp.blockedApps.filter {
                        it.packageName != packageName
                    }
                )
            )
        }
    }

}

data class BlockedAppsBottomSheetState(
    val blockedApp: BlockedAppUi = BlockedAppUi(
        title = "blocked apps",
        subtitle = "block any specific apps during your focus time",
        blockedApps = emptyList(),
        totalApps = 0,
        buttonText = "add an app"
    ),
    val installedApps: List<AppInfo> = emptyList()
)

data class BlockedAppUi(
    val title: String,
    val totalApps: Int,
    val subtitle: String,
    val buttonText: String,
    val blockedApps: List<AppInfo>,
    val showAddButton: Boolean = true
)