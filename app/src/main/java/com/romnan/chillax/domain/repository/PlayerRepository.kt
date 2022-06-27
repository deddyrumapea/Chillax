package com.romnan.chillax.domain.repository

import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.PlayerState
import com.romnan.chillax.domain.model.Sound
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val sounds: Flow<List<Sound>>
    val moods: Flow<List<Mood>>
    val playerState: Flow<PlayerState>
    suspend fun playOrPausePlayer()
    suspend fun addOrRemoveSound(sound: Sound)
    suspend fun removeAllSounds()
    suspend fun addMood(mood: Mood)
}