package com.honeycomb.disciplineapp.presentation.ui.common


import kotlinx.datetime.LocalDateTime

// Shared Code
expect fun pickDate(context: Any?, onDatePicked: (LocalDateTime) -> Unit)