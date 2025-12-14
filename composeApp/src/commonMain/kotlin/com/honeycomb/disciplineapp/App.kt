package com.honeycomb.disciplineapp

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
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
import com.honeycomb.disciplineapp.presentation.focus_app.ui.focus_screen.FocusAppScreen
import com.honeycomb.disciplineapp.presentation.models.CreateFocusStateType
import com.honeycomb.disciplineapp.presentation.ui.DemoScreen
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusScreen
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusState
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusViewModel
import com.honeycomb.disciplineapp.presentation.ui.focus_app.FocusSuccessScreen
import com.honeycomb.disciplineapp.presentation.ui.start_routine.StartRoutineScreen
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.reflect.typeOf

@Composable
@Preview
fun App() {
    DisciplineAppTheme {
        val navController = rememberNavController()
        val theme = LocalTheme.current
        val focusViewModel = koinViewModel<CreateFocusViewModel>()

        LaunchedEffect(Unit) {
            focusViewModel.initVm()
        }

        Scaffold {
            NavHost(
                modifier = Modifier
                    .background(theme.backgroundColorGradient.first()),
//                    .padding(top = it.calculateTopPadding() ),
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
                composable<Screen.CreateFocusScreenRoute>(
                    enterTransition = {
                        slideInVertically(
                            animationSpec = tween(
                                durationMillis = 300
                            ),
                            initialOffsetY = { it }
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            animationSpec = tween(
                                durationMillis = 300
                            ),
                            targetOffsetY = { it }
                        )
                    }
                ) {
                    CreateFocusScreen(
                        navController = navController,
                        viewModel = focusViewModel
                    )
                }
                composable<Screen.FocusSuccessScreenRoute>(
                    enterTransition = {
                        slideInVertically(
                            animationSpec = tween(
                                durationMillis = 300
                            ),
                            initialOffsetY = { it }
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            animationSpec = tween(
                                durationMillis = 300
                            ),
                            targetOffsetY = { it }
                        )
                    },
                    typeMap = mapOf(typeOf<CreateFocusState>() to CreateFocusStateType)
                ) {
                    val args = it.toRoute<Screen.FocusSuccessScreenRoute>()
                    FocusSuccessScreen(
                        navController = navController,
                        state = args.state,
                        viewModel = focusViewModel
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
                    MainScreen(
                        focusViewModel = focusViewModel,
                        navController = navController,
                        modifier = Modifier
                    )
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
