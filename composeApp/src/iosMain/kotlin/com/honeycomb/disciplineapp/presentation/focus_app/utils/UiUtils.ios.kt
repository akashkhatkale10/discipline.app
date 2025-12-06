package com.honeycomb.disciplineapp.presentation.focus_app.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import platform.UIKit.UIScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenHeight(): Dp {
    return LocalWindowInfo.current.containerSize.height.pxToPoint().dp
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth(): Dp {
    return LocalWindowInfo.current.containerSize.width.pxToPoint().dp
}

fun Int.pxToPoint(): Double = this.toDouble() / UIScreen.mainScreen.scale