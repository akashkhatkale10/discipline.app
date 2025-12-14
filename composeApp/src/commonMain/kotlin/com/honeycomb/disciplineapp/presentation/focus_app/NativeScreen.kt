package com.honeycomb.disciplineapp.presentation.focus_app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun NativeScreen(
    modifier: Modifier
)


@Composable
expect fun OnboardingUsageScreen(
    modifier: Modifier
)

@Composable
expect fun SuccessScreenUsageScreen(
    startTime: Long,
    endTime: Long,
    modifier: Modifier
)