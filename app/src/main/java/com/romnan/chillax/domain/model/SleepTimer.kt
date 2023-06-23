package com.romnan.chillax.domain.model

data class SleepTimer(
    val timerRunning: Boolean,
    val timeLeftInMillis: Long,
)