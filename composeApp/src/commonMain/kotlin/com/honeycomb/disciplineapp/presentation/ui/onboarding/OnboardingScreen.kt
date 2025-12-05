package com.honeycomb.disciplineapp.presentation.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.BlueColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TertiaryColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.data.dto.ButtonDto
import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import com.honeycomb.disciplineapp.data.dto.StyleText
import com.honeycomb.disciplineapp.data.dto.UserData
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.focus_app.AppBlocker
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.ButtonComponent
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.ui.common.StyleText
import com.honeycomb.disciplineapp.presentation.utils.DataUtils.getCurrentFormattedDate
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import com.honeycomb.disciplineapp.presentation.utils.addBlurBackground
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.noRippleClickable
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import disciplineapp.composeapp.generated.resources.Res
import disciplineapp.composeapp.generated.resources.facebook
import disciplineapp.composeapp.generated.resources.gmail
import disciplineapp.composeapp.generated.resources.instagram
import disciplineapp.composeapp.generated.resources.permission
import disciplineapp.composeapp.generated.resources.snapchat
import disciplineapp.composeapp.generated.resources.youtube
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


enum class OnboardingPages {
    FIRST, SECOND
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    data: OnboardingDto,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<OnboardingViewModel>()
    val state by viewModel.loginState.collectAsStateWithLifecycle()
    var authReady by remember { mutableStateOf(false) }
    val theme = LocalTheme.current
    val currentPage = remember { mutableStateOf(OnboardingPages.FIRST) }

    LaunchedEffect(Unit) {
        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(
                serverId = "1015502528236-56fd6ig469qluu5n47u9ehekoamvh9hu.apps.googleusercontent.com"
            )
        )
        authReady = true
    }

    LaunchedEffect(state) {
        if (state.data != null) {
            navController.navigate(Screen.MainScreenRoute) {
                popUpTo<Screen.OnboardingScreenRoute> {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color(0xFF0D0D0D),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = theme.backgroundColorGradient
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTopBar(
                    backgroundColor = Color.Transparent,
                    modifier = Modifier
                        .addStandardHorizontalPadding(),
                    midComposable = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedLogo(
                                size = 12,
                                modifier = Modifier
                            )
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
                                        append("lockdown")
                                    }
                                },
                                style = CustomTextStyle
                            )
                        }
                    }
                )

                AnimatedContent(
                    targetState = currentPage.value,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(1000)) with
                                fadeOut(animationSpec = tween(100))
                    }
                ) {
                    when (it) {
                        OnboardingPages.FIRST -> {
                            OnboardingFirstScreen(
                                modifier = Modifier,
                                onPermissionApproved = {
                                    currentPage.value = OnboardingPages.SECOND
                                }
                            )
                        }
                        OnboardingPages.SECOND -> {
                            OnboardingSecondScreen(
                                modifier = Modifier,
                            )
                        }
                    }
                }


//                data.login?.title.let {
//                    StyleText(
//                        modifier = Modifier
//                            .padding(top = 50.dp)
//                            .addStandardHorizontalPadding(),
//                        style = it,
//                        textAlign = TextAlign.Center,
//                        lineHeight = 50,
//                    )
//                }
//                data.login?.subTitle.let {
//                    StyleText(
//                        modifier = Modifier
//                            .padding(top = 16.dp)
//                            .padding(horizontal = 30.dp),
//                        style = it,
//                        textAlign = TextAlign.Center,
//                        lineHeight = 30,
//                    )
//                }
//                Spacer(
//                    modifier = Modifier
//                        .weight(1f)
//                )
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    data.login?.footer.let {
//                        StyleText(
//                            modifier = Modifier
//                                .padding(horizontal = 30.dp),
//                            style = it,
//                            textAlign = TextAlign.Center,
//                            lineHeight = 30,
//                        )
//                    }
//                    data.login?.firstButtons?.forEach {
//                        val startIcon: @Composable () -> Unit = {
//                            if (it.action == "next") {
//                                Icon(
//                                    Icons.Default.ArrowForward,
//                                    contentDescription = null,
//                                    tint = WhiteColor
//                                )
//                            }
//                        }
//                        ButtonComponent(
//                            button = it,
//                            onClick = {
//
//                            },
//                            endIconComposable = {
//                                startIcon()
//                            },
//                            modifier = Modifier
//                                .addStandardHorizontalPadding()
//                        )
//                    }
//                }
            }
        }
    }
}

