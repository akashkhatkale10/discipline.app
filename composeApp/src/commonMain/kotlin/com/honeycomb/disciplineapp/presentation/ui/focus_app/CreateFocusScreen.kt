package com.honeycomb.disciplineapp.presentation.ui.focus_app

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.RedColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.data.dto.ButtonDto
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.dashedBorder
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.ui.add_habit.formatToReadableDateTime
import com.honeycomb.disciplineapp.presentation.ui.common.pickDate
import com.honeycomb.disciplineapp.presentation.utils.Constants
import com.honeycomb.disciplineapp.presentation.utils.LocalPlatformContext
import com.honeycomb.disciplineapp.presentation.utils.noRippleClickable
import disciplineapp.composeapp.generated.resources.Res
import disciplineapp.composeapp.generated.resources.glow
import disciplineapp.composeapp.generated.resources.pause
import disciplineapp.composeapp.generated.resources.stop
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class, KoinExperimentalAPI::class)
@Composable
fun CreateFocusScreen(
    viewModel: CreateFocusViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val minutes by viewModel.minutes.collectAsState()
    val seconds by viewModel.seconds.collectAsState()
    val remainingSeconds by viewModel.timer.remainingSeconds.collectAsState()
    val theme = LocalTheme.current
    var selectedScheduleType by remember { mutableStateOf(ScheduleType.NOW) }
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current

    var selectedAppTokensSize: Int by remember {
        mutableStateOf(0)
    }

    val bgAnimProgress by animateFloatAsState(
        targetValue = if (state.timerState == TimerState.IDLE) 0f else 1f,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )
    val animatedProgress by animateFloatAsState(
        targetValue = remainingSeconds / (1 * 60f),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    // Gradient A -> B
    val startColor = lerp(
        Color(0xFF0D0D0D), // A's start color
        Color(0xFF0A2C2D),
        bgAnimProgress
    )
    val endColor = lerp(
        Color(0xFF0D0D0D), // A's end color
        Color(0xFF081717),
        bgAnimProgress
    )

    LaunchedEffect(state) {
        if (state.timerState == TimerState.COMPLETED) {
            navController.navigate(Screen.FocusSuccessScreenRoute(
                state = CreateFocusState(
                    timerState = state.timerState,
                    time = viewModel.timer.totalDurationSeconds / 60
                )
            )) {
                popUpTo<Screen.CreateFocusScreenRoute> {
                    inclusive = true
                }
            }
        }
    }


    Scaffold(
        containerColor = endColor
    ) {
        Box {
            Column(
                modifier = modifier
                    .padding(bottom = 30.dp)
                    .fillMaxSize()
                    .background(
                        brush = Brush
                            .verticalGradient(
                                colors = listOf(
                                    startColor,
                                    endColor
                                )
                            )
                    )
                    .padding(top = it.calculateTopPadding())
                    .noRippleClickable {
                        keyboard?.hide()
                    },
            ) {
                CustomTopBar(
                    backgroundColors = listOf(Color.Transparent, Color.Transparent),
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    startComposable = {
                        BorderIconButton(
                            modifier = Modifier
                                .bounceClick {
                                    println("popped: ${navController.popBackStack()}")
                                },
                            iconComposable = {
                                Icon(
                                    Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = theme.quaternaryColor
                                )
                            },
                        )
                    },
                    endComposable = {},
                    midComposable = {
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
                                        append("start a focus session")
                                    }
                                },
                                style = CustomTextStyle
                            )
                        }
                    }
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = theme.quaternaryColor,
                                fontFamily = nunitoFontFamily
                            )
                        ) {
                            append("every distraction is a betrayal your future self will remember")
                        }
                    },
                    style = CustomTextStyle,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 30.dp)
                )


                AnimatedGlow(
                    modifier = Modifier
                        .padding(horizontal = 60.dp),
                    showProgress = state.timerState != TimerState.IDLE,
                    progress = { animatedProgress }
                )
            }

            Column(
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .align(Alignment.BottomCenter)
                    .addStandardHorizontalPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.timerState != TimerState.IDLE) {
                    TimerDisplay(
                        minutes = minutes,
                        seconds = seconds,
                        endTimeText = "ends at ${calculateEndTimeKotlinx(remainingSeconds / 60)}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        title = "time remaining"
                    )
                }

                // Time Picker
                if (state.timerState == TimerState.IDLE) {
                    TimePickerRow(
                        time = state.time,
                        onTimeChange = { t ->
                            viewModel.updateTime(t)
                        }
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FocusOptionRow(
                        end = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "$selectedAppTokensSize",
                                    style = CustomTextStyle.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = RedColor
                                    )
                                )

                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = theme.quaternaryColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        title = "\uD83D\uDEAB   blocked apps",
                        onClick = {
                            viewModel.appBlocker.selectApps(
                                exclude = true,
                                onAppsSelected = {
                                    selectedAppTokensSize = it.size
                                }
                            )
                        },
                        modifier = Modifier
                            .weight(0.55f)
                    )

                    // Settings Section
                    FocusOptionRow(
                        end = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "all",
                                    style = CustomTextStyle.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = theme.quaternaryColor
                                    )
                                )
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = theme.quaternaryColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        title = "\uD83D\uDEAB   settings",
                        modifier = Modifier
                            .weight(0.45f)
                    )
                }

                FocusOptionRow(
                    end = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "hard",
                                style = CustomTextStyle.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = theme.quaternaryColor
                                )
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = theme.quaternaryColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    titleIcon = {
                        AnimatedLogo(14)
                    },
                    title = "focus mode",
                    modifier = Modifier
                )

                if (state.timerState == TimerState.RUNNING || state.timerState == TimerState.PAUSED) {
                    CustomButton(
                        type = ButtonDto.ButtonType.TERTIARY_BUTTON,
                        text = if (state.timerState == TimerState.RUNNING) "pause the session" else "resume the session",
                        modifier = Modifier,
                        startIconComposable = {
                            if (state.timerState == TimerState.RUNNING) {
                                Image(
                                    imageVector = vectorResource(Res.drawable.pause),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(14.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    tint = Color(0xFF121212),
                                    modifier = Modifier
                                        .size(20.dp)
                                )
                            }
                        },
                        onClick = {
                            if (state.timerState == TimerState.RUNNING) {
                                viewModel.pauseTimer()
                            } else {
                                viewModel.resumeTimer()
                            }
                        },
                        backgroundColor = WhiteColor,
                        borderColor = WhiteColor,
                        textColor = Color(0xFF121212)
                    )
                }


                if (state.timerState == TimerState.IDLE) {
                    CustomButton(
                        type = ButtonDto.ButtonType.PRIMARY_BUTTON,
                        text = "start timer",
                        modifier = Modifier,
                        startIconComposable = {
                            Icon(
                                Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = WhiteColor,
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        },
                        onClick = {
                            viewModel.startTimer(state.time)
                        }
                    )
                }

                if (state.timerState == TimerState.RUNNING || state.timerState == TimerState.PAUSED) {
                    CustomButton(
                        type = ButtonDto.ButtonType.SECONDARY_BUTTON,
                        text = "stop",
                        modifier = Modifier,
                        startIconComposable = {
                            Image(
                                imageVector = vectorResource(Res.drawable.stop),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(16.dp)
                            )
                        },
                        onClick = {
                            viewModel.stopTimer()
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun AnimatedGlow(
    showProgress: Boolean,
    progress: () -> Float = { 0f },
    midComposable: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    size: Int = 110
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val opacity by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                Res.drawable.glow
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    this.alpha = opacity
                }
        )

        AnimatedLogo(
            size = size,
            modifier = Modifier
                .graphicsLayer {
                    this.rotationZ = rotation
                }
                .bounceClick {},
            showBorder = true
        )

        if (showProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .graphicsLayer {
                        this.scaleX = -1f
                    },
                progress = progress,
                trackColor = WhiteColor.copy(0.3f),
                strokeWidth = 7.dp,
                color = WhiteColor,
                gapSize = 0.dp
            )
        }

        midComposable()
    }
}

@Composable
fun SchedulePickerRow(
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    onStartPicked: (LocalDateTime) -> Unit,
    onEndPicked: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    val context = LocalPlatformContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .addStandardHorizontalPadding()
    ) {
        Box(
            modifier = Modifier
                .bounceClick {
                    pickDate(
                        context = context,
                        onDatePicked = { dateTime ->
                            onStartPicked(dateTime)
                        }
                    )
                }
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = startDate.formatToReadableDateTime(),
                style = CustomTextStyle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }

        Text(
            text = "-",
            style = CustomTextStyle.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = WhiteColor
            )
        )

        Box(
            modifier = Modifier
                .bounceClick {
                    pickDate(
                        context = context,
                        onDatePicked = { dateTime ->
                            onEndPicked(dateTime)
                        }
                    )
                }
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = endDate.formatToReadableDateTime(),
                style = CustomTextStyle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }
    }
}

