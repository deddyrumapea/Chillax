package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.UIText
import com.romnan.chillax.presentation.constant.TimeConstants
import java.text.SimpleDateFormat
import java.util.*

data class BedtimePresentation(
    val isActivated: Boolean,
    val calendar: Calendar,
    val readableTime: UIText?,
) {
    companion object {
        val defaultValue = BedtimePresentation(
            isActivated = false,
            calendar = TimeConstants.DEFAULT_BED_TIME,
            readableTime = null,
        )
    }
}

fun Long?.toBedtimePresentation(): BedtimePresentation {
    val calendar = this?.let { millis ->
        Calendar.getInstance().apply { timeInMillis = millis }
    } ?: TimeConstants.DEFAULT_BED_TIME

    val readableTime = UIText.DynamicString(
        SimpleDateFormat(
            TimeConstants.TWELVE_HOUR_FORMAT,
            Locale.getDefault()
        ).format(calendar.time)
    )

    return BedtimePresentation(
        isActivated = this != null,
        calendar = calendar,
        readableTime = readableTime,
    )
}