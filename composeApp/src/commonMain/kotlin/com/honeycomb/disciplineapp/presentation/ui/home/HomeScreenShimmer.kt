package com.honeycomb.disciplineapp.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.honeycomb.disciplineapp.presentation.utils.shimmer

@Composable
fun HomeScreenShimmer(
    modifier: Modifier = Modifier
) {
    val cornerRadius = 10
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .shimmer()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(30.dp)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .shimmer()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .shimmer()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(20.dp)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .shimmer()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(30.dp)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .shimmer()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .shimmer()
        )
    }
}