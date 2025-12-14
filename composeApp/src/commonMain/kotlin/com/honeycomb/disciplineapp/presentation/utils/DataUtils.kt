package com.honeycomb.disciplineapp.presentation.utils

import com.honeycomb.disciplineapp.presentation.models.CurrentDate
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
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

    fun getCurrentFormattedDate(today: LocalDate): CurrentDate {
        val day = today.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }       // Friday
        val month = today.month.name.lowercase().replaceFirstChar { it.uppercase() }         // September
        val date = today.dayOfMonth.toString()                                               // 12

        return CurrentDate(day = day, month = month, date = date)
    }

}

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}
fun LocalDate.Companion.now(): LocalDate {
    return LocalDateTime.now().date
}
fun LocalTime.Companion.now(): LocalTime {
    return LocalDateTime.now().time
}
@OptIn(ExperimentalTime::class)
fun LocalDateTime.plus(value: Long, unit: DateTimeUnit.TimeBased): LocalDateTime {
    val timeZone = TimeZone.currentSystemDefault()
    return this.toInstant(timeZone)
        .plus(value, unit)
        .toLocalDateTime(timeZone)
}
@OptIn(ExperimentalTime::class)
fun LocalDateTime.minus(value: Long, unit: DateTimeUnit.TimeBased): LocalDateTime {
    val timeZone = TimeZone.currentSystemDefault()
    return this.toInstant(timeZone)
        .minus(value, unit)
        .toLocalDateTime(timeZone)
}

fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60

    return buildString {
        if (hours > 0) append("${hours}h ")
        append("${minutes}m")
    }.trim()
}

fun epochMillisToHoursMinutes(epochMillis: Long): String {
    val totalSeconds = epochMillis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return buildString {
        if (hours > 0) append("${hours}h ")
        if (minutes > 0 || hours > 0) append("${minutes}m ")
        append("${seconds}s")
    }.trim()
}