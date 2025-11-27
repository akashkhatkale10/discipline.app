package com.honeycomb.disciplineapp.presentation.ui.common

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle

actual fun pickDate(context: Any?, onDatePicked: (LocalDateTime) -> Unit) {
    val datePicker = UIDatePicker().apply {
        datePickerMode = UIDatePickerMode.UIDatePickerModeDateAndTime
        preferredDatePickerStyle =
            UIDatePickerStyle.UIDatePickerStyleWheels // Use the wheel-style picker
        translatesAutoresizingMaskIntoConstraints = false
    }

    val alertController = UIAlertController.alertControllerWithTitle(
        title = "Pick a date and time",
        message = "\n\n\n\n\n\n\n",
        preferredStyle = UIAlertControllerStyleAlert
    )

    alertController.view.addSubview(datePicker)

    // Set constraints for the DatePicker
    datePicker.centerXAnchor.constraintEqualToAnchor(alertController.view.centerXAnchor).active =
        true
    datePicker.topAnchor.constraintEqualToAnchor(
        alertController.view.topAnchor,
        constant = 10.0
    ).active = true
    datePicker.widthAnchor.constraintEqualToAnchor(
        alertController.view.widthAnchor,
        constant = -20.0
    ).active = true

    alertController.addAction(
        UIAlertAction.actionWithTitle(
            title = "Done",
            style = UIAlertActionStyleDefault
        ) { _ ->
            onDatePicked(datePicker.date.toKotlinLocalDateTime())
        }
    )

    // Present the alert
    UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
        alertController,
        animated = true,
        completion = null
    )
}

fun LocalDateTime.toNSDate(): NSDate =
    NSDate.dateWithTimeIntervalSince1970(this.toInstant(TimeZone.currentSystemDefault()).epochSeconds.toDouble())

fun NSDate.toKotlinLocalDateTime(): LocalDateTime =
    Instant.fromEpochMilliseconds((timeIntervalSince1970() * 1000).toLong())
        .toLocalDateTime(TimeZone.currentSystemDefault())