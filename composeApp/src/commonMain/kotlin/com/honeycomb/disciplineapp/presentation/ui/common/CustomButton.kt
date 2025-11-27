package com.honeycomb.disciplineapp.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeycomb.disciplineapp.AccentColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.LightAccentColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import org.jetbrains.compose.ui.tooling.preview.Preview

sealed class CustomButtonState {
    data object Enabled : CustomButtonState()
    data object Disabled : CustomButtonState()
    data object Loading : CustomButtonState()
}

@Composable
fun CustomButton(
    text: String,
    modifier: Modifier = Modifier,
    state: CustomButtonState = CustomButtonState.Enabled,
    backgroundColor: Color = AccentColor,
    borderColor: Color = LightAccentColor,
    onClick: () -> Unit = {},
    startIconComposable: @Composable () -> Unit = {},
    endIconComposable: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .bounceClick(
                enabled = state == CustomButtonState.Enabled
            ) {
                if (state == CustomButtonState.Disabled || state == CustomButtonState.Loading) return@bounceClick

                onClick()
            }
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(100.dp)
            )
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
        ) {
            startIconComposable()
        }

        Text(
            text = text,
            style = CustomTextStyle.copy(
                color = WhiteColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .size(18.dp)
        ) {
            if (state == CustomButtonState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(18.dp),
                    color = WhiteColor,
                    strokeWidth = 2.dp
                )
            } else {
                endIconComposable()
            }
        }
    }
}

@Composable
@Preview
fun CustomButtonPreview(modifier: Modifier = Modifier) {
    CustomButton(
        text = "Continue",
        endIconComposable = {
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null
            )
        }
    )
}