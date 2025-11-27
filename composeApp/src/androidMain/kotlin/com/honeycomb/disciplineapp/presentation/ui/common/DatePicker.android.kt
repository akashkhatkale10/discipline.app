package com.honeycomb.disciplineapp.presentation.ui.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import kotlinx.datetime.LocalDateTime
import java.lang.String.format
import java.util.Calendar

// Android-specific implementation
actual fun pickDate(context: Any?, onDatePicked: (LocalDateTime) -> Unit) {
    // Ensure context is of type Context
    if (context !is Context) return

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    // Android-specific DatePickerDialog
    DatePickerDialog(
        context,
        { _, sYear, sMonth, sDay ->
            val timePicker = TimePickerDialog(
                context,
                { _, sHour, sMinute ->
                    onDatePicked(
                        LocalDateTime(
                            sYear,
                            sMonth + 1,
                            sDay,
                            sHour,
                            sMinute
                        )
                    )
                },
                hour,
                minute,
                true
            )
            timePicker.show()
        },
        year,
        month,
        day
    ).show()
}