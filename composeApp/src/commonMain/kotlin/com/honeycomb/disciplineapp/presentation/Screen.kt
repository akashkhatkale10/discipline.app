package com.honeycomb.disciplineapp.presentation

import com.honeycomb.disciplineapp.data.dto.HabitDataDto
import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusState
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    // focus app
    @Serializable
    data object FocusAppScreenRoute : Screen()
    @Serializable
    data object CreateFocusScreenRoute : Screen()
    @Serializable
    data class FocusSuccessScreenRoute(
        val state: CreateFocusState
    ) : Screen()


    @Serializable
    data object DemoScreenRoute : Screen()


    // habit
    @Serializable
    data object SplashScreenRoute : Screen()

    @Serializable
    data object MainScreenRoute : Screen()

    @Serializable
    data object StartRoutineScreenRoute : Screen()

    @Serializable
    data class AddHabitScreenRoute
        (val habitData: HabitDataDto)
         : Screen()

    @Serializable
    data class OnboardingScreenRoute(
        val data: OnboardingDto
    ) : Screen()
}