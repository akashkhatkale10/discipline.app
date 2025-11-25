package com.honeycomb.disciplineapp.presentation.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.home.ActionTitleSubtitle
import com.honeycomb.disciplineapp.presentation.ui.home.TitleSubtitleAction
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SplashScreen(
    navController: NavController
) {
    val splashViewModel = koinViewModel<SplashViewModel>()
    val state by splashViewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state.user != null) {
            navController.navigate(Screen.MainScreenRoute) {
                popUpTo<Screen.SplashScreenRoute> {
                    inclusive = true
                }
            }
        } else if (state.onboarding != null) {
            navController.navigate(Screen.OnboardingScreenRoute(
                    data = state.onboarding!!
                )
            ) {
                popUpTo<Screen.SplashScreenRoute> {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        containerColor = BackgroundColor,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ActionTitleSubtitle(
                title = "Unparalleled Discipline",
                subtitle = "Building your no-excuse zone..",
                action = {
                    AnimatedLogo(
                        size = 80,
                        modifier = Modifier
                            .padding(top = 30.dp)
                    )
                }
            )
        }
    }
}