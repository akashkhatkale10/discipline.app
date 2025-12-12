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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.honeycomb.disciplineapp.LightestBackgroundColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.data.dto.BottomSheetValueDto
import com.honeycomb.disciplineapp.data.dto.TimeSelectionDto
import com.honeycomb.disciplineapp.presentation.ui.add_habit.EvidenceSelectionSheet
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.common.ButtonComponent
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButtonState
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.ui.common.DashedCheckBox
import com.honeycomb.disciplineapp.presentation.ui.common.pickDate
import com.honeycomb.disciplineapp.presentation.utils.Constants.TIME_ANYTIME_ENABLED
import com.honeycomb.disciplineapp.presentation.utils.LocalPlatformContext
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.dashedBorder
import com.honeycomb.disciplineapp.presentation.utils.noRippleClickable
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun AddHabitScreen(
    navController: NavController,
    habitData: HabitDataDto?,
    modifier: Modifier = Modifier,
) {
    val viewModel = koinViewModel<AddHabitViewModel>()
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
    var showTimeDropDownSheet by remember { mutableStateOf(false) }
    val dropDownSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val timeDropDownSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(evidenceSelection) {
        showEvidenceSheet = evidenceSelection != null
        selectedEvidence = evidencesByType[selectedEvidenceType]
        selectedEvidence?.let { viewModel.setInitialData(it) }
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
            containerColor = LightestBackgroundColor,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            EvidenceSelectionSheet(
                evidence = evidenceSelection,
                onClose = dismissSheet,
                onEvidenceSelected = { option ->
                    selectedEvidenceType = option.type
                    selectedEvidence = evidencesByType[option.type]
                    selectedEvidence?.let { viewModel.setInitialData(it) }
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
            },
            navController = navController,
            viewModel = viewModel,
            onOpenTimeSelection = { field ->
                activeDropDownField = field
                showTimeDropDownSheet = true
            }
        )
    }
    // Dropdown bottom sheet shared for all DROP_DOWN fields
    val dropDown = activeDropDownField?.dropDown
    val timeDropDown = activeDropDownField?.timeSelection
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
            containerColor = LightestBackgroundColor,
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
                    viewModel.setParam(
                        key = activeDropDownField?.id.orEmpty(),
                        value = option.id.orEmpty()
                    )
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

    if (showTimeDropDownSheet && timeDropDown != null) {
        ModalBottomSheet(
            sheetState = timeDropDownSheetState,
            onDismissRequest = {
                scope.launch {
                    timeDropDownSheetState.hide()
                    showTimeDropDownSheet = false
                }
            },
            dragHandle = null,
            containerColor = LightestBackgroundColor,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            TimeDropDownOptionsSheet(
                dropDown = timeDropDown,
                onOptionSelected = { option ->
                    // update in-memory selection flags
                    scope.launch {
                        timeDropDownSheetState.hide()
                        showTimeDropDownSheet = false
                    }
                    viewModel.setParam(
                        key = activeDropDownField?.id.orEmpty(),
                        value = option.id.orEmpty()
                    )
                },
                onClose = {
                    scope.launch {
                        timeDropDownSheetState.hide()
                        showTimeDropDownSheet = false
                    }
                },
                onClick = { localDateTime, isSelected ->
                    viewModel.setParam(
                        key = activeDropDownField?.id.orEmpty(),
                        value = localDateTime.toString()
                    )
                    viewModel.setParam(
                        key = TIME_ANYTIME_ENABLED,
                        value = isSelected.toString()
                    )
                    scope.launch {
                        timeDropDownSheetState.hide()
                        showTimeDropDownSheet = false
                    }
                }
            )
        }
    }
}

