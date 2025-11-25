package com.honeycomb.disciplineapp.presentation

import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object SplashScreenRoute : Screen()

    @Serializable
    data object MainScreenRoute : Screen()

    @Serializable
    data object StartRoutineScreenRoute : Screen()

    @Serializable
    data class OnboardingScreenRoute(
        val data: OnboardingDto
    ) : Screen()
}