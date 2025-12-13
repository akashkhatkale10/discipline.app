package com.honeycomb.disciplineapp.presentation.ui.focus_app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.presentation.ui.add_habit.formatToReadableDateTime
import com.honeycomb.disciplineapp.presentation.ui.add_habit.formatToTime
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import com.honeycomb.disciplineapp.presentation.utils.now
import com.honeycomb.disciplineapp.presentation.utils.plus
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TimerDisplay(
    minutes: Int,
    seconds: Int,
    title: String,
    endTimeText: String,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // Top label
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = CustomTextStyle.copy(
                    color = WhiteColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
            )
        }

        // Timer boxes
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            TimeBox(value = minutes, suffix = "m")
            Text(
                text = ":",
                style = CustomTextStyle.copy(
                    color = WhiteColor,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                ),
            )
            TimeBox(value = seconds, suffix = "s")
        }

        // End time text
        if (endTimeText.isNotEmpty()) {
            Text(
                text = endTimeText,
                style = CustomTextStyle.copy(
                    color = WhiteColor.copy(alpha = 0.3f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
            )
        }
    }
}

@Composable
private fun TimeBox(
    value: Int,
    suffix: String = "",
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    Box(
        modifier = modifier
            .size(width = 70.dp, height = 70.dp)
            .border(
                width = 1.dp,
                color = theme.tertiaryColor,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = theme.secondaryButtonColor,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${if (value < 10) "0" else ""}$value$suffix",
            style = CustomTextStyle.copy(
                color = WhiteColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            ),
        )
    }
}

fun calculateEndTimeKotlinx(minutesToAdd: Int): String {
    val now = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val endTime: LocalDateTime = now.plus(minutesToAdd.toLong(), DateTimeUnit.MINUTE)
    return "ends at ${endTime.formatToTime()}"
}