package com.romnan.chillax.presentation.composable.settings.component

import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat

fun TimePickerDialog(
    context: Context,
    initHourOfDay: Int,
    initMinute: Int,
    onPicked: (hourOfDay: Int, minute: Int) -> Unit,
) = TimePickerDialog(
    context,
    { _, hourOfDay: Int, minute: Int -> onPicked(hourOfDay, minute) },
    initHourOfDay,
    initMinute,
    DateFormat.is24HourFormat(context),
)