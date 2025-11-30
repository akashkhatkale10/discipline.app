package com.honeycomb.disciplineapp.presentation.focus_app.ui.blocked_apps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.honeycomb.disciplineapp.BlueColor
import com.honeycomb.disciplineapp.BottomSheetColor
import com.honeycomb.disciplineapp.CustomTextStyle
import com.honeycomb.disciplineapp.RedColor
import com.honeycomb.disciplineapp.SubtitleTextColor
import com.honeycomb.disciplineapp.TitleTextColor
import com.honeycomb.disciplineapp.WhiteColor
import com.honeycomb.disciplineapp.nunitoFontFamily
import com.honeycomb.disciplineapp.presentation.focus_app.BreakPolicy
import com.honeycomb.disciplineapp.presentation.focus_app.FocusSessionConfig
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo
import com.honeycomb.disciplineapp.presentation.focus_app.models.toPainter
import com.honeycomb.disciplineapp.presentation.focus_app.ui.common.BlurButton
import com.honeycomb.disciplineapp.presentation.focus_app.ui.common.SelectedBlurButton
import com.honeycomb.disciplineapp.presentation.focus_app.ui.common.SmallButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomSecondaryButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTertiaryButton
import com.honeycomb.disciplineapp.presentation.utils.LocalPlatformContext
import com.honeycomb.disciplineapp.presentation.utils.addBlurBackground
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import com.honeycomb.disciplineapp.presentation.utils.addStandardTopPadding
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedAppsBottomSheet(
    paddingTop: Dp,
    selectedApps: List<AppInfo>,
    dismiss: (selectedApps: List<AppInfo>) -> Unit = {}
) {
    val viewModel = koinViewModel<BlockedAppsBottomSheetViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalPlatformContext.current

    var showInstalledAppsSheet by remember { mutableStateOf(false) }
    val showInstalledAppsSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.initData(context, selectedApps)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(30.dp),
        color = BottomSheetColor, // or any solid color
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .addStandardHorizontalPadding()
                .padding(top = 12.dp, bottom = 30.dp),
        ) {
            Scrimmer(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            BottomSheetTitle(
                emojie = "\uD83D\uDED1",
                title = "block apps / websites",
                modifier = Modifier
                    .padding(top = 32.dp)
            )

            with(state.blockedApp) {
                Column(
                    modifier = Modifier
                        .padding(top = 45.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = TitleTextColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = nunitoFontFamily
                                )
                            ) {
                                append(title)
                            }
                            withStyle(
                                SpanStyle(
                                    color = SubtitleTextColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = nunitoFontFamily
                                )
                            ) {
                                append("  (${blockedApps.size} / $totalApps)")
                            }
                        },
                        modifier = Modifier
                    )
                    Text(
                        subtitle,
                        style = CustomTextStyle.copy(
                            color = SubtitleTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(blockedApps) { index, item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                BlurButton(
                                    content = {
                                        Image(
                                            painter = item.icon.toPainter(),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )

                                        Text(
                                            item.name,
                                            style = CustomTextStyle.copy(
                                                color = TitleTextColor,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )
                                    },
                                    onClick = {

                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Box(
                                    modifier = Modifier
                                        .bounceClick {
                                            viewModel.removeApp(
                                                packageName = item.packageName,
                                            )
                                        }
                                        .size(18.dp)
                                        .background(
                                            color = RedColor,
                                            shape = CircleShape
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = RedColor.copy(alpha = 0.5f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = null,
                                        tint = WhiteColor,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                    SmallButton(
                        startComposable = {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = WhiteColor,
                                modifier = Modifier.size(12.dp)
                            )
                        },
                        text = buttonText,
                        onClick = {
                            showInstalledAppsSheet = true
                        }
                    )

                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(top = 60.dp)
            ) {
                CustomTertiaryButton(
                    text = "save",
                    onClick = {
                        dismiss(state.blockedApp.blockedApps)
                    },
                )
                CustomSecondaryButton(
                    text = "cancel",
                    onClick = {
                        dismiss(emptyList())
                    }
                )
            }
        }
    }

    if (showInstalledAppsSheet) {
        ModalBottomSheet(
            sheetState = showInstalledAppsSheetState,
            onDismissRequest = {
                showInstalledAppsSheet = false
            },
            dragHandle = null,
            containerColor = Color.Transparent,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            scrimColor = Color.Black.copy(alpha = 0.4f),
            modifier = Modifier
                .padding(top = paddingTop)
        ) {
            InstalledAppsBottomSheet(
                dismiss = {
                    showInstalledAppsSheet = false
                    if (it.isNotEmpty()) viewModel.addApps(it)
                },
                installedApps = state.installedApps,
                selectedApps = state.blockedApp.blockedApps
            )
        }
    }
}

@Composable
fun BottomSheetTitle(
    title: String,
    modifier: Modifier = Modifier,
    emojie: String = "",
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (emojie.isNotEmpty()) {
            Text(
                "$emojie  ",
                style = CustomTextStyle.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
            )
        }
        Text(
            title,
            style = CustomTextStyle.copy(
                color = TitleTextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Scrimmer(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(60.dp)
            .background(
                color = WhiteColor,
                shape = RoundedCornerShape(100.dp)
            )
            .height(4.dp)
    )
}
