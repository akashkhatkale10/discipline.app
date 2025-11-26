package com.honeycomb.disciplineapp.presentation.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ConditionalLogo(
    type: String?
) {
    if (type == "HABIT") {
        Logo(
            size = 10
        )
    } else if (type == "PENALTY") {
        Logo(
            size = 10,
            colors = listOf(
                Color(0xFFFD1D1D),
                Color(0xFFFCB045),
            )
        )
    }
}