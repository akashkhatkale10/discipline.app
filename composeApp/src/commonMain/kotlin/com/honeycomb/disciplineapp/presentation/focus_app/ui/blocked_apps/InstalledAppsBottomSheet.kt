package com.honeycomb.disciplineapp.presentation.focus_app.ui.blocked_apps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon

import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
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
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppCategory
import com.honeycomb.disciplineapp.presentation.focus_app.models.AppInfo
import com.honeycomb.disciplineapp.presentation.focus_app.models.toPainter
import com.honeycomb.disciplineapp.presentation.focus_app.ui.common.BlurButton
import com.honeycomb.disciplineapp.presentation.focus_app.ui.common.SelectedBlurButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomSecondaryButton
import com.honeycomb.disciplineapp.presentation.ui.common.CustomTertiaryButton
import com.honeycomb.disciplineapp.presentation.utils.addStandardHorizontalPadding
import com.honeycomb.disciplineapp.presentation.utils.bounceClick
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

private val pills = listOf(
    ALL,
    CATEGORY
)
private const val ALL = "All"
private const val CATEGORY = "Categories"
@OptIn(KoinExperimentalAPI::class)
@Composable
fun InstalledAppsBottomSheet(
    installedApps: List<AppInfo>,
    selectedApps: List<AppInfo>,
    dismiss: (selectedApps: List<AppInfo>) -> Unit = {}
) {
    val viewModel = koinViewModel<InstalledAppsBottomSheetViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedPill by remember {
        mutableStateOf(ALL)
    }


    LaunchedEffect(Unit) {
        viewModel.initData(installedApps, selectedApps)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(30.dp),
        color = BottomSheetColor, // or any solid color
        shadowElevation = 8.dp
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .addStandardHorizontalPadding()
                    .padding(top = 12.dp),
            ) {
                Scrimmer(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                BottomSheetTitle(
                    emojie = "",
                    title = "all apps (${installedApps.size})",
                    modifier = Modifier
                        .padding(top = 32.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(top = 20.dp)
                ) {
                    pills.forEach {
                        Row(
                            modifier = Modifier
                                .height(30.dp)
                                .bounceClick {
                                    selectedPill = it
                                }
                                .clip(
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (it == selectedPill) BlueColor.copy(
                                        0.1f
                                    ) else WhiteColor.copy(
                                        alpha = 0.1f
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color =if (it == selectedPill) BlueColor else WhiteColor.copy(
                                        alpha = 0.02f
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                it,
                                style = CustomTextStyle.copy(
                                    color = TitleTextColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(top = 45.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 180.dp)
                ) {
                    if (selectedPill == ALL) {
                        itemsIndexed(state.installedApp) { index, item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (item.isSelected) {
                                    SelectedBlurButton(
                                        content = {
//                                            Image(
//                                                painter = item.app.icon.toPainter()!!,
//                                                contentDescription = null,
//                                                modifier = Modifier.size(20.dp)
//                                            )

                                            Text(
                                                item.app.name,
                                                style = CustomTextStyle.copy(
                                                    color = TitleTextColor,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                        },
                                        onClick = {
                                            viewModel.selectAnApp(
                                                false,
                                                packageName = item.app.packageName,
                                            )
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                } else {
                                    BlurButton(
                                        content = {
//                                            Image(
//                                                painter = item.app.icon.toPainter()!!,
//                                                contentDescription = null,
//                                                modifier = Modifier.size(20.dp)
//                                            )

                                            Text(
                                                item.app.name,
                                                style = CustomTextStyle.copy(
                                                    color = TitleTextColor,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                        },
                                        onClick = {
                                            viewModel.selectAnApp(
                                                true,
                                                packageName = item.app.packageName,
                                            )
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                }

                                if (item.isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .bounceClick {
                                                viewModel.selectAnApp(
                                                    false,
                                                    packageName = item.app.packageName,
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
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .bounceClick {
                                                viewModel.selectAnApp(
                                                    true,
                                                    packageName = item.app.packageName,
                                                )
                                            }
                                            .size(18.dp)
                                            .background(
                                                color = BlueColor,
                                                shape = CircleShape
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = BlueColor.copy(alpha = 0.5f),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = null,
                                            tint = WhiteColor,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (selectedPill == CATEGORY) {
                        val categories = groupAppsByCategory(state.installedApp)
                        itemsIndexed(categories.keys.toList()) { index, item ->

                            var isExpanded by remember { mutableStateOf(false) }
                            var isCategorySelected by remember(state.installedApp) {
                                mutableStateOf(
                                    categories[item]?.all { it.isSelected } ?: false
                                )
                            }

                            val selectedCount by remember(state.installedApp) {
                                mutableStateOf(categories[item]?.count { it.isSelected } ?: 0)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                BlurButton(
                                    content = {
                                        if (item.emojie.isNotEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(
                                                        color = WhiteColor,
                                                        shape = RoundedCornerShape(4.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    item.emojie,
                                                    style = CustomTextStyle.copy(
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                    ),
                                                    modifier = Modifier
                                                )
                                            }
                                        }

                                        Text(
                                            item.title,
                                            style = CustomTextStyle.copy(
                                                color = TitleTextColor,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )


                                        Text(
                                            if (isCategorySelected) "All"
                                            else if (selectedCount > 1) "$selectedCount apps"
                                            else if (selectedCount == 1) "1 app" else "",
                                            style = CustomTextStyle.copy(
                                                color = SubtitleTextColor,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            modifier = Modifier
                                        )


                                        Icon(
                                            Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = SubtitleTextColor,
                                        )
                                    },
                                    onClick = {
                                        isExpanded = !isExpanded
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                if (isCategorySelected) {
                                    Box(
                                        modifier = Modifier
                                            .bounceClick {
                                                viewModel.selectCategory(
                                                    category = item,
                                                    select = false
                                                )
                                            }
                                            .size(18.dp)
                                            .background(
                                                color = BlueColor,
                                                shape = CircleShape
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = BlueColor,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = WhiteColor,
                                            modifier = Modifier
                                                .size(12.dp)
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .bounceClick {
                                                viewModel.selectCategory(
                                                    category = item,
                                                    select = true
                                                )
                                            }
                                            .size(18.dp)
                                            .background(
                                                color = Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = BlueColor,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {

                                    }
                                }
                            }

                            if (isExpanded) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier
                                        .padding(top = 12.dp)
                                ) {
                                    categories[item]?.forEach { item ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Spacer(
                                                modifier = Modifier
                                                    .width(30.dp)
                                            )
                                            if (item.isSelected) {
                                                SelectedBlurButton(
                                                    content = {
//                                                        Image(
//                                                            painter = item.app.icon.toPainter()!!,
//                                                            contentDescription = null,
//                                                            modifier = Modifier.size(20.dp)
//                                                        )

                                                        Text(
                                                            item.app.name,
                                                            style = CustomTextStyle.copy(
                                                                color = TitleTextColor,
                                                                fontSize = 14.sp,
                                                                fontWeight = FontWeight.SemiBold
                                                            ),
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                    },
                                                    onClick = {
                                                        viewModel.selectAnApp(
                                                            false,
                                                            packageName = item.app.packageName,
                                                        )
                                                    },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                )
                                            } else {
                                                BlurButton(
                                                    content = {
//                                                        Image(
//                                                            painter = item.app.icon.toPainter()!!,
//                                                            contentDescription = null,
//                                                            modifier = Modifier.size(20.dp)
//                                                        )

                                                        Text(
                                                            item.app.name,
                                                            style = CustomTextStyle.copy(
                                                                color = TitleTextColor,
                                                                fontSize = 14.sp,
                                                                fontWeight = FontWeight.SemiBold
                                                            ),
                                                            modifier = Modifier.weight(1f)
                                                        )
                                                    },
                                                    onClick = {
                                                        viewModel.selectAnApp(
                                                            true,
                                                            packageName = item.app.packageName,
                                                        )
                                                    },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                )
                                            }

                                            if (item.isSelected) {
                                                Box(
                                                    modifier = Modifier
                                                        .bounceClick {
                                                            viewModel.selectAnApp(
                                                                false,
                                                                packageName = item.app.packageName,
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
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .bounceClick {
                                                            viewModel.selectAnApp(
                                                                true,
                                                                packageName = item.app.packageName,
                                                            )
                                                        }
                                                        .size(18.dp)
                                                        .background(
                                                            color = BlueColor,
                                                            shape = CircleShape
                                                        )
                                                        .border(
                                                            width = 1.dp,
                                                            color = BlueColor.copy(alpha = 0.5f),
                                                            shape = CircleShape
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        Icons.Default.Add,
                                                        contentDescription = null,
                                                        tint = WhiteColor,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .addStandardHorizontalPadding()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                BottomSheetColor,
                                BottomSheetColor,
                                BottomSheetColor,
                                BottomSheetColor,
                            )
                        )
                    )
                    .padding(bottom = 30.dp, top = 50.dp)
            ) {
                val s = state
                    .installedApp
                    .filter { it.isSelected }.map { it.app }
                if (s.isNotEmpty()) {
                    CustomTertiaryButton(
                        text = "add",
                        onClick = {
                            dismiss(s)
                        },
                    )
                }
                CustomSecondaryButton(
                    text = "cancel",
                    onClick = {
                        dismiss(emptyList())
                    }
                )
            }
        }
    }
}

fun getAppCount(apps: List<InstalledAppState>?): String {
    if (apps == null) return ""

    if (apps.size == 1) return "1 app"

    return "${apps.size} apps"
}
fun getAppInfoCount(apps: List<AppInfo>?): String {
    if (apps == null) return ""

    if (apps.size == 1) return "1 app"

    return "${apps.size} apps"
}

fun groupAppsByCategory(apps: List<InstalledAppState>): Map<AppCategory, List<InstalledAppState>> {
    // Group by category
//    val grouped = apps.groupBy { it.app.category }
//
//    // Move OTHER to last
//    val otherApps = grouped[AppCategory.UNKNOWN]
//    val ordered = linkedMapOf<AppCategory, List<InstalledAppState>>()
//
//    grouped.forEach { category ->
//        if (category.key !is AppCategory.UNKNOWN) {
//            grouped[category.key]?.let { ordered[category.key] = it }
//        }
//    }
//
//    // Add OTHER at the end if exists
//    if (otherApps != null) {
//        ordered[AppCategory.UNKNOWN] = otherApps
//    }

    return mapOf()
}
