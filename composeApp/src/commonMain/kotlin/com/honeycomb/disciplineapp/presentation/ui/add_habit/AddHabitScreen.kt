package com.honeycomb.disciplineapp.presentation.ui.add_habit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import com.honeycomb.disciplineapp.data.dto.DropDownDto
import com.honeycomb.disciplineapp.data.dto.DropDownOptionDto
import com.honeycomb.disciplineapp.data.dto.EvidenceBsDto
import com.honeycomb.disciplineapp.data.dto.EvidenceDto
import com.honeycomb.disciplineapp.data.dto.FieldDto
import com.honeycomb.disciplineapp.data.dto.HabitDataDto
import com.honeycomb.disciplineapp.data.dto.OptionDto
import com.honeycomb.disciplineapp.data.dto.ButtonDto
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
import com.honeycomb.disciplineapp.AccentColor
import com.honeycomb.disciplineapp.AccentColor2
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.data.dto.BottomSheetValueDto
import com.honeycomb.disciplineapp.presentation.ui.add_habit.EvidenceSelectionSheet
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.common.ButtonComponent
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.clickableWithoutRipple
import com.honeycomb.disciplineapp.presentation.utils.dashedBorder
import com.honeycomb.disciplineapp.presentation.utils.noRippleClickable
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    navController: NavController,
    habitData: HabitDataDto?,
    modifier: Modifier = Modifier,
) {
    var showEvidenceSheet by remember { mutableStateOf(false) }
    val evidenceSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val evidenceSelection = habitData?.evidenceBs

    // evidences mapped by type for quick lookup
    val evidencesByType = remember(habitData) {
        habitData?.evidences.orEmpty().associateBy { it.type }
    }

    // currently selected evidence type and dto
    var selectedEvidenceType by remember { mutableStateOf<String?>(habitData?.evidenceBs?.options?.firstOrNull()?.type) }
    var selectedEvidence by remember { mutableStateOf<EvidenceDto?>(null) }

    // Dropdown state for any DROP_DOWN / SELECTION bottom sheet
    var activeDropDownField by remember { mutableStateOf<FieldDto?>(null) }
    var showDropDownSheet by remember { mutableStateOf(false) }
    val dropDownSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(evidenceSelection) {
        showEvidenceSheet = evidenceSelection != null
        selectedEvidence = evidencesByType[selectedEvidenceType]
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
    )

    val dismissSheet: () -> Unit = {
        scope.launch {
            evidenceSheetState.hide()
            showEvidenceSheet = false
        }
    }

    if (showEvidenceSheet && evidenceSelection != null) {
        ModalBottomSheet(
            sheetState = evidenceSheetState,
            onDismissRequest = dismissSheet,
            dragHandle = null,
            containerColor = BackgroundColor,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            EvidenceSelectionSheet(
                evidence = evidenceSelection,
                onClose = dismissSheet,
                onEvidenceSelected = { option ->
                    selectedEvidenceType = option.type
                    selectedEvidence = evidencesByType[option.type]
                    dismissSheet()
                }
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
        HabitContent(
            habitData = habitData,
            evidence = selectedEvidence,
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            onShowEvidencePicker = {
                showEvidenceSheet = true
            },
            onOpenDropDown = { field ->
                activeDropDownField = field
                showDropDownSheet = true
            }
        )
    }
    // Dropdown bottom sheet shared for all DROP_DOWN fields
    val dropDown = activeDropDownField?.dropDown
    if (showDropDownSheet && dropDown != null) {
        ModalBottomSheet(
            sheetState = dropDownSheetState,
            onDismissRequest = {
                scope.launch {
                    dropDownSheetState.hide()
                    showDropDownSheet = false
                }
            },
            dragHandle = null,
            containerColor = BackgroundColor,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            DropDownOptionsSheet(
                dropDown = dropDown,
                onOptionSelected = { option ->
                    // update in-memory selection flags
                    activeDropDownField = activeDropDownField?.copy(
                        dropDown = dropDown.copy(
                            options = dropDown.options?.map {
                                it.copy(selected = it.id == option.id)
                            }
                        )
                    )
                    scope.launch {
                        dropDownSheetState.hide()
                        showDropDownSheet = false
                    }
                },
                onClose = {
                    scope.launch {
                        dropDownSheetState.hide()
                        showDropDownSheet = false
                    }
                }
            )
        }
    }
}

@Composable
private fun HabitContent(
    habitData: HabitDataDto?,
    evidence: EvidenceDto?,
    modifier: Modifier = Modifier,
    onShowEvidencePicker: () -> Unit,
    onOpenDropDown: (FieldDto) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)

    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 30.dp)
        ) {
            evidence?.fields.orEmpty().forEach { field ->
                item {
                    when (field.type) {
                        "INPUT_FIELD" -> InputFieldBlock(field)
                        "DROP_DOWN" -> DropDownFieldBlock(field, onOpenDropDown)
                        "SELECTION" -> SelectionFieldBlock(field)
                        "BOTTOMSHEET" -> BottomSheetFieldBlock(
                            field.bs,
                            onClick = {
                                if (field.id == "evidence") {
                                    onShowEvidencePicker()
                                }
                            }
                        )
                        // Skip BOTTOMSHEET for now
                        else -> Unit
                    }
                }
            }
        }
        // Bottom button
        habitData?.button?.let { btn ->
            ButtonComponent(
                button = btn,
                onClick = { /* TODO: Hook submit later */ },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun BottomSheetFieldBlock(
    field: BottomSheetValueDto?,
    onClick: () -> Unit,
) {
    if (field == null) return

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        field.title?.let {
            Text(
                text = field.title,
                style = CustomTextStyle.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = WhiteColor
                )
            )
        }

        Row(
            modifier = Modifier
                .background(LightBackgroundColor, RoundedCornerShape(100.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .noRippleClickable(onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = field.selected.orEmpty(),
                style = CustomTextStyle.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = WhiteColor
                ),
                modifier = Modifier
                    .weight(1f)
            )

            Icon(
                Icons.Default.KeyboardArrowRight,
                tint = WhiteColor,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun InputFieldBlock(
    field: FieldDto,
) {
    val inputMeta = field.inputField ?: return
    var text by remember { mutableStateOf("") }
    var hintIndex by remember { mutableStateOf(0) }

    val hints = inputMeta.hint.orEmpty()
    val displayedHint = if (hints.isNotEmpty()) hints[hintIndex % hints.size] else ""

    LaunchedEffect(hints) {
        if (hints.isEmpty()) return@LaunchedEffect
        while (true) {
            delay(2500)
            hintIndex = (hintIndex + 1) % hints.size
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        inputMeta.title?.let {
            Text(
                text = inputMeta.title,
                style = CustomTextStyle.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = WhiteColor
                )
            )
        }

        BasicTextField(
            value = text,
            onValueChange = { new ->
                if (inputMeta.maxLength == null || new.length <= inputMeta.maxLength) {
                    text = new
                }
            },
            textStyle = CustomTextStyle.copy(
                fontSize = 14.sp,
                color = WhiteColor
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 44.dp)
                        .background(LightBackgroundColor, shape = RoundedCornerShape(6.dp))
                        .dashedBorder(
                            strokeWidth = 1.dp,
                            color = SubtitleTextColor,
                            cornerRadius = 6.dp
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = displayedHint,
                            style = CustomTextStyle.copy(
                                fontSize = 14.sp,
                                color = SubtitleColor
                            )
                        )
                    }

                    it()
                }
            }
        )
    }
}

@Composable
private fun DropDownFieldBlock(
    field: FieldDto,
    onOpenDropDown: (FieldDto) -> Unit,
) {
    val dd = field.dropDown ?: return
    val selected = dd.options?.firstOrNull { it.selected == true }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dd.title.orEmpty(),
            style = CustomTextStyle.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = SubtitleTextColor
            ),
            modifier = Modifier
                .weight(1f)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(AccentColor2)
                .clickableWithoutRipple { onOpenDropDown(field) }
                .padding(horizontal = 16.dp)
                .height(35.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selected?.title.orEmpty(),
                    style = CustomTextStyle.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = WhiteColor
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = WhiteColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun SelectionFieldBlock(
    field: FieldDto,
) {
    val selection = field.selection ?: return
    var selectedId by remember(selection) {
        mutableStateOf(selection.options?.firstOrNull { it.selected == true }?.id)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selection.title.orEmpty(),
            style = CustomTextStyle.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = SubtitleTextColor
            ),
            modifier = Modifier
                .weight(1f)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            selection.options.orEmpty().forEach { opt ->
                val isSelected = opt.id == selectedId
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) AccentColor2 else LightBackgroundColor)
                        .border(
                            width = if (isSelected) 0.dp else 1.dp,
                            color = LightBackgroundColor,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickableWithoutRipple { selectedId = opt.id }
                        .padding(horizontal = 22.dp)
                        .height(35.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opt.title.orEmpty(),
                        style = CustomTextStyle.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = WhiteColor
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun DropDownOptionsSheet(
    dropDown: DropDownDto,
    onOptionSelected: (DropDownOptionDto) -> Unit,
    onClose: () -> Unit,
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
                    text = dropDown.bsTitle ?: dropDown.title.orEmpty(),
                    style = CustomTextStyle.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = WhiteColor
                    )
                )
            }

            CloseChip(
                onClick = onClose,
                icon = Icons.Default.Close,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp),
            modifier = Modifier.padding(top = 50.dp)
        ) {
            dropDown.options.orEmpty().forEach { option ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableWithoutRipple { onOptionSelected(option) },
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = option.title.orEmpty(),
                        style = CustomTextStyle.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = WhiteColor
                        )
                    )
                    option.subtitle?.let { subtitle ->
                        Text(
                            text = subtitle,
                            style = CustomTextStyle.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = SubtitleColor
                            )
                        )
                    }
                }
            }
        }
    }
}