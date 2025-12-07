package com.honeycomb.disciplineapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.honeycomb.disciplineapp.data.dto.HabitDataDto
import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.models.HabitDataScreenType
import com.honeycomb.disciplineapp.presentation.models.OnboardingScreenType
import com.honeycomb.disciplineapp.presentation.ui.main.MainScreen
import com.honeycomb.disciplineapp.presentation.ui.onboarding.OnboardingScreen
import com.honeycomb.disciplineapp.presentation.ui.splash.SplashScreen
import com.honeycomb.disciplineapp.presentation.ui.add_habit.AddHabitScreen
import com.honeycomb.disciplineapp.presentation.focus_app.ui.focus_screen.FocusAppScreen
import com.honeycomb.disciplineapp.presentation.ui.DemoScreen
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusScreen
import com.honeycomb.disciplineapp.presentation.ui.start_routine.StartRoutineScreen
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.reflect.typeOf

@Composable
@Preview
fun App() {
    DisciplineAppTheme {
        val navController = rememberNavController()
        val theme = LocalTheme.current

        Scaffold {
            NavHost(
                modifier = Modifier
                    .background(theme.backgroundColorGradient.first())
                    .padding(it),
                navController = navController,
                startDestination = Screen.SplashScreenRoute
            ) {
                // focus app
                composable<Screen.FocusAppScreenRoute> {
                    FocusAppScreen(
                        navController = navController,
                        modifier = Modifier
                    )
                }
                composable<Screen.DemoScreenRoute> {
                    DemoScreen(
                        navController = navController
                    )
                }
                composable<Screen.CreateFocusScreenRoute> {
                    CreateFocusScreen(
                        navController = navController
                    )
                }








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

                // add habit
                composable<Screen.AddHabitScreenRoute>(
                    typeMap = mapOf(typeOf<HabitDataDto>() to HabitDataScreenType)
                ) {
                    val args = it.toRoute<Screen.AddHabitScreenRoute>()
                    AddHabitScreen(
                        navController = navController,
                        habitData = args.habitData,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
