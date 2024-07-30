package com.romnan.chillax.data.model.serializable

import kotlinx.serialization.Serializable

@Serializable
data class SleepTimerSerializable(
    val timerRunning: Boolean = false,
    val timeLeftInMillis: Long = 0L,
)