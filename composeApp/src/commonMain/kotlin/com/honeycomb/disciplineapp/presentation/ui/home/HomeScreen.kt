package com.honeycomb.disciplineapp.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.data.dto.ButtonDto
import com.honeycomb.disciplineapp.data.dto.FocusSessionDto
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.ui.add_habit.formatToTime
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.ui.common.DashedLine
import com.honeycomb.disciplineapp.presentation.ui.focus_app.CreateFocusViewModel
import com.honeycomb.disciplineapp.presentation.ui.focus_app.TimerState
import com.honeycomb.disciplineapp.presentation.utils.Constants.HORIZONTAL_PADDING
import com.honeycomb.disciplineapp.presentation.utils.DataUtils.getCurrentFormattedDate
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.epochMillisToHoursMinutes
import com.honeycomb.disciplineapp.presentation.utils.formatDuration
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    focusViewModel: CreateFocusViewModel,
    isExpanded: Boolean,
    navController: NavController,
    onExpandClick: () -> Unit
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusState by focusViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getHomeData()
    }

    var selectedDate by remember {
        mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    }
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            WeeklyDateSelector(
                onDateSelected = { date ->
                    println("Selected date: $date")
                    selectedDate = date
                },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .addStandardHorizontalPadding(),
                today = today
            )

            when {
                state.isLoading -> {
                    HomeScreenShimmer(
                        modifier = Modifier
                            .padding(horizontal = HORIZONTAL_PADDING.dp)
                            .fillMaxSize()
                            .padding(top = 100.dp)
                    )
                }

                state.data != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 30.dp, bottom = 30.dp),
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (state.data!!.getOrElse(selectedDate.toString(), { emptyList() }).isEmpty()) {
                                item {
                                    TitleSubtitleAction(
                                        title = "no focus session",
                                        subtitle = "looks like there is no focus session recorded on this day",
                                        modifier = Modifier
                                            .padding(top = 50.dp)
                                            .addStandardHorizontalPadding(),
                                        action = {}
                                    )
                                }
                            } else {
                                itemsIndexed(
                                    state.data!![selectedDate.toString()] ?: emptyList()
                                ) { index, item ->
                                    HomeFocusItem(
                                        item = item,
                                        index = index,
                                        onItemClick = {

                                        },
                                        modifier = Modifier
                                            .addStandardHorizontalPadding()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


        if (today == selectedDate) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                LocalTheme.current.backgroundColorGradient.first(),
                                LocalTheme.current.backgroundColorGradient.first(),
                            )
                        )
                    )
                    .addStandardHorizontalPadding()
                    .padding(bottom = 30.dp)
            ) {
                TitleSubtitleAction(
                    title = "",
                    subtitle = "",
                    modifier = Modifier,
                    action = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (focusState == null || focusState?.timerState == TimerState.IDLE
                                || focusState?.timerState == TimerState.STOPPED
                                || focusState?.timerState == TimerState.COMPLETED
                            ) {
                                CustomButton(
                                    text = "start a focus session",
                                    startIconComposable = {
                                        Icon(
                                            Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = WhiteColor,
                                            modifier = Modifier
                                                .size(18.dp)
                                        )
                                    },
                                    onClick = {
                                        navController
                                            .navigate(Screen.CreateFocusScreenRoute)
                                    }
                                )
                            }

                            CustomButton(
                                text = "schedule a focus session",
                                startIconComposable = {
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = WhiteColor,
                                        modifier = Modifier
                                            .size(14.dp)
                                    )
                                },
                                onClick = {

                                },
                                type = ButtonDto.ButtonType.SECONDARY_BUTTON
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HomeFocusItem(
    item: FocusSessionDto,
    index: Int,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Column(
        modifier = modifier
            .bounceClick {
                onItemClick()
            }
            .background(
                color = theme.secondaryButtonColor,
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = 1.dp,
                color = theme.tertiaryColor,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedLogo(14)

            Text(
                text = "focus session",
                style = CustomTextStyle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = theme.titleColor
                ),
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = epochMillisToHoursMinutes(
                        ((item.endTimestamp ?: item.startTimestamp) - item.startTimestamp)
                    ),
                    style = CustomTextStyle.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.titleColor
                    )
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = theme.quaternaryColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Instant
                .fromEpochMilliseconds(item.startTimestamp)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .let {
                    Text(
                        text = it.formatToTime(),
                        style = CustomTextStyle.copy(
                            color = theme.quaternaryColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                    )
            }

            DashedLine(
                color = theme.quaternaryColor,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .weight(1f)
            )

            Instant
                .fromEpochMilliseconds(
                    item.endTimestamp ?: (item.startTimestamp + item.durationSeconds * 1000)
                )
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .let {
                    Text(
                        text = it.formatToTime(),
                        style = CustomTextStyle.copy(
                            color = theme.quaternaryColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                    )
                }
        }
    }
}

@Composable
fun WeeklyDateSelector(
    modifier: Modifier = Modifier,
    today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    onDateSelected: (LocalDate) -> Unit
) {
    val theme = LocalTheme.current
    var state by remember {
        val startOfWeek = today.startOfWeek()
        mutableStateOf(
            WeekState(
                weekStart = startOfWeek,
                selectedDate = today
            )
        )
    }

    val currentWeekStart = today.startOfWeek()
    val isOnCurrentWeek = state.weekStart == currentWeekStart
    val selected = getCurrentFormattedDate(state.selectedDate)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---------- Header ----------
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            ArrowButton(
                enabled = true,
                icon = Icons.Default.KeyboardArrowLeft,
                modifier = Modifier
                    .align(Alignment.CenterStart),
                onClick = {
                    state = state.copy(
                        weekStart = state.weekStart.plusWeeks(-1),
                        selectedDate = state.selectedDate.plusWeeks(-1)
                    )
                    println("Selecting date: ${state.selectedDate}")
                    onDateSelected(state.selectedDate)
                }
            )

            Text(
                text = buildAnnotatedString {
                    if (state.selectedDate == today) {
                        withStyle(
                            SpanStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = WhiteColor,
                                fontFamily = nunitoFontFamily
                            )
                        ) {
                            append("today")
                        }
                    } else {
                        withStyle(
                            SpanStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = WhiteColor,
                                fontFamily = nunitoFontFamily
                            )
                        ) {
                            append("${selected.day}, ")
                        }

                        withStyle(
                            SpanStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = SubtitleTextColor,
                                fontFamily = nunitoFontFamily
                            )
                        ) {
                            append("${selected.month} ${selected.date}")
                        }
                    }
                },
                style = CustomTextStyle,
                modifier = Modifier
                    .align(Alignment.Center)
            )

            ArrowButton(
                enabled = !isOnCurrentWeek,
                icon = Icons.Default.KeyboardArrowRight,
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                onClick = {
                    state = state.copy(
                        weekStart = state.weekStart.plusWeeks(1),
                        selectedDate = state.selectedDate.plusWeeks(1)
                    )
                    println("Selecting date: ${state.selectedDate}")
                    onDateSelected(state.selectedDate)
                }
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---------- Days ----------
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            (0..6).forEach { index ->
                val date = state.weekStart.plus(index, DateTimeUnit.DAY)
                val isSelected = date == state.selectedDate

                DayItem(
                    date = date,
                    isSelected = isSelected
                ) {
                    state = state.copy(selectedDate = date)
                    onDateSelected(date)
                }
            }
        }
    }
}

