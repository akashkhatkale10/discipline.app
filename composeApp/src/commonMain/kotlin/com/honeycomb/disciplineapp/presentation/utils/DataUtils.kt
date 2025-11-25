package com.honeycomb.disciplineapp.presentation.utils

import com.honeycomb.disciplineapp.presentation.models.CurrentDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

object DataUtils {
    @OptIn(ExperimentalTime::class)
    fun getCurrentFormattedDate(): CurrentDate {
        val now = Clock.System.now()
        val today: LocalDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date

        // Format using Kotlin (manual since no full formatter support)
        val day = today.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }       // Friday
        val month = today.month.name.lowercase().replaceFirstChar { it.uppercase() }         // September
        val date = today.dayOfMonth.toString()                                               // 12

        return CurrentDate(day = day, month = month, date = date)
    }
}