package com.honeycomb.disciplineapp.presentation.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.Screen
import com.honeycomb.disciplineapp.presentation.ui.common.AnimatedLogo
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.utils.Constants.HORIZONTAL_PADDING
import com.honeycomb.disciplineapp.presentation.utils.DataUtils.getCurrentFormattedDate
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    isExpanded: Boolean,
    navController: NavController,
    onExpandClick: () -> Unit
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getHomeData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
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
                                append("${formatted.day}, ")
                            }
                            withStyle(
                                SpanStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = SubtitleTextColor,
                                    fontFamily = nunitoFontFamily
                                )
                            ) {
                                append("${formatted.month} ${formatted.date}")
                            }
                        },
                        style = CustomTextStyle
                    )
                }
            }
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
                if (state.data?.habits != null && state.data?.habits?.isNotEmpty() == true) {

                } else {
                    val addNewScreen = state.data?.addNewScreen
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        TitleSubtitleAction(
                            title = addNewScreen?.title.orEmpty(),
                            subtitle = addNewScreen?.subtitle.orEmpty(),
                            titleFontSize = 18,
                            titleFontWeight = FontWeight.ExtraBold,
                            action = {
                                CustomButton(
                                    text = addNewScreen?.buttonTitle.orEmpty(),
                                    endIconComposable = {
                                        Icon(
                                            Icons.Default.ArrowForward,
                                            contentDescription = null,
                                            tint = WhiteColor
                                        )
                                    },
                                    onClick = {
                                        navController.navigate(
                                            Screen.StartRoutineScreenRoute
                                        )
                                    }
                                )
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(horizontal = 20.dp)
                        )

                        if (isExpanded.not()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(bottom = 50.dp)
                                    .align(Alignment.BottomCenter)
                                    .bounceClick {
                                        onExpandClick()
                                    }
                            ) {
                                Text(
                                    "See more",
                                    style = CustomTextStyle.copy(
                                        color = SubtitleTextColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier
                                )

                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    tint = SubtitleTextColor,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }


        Spacer(
            modifier = Modifier
                .height(100.dp)
        )

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
                fontWeight = FontWeight.Bold
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
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(top = 14.dp),
            textAlign = TextAlign.Center
        )
    }
}