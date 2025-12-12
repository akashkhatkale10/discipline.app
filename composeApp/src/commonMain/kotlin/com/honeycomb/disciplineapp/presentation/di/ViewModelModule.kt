package com.honeycomb.disciplineapp.presentation.di

import com.honeycomb.disciplineapp.presentation.focus_app.ui.blocked_apps.BlockedAppsBottomSheetViewModel
import com.honeycomb.disciplineapp.presentation.focus_app.ui.blocked_apps.InstalledAppsBottomSheetViewModel
import com.honeycomb.disciplineapp.presentation.ui.add_habit.AddHabitViewModel
import com.honeycomb.disciplineapp.presentation.focus_app.ui.focus_screen.FocusViewModel
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusViewModel
import com.honeycomb.disciplineapp.presentation.ui.home.HomeViewModel
import com.honeycomb.disciplineapp.presentation.ui.onboarding.OnboardingViewModel
import com.honeycomb.disciplineapp.presentation.ui.splash.SplashViewModel
import com.honeycomb.disciplineapp.presentation.ui.start_routine.StartRoutineViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { StartRoutineViewModel(get()) }
    viewModel { AddHabitViewModel() }
    viewModel { FocusViewModel() }
    viewModel { BlockedAppsBottomSheetViewModel() }
    viewModel { InstalledAppsBottomSheetViewModel() }
    viewModel { CreateFocusViewModel() }
}

//val remoteConfigModule = module {
//    factory<RemoteConfig> { RemoteConfigImpl() }
//}