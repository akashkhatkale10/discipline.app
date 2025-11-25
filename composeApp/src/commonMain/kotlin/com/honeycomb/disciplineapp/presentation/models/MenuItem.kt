package com.honeycomb.disciplineapp.presentation.models

import com.honeycomb.disciplineapp.presentation.ui.main.MainScreen
import org.jetbrains.compose.resources.DrawableResource


data class MenuItem(
    val label: String,
    val icon: DrawableResource,
    val route: MainScreen
)