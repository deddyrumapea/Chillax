package com.romnan.chillax.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SleepTimerSerializable(
    val timerRunning: Boolean,
    val timeLeftInMillis: Long,
) {
    companion object {
        val defaultValue = SleepTimerSerializable(
            timerRunning = false,
            timeLeftInMillis = 0L,
        )
    }
}