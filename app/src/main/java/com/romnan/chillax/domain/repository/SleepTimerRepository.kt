package com.romnan.chillax.domain.repository

import com.romnan.chillax.domain.model.SleepTimer
import kotlinx.coroutines.flow.Flow

interface SleepTimerRepository {
    val sleepTimer: Flow<SleepTimer>

    suspend fun updateTimerRunning(
        timerRunning: Boolean,
    )

    suspend fun updateTimeLeftInMillis(
        timeLeftInMillis: Long,
    )
}