@Composable
fun DashedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    val theme = LocalTheme.current
    BasicTextField(
        value = value,
        onValueChange = { new ->
            onValueChange(new)
        },
        cursorBrush = Brush.verticalGradient(
            colors = listOf(WhiteColor, WhiteColor)
        ),
        textStyle = CustomTextStyle.copy(
            fontSize = 14.sp,
            color = theme.titleColor
        ),
        modifier = modifier.fillMaxWidth(),
        decorationBox = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 44.dp)
                    .background(theme.secondaryButtonColor, shape = RoundedCornerShape(14.dp))
                    .dashedBorder(
                        strokeWidth = 1.dp,
                        color = SubtitleTextColor,
                        cornerRadius = 14.dp
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(16.dp)
                    ) {
                        icon()
                    }

                    if (value.isEmpty()) {
                        Text(
                            text = "name your focus session",
                            style = CustomTextStyle.copy(
                                fontSize = 14.sp,
                                color = theme.quaternaryColor
                            )
                        )
                    }

                    it()
                }
            }
        }
    )
}

@Composable
fun FocusOptionRow(
    end: @Composable () -> Unit = {},
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    subtitle: String = ""
) {
    val theme = LocalTheme.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(36.dp)
                .bounceClick(onClick = onClick)
                .fillMaxWidth()
                .background(
                    color = theme.secondaryButtonColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(end = 8.dp, start = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = CustomTextStyle.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = WhiteColor
                    ),
                    modifier = Modifier.weight(1f)
                )

                end()
            }
        }

        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = CustomTextStyle.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = theme.quaternaryColor
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun FocusOptionRow(
    end: @Composable () -> Unit = {},
    title: String,
    titleIcon: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    subtitle: String = ""
) {
    val theme = LocalTheme.current
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = modifier
                .bounceClick(onClick = onClick)
                .fillMaxWidth()
                .background(
                    color = theme.secondaryButtonColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                titleIcon()


                Text(
                    text = title,
                    style = CustomTextStyle.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = WhiteColor
                    ),
                    modifier = Modifier.weight(1f)
                )

                end()
            }
        }

        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = CustomTextStyle.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = theme.quaternaryColor
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun TimePickerRow(
    time: Int,
    onTimeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .addStandardHorizontalPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Minus Button
        Box(
            modifier = Modifier
                .width(50.dp)
                .bounceClick {
                    if (time > Constants.getMinimumTime()) {
                        onTimeChange(time - 5)
                    }
                }
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "—",
                style = CustomTextStyle.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }

        // Time Display
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$time mins",
                style = CustomTextStyle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }

        // Plus Button
        Box(
            modifier = Modifier
                .width(50.dp)
                .bounceClick {
                    onTimeChange(time + 5)
                }
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                style = CustomTextStyle.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }
    }
}

@Composable
@Preview
fun CreateFocusScreenPreview() {
    CreateFocusScreen(
        navController = rememberNavController(),
        viewModel = koinViewModel<CreateFocusViewModel>()
    )
}

@Composable
@Preview
fun GradientCircularShadowBoxPreview() {

}

