package com.romnan.chillax.presentation.constant

import java.util.*

object TimeConstants {
    const val TWELVE_HOUR_FORMAT = "hh:mm a"

    val DEFAULT_BED_TIME: Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 21)
        set(Calendar.MINUTE, 0)
    }
}