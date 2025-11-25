package com.honeycomb.disciplineapp.presentation.ui.main

sealed class MainScreen(val route: String) {
    object Home : MainScreen("Home")
    object Friends : MainScreen("Friends")
    object Leaderboard : MainScreen("Leaderboard")
    object Settings : MainScreen("Settings")
}