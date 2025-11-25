package com.honeycomb.disciplineapp.presentation.ui.start_routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.BackgroundColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.LightBackgroundColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomSmallButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.ui.common.Logo
import com.honeycomb.disciplineapp.presentation.ui.home.TitleSubtitleAction
import com.honeycomb.disciplineapp.presentation.utils.DataUtils.getCurrentFormattedDate
import com.honeycomb.disciplineapp.presentation.utils.bounceClick

@Composable
fun StartRoutineScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
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
                                append("Start a routine")
                            }
                        },
                        style = CustomTextStyle
                    )
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column {
                TitleSubtitleAction(
                    title = "Your routine starts here",
                    subtitle = "Add habits. Then prove you’re serious",
                    action = {
                        CustomSmallButton(
                            text = "Add a habit",
                            endIconComposable = {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    tint = WhiteColor,
                                    modifier = Modifier
                                        .size(18.dp)
                                )
                            },
                            backgroundColor = LightBackgroundColor
                        )
                    },
                    titleComposable = {
                        Logo(
                            size = 10
                        )
                    }
                )

                TitleSubtitleAction(
                    modifier = Modifier
                        .padding(top = 120.dp),
                    title = "Put something at stake",
                    subtitle = "Commit hard. Make quitting expensive",
                    action = {
                        CustomSmallButton(
                            text = "Add a penalty",
                            endIconComposable = {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    tint = WhiteColor,
                                    modifier = Modifier
                                        .size(18.dp)
                                )
                            },
                        )
                    },
                    titleComposable = {
                        Logo(
                            size = 10,
                            colors = listOf(
                                Color(0xFFFD1D1D),
                                Color(0xFFFCB045),
                            )
                        )
                    }
                )
            }
        }
    }
}