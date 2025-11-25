package com.honeycomb.disciplineapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.models.OnboardingScreenType
import com.honeycomb.disciplineapp.presentation.ui.main.MainScreen
import com.honeycomb.disciplineapp.presentation.ui.onboarding.OnboardingScreen
import com.honeycomb.disciplineapp.presentation.ui.splash.SplashScreen
import com.honeycomb.disciplineapp.presentation.ui.start_routine.StartRoutineScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.reflect.typeOf

@Composable
@Preview
fun App() {
    DisciplineAppTheme {
        val navController = rememberNavController()

        Scaffold {
            NavHost(
                modifier = Modifier
                    .background(BackgroundColor)
                    .padding(it),
                navController = navController,
                startDestination = Screen.SplashScreenRoute
            ) {
                // splash
                composable<Screen.SplashScreenRoute> {
                    SplashScreen(
                        navController = navController
                    )
                }

                // onboarding
                composable<Screen.OnboardingScreenRoute>(
                    typeMap = mapOf(typeOf<OnboardingDto>() to OnboardingScreenType)
                ) {
                    val args = it.toRoute<Screen.OnboardingScreenRoute>()
                    OnboardingScreen(
                        navController = navController,
                        data = args.data,
                        modifier = Modifier
                    )
                }

                // home
                composable<Screen.MainScreenRoute> {
                    MainScreen(navController = navController, modifier = Modifier)
                }

                // start routine
                composable<Screen.StartRoutineScreenRoute> {
                    StartRoutineScreen(
                        navController = navController,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
