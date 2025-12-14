package com.honeycomb.disciplineapp.presentation.ui.home

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

data class WeekState(
    val weekStart: LocalDate,   // Monday
    val selectedDate: LocalDate
)

fun LocalDate.startOfWeek(): LocalDate {
    val diff = dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
    return minus(diff, DateTimeUnit.DAY)
}

fun LocalDate.plusWeeks(weeks: Int) =
    plus(weeks * 7, DateTimeUnit.DAY)