package com.honeycomb.disciplineapp.presentation.focus_app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.honeycomb.disciplineapp.LocalNativeViewFactory
import platform.UIKit.UIViewController

@Composable
actual fun NativeScreen(modifier: Modifier) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = modifier,
        factory = {
            factory.createNativeScreen()
        }
    )
}

interface NativeViewFactory {
    fun createNativeScreen(): UIViewController
    fun createOnboardingUsageScreen(): UIViewController
}

@Composable
actual fun OnboardingUsageScreen(
    modifier: Modifier
) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = modifier,
        factory = {
            factory.createOnboardingUsageScreen()
        }
    )
}