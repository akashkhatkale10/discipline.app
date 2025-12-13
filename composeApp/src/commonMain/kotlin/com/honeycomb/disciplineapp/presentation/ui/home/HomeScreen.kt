package com.honeycomb.disciplineapp.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.RedColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.data.dto.ButtonDto
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.focus_app.AppBlocker
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusViewModel
import com.honeycomb.disciplineapp.presentation.ui.focus_app.TimerState
import com.honeycomb.disciplineapp.presentation.utils.Constants.HORIZONTAL_PADDING
import com.honeycomb.disciplineapp.presentation.utils.DataUtils.getCurrentFormattedDate
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    focusViewModel: CreateFocusViewModel,
    isExpanded: Boolean,
    navController: NavController,
    onExpandClick: () -> Unit
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusState by focusViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getHomeData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        CustomTopBar(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            startComposable = {

            },
            endComposable = {

            },
            midComposable = {
                val formatted = getCurrentFormattedDate()
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                                append("${formatted.day}, ")
                            }
                            withStyle(
                                SpanStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = SubtitleTextColor,
                                    fontFamily = nunitoFontFamily
                                )
                            ) {
                                append("${formatted.month} ${formatted.date}")
                            }
                        },
                        style = CustomTextStyle
                    )
                }
            }
        )

        when {
            state.isLoading -> {
                HomeScreenShimmer(
                    modifier = Modifier
                        .padding(horizontal = HORIZONTAL_PADDING.dp)
                        .fillMaxSize()
                        .padding(top = 100.dp)
                )
            }

            state.data != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 30.dp, bottom = 30.dp),
                ) {
                    if (state.data!!.isNotEmpty()) {

                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                        ){
                            item {
                                TitleSubtitleAction(
                                    title = "start a first focus session",
                                    subtitle = "every distraction is a betrayal your future self will remember",
                                    modifier = Modifier.addStandardHorizontalPadding(),
                                    action = {
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            if (focusState.timerState == TimerState.IDLE
                                                || focusState.timerState == TimerState.STOPPED
                                                || focusState.timerState == TimerState.COMPLETED) {
                                                CustomButton(
                                                    text = "start a focus session",
                                                    startIconComposable = {
                                                        Icon(
                                                            Icons.Default.PlayArrow,
                                                            contentDescription = null,
                                                            tint = WhiteColor,
                                                            modifier = Modifier
                                                                .size(18.dp)
                                                        )
                                                    },
                                                    onClick = {
                                                        navController
                                                            .navigate(Screen.CreateFocusScreenRoute)
                                                    }
                                                )
                                            }

                                            CustomButton(
                                                text = "schedule a focus session",
                                                startIconComposable = {
                                                    Icon(
                                                        Icons.Default.DateRange,
                                                        contentDescription = null,
                                                        tint = WhiteColor,
                                                        modifier = Modifier
                                                            .size(14.dp)
                                                    )
                                                },
                                                onClick = {

                                                },
                                                type = ButtonDto.ButtonType.SECONDARY_BUTTON
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun TitleSubtitleAction(
    title: String,
    subtitle: String,
    action: @Composable () -> Unit,
    titleComposable: @Composable () -> Unit = {},
    middleComposable: (@Composable () -> Unit) = {  },
    modifier: Modifier = Modifier,
    titleFontWeight: FontWeight = FontWeight.Bold,
    titleFontSize: Int = 16,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            titleComposable()
            Text(
                title,
                style = CustomTextStyle.copy(
                    color = TitleTextColor,
                    fontSize = titleFontSize.sp,
                    fontWeight = titleFontWeight
                ),
                textAlign = TextAlign.Center
            )
        }
        Text(
            subtitle,
            style = CustomTextStyle.copy(
                color = SubtitleTextColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .padding(top = 14.dp),
            textAlign = TextAlign.Center
        )

        middleComposable()

        Box(
            modifier = Modifier
                .padding(top = 30.dp)
        ) {
            action()
        }
    }
}

@Composable
fun ActionTitleSubtitle(
    title: String,
    subtitle: String,
    action: @Composable () -> Unit,
    titleComposable: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box() {
            action()
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(top = 30.dp)
        ) {
            titleComposable()
            Text(
                title,
                style = CustomTextStyle.copy(
                    color = TitleTextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
        Text(
            subtitle,
            style = CustomTextStyle.copy(
                color = SubtitleTextColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .padding(top = 14.dp),
            textAlign = TextAlign.Center
        )
    }
}