package com.honeycomb.disciplineapp.presentation.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.honeycomb.disciplineapp.AccentColor
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.focus_app.ui.focus_screen.FocusViewModel
import com.honeycomb.disciplineapp.presentation.models.MenuItem
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusViewModel
import com.honeycomb.disciplineapp.presentation.ui.focus_app.TimerState
import com.honeycomb.disciplineapp.presentation.ui.friends.FriendsScreen
import com.honeycomb.disciplineapp.presentation.ui.home.HomeScreen
import com.honeycomb.disciplineapp.presentation.ui.leaderboard.LeaderboardScreen
import com.honeycomb.disciplineapp.presentation.ui.main.MainUtils.menuItems
import com.honeycomb.disciplineapp.presentation.ui.settings.SettingsScreen
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.noRippleClickable
import disciplineapp.composeapp.generated.resources.Res
import disciplineapp.composeapp.generated.resources.pause
import disciplineapp.composeapp.generated.resources.stop
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.toString

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MainScreen(
    focusViewModel: CreateFocusViewModel,
    navController: NavController,
    modifier: Modifier,
) {
    val theme = LocalTheme.current
    var isBottomBarVisible by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val mainNavHost = rememberNavController()
    val state by focusViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        if (state.timerState == TimerState.COMPLETED) {
            navController.navigate(Screen.FocusSuccessScreenRoute(
                state = state
            )) {
                popUpTo<Screen.CreateFocusScreenRoute> {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        containerColor = theme.backgroundColorGradient.first(),
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            Column {
                // live timer
                StickyTimer(
                    modifier = Modifier
                        .noRippleClickable {
                            navController.navigate(Screen.CreateFocusScreenRoute)
                        },
                    focusViewModel = focusViewModel
                )

                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    containerColor = Color(0xFF1A1A1B),
                    contentColor = Color.White
                ) {
                    menuItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                if (index == 0) {
                                    if (index == selectedIndex) {
                                        AnimatedLogo(
                                            size = 24,
                                            modifier = Modifier
                                        )
                                    } else {
                                        Box(
                                            modifier = modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    brush = Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color(0xFFA4A4A4),
                                                            Color(0xFF676767)
                                                        )
                                                    )
                                                )
                                        )
                                    }
                                } else {
                                    Image(
                                        imageVector = vectorResource(item.icon),
                                        contentDescription = item.label,
                                        modifier = Modifier
                                            .height(24.dp),
                                        colorFilter = ColorFilter.tint(
                                            color = if (index == selectedIndex) WhiteColor else SubtitleTextColor,
                                        )
                                    )
                                }

                            },
                            label = {
                                Text(
                                    item.label,
                                    style = CustomTextStyle.copy(
                                        fontSize = 12.sp,
                                        color = if (index == selectedIndex) WhiteColor else SubtitleTextColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            },
                            interactionSource = null,
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                mainNavHost.navigate(item.route.route)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.LightGray,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = modifier
                .padding(bottom = 100.dp)
                .fillMaxSize()
        ) {
            val graph =
                mainNavHost.createGraph(startDestination = MainScreen.Home.route) {
                    composable(
                        route = MainScreen.Home.route
                    ) {
                        HomeScreen(
                            navController = navController,
                            onExpandClick = {
                                isBottomBarVisible = true
                            },
                            isExpanded = isBottomBarVisible,
                            focusViewModel = focusViewModel
                        )
                    }
                    composable(route = MainScreen.Friends.route) {
                        FriendsScreen()
                    }
                    composable(route = MainScreen.Leaderboard.route) {
                        LeaderboardScreen()
                    }
                    composable(route = MainScreen.Settings.route) {
                        SettingsScreen()
                    }
                }
            NavHost(
                navController = mainNavHost,
                graph = graph,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            theme.backgroundColorGradient
                        )
                    )
                    .padding(top = it.calculateTopPadding())
            )
        }
    }
}

@Composable
fun StickyTimer(
    modifier: Modifier,
    focusViewModel: CreateFocusViewModel,
) {
    val theme = LocalTheme.current
    val focusState by focusViewModel.state.collectAsStateWithLifecycle()
    val minutes by focusViewModel.minutes.collectAsStateWithLifecycle()
    val seconds by focusViewModel.seconds.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
//        focusViewModel.initVm()
    }

    if (focusState.timerState == TimerState.RUNNING || focusState.timerState == TimerState.PAUSED) {
        Row(
            modifier = modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1A9597),
                            Color(0xFF0B80E6),
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .border(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF52C0C1),
                            Color(0xFF0B80E6),
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    width = 2.dp
                )
                .padding(horizontal = 20.dp)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AnimatedLogo(size = 20)
            Text(
                text = "focus session",
                style = CustomTextStyle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = WhiteColor,
                ),
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = "${minutes.toString().padStart(2, '0')}:${
                    seconds.toString().padStart(2, '0')
                }",
                style = CustomTextStyle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = WhiteColor,
                )
            )

            Box(
                modifier = Modifier
                    .bounceClick {
                        if (focusState.timerState == TimerState.RUNNING) {
                            focusViewModel.pauseTimer()
                        } else {
                            focusViewModel.resumeTimer()
                        }
                    }
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = theme.tertiaryColor,
                        shape = CircleShape
                    )
                    .background(
                        color = WhiteColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (focusState.timerState == TimerState.RUNNING) {
                    Image(
                        imageVector = vectorResource(Res.drawable.pause),
                        contentDescription = null,
                        modifier = Modifier
                            .size(14.dp),
                        colorFilter = ColorFilter.tint(
                            color = Color(0xFF0D82DF)
                        )
                    )
                } else {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = Color(0xFF0D82DF),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .bounceClick {
                        focusViewModel.stopTimer()
                    }
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = theme.quaternaryColor,
                        shape = CircleShape
                    )
                    .background(
                        color = theme.tertiaryColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = vectorResource(Res.drawable.stop),
                    contentDescription = null,
                    modifier = Modifier
                        .size(13.dp)
                )
            }
        }
    }
}