@Composable
private fun HabitContent(
    viewModel: AddHabitViewModel,
    navController: NavController,
    habitData: HabitDataDto?,
    evidence: EvidenceDto?,
    modifier: Modifier = Modifier,
    onShowEvidencePicker: () -> Unit,
    onOpenDropDown: (FieldDto) -> Unit,
    onOpenTimeSelection: (FieldDto) -> Unit,
) {
    val state by viewModel.state.collectAsState()

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
                        "INPUT_FIELD" -> InputFieldBlock(
                            field = field,
                            onValueChange = {
                                viewModel.setParam(
                                    key = field.id.orEmpty(),
                                    value = it
                                )
                            }
                        )
                        "DROP_DOWN" -> DropDownFieldBlock(
                            field = field,
                            onOpenDropDown = {
                                onOpenDropDown(field)
                            },
                            viewModel = viewModel
                        )
                        "TIME_SELECTION" -> TimeSelectionBlock(
                            field = field,
                            onOpenTimeSelection = {
                                onOpenTimeSelection(field)
                            },
                            viewModel = viewModel
                        )
                        "SELECTION" -> SelectionFieldBlock(
                            viewModel = viewModel,
                            field = field,
                            selectedId = {
                                viewModel.setParam(
                                    key = field.id.orEmpty(),
                                    value = it
                                )
                            }
                        )
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
            if (state.habit?.details?.get("name")?.isNotEmpty() == true) {
                ButtonComponent(
                    button = btn,
                    onClick = {
                        println(viewModel.state.value.habit?.details)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("habit", Json.encodeToString(state.habit))
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    endIconComposable = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = WhiteColor,
                            modifier = Modifier
                                .size(18.dp)
                        )
                    }
                )
            }
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
    onValueChange: (String) -> Unit
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
                    onValueChange(text)
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
                        .background(LightestBackgroundColor, shape = RoundedCornerShape(6.dp))
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
    viewModel: AddHabitViewModel,
    field: FieldDto,
    onOpenDropDown: (FieldDto) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val dd = field.dropDown ?: return
    val selected = dd.options?.firstOrNull {
        it.id == state.habit?.details?.get(field.id)
    }

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
                .bounceClick {
                    onOpenDropDown(field)
                }
                .clip(RoundedCornerShape(20.dp))
                .background(AccentColor2)
                .padding(horizontal = 16.dp)
                .height(35.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selected?.selectedTitle.orEmpty(),
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
private fun TimeSelectionBlock(
    viewModel: AddHabitViewModel,
    field: FieldDto,
    onOpenTimeSelection: (FieldDto) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val dd = field.timeSelection ?: return
    val selectedTitle = state.habit?.details?.get(field.id)
    val anytime = state.habit?.details?.get(TIME_ANYTIME_ENABLED) ?: true

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
                .bounceClick {
                    onOpenTimeSelection(field)
                }
                .clip(RoundedCornerShape(20.dp))
                .background(AccentColor2)
                .padding(horizontal = 16.dp)
                .height(35.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getTimeText(selectedTitle, anytime as? Boolean),
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

fun getTimeText(value: String?, anytime: Boolean?): String {
    if (value != null) {
        runCatching {
            val date = LocalDateTime.parse(value)
            if (anytime ?: false) return "Anytime (${date.formatToReadableDate()})"

            return date.formatToReadableDateTime()
        }
    }

    return "Select a date"
}

fun LocalDateTime.formatToReadableDate(): String {
    val day = this.dayOfMonth
    val month = this.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val year = this.year

    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }

    return "$day$suffix ${month.take(3)} $year"
}

fun LocalDateTime.formatToReadableDateTime(): String {
    val day = this.dayOfMonth
    val month = this.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val year = this.year

    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }

    val hour = this.hour
    val minute = this.minute.toString().padStart(2, '0')
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }

    return "$day$suffix ${month.take(3)} $hour12:$minute $amPm"
}

fun LocalDateTime.formatToTime(): String {
    val hour = this.hour
    val minute = this.minute.toString().padStart(2, '0')
    val amPm = if (hour < 12) "am" else "pm"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }

    return "$hour12:$minute $amPm"
}

@Composable
private fun SelectionFieldBlock(
    viewModel: AddHabitViewModel,
    field: FieldDto,
    selectedId: (String) -> Unit
) {
    val selection = field.selection ?: return
    val state by viewModel.state.collectAsState()
    var selectedId by remember(selection) {
        mutableStateOf(selection.options?.firstOrNull {
            it.id == state.habit?.details?.get(field.id)
        }?.id)
    }

    selection.options?.firstOrNull {
        it.id == state.habit?.details?.get(field.id)
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
                        .bounceClick {
                            selectedId = opt.id
                            selectedId(selectedId.orEmpty())
                        }
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
                        .bounceClick { onOptionSelected(option) },
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

@Composable
fun TimeDropDownOptionsSheet(
    dropDown: TimeSelectionDto,
    onOptionSelected: (DropDownOptionDto) -> Unit,
    onClose: () -> Unit,
    onClick: (LocalDateTime, Boolean) -> Unit
) {
    val platformContext = LocalPlatformContext.current
    var isChecked by remember { mutableStateOf(false) }
    var selectedDate: LocalDateTime? by remember { mutableStateOf(null) }

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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select time",
                    style = CustomTextStyle.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = WhiteColor
                    ),
                    modifier = Modifier
                        .weight(1f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .bounceClick {
                            pickDate(platformContext, onDatePicked = {
                                println(it)
                                selectedDate = it
                            })
                        },
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = selectedDate?.formatToReadableDateTime() ?: "Select",
                        style = CustomTextStyle.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = WhiteColor
                        ),
                        modifier = Modifier
                    )
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = WhiteColor
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                DashedCheckBox(
                    isChecked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                    }
                )
                Text(
                    text = "Anytime",
                    style = CustomTextStyle.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = WhiteColor
                    ),
                    modifier = Modifier
                )
            }
        }


        CustomButton(
            text = "Select this date",
            modifier = Modifier
                .padding(top = 50.dp),
            onClick = {
                selectedDate?.let {
                    onClick(it, isChecked)
                }
            }
        )
    }
}