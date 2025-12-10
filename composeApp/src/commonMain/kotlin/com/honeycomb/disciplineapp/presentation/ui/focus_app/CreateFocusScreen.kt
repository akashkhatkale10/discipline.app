package com.honeycomb.disciplineapp.presentation.ui.focus_app

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.honeycomb.disciplineapp.AccentColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.LightBackgroundColor
import com.honeycomb.disciplineapp.RedColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.data.dto.ButtonDto
import com.honeycomb.disciplineapp.data.dto.FieldDto
import com.honeycomb.disciplineapp.data.dto.InputFieldDto
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.ui.Theme
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.dashedBorder
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.honeycomb.disciplineapp.LightestBackgroundColor
import com.honeycomb.disciplineapp.presentation.focus_app.AppBlocker
import com.honeycomb.disciplineapp.presentation.focus_app.SelectedAppsIconView
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo
import com.honeycomb.disciplineapp.presentation.ui.add_habit.TimeDropDownOptionsSheet
import com.honeycomb.disciplineapp.presentation.ui.add_habit.formatToReadableDate
import com.honeycomb.disciplineapp.presentation.ui.add_habit.formatToReadableDateTime
import com.honeycomb.disciplineapp.presentation.ui.common.pickDate
import com.honeycomb.disciplineapp.presentation.utils.Constants.TIME_ANYTIME_ENABLED
import com.honeycomb.disciplineapp.presentation.utils.LocalPlatformContext
import com.honeycomb.disciplineapp.presentation.utils.noRippleClickable
import com.honeycomb.disciplineapp.presentation.utils.now
import com.honeycomb.disciplineapp.presentation.utils.plus
import disciplineapp.composeapp.generated.resources.Res
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


enum class FocusType {
    SOFT, HARD
}

enum class ScheduleType {
    NOW, LATER
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun CreateFocusScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    var selectedFocusType by remember { mutableStateOf(FocusType.HARD) }
    var selectedScheduleType by remember { mutableStateOf(ScheduleType.NOW) }
    var focusSessionName by remember { mutableStateOf("") }
    var time by remember { mutableStateOf(15) }
    var fromDate by remember { mutableStateOf(LocalDateTime.now()) }
    var endDate by remember { mutableStateOf(LocalDateTime.now().plus(15, DateTimeUnit.MINUTE)) }
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current
    val appBlocker = remember {
        AppBlocker()
    }

    var selectedAppTokens: List<AppInfo> by remember {
        mutableStateOf(emptyList())
    }

    Column(
        modifier = modifier
            .padding(bottom = 30.dp)
            .fillMaxSize()
            .background(
                brush = Brush
                    .verticalGradient(theme.backgroundColorGradient)
            )
            .noRippleClickable {
                keyboard?.hide()
            },
    ) {
        CustomTopBar(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            startComposable = {
                BorderIconButton(
                    modifier = Modifier
                        .bounceClick {
                            println("popped: ${navController.popBackStack()}")
                        },
                    iconComposable = {
                        Icon(
                            Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = theme.quaternaryColor
                        )
                    },
                )
            },
            endComposable = {
            },
            midComposable = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                                append("start a focus session")
                            }
                        },
                        style = CustomTextStyle
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .addStandardHorizontalPadding()
                .padding(top = 24.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = theme.quaternaryColor,
                            fontFamily = nunitoFontFamily
                        )
                    ) {
                        append("every distraction is a betrayal your future self will remember")
                    }
                },
                style = CustomTextStyle,
                textAlign = TextAlign.Center
            )


            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                Image(
                    painter = painterResource(
                        Res.drawable.glow
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }


            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            // Time Picker
            if (selectedScheduleType == ScheduleType.LATER) {
                SchedulePickerRow(
                    startDate = fromDate,
                    endDate = endDate,
                    onEndPicked = {
                        endDate = it
                    },
                    onStartPicked = {
                        fromDate = it
                    }
                )
            } else {
                TimePickerRow(
                    time = time,
                    onTimeChange = { time = it }
                )
            }

            Row (
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                FocusOptionRow(
                    end = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "all",
                                style = CustomTextStyle.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = RedColor
                                )
                            )

                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = theme.quaternaryColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    title = "\uD83D\uDEAB   blocked apps",
                    onClick = {
                        appBlocker.selectApps(
                            exclude = true,
                            onAppsSelected = {
                                selectedAppTokens = it
                            }
                        )
                    },
                    subtitle = if (selectedAppTokens.isNotEmpty()) "except: ${selectedAppTokens.size} apps" else "",
                    modifier = Modifier
                        .weight(0.55f)
                )

                // Settings Section
                FocusOptionRow(
                    end = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "all",
                                style = CustomTextStyle.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = theme.quaternaryColor
                                )
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint =  theme.quaternaryColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    title = "settings",
                    modifier = Modifier
                        .weight(0.45f)
                )
            }

            FocusOptionRow(
                end = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "hard",
                            style = CustomTextStyle.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = theme.quaternaryColor
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint =  theme.quaternaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                titleIcon = {
                    AnimatedLogo(14)
                },
                title = "focus mode",
                modifier = Modifier
            )

            CustomButton(
                type = ButtonDto.ButtonType.PRIMARY_BUTTON,
                text = if (selectedScheduleType == ScheduleType.NOW) "start timer" else "schedule",
                modifier = Modifier,
                startIconComposable = {
                    if (selectedScheduleType == ScheduleType.NOW) {
                        Icon(
                            Icons.Filled.PlayArrow,
                            contentDescription = null,
                            tint = WhiteColor,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SchedulePickerRow(
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    onStartPicked: (LocalDateTime) -> Unit,
    onEndPicked: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    val context = LocalPlatformContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .addStandardHorizontalPadding()
    ) {
        Box(
            modifier = Modifier
                .bounceClick {
                    pickDate(
                        context = context,
                        onDatePicked = { dateTime ->
                            onStartPicked(dateTime)
                        }
                    )
                }
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = startDate.formatToReadableDateTime(),
                style = CustomTextStyle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }

        Text(
            text = "-",
            style = CustomTextStyle.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = WhiteColor
            )
        )

        Box(
            modifier = Modifier
                .bounceClick {
                    pickDate(
                        context = context,
                        onDatePicked = { dateTime ->
                            onEndPicked(dateTime)
                        }
                    )
                }
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = endDate.formatToReadableDateTime(),
                style = CustomTextStyle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }
    }
}

@Composable
fun BorderFocusTypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    Box(
        modifier = modifier
            .bounceClick(onClick = onClick)
            .clip(RoundedCornerShape(14.dp))
            .background(
                theme.secondaryButtonColor
            )
            .border(
                width = 1.dp,
                color = if (isSelected) theme.titleColor else theme.tertiaryColor,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = CustomTextStyle.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = WhiteColor
            )
        )
    }
}

