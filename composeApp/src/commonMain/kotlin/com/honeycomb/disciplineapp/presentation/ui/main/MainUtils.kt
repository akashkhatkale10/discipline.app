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
            "focus",
            icon = Res.drawable.home,
            route = MainScreen.Home
        ),
        MenuItem(
            "friends",
            icon = Res.drawable.friends,
            route = MainScreen.Friends
        ),
        MenuItem(
            "stats",
            icon = Res.drawable.leaderboard,
            route = MainScreen.Leaderboard
        ),
        MenuItem(
            "settings",
            icon = Res.drawable.settings,
            route = MainScreen.Settings
        )
    )
}