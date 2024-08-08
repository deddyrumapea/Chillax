package com.romnan.chillax.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SleepTimerSerializable(
    val timerRunning: Boolean = false,
    val timeLeftInMillis: Long = 0L,
)