@Composable
fun FocusTypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    Box(
        modifier = modifier
            .bounceClick(onClick = onClick)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) RedColor else theme.secondaryButtonColor
            )
            .border(
                width = 1.dp,
                color = if (isSelected) RedColor.copy(alpha = 0.3f) else theme.tertiaryColor,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = CustomTextStyle.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = WhiteColor
            )
        )
    }
}

@Composable
fun FocusSessionNameInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Input field with icon and placeholder
        DashedInputField(
            value = value,
            onValueChange = onValueChange,
            icon = {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = null,
                    tint = WhiteColor,
                    modifier = Modifier
                        .size(16.dp)
                )
            }
        )
        // Example text
        Text(
            text = "ex: my work session, my gym session",
            style = CustomTextStyle.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = theme.quaternaryColor
            ),
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}

@Composable
fun DashedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    val theme = LocalTheme.current
    BasicTextField(
        value = value,
        onValueChange = { new ->
            onValueChange(new)
        },
        cursorBrush = Brush.verticalGradient(
            colors = listOf(WhiteColor, WhiteColor)
        ),
        textStyle = CustomTextStyle.copy(
            fontSize = 14.sp,
            color = theme.titleColor
        ),
        modifier = modifier.fillMaxWidth(),
        decorationBox = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 44.dp)
                    .background(theme.secondaryButtonColor, shape = RoundedCornerShape(14.dp))
                    .dashedBorder(
                        strokeWidth = 1.dp,
                        color = SubtitleTextColor,
                        cornerRadius = 14.dp
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(16.dp)
                    ) {
                        icon()
                    }

                    if (value.isEmpty()) {
                        Text(
                            text = "name your focus session",
                            style = CustomTextStyle.copy(
                                fontSize = 14.sp,
                                color = theme.quaternaryColor
                            )
                        )
                    }

                    it()
                }
            }
        }
    )
}

@Composable
fun FocusOptionRow(
    end: @Composable () -> Unit = {},
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    subtitle: String = ""
) {
    val theme = LocalTheme.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .bounceClick(onClick = onClick)
                .fillMaxWidth()
                .background(
                    color = theme.secondaryButtonColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding( vertical = 12.dp)
                .padding( end = 8.dp, start = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = CustomTextStyle.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = WhiteColor
                    ),
                    modifier = Modifier.weight(1f)
                )

                end()
            }
        }

        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = CustomTextStyle.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = theme.quaternaryColor
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun FocusOptionRow(
    end: @Composable () -> Unit = {},
    title: String,
    titleIcon: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    subtitle: String = ""
) {
    val theme = LocalTheme.current
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = modifier
                .bounceClick(onClick = onClick)
                .fillMaxWidth()
                .background(
                    color = theme.secondaryButtonColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                titleIcon()


                Text(
                    text = title,
                    style = CustomTextStyle.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = WhiteColor
                    ),
                    modifier = Modifier.weight(1f)
                )

                end()
            }
        }

        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = CustomTextStyle.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = theme.quaternaryColor
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun TimePickerRow(
    time: Int,
    onTimeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .addStandardHorizontalPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Minus Button
        Box(
            modifier = Modifier
                .width(50.dp)
                .bounceClick {
                    if (time > 15) {
                        onTimeChange(time - 5)
                    }
                }
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "—",
                style = CustomTextStyle.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }

        // Time Display
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$time mins",
                style = CustomTextStyle.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }

        // Plus Button
        Box(
            modifier = Modifier
                .width(50.dp)
                .bounceClick {
                    onTimeChange(time + 5)
                }
                .clip(RoundedCornerShape(14.dp))
                .background(theme.secondaryButtonColor)
                .border(
                    width = 1.dp,
                    color = theme.tertiaryColor,
                    shape = RoundedCornerShape(14.dp)
                )
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                style = CustomTextStyle.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = WhiteColor
                )
            )
        }
    }
}

@Composable
@Preview
fun CreateFocusScreenPreview() {
    CreateFocusScreen(
        navController = rememberNavController()
    )
}

@Composable
@Preview
fun GradientCircularShadowBoxPreview() {

}