@Composable
fun OnboardingSecondScreen(
    modifier: Modifier.Companion
) {
    var isVisible by remember { mutableStateOf(false) }
    val titleOpacity by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec =  tween(durationMillis = 2000)
    )

    LaunchedEffect(Unit) {
        delay(500)
        isVisible = true
    }

    Column(
        modifier = modifier
    ) {
        StyleText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .addStandardHorizontalPadding()
                .graphicsLayer {
                    this.alpha = titleOpacity
                },
            style = listOf(
                StyleText(
                    text = "analysing your",
                    fontWeight = "regular",
                    fontSize = 20,
                    color = "#ffffff"
                ),
                StyleText(
                    text = "\nscreen time",
                    fontWeight = "bold",
                    fontSize = 26,
                    color = "#ffffff"
                )
            ),
            textAlign = TextAlign.Center,
            lineHeight = 50,
        )
    }
}

@Composable
fun OnboardingFirstScreen(
    modifier: Modifier,
    onPermissionApproved: () -> Unit
) {
    val appBlocked = AppBlocker()
    val scope = rememberCoroutineScope()
    var isVisible by remember { mutableStateOf(false) }
    var upAnimEnded by remember { mutableStateOf(false) }
    var slideEnded by remember { mutableStateOf(false) }
    var notNowEnded by remember { mutableStateOf(false) }
    var isScreenTimeVisible by remember { mutableStateOf(false) }
    val upTime = 500
    val hapticFeedback = LocalHapticFeedback.current

    val titleOpacity by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec =  tween(durationMillis = 2000)
    )
    val subtitleOpacity by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec =  tween(delayMillis = 200, durationMillis = 2000)
    )
    val notNowOpacity by animateFloatAsState(
        targetValue = if (slideEnded) 1f else 0f,
        animationSpec =  tween(delayMillis = 200, durationMillis = 2000),
        finishedListener = {
            notNowEnded = true

            scope.launch {
                delay(1000)
                isScreenTimeVisible = true
            }
        }
    )
    val slideAnim by animateFloatAsState(
        targetValue = if (upAnimEnded) -3000f else 0f,
        animationSpec =  tween(delayMillis = 0, durationMillis = 500),
        finishedListener = {
            slideEnded = true
        }
    )
    val screenTimePosition by animateFloatAsState(
        targetValue = if (isScreenTimeVisible.not()) 3000f else 0f,
        animationSpec =  tween(delayMillis = 0, durationMillis = 500),
    )
    var isEnabled by remember { mutableStateOf(true) }



    LaunchedEffect(Unit) {
        delay(500)
        isVisible = true
    }

    Column(
        modifier = modifier
    ) {
        StyleText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .addStandardHorizontalPadding()
                .graphicsLayer {
                    this.alpha = titleOpacity
                },
            style = listOf(
                StyleText(
                    text = "your phone ",
                    fontWeight = "regular",
                    fontSize = 20,
                    color = if (slideEnded) "#30ffffff" else "#ffffff"
                ),
                StyleText(
                    text = "owns you",
                    fontWeight = if (slideEnded) "regular" else "bold",
                    fontSize = 26,
                    color = if (slideEnded) "#30ffffff" else "#ffffff"
                )
            ),
            textAlign = TextAlign.Center,
            lineHeight = 50,
            textDecoration = if (slideEnded) TextDecoration.LineThrough else null
        )

        StyleText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .addStandardHorizontalPadding()
                .graphicsLayer {
                    this.alpha = notNowOpacity
                },
            style = listOf(
                StyleText(
                    text = "but not now",
                    fontWeight = "bold",
                    fontSize = 26,
                    color = "#ffffff"
                )
            ),
            textAlign = TextAlign.Center,
            lineHeight = 50,
        )

        Box {
            ScreenTimePermissionContent(
                screenTimePosition = screenTimePosition,
                subtitleOpacity = subtitleOpacity,
                isEnabled = isEnabled,
                onPermissionClick = {
                    appBlocked.requestPermission(
                        onFailure = {
                            isEnabled = true
                        },
                        onSuccess = {
                            isEnabled = true
                            onPermissionApproved()
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        this.translationX = slideAnim
                    }
            ) {
                StyleText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .addStandardHorizontalPadding()
                        .graphicsLayer {
                            this.alpha = subtitleOpacity
                        },
                    style = listOf(
                        StyleText(
                            text = "Average user opens Instagram ",
                            fontWeight = "medium",
                            fontSize = 14,
                            color = "#30ffffff"
                        ),
                        StyleText(
                            text = "\n87 times",
                            fontWeight = "bold",
                            fontSize = 14,
                            color = "#ffffff"
                        ),
                        StyleText(
                            text = "  a day",
                            fontWeight = "medium",
                            fontSize = 14,
                            color = "#30ffffff"
                        ),
                    ),
                    textAlign = TextAlign.Center,
                    lineHeight = 30,
                )

                val items = listOf(
                    "Instagram",
                    "Instagram",
                    "Youtube",
                    "Snapchat",
                    "Facebook",
                    "Facebook",
                    "Gmail",
                    "Instagram",
                    "Snapchat",
                    "Instagram",
                )
                val state = rememberScrollState()
                Column(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .verticalScroll(state)
                        .addStandardHorizontalPadding()
                ) {
                    items.forEachIndexed { index, title ->
                        var isVisible by remember { mutableStateOf(false) }
                        val opacity by animateFloatAsState(
                            targetValue = if (isVisible) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = 1000,
                                delayMillis = index * 200
                            ),
                        )
                        val y by animateFloatAsState(
                            targetValue = if (isVisible) 0f else 3000f,
                            animationSpec = tween(
                                durationMillis = 1000,
                                delayMillis = index * 200
                            ),
                            finishedListener = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (index == items.lastIndex) {
                                    scope.launch {
                                        delay(1000)
                                        upAnimEnded = true
                                    }
                                }
                            }
                        )
                        LaunchedEffect(Unit) {
                            isVisible = true
                        }
                        val icon = when (title) {
                            "Instagram" -> Res.drawable.instagram
                            "Youtube" -> Res.drawable.youtube
                            "Snapchat" -> Res.drawable.snapchat
                            "Facebook" -> Res.drawable.facebook
                            else -> Res.drawable.gmail
                        }
                        OnboardingNotification(
                            title = title,
                            icon = icon,
                            modifier = Modifier
                                .graphicsLayer {
                                    this.alpha = opacity
                                    this.translationY = y
                                }
                                .padding(top = (14).dp)
                                .fillMaxWidth()
                        )
                    }
                }


//                Box(
//                    modifier = Modifier
//                        .padding(top = 50.dp)
//                        .fillMaxWidth()
//                        .addStandardHorizontalPadding(),
//                ) {
//                    var startAnim by remember {
//                        mutableStateOf(false)
//                    }
//                    val haptic = LocalHapticFeedback.current
//                    LaunchedEffect(Unit) {
//                        delay(2000)
//                        startAnim = true
//                    }
//
//                    repeat(4) {
//                        val offset by animateIntOffsetAsState(
//                            targetValue = if (startAnim) {
//                                IntOffset(0, 0)
//                            } else {
//                                IntOffset(0, 2000)
//                            },
//                            label = "offset",
//                            animationSpec = tween(
//                                durationMillis = upTime,
//                                delayMillis = it * 250
//                            ),
//                            finishedListener = {
//                                haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.Companion.LongPress)
//                            }
//                        )
//                        OnboardingNotification(
//                            title = "Instagram",
//                            icon = Res.drawable.instagram,
//                            modifier = Modifier
//                                .offset {
//                                    offset
//                                }
//                                .padding(top = (it * 30).dp)
//                                .fillMaxWidth()
//                        )
//                    }
//                }
//
//                Box(
//                    modifier = Modifier
//                        .padding(top = 20.dp)
//                        .fillMaxWidth()
//                        .addStandardHorizontalPadding(),
//                ) {
//                    var startAnim by remember {
//                        mutableStateOf(false)
//                    }
//                    val haptic = LocalHapticFeedback.current
//                    LaunchedEffect(Unit) {
//                        delay(3000)
//                        startAnim = true
//                    }
//
//                    repeat(2) {
//                        val offset by animateIntOffsetAsState(
//                            targetValue = if (startAnim) {
//                                IntOffset(0, 0)
//                            } else {
//                                IntOffset(0, 2000)
//                            },
//                            label = "offset",
//                            animationSpec = tween(
//                                durationMillis = upTime,
//                                delayMillis = it * 250
//                            ),
//                            finishedListener = {
//                                haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.Companion.LongPress)
//                            }
//                        )
//                        OnboardingNotification(
//                            title = "Gmail",
//                            icon = Res.drawable.gmail,
//                            modifier = Modifier
//                                .offset {
//                                    offset
//                                }
//                                .padding(top = (it * 30).dp)
//                                .fillMaxWidth()
//                        )
//                    }
//                }
//
//                Box(
//                    modifier = Modifier
//                        .padding(top = 20.dp)
//                        .fillMaxWidth()
//                        .addStandardHorizontalPadding(),
//                ) {
//                    var startAnim by remember {
//                        mutableStateOf(false)
//                    }
//                    val haptic = LocalHapticFeedback.current
//                    LaunchedEffect(Unit) {
//                        delay(3500)
//                        startAnim = true
//                    }
//
//                    repeat(3) { index ->
//                        val offset by animateIntOffsetAsState(
//                            targetValue = if (startAnim) {
//                                IntOffset(0, 0)
//                            } else {
//                                IntOffset(0, 2000)
//                            },
//                            label = "offset",
//                            animationSpec = tween(
//                                durationMillis = upTime,
//                                delayMillis = index * 250
//                            ),
//                            finishedListener = {
//                                haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.Companion.LongPress)
//
//                                scope.launch {
//                                    delay(1000)
//                                    if (index == 2) {
//                                        upAnimEnded = true
//                                    }
//                                }
//                            }
//                        )
//                        OnboardingNotification(
//                            title = "Youtube",
//                            icon = Res.drawable.youtube,
//                            modifier = Modifier
//                                .offset {
//                                    offset
//                                }
//                                .padding(top = (index * 30).dp)
//                                .fillMaxWidth()
//                        )
//                    }
//                }
            }
        }
    }
}