@Composable
private fun ArrowButton(
    enabled: Boolean,
    icon: ImageVector,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    if (enabled) {
        Box(
            modifier = modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .bounceClick {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
            )
        }
    }
}

@Composable
private fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val theme = LocalTheme.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(44.dp)
            .bounceClick {
                onClick()
            }
    ) {

        Text(
            text = date.dayOfWeek.name.first().toString(),
            style = CustomTextStyle.copy(
                color = theme.titleColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Color.White else theme.secondaryButtonColor
                )
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = CustomTextStyle.copy(
                    color = if (isSelected) Color.Black else theme.titleColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun TitleSubtitleAction(
    title: String,
    subtitle: String,
    action: @Composable () -> Unit,
    titleComposable: @Composable () -> Unit = {},
    middleComposable: (@Composable () -> Unit) = {  },
    modifier: Modifier = Modifier,
    titleFontWeight: FontWeight = FontWeight.Bold,
    titleFontSize: Int = 16,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            titleComposable()
            Text(
                title,
                style = CustomTextStyle.copy(
                    color = TitleTextColor,
                    fontSize = titleFontSize.sp,
                    fontWeight = titleFontWeight
                ),
                textAlign = TextAlign.Center
            )
        }
        Text(
            subtitle,
            style = CustomTextStyle.copy(
                color = SubtitleTextColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .padding(top = 14.dp),
            textAlign = TextAlign.Center
        )

        middleComposable()

        Box(
            modifier = Modifier
                .padding(top = 30.dp)
        ) {
            action()
        }
    }
}

@Composable
fun ActionTitleSubtitle(
    title: String,
    subtitle: String,
    action: @Composable () -> Unit,
    titleComposable: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box() {
            action()
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(top = 30.dp)
        ) {
            titleComposable()
            Text(
                title,
                style = CustomTextStyle.copy(
                    color = TitleTextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
        Text(
            subtitle,
            style = CustomTextStyle.copy(
                color = SubtitleTextColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier
                .padding(top = 14.dp),
            textAlign = TextAlign.Center
        )
    }
}