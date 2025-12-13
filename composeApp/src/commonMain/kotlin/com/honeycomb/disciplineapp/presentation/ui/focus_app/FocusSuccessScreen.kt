package com.honeycomb.disciplineapp.presentation.ui.focus_app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import com.honeycomb.disciplineapp.presentation.utils.noRippleClickable
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


enum class FocusType {
    SOFT, HARD
}

enum class ScheduleType {
    NOW, LATER
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class, KoinExperimentalAPI::class)
@Composable
fun FocusSuccessScreen(
    state: CreateFocusState,
    viewModel: CreateFocusViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current

    var selectedAppTokensSize: Int by remember {
        mutableStateOf(0)
    }

    var animationCompleted by remember {
        mutableStateOf(false)
    }
    val glowingSize by animateIntAsState(
        targetValue = if (animationCompleted) 80 else 110,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    val glowingPadding by animateDpAsState(
        targetValue = if (animationCompleted) 90.dp else 60.dp,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    val transY by animateFloatAsState(
        targetValue = if (animationCompleted) 0f else 200f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    LaunchedEffect(Unit) {
        viewModel.resetTimer()
        delay(1000)
        animationCompleted = true
    }


    Scaffold(
        containerColor = Color(0xFF081717)
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
                                    Color(0xFF0A2C2D),
                                    Color(0xFF081717)
                                )
                            )
                    )
                    .padding(top = it.calculateTopPadding())
                    .noRippleClickable {
                        keyboard?.hide()
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTopBar(
                    backgroundColors = listOf(Color.Transparent, Color.Transparent),
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    startComposable = {
                        BorderIconButton(
                            modifier = Modifier
                                .bounceClick {
                                    navController.popBackStack()
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
                                        append("congrats  \uD83C\uDF89")
                                    }
                                },
                                style = CustomTextStyle
                            )
                        }
                    }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
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
                                    append("your focus session is completed")
                                }
                            },
                            style = CustomTextStyle,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 30.dp)
                        )
                    }

                    item {
                        AnimatedGlow(
                            modifier = Modifier
                                .padding(horizontal = glowingPadding),
                            showProgress = false,
                            progress = { 0f },
                            midComposable = {
                                Icon(
                                    Icons.Default.Done,
                                    contentDescription = null,
                                    tint = WhiteColor,
                                    modifier = Modifier
                                        .size(50.dp)
                                )
                            },
                            size = glowingSize
                        )
                    }

                    item {
                        AnimatedVisibility(
                            visible = animationCompleted,
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 1000
                                )
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .graphicsLayer {
                                        this.translationY = transY
                                    }
                                    .padding(bottom = 30.dp)
                                    .addStandardHorizontalPadding(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                TimerDisplay(
                                    minutes = state.time,
                                    seconds = 0,
                                    endTimeText = "${state.timerState}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally),
                                    title = ""
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = animationCompleted,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                ),
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .addStandardHorizontalPadding()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    CustomButton(
                        type = ButtonDto.ButtonType.PRIMARY_BUTTON,
                        text = "start a new focus session",
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
//                        viewModel.startTimer(state.time)
                        }
                    )

                    CustomButton(
                        type = ButtonDto.ButtonType.SECONDARY_BUTTON,
                        text = "go to home",
                        modifier = Modifier,
                        onClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun FocusSuccessScreenPreview() {
    FocusSuccessScreen(
        navController = rememberNavController(),
        state = CreateFocusState(
            timerState = TimerState.COMPLETED,
            time = 20
        ),
        viewModel = koinViewModel()
    )
}