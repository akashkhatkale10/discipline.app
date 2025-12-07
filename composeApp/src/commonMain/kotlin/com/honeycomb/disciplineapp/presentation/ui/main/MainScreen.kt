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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.honeycomb.disciplineapp.presentation.models.MenuItem
import com.honeycomb.disciplineapp.presentation.ui.friends.FriendsScreen
import com.honeycomb.disciplineapp.presentation.ui.home.HomeScreen
import com.honeycomb.disciplineapp.presentation.ui.leaderboard.LeaderboardScreen
import com.honeycomb.disciplineapp.presentation.ui.main.MainUtils.menuItems
import com.honeycomb.disciplineapp.presentation.ui.settings.SettingsScreen
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier,
) {
    val theme = LocalTheme.current
    var isBottomBarVisible by remember {
        mutableStateOf(false)
    }
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val mainNavHost = rememberNavController()

    Scaffold(
        containerColor = theme.backgroundColorGradient.first(),
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
                BottomAppBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        menuItems.forEachIndexed { index, item ->
                            MenuItemContent(
                                item = item,
                                index = index,
                                selectedIndex = selectedIndex,
                                onMenuItemClick = {
                                    selectedIndex = index
                                    mainNavHost.navigate(
                                        it.route.route
                                    )
                                }
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
                            isExpanded = isBottomBarVisible
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
            )
        }
    }
}


@Composable
fun MenuItemContent(
    index: Int,
    selectedIndex: Int,
    item: MenuItem,
    modifier: Modifier = Modifier,
    onMenuItemClick: (MenuItem) -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .bounceClick {
                    onMenuItemClick(item)
                }
                .size(54.dp)
                .background(
                    color = if (index == selectedIndex) AccentColor else Color.Transparent,
                    shape = CircleShape
                )
                .then(
                    if (index != selectedIndex) {
                        Modifier
                            .border(
                                width = 1.dp,
                                shape = CircleShape,
                                color = SubtitleTextColor
                            )
                    } else Modifier
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = vectorResource(item.icon),
                contentDescription = item.label,
                modifier = Modifier
                    .align(Alignment.Center),
                colorFilter = ColorFilter.tint(
                    color = if (index == selectedIndex) WhiteColor else SubtitleTextColor,
                )
            )
        }
    }
}