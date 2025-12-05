package com.honeycomb.disciplineapp.presentation.focus_app.ui.focus_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.LightestBackgroundColor
import com.honeycomb.disciplineapp.RedColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.focus_app.AppBlocker
import com.honeycomb.disciplineapp.presentation.focus_app.ui.blocked_apps.BlockedAppsBottomSheet
import com.honeycomb.disciplineapp.presentation.focus_app.ui.blocked_apps.getAppCount
import com.honeycomb.disciplineapp.presentation.focus_app.ui.blocked_apps.getAppInfoCount
import com.honeycomb.disciplineapp.presentation.focus_app.ui.common.BlurButton
import com.honeycomb.disciplineapp.presentation.ui.add_habit.EvidenceSelectionSheet
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomSecondaryButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.utils.DataUtils.getCurrentFormattedDate
import com.honeycomb.disciplineapp.presentation.utils.LocalPlatformContext
import com.honeycomb.disciplineapp.presentation.utils.accentBackgroundColor
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import com.honeycomb.disciplineapp.presentation.utils.addStandardTopPadding
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun FocusAppScreen(
    navController: NavController,
    modifier: Modifier
) {
    val viewModel = koinViewModel<FocusViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showBlockedAppsSheet by remember { mutableStateOf(false) }
    val showBlockedAppsSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = BackgroundColor,
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopBar(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                startComposable = {

                },
                endComposable = {

                },
                midComposable = {
                    val formatted = getCurrentFormattedDate()
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedLogo(
                            size = 12,
                            modifier = Modifier
                        )
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
                                    append("${formatted.day.lowercase()}, ")
                                }
                                withStyle(
                                    SpanStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = SubtitleTextColor,
                                        fontFamily = nunitoFontFamily
                                    )
                                ) {
                                    append(" ${formatted.month.lowercase()} ${formatted.date.lowercase()}")
                                }
                            },
                            style = CustomTextStyle
                        )
                    }
                }
            )
        }
    ) {

        val context = LocalPlatformContext.current

        Box(
            modifier = modifier
                .fillMaxSize()
                .accentBackgroundColor()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "your future self is watching",
                    style = CustomTextStyle.copy(
                        color = TitleTextColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    "every distraction is a betrayal they’ll remember",
                    style = CustomTextStyle.copy(
                        color = SubtitleTextColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .addStandardTopPadding()
                )

                BlurButton(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .addStandardHorizontalPadding(),
                    onClick = {
                        showBlockedAppsSheet = true
                    },
                    content = {
                        Text(
                            "\uD83D\uDED1",
                            style = CustomTextStyle.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                        Text(
                            "apps blocked",
                            style = CustomTextStyle.copy(
                                color = TitleTextColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            getAppInfoCount(state.selectedApps),
                            style = CustomTextStyle.copy(
                                color = RedColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = SubtitleTextColor
                        )
                    }
                )
                BlurButton(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .addStandardHorizontalPadding(),
                    onClick = {

                    },
                    content = {
                        Text(
                            "⚠\uFE0F",
                            style = CustomTextStyle.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                        Text(
                            "penalty",
                            style = CustomTextStyle.copy(
                                color = TitleTextColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "set a penalty",
                            style = CustomTextStyle.copy(
                                color = TitleTextColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = SubtitleTextColor
                        )
                    }
                )
                BlurButton(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .addStandardHorizontalPadding(),
                    onClick = {

                    },
                    content = {
                        Text(
                            "breaks allowed ?",
                            style = CustomTextStyle.copy(
                                color = TitleTextColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "yes",
                            style = CustomTextStyle.copy(
                                color = TitleTextColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = SubtitleTextColor
                        )
                    }
                )
            }


            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 50.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                ) {
                    BlurButton(
                        content = {
                            Box(modifier = Modifier
                                .width(16.dp).height(2.dp).background(WhiteColor))
                        },
                        modifier = Modifier
                            .weight(0.15f),
                        alignment = Alignment.CenterHorizontally
                    )
                    BlurButton(
                        content = {
                            Text(
                                text = "30 mins",
                                style = CustomTextStyle.copy(
                                    color = WhiteColor,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            )
                        },
                        modifier = Modifier
                            .weight(0.7f),
                        alignment = Alignment.CenterHorizontally
                    )
                    BlurButton(
                        content = {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = WhiteColor
                            )
                        },
                        modifier = Modifier
                            .weight(0.15f),
                        alignment = Alignment.CenterHorizontally
                    )
                }

                CustomButton(
                    text = "start focus session",
                    onClick = {
                        scope.launch {
                            val a = AppBlocker()
                            a.requestPermission()
                        }
//                        navController
//                            .navigate(Screen.DemoScreenRoute)
                    },
                    startIconComposable = {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = WhiteColor,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                )
            }
        }

        if (showBlockedAppsSheet) {
            ModalBottomSheet(
                sheetState = showBlockedAppsSheetState,
                onDismissRequest = {
                    showBlockedAppsSheet = false
                },
                dragHandle = null,
                containerColor = Color.Transparent,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                scrimColor = Color.Black.copy(alpha = 0.4f),
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
            ) {
                BlockedAppsBottomSheet(
                    dismiss = { apps ->
                        showBlockedAppsSheet = false
                        if (apps.isNotEmpty()) viewModel.addAppsBlocked(apps)
                    },
                    paddingTop = it.calculateTopPadding(),
                    selectedApps = state.selectedApps
                )
            }
        }
    }
}