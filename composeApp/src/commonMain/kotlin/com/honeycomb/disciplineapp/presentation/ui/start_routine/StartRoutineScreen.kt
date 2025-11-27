package com.honeycomb.disciplineapp.presentation.ui.start_routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.honeycomb.disciplineapp.data.dto.RoutineOptionDto
import com.honeycomb.disciplineapp.data.dto.SetRoutineDto
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.common.ButtonComponent
import com.honeycomb.disciplineapp.presentation.ui.common.ConditionalLogo
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.ui.common.Logo
import com.honeycomb.disciplineapp.presentation.ui.home.HomeScreenShimmer
import com.honeycomb.disciplineapp.presentation.ui.home.TitleSubtitleAction
import com.honeycomb.disciplineapp.presentation.utils.Constants.HABIT
import com.honeycomb.disciplineapp.presentation.utils.Constants.HORIZONTAL_PADDING
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.noRippleClickable
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class, ExperimentalComposeUiApi::class)
@Composable
fun StartRoutineScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel = koinViewModel<StartRoutineViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val result = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("habit", initialValue = null)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(result) {
        result?.value?.let {
            runCatching {
                viewModel.addHabit(Json.decodeFromString(it))
            }
        }
    }
    val onActionClick: (RoutineOptionDto) -> Unit = { option ->
        val isHabit = option.type?.equals(HABIT, ignoreCase = true) == true
        if (isHabit && option.habitData != null) {
            option.habitData.let {
                navController.navigate(
                    Screen.AddHabitScreenRoute(
                        habitData = it
                    )
                )
            }
        }
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
                .padding(it)
                .padding(top = 100.dp),
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
                        verticalArrangement = Arrangement.spacedBy(120.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        state.data?.options?.forEachIndexed { index, option ->
                            if (option.type == HABIT) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(34.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
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
                                                    onActionClick(option)
                                                }
                                            )
                                        },
                                        titleComposable = {
                                            if (state.routine?.habits?.isEmpty() == true) {
                                                ConditionalLogo(
                                                    type = option.type
                                                )
                                            }
                                        },
                                        middleComposable = {
                                            if (state.routine?.habits?.isNotEmpty() == true) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 30.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.spacedBy(26.dp)
                                                ) {
                                                    state.routine?.habits?.forEach {
                                                        Row(
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(
                                                                16.dp
                                                            ),
                                                            modifier = Modifier
                                                                .noRippleClickable {

                                                                }
                                                        ) {
                                                            HabitPoint(
                                                                habitDto = it
                                                            )

                                                            Icon(
                                                                Icons.Default.KeyboardArrowRight,
                                                                contentDescription = null,
                                                                tint = SubtitleTextColor
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            } else {
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
                                                onActionClick(option)
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
                }

                state.error != null -> {

                }
            }
        }
    }
}

@Composable
fun HabitPoint(
    habitDto: SetRoutineDto.SetHabitDto
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {
        Logo(
            size = 10
        )
        Text(
            text = habitDto.details["name"].orEmpty(),
            style = CustomTextStyle.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = WhiteColor
            )
        )
    }
}