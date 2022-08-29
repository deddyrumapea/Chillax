package com.romnan.chillax.domain.repository

import com.romnan.chillax.data.model.CategoryData
import com.romnan.chillax.data.model.MoodData
import com.romnan.chillax.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val moods: Flow<List<MoodData>>
    val categories: Flow<List<CategoryData>>
    val player: Flow<Player>
    suspend fun playOrPausePlayer()
    suspend fun addOrRemoveSound(soundName: String)
    suspend fun removeAllSounds()
    suspend fun changeSoundVolume(soundName: String, volume: Float)
    suspend fun addMood(mood: MoodData)
}