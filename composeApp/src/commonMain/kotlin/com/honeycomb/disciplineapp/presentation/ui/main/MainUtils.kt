package com.honeycomb.disciplineapp.presentation.ui.main

import com.honeycomb.disciplineapp.presentation.models.MenuItem
import disciplineapp.composeapp.generated.resources.Res
import disciplineapp.composeapp.generated.resources.friends
import disciplineapp.composeapp.generated.resources.home
import disciplineapp.composeapp.generated.resources.leaderboard
import disciplineapp.composeapp.generated.resources.settings

object MainUtils {
    val menuItems = listOf(
        MenuItem(
            "Home",
            icon = Res.drawable.home,
            route = MainScreen.Home
        ),
        MenuItem(
            "Friends",
            icon = Res.drawable.friends,
            route = MainScreen.Friends
        ),
        MenuItem(
            "Leaderboard",
            icon = Res.drawable.leaderboard,
            route = MainScreen.Leaderboard
        ),
        MenuItem(
            "Settings",
            icon = Res.drawable.settings,
            route = MainScreen.Settings
        )
    )
}