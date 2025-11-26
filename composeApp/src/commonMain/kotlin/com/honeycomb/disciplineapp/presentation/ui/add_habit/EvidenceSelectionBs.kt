package com.honeycomb.disciplineapp.presentation.ui.add_habit

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.SubtitleColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.data.dto.EvidenceBsDto
import com.honeycomb.disciplineapp.data.dto.OptionDto
import com.honeycomb.disciplineapp.presentation.utils.clickableWithoutRipple
import com.honeycomb.disciplineapp.presentation.utils.dashedBorder
import kotlin.collections.forEachIndexed
import kotlin.collections.orEmpty


@Composable
fun EvidenceSelectionSheet(
    evidence: EvidenceBsDto,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 30.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = evidence.title.orEmpty(),
                    style = CustomTextStyle.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = WhiteColor
                    )
                )
            }

            CloseChip(
                onClick = onClose,
                icon = Icons.Default.Close
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp),
            modifier = Modifier
                .padding(top = 40.dp)
        ) {
            evidence.options.orEmpty().forEachIndexed { index, option ->
                EvidenceOptionRow(option = option)
            }
        }
    }
}

@Composable
private fun CloseChip(
    onClick: () -> Unit,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                shape = CircleShape,
                color = WhiteColor
            )
            .clickableWithoutRipple(onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = WhiteColor,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun EvidenceOptionRow(
    option: OptionDto
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = option.title.orEmpty(),
                overflow = TextOverflow.Ellipsis,
                style = CustomTextStyle.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = TitleTextColor,
                )
            )
            Text(
                text = option.subtitle.orEmpty(),
                overflow = TextOverflow.Ellipsis,
                style = CustomTextStyle.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = SubtitleTextColor
                )
            )
        }

        Icon(
            Icons.Default.KeyboardArrowRight,
            tint = WhiteColor,
            contentDescription = null
        )
    }
}