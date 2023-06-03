package com.romnan.chillax.domain.repository

import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val moods: Flow<List<Mood>>
    val categories: Flow<List<Category>>
    val player: Flow<Player>
    suspend fun playOrPausePlayer()
    suspend fun addOrRemoveSound(soundName: String)
    suspend fun removeAllSounds()
    suspend fun changeSoundVolume(soundName: String, volume: Float)
    suspend fun addMood(moodName: String)
    suspend fun setSleepTimer(
        hours: Int,
        minutes: Int,
    )

    suspend fun stopSleepTimer()
}