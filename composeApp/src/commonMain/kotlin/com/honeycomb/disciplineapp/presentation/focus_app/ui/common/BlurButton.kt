package com.honeycomb.disciplineapp.presentation.focus_app.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.honeycomb.disciplineapp.presentation.utils.addBlurBackground
import com.honeycomb.disciplineapp.presentation.utils.addMediumHeight
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import com.honeycomb.disciplineapp.presentation.utils.bounceClick

@Composable
fun BlurButton(
    content: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .addStandardHorizontalPadding()
            .fillMaxWidth()
            .addMediumHeight()
            .bounceClick {
                onClick()
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .addBlurBackground()
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            content()
        }
    }
}