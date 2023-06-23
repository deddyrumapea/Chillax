package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.SleepTimer
import com.romnan.chillax.domain.model.UIText

data class SleepTimerPresentation(
    val timerRunning: Boolean,
    val timeLeftInMillis: Long,
    val isPickerDialogVisible: Boolean,
) {
    val isEnabled: Boolean
        get() = timeLeftInMillis > 0

    val readableTimeLeft: UIText
        get() {
            val roundedUp = (timeLeftInMillis + 999) / 1000 * 1000

            val hours = (roundedUp / 1000) / 3600
            val minutes = (((roundedUp / 1000) % 3600) / 60).toString().padStart(2, '0')
            val seconds = ((roundedUp / 1000) % 60).toString().padStart(2, '0')

            return UIText.DynamicString(
                if (hours > 0) "$hours:$minutes:$seconds"
                else "$minutes:$seconds"
            )
        }

    companion object {
        val defaultValue = SleepTimerPresentation(
            timerRunning = false,
            timeLeftInMillis = 0L,
            isPickerDialogVisible = false,
        )
    }
}

fun SleepTimer.toPresentation(
    isPickerDialogVisible: Boolean,
) = SleepTimerPresentation(
    timerRunning = this.timerRunning,
    timeLeftInMillis = this.timeLeftInMillis,
    isPickerDialogVisible = isPickerDialogVisible,
)