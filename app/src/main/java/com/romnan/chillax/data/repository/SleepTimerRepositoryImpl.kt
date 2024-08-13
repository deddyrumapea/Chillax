package com.romnan.chillax.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.SleepTimerSerializable
import com.romnan.chillax.data.serializer.SleepTimerSerializer
import com.romnan.chillax.domain.model.SleepTimer
import com.romnan.chillax.domain.repository.SleepTimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SleepTimerRepositoryImpl(
    private val appContext: Context,
) : SleepTimerRepository {

    private val sleepTimerDataStore: DataStore<SleepTimerSerializable> =
        appContext.sleepTimerDataStore

    override val sleepTimer: Flow<SleepTimer>
        get() = sleepTimerDataStore.data.map { serializable ->
            SleepTimer(
                timerRunning = serializable.timerRunning,
                timeLeftInMillis = serializable.timeLeftInMillis,
            )
        }

    override suspend fun updateTimerRunning(
        timerRunning: Boolean,
    ) {
        sleepTimerDataStore.updateData {
            it.copy(timerRunning = timerRunning)
        }
    }

    override suspend fun updateTimeLeftInMillis(
        timeLeftInMillis: Long,
    ) {
        sleepTimerDataStore.updateData {
            it.copy(timeLeftInMillis = timeLeftInMillis)
        }
    }

    companion object {
        private const val FILE_NAME = "sleep_timer.json"

        private val Context.sleepTimerDataStore by dataStore(
            fileName = FILE_NAME,
            serializer = SleepTimerSerializer,
        )
    }
}