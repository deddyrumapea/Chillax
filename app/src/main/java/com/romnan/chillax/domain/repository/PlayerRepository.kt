package com.romnan.chillax.domain.repository

import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Mix
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.Sound
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    val sounds: Flow<List<Sound>>

    val categories: Flow<List<Category>>

    val player: Flow<Player>

    suspend fun playOrPausePlayer()

    suspend fun addOrRemoveSound(
        soundId: String,
    )

    suspend fun removeAllSounds()

    suspend fun changeSoundVolume(
        soundId: String,
        newVolume: Float,
    )

    suspend fun addMix(
        mix: Mix,
        autoplay: Boolean = true,
    )

    suspend fun setSleepTimer(
        hours: Int,
        minutes: Int,
    )

    suspend fun stopSleepTimer()

    suspend fun pausePlayer()
}