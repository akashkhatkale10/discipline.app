package com.honeycomb.disciplineapp.presentation.ui.focus_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.ui.common.BorderIconButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTopBar
import com.honeycomb.disciplineapp.presentation.utils.LocalTheme
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import com.honeycomb.disciplineapp.presentation.utils.noRippleClickable

@Composable
fun FocusScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val theme = LocalTheme.current
    Column(
        modifier = modifier
            .padding(bottom = 30.dp)
            .fillMaxSize()
            .background(
                brush = Brush
                    .verticalGradient(theme.backgroundColorGradient)
            )
            .noRippleClickable {

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
                        append("apps and notifications will be limited during this session.")
                    }
                },
                style = CustomTextStyle,
                textAlign = TextAlign.Center
            )

        }
    }
}