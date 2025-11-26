package com.honeycomb.disciplineapp.presentation.ui.start_routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.LightBackgroundColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.common.ButtonComponent
import com.honeycomb.disciplineapp.presentation.ui.common.ConditionalLogo
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomSmallButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.ui.common.Logo
import com.honeycomb.disciplineapp.presentation.ui.home.HomeScreenShimmer
import com.honeycomb.disciplineapp.presentation.ui.home.TitleSubtitleAction
import com.honeycomb.disciplineapp.presentation.utils.Constants.HORIZONTAL_PADDING
import com.honeycomb.disciplineapp.presentation.utils.DataUtils.getCurrentFormattedDate
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun StartRoutineScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<StartRoutineViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getStartRoutine()
    }

    Scaffold(
        containerColor = BackgroundColor,
        modifier = modifier,
        topBar = {
            CustomTopBar(
                dividerVisible = true,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                startComposable = {
                    BorderIconButton(
                        iconComposable = {
                            Icon(
                                Icons.Default.ArrowBack,
                                tint = SubtitleTextColor,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                                    .bounceClick {
                                        navController.popBackStack()
                                    }
                            )
                        }
                    )
                },
                midComposable = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = WhiteColor,
                                    fontFamily = nunitoFontFamily
                                )
                            ) {
                                if (state.data != null) {
                                    append(state.data?.title ?: "Start a routine")
                                }
                            }
                        },
                        style = CustomTextStyle
                    )
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {

            when {

                state.isLoading -> {
                    HomeScreenShimmer(
                        modifier = Modifier
                            .padding(horizontal = HORIZONTAL_PADDING.dp)
                            .fillMaxSize()
                    )
                }

                state.data != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(120.dp)
                    ) {
                        state.data?.options?.forEachIndexed { index, option ->
                            TitleSubtitleAction(
                                title = option.title.orEmpty(),
                                subtitle = option.subtitle.orEmpty(),
                                action = {
                                    ButtonComponent(
                                        button = option.button,
                                        endIconComposable = {
                                            Icon(
                                                Icons.Default.Add,
                                                contentDescription = null,
                                                tint = WhiteColor,
                                                modifier = Modifier
                                                    .size(18.dp)
                                            )
                                        },
                                        onClick = {
                                            val isHabit = option.type?.equals("HABIT", ignoreCase = true) == true
                                            if (isHabit && option.habitData != null) {
                                                navController.navigate(
                                                    Screen.AddHabitScreenRoute(
                                                        habitData = option.habitData
                                                    )
                                                )
                                            }
                                        }
                                    )
                                },
                                titleComposable = {
                                    ConditionalLogo(
                                        type = option.type
                                    )
                                }
                            )
                        }
                    }
                }

                state.error != null -> {

                }
            }
        }
    }
}