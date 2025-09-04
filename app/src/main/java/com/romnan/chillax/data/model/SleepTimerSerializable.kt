package com.romnan.chillax.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class SleepTimerSerializable(
    val timerRunning: Boolean = false,
    val timeLeftInMillis: Long = 0L,
)