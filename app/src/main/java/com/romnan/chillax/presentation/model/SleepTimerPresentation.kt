package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.SleepTimer

data class SleepTimerPresentation(
    val timerRunning: Boolean,
    val timeLeftInMillis: Long,
) {
    companion object {
        val defaultValue = SleepTimerPresentation(
            timerRunning = false,
            timeLeftInMillis = 0L,
        )
    }
}

fun SleepTimer.toPresentation() = SleepTimerPresentation(
    timerRunning = this.timerRunning,
    timeLeftInMillis = this.timeLeftInMillis,
)