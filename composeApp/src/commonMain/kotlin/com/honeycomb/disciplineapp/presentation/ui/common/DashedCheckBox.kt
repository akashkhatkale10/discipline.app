package com.honeycomb.disciplineapp.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.honeycomb.disciplineapp.AccentColor2
import com.honeycomb.disciplineapp.SubtitleColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.dashedBorder

@Composable
fun DashedCheckBox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .bounceClick { onCheckedChange(!isChecked) }
            .then(
                if (!isChecked)
                    Modifier.dashedBorder(
                        strokeWidth = 1.dp,
                        color = SubtitleColor,
                        cornerRadius = 6.dp
                    ) else Modifier
            )
            .then(
                if (isChecked)
                    Modifier.background(
                        color = AccentColor2,
                        shape = RoundedCornerShape(6.dp)
                    ) else Modifier
            )
            .size(25.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isChecked) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = WhiteColor,
                modifier = Modifier
                    .size(14.dp)
            )
        }
    }
}