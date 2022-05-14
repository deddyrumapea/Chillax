package com.romnan.chillax.core.domain.repository

import com.romnan.chillax.core.domain.model.Mood
import com.romnan.chillax.core.domain.model.PlayerState
import com.romnan.chillax.core.domain.model.Sound
import kotlinx.coroutines.flow.Flow

interface PlayerStateRepository {
    fun getState(): Flow<PlayerState>
    suspend fun playOrPausePlayer()
    suspend fun addOrRemoveSound(sound: Sound)
    suspend fun removeAllSounds()
    suspend fun addMood(mood: Mood)
}