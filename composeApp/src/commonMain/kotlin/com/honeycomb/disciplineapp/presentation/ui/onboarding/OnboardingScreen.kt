package com.honeycomb.disciplineapp.presentation.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.data.dto.OnboardingDto
import com.honeycomb.disciplineapp.data.dto.UserData
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButtonState
import com.honeycomb.disciplineapp.presentation.ui.common.Logo
import com.honeycomb.disciplineapp.presentation.ui.home.ActionTitleSubtitle
import com.honeycomb.disciplineapp.presentation.utils.Constants.HORIZONTAL_PADDING
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(
    navController: NavController,
    data: OnboardingDto,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = {data.pages?.size ?: 0})
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<OnboardingViewModel>()
    val state by viewModel.loginState.collectAsStateWithLifecycle()
    var authReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                delay(5000)
                val target = pagerState.currentPage + 1
                pagerState.animateScrollToPage(
                    target % (data.pages?.size ?: 1)
                )
            }
        }
    }

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


    Column(
        modifier = modifier
            .background(BackgroundColor)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(top = 50.dp)
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()

                            // Detect movement beyond a threshold, not just press
                            val isDragging = event.changes.any { change ->
                                change.positionChange() != Offset.Zero // Movement happened
                            }

                            if (isDragging) {
                                scope.cancel()
                            }
                        }
                    }
                },
        ) { page ->
            val pageData = data.pages?.get(page)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ActionTitleSubtitle(
                    title = pageData?.title.orEmpty(),
                    subtitle = pageData?.subtitle.orEmpty(),
                    action = {
                        if (pageData?.type == "habit") {
                            Logo(
                                size = 80
                            )
                        } else {
                            Logo(
                                size = 80,
                                colors = listOf(
                                    Color(0xFFFD1D1D),
                                    Color(0xFFFCB045),
                                )
                            )
                        }
                    }
                )

                if (pageData?.points?.isNotEmpty() == true) {
                    OnboardingPoint(
                        type = pageData.type.orEmpty(),
                        points = pageData.points,
                        modifier = Modifier.padding(top = 50.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(bottom = 24.dp),
            pageCount = data.pages?.size ?: 0,
            indicatorSize = 10.dp,
        )

        if (authReady) {
            GoogleButtonUiContainerFirebase(
                modifier = Modifier,
                onResult = {
                    if (it.isSuccess) {
                        val user = it.getOrNull()
                        viewModel.loginUser(
                            signInResult = UserData(
                                userId = user?.uid.orEmpty(),
                                personalDetails = UserData.PersonalDetails(
                                    name = user?.displayName.orEmpty(),
                                    profilePictureUrl = user?.photoURL.orEmpty(),
                                    email = user?.email.orEmpty()
                                )
                            ),
                        )
                    } else if (it.isFailure) {
                        // show toast
                    }
                }
            ) {
                CustomButton(
                    text = data.buttonText.orEmpty(),
                    endIconComposable = {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = WhiteColor
                            )
                    },
                    modifier = Modifier
                        .padding(horizontal = HORIZONTAL_PADDING.dp)
                        .padding(bottom = 50.dp),
                    onClick = {
                        this.onClick()
                    },
                    state = if (state.isLoading) CustomButtonState.Loading else CustomButtonState.Enabled,
                )
            }
        }
    }
}

@Composable
fun OnboardingPoint(
    type: String,
    points: List<String>,
    modifier: Modifier = Modifier
) {
    var show by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(200)
        show = true
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        points.forEachIndexed { index,  point ->
            val alpha by animateIntAsState(
                targetValue = if (show) 1 else 0,
                animationSpec = tween(
                    durationMillis = 300 * index
                )
            )

            AnimatedVisibility(
                visible = show,
                enter = fadeIn(animationSpec = tween(1000 * index)),
                exit = fadeOut()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
//                        .graphicsLayer {
//                            this.alpha = alpha.toFloat()
//                        }
                ) {
                    if (type == "habit") {
                        Logo(
                            size = 10
                        )
                    } else {
                        Logo(
                            size = 10,
                            colors = listOf(
                                Color(0xFFFD1D1D),
                                Color(0xFFFCB045),
                            )
                        )
                    }
                    Text(
                        text = point,
                        style = CustomTextStyle.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = WhiteColor
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CustomPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = WhiteColor,  // Light circle (active)
    inactiveColor: Color = SubtitleTextColor, // Dark circles (inactive)
    indicatorSize: Dp = 12.dp,
    spacing: Dp = 12.dp
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        repeat(pageCount) { index ->
            val isSelected = pagerState.currentPage == index

            val animatedColor = animateColorAsState(
                targetValue = if (isSelected) activeColor else inactiveColor
            )

            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .background(animatedColor.value, CircleShape)
            )
        }
    }
}













