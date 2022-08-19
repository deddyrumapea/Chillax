package com.romnan.chillax.domain.repository

import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val moods: Flow<List<Mood>>
    val categories: Flow<List<Category>>
    val playerState: Flow<PlayerState>
    suspend fun playOrPausePlayer()
    suspend fun addOrRemoveSound(soundName: String)
    suspend fun removeAllSounds()
    suspend fun addMood(mood: Mood)
}