@Composable
fun OnboardingNotification(
    title: String,
    icon: DrawableResource,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = WhiteColor.copy(
                        0.1f
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .background(
                    color = Color(0xFF121212),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                painter = painterResource(resource = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    style = CustomTextStyle.copy(
                        color = TitleTextColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "Notification",
                    style = CustomTextStyle.copy(
                        color = theme.quaternaryColor,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )
            }
            Text(
                text = "now",
                style = CustomTextStyle.copy(
                    color = TitleTextColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
        }
    }
}


@Composable
fun ScreenTimePermissionContent(
    isEnabled: Boolean,
    screenTimePosition: Float,
    subtitleOpacity: Float,
    onPermissionClick: () -> Unit
) {
    val theme = LocalTheme.current
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .graphicsLayer {
                this.translationX = screenTimePosition
            }
    ) {
        StyleText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .addStandardHorizontalPadding()
                .graphicsLayer {
                    this.alpha = subtitleOpacity
                },
            style = listOf(
                StyleText(
                    text = "let’s analyse your ",
                    fontWeight = "medium",
                    fontSize = 14,
                    color = "#30ffffff"
                ),
                StyleText(
                    text = "screentime",
                    fontWeight = "bold",
                    fontSize = 14,
                    color = "#ffffff"
                ),
                StyleText(
                    text = "  first",
                    fontWeight = "medium",
                    fontSize = 14,
                    color = "#30ffffff"
                ),
            ),
            textAlign = TextAlign.Center,
            lineHeight = 30,
        )

        Image(
            painter = painterResource(resource = Res.drawable.permission),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .padding(top = 30.dp)
                .fillMaxWidth()
                .bounceClick {
                    if (isEnabled) onPermissionClick()
                }
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .addStandardHorizontalPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = BlueColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "apple privacy sandbox",
                    style = CustomTextStyle.copy(
                        color = BlueColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )
            }

            Text(
                text = "your data is never shared with anyone, not even us",
                style = CustomTextStyle.copy(
                    color = theme.quaternaryColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .addStandardHorizontalPadding()
            )

            CustomButton(
                type = ButtonDto.ButtonType.PRIMARY_BUTTON,
                text = "request permission",
                onClick = {
                    // Handle button click
                    if (isEnabled) onPermissionClick()
                },
                endIconComposable = {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = WhiteColor,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

