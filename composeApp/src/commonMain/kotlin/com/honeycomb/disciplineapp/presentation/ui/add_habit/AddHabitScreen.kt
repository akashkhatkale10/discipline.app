package com.honeycomb.disciplineapp.presentation.ui.add_habit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.LightBackgroundColor
import com.honeycomb.disciplineapp.SecondaryStrokeColor
import com.honeycomb.disciplineapp.SubtitleColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.data.dto.EvidenceBsDto
import com.honeycomb.disciplineapp.data.dto.OptionDto
import com.honeycomb.disciplineapp.data.dto.HabitDataDto
import com.honeycomb.disciplineapp.nunitoFontFamily
import kotlinx.coroutines.launch
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.utils.bounceClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    navController: NavController,
    habitData: HabitDataDto?,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val evidenceSelection = habitData?.evidenceBs

    LaunchedEffect(evidenceSelection) {
        showBottomSheet = evidenceSelection != null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
    )

    val dismissSheet: () -> Unit = {
        scope.launch {
            sheetState.hide()
            showBottomSheet = false
        }
    }

    if (showBottomSheet && evidenceSelection != null) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = dismissSheet,
            dragHandle = null,
            containerColor = BackgroundColor,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            EvidenceSelectionSheet(
                evidence = evidenceSelection,
                onClose = dismissSheet
            )
        }
    }


    Scaffold(
        containerColor = BackgroundColor,
        modifier = modifier,
        topBar = {
            CustomTopBar(
                dividerVisible = true,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                startComposable = {
                    BorderIconButton(
                        iconComposable = {
                            Icon(
                                Icons.Default.ArrowBack,
                                tint = SubtitleTextColor,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                                    .bounceClick {
                                        navController.popBackStack()
                                    }
                            )
                        }
                    )
                },
                midComposable = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = WhiteColor,
                                    fontFamily = nunitoFontFamily
                                )
                            ) {
                                append("Add a new habit")
                            }
                        },
                        style = CustomTextStyle
                    )
                }
            )
        }
    ) {
    }
}

