package com.honeycomb.disciplineapp.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.honeycomb.disciplineapp.SubtitleTextColor

@Composable
fun BorderIconButton(
    modifier: Modifier = Modifier,
    iconComposable: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = SubtitleTextColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        iconComposable()
    }
}

@Composable
fun IconButton(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    iconComposable: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(
                color = backgroundColor,
            ),
        contentAlignment = Alignment.Center
    ) {
        iconComposable()
    }
}