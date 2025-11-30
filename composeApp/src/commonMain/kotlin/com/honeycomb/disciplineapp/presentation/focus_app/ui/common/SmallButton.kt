package com.honeycomb.disciplineapp.presentation.focus_app.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeycomb.disciplineapp.BlueColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.presentation.utils.bounceClick

@Composable
fun SmallButton(
    text: String,
    startComposable: @Composable () -> Unit,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .bounceClick {
                onClick()
            }
            .height(24.dp)
            .background(
                color = BlueColor,
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        startComposable()
        Text(
            text,
            style = CustomTextStyle.copy(
                color = TitleTextColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
        )
    }
}