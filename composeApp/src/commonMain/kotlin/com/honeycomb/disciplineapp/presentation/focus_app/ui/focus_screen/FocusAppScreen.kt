package com.honeycomb.disciplineapp.presentation.focus_app.ui.focus_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.RedColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.focus_app.BreakPolicy
import com.honeycomb.disciplineapp.presentation.focus_app.FocusSessionConfig
import com.honeycomb.disciplineapp.presentation.focus_app.PlatformScreenTimeManager
import com.honeycomb.disciplineapp.presentation.focus_app.ui.common.BlurButton
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.utils.DataUtils.getCurrentFormattedDate
import com.honeycomb.disciplineapp.presentation.utils.LocalPlatformContext
import com.honeycomb.disciplineapp.presentation.utils.accentBackgroundColor
import com.honeycomb.disciplineapp.presentation.utils.addStandardTopPadding
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FocusAppScreen(
    navController: NavController,
    modifier: Modifier
) {
    val viewModel = koinViewModel<FocusViewModel>()

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
        val controller = remember { PlatformScreenTimeManager().apply {
            setup(context)
        } }

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
                        .padding(top = 32.dp),
                    onClick = {

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
                            "2 apps",
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
                        .padding(top = 16.dp),
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
                        .padding(top = 16.dp),
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
                    .padding(bottom = 50.dp)
            ) {
                CustomButton(
                    text = "Start focus session",
                    onClick = {
                        // viewModel.requestPermission()
                        controller.startMonitoring(
                            config = FocusSessionConfig(
                                penaltyEnabled = false,
                                breaksAllowed = BreakPolicy.TWO_BREAKS,
                                durationMinutes = 2,
                                blockedApps = listOf(
                                    "com.google.android.youtube"
                                )
                            )
                        )
                    },
                    startIconComposable = {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = WhiteColor,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                )
            }
        }

    }
}