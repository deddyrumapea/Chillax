package com.romnan.chillax.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.PlayerStateSerializable
import com.romnan.chillax.data.serializer.PlayerStateSerializer
import com.romnan.chillax.data.source.PlayerDataSource
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.PlayerState
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.repository.PlayerRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class PlayerRepositoryImpl(
    appContext: Context
) : PlayerRepository {

    private val dataStore: DataStore<PlayerStateSerializable> = appContext.dataStore

    private val isPlaying = MutableStateFlow(false)

    override val sounds: Flow<List<Sound>>
        get() = flow { emit(PlayerDataSource.soundsList) }

    override val moods: Flow<List<Mood>>
        get() = flow { emit(PlayerDataSource.moodsList) }

    override val playerState: Flow<PlayerState>
        get() = combine(
            dataStore.data,
            isPlaying
        ) { stateSerializable: PlayerStateSerializable, isPlaying: Boolean ->
            PlayerState(
                phase = when {
                    stateSerializable.playingSounds.isEmpty() -> PlayerPhase.Stopped
                    !isPlaying -> PlayerPhase.Paused
                    else -> PlayerPhase.Playing
                },
                soundsList = stateSerializable.playingSounds.toPersistentList()
            )
        }

    override suspend fun playOrPausePlayer() {
        isPlaying.value = !isPlaying.value
    }

    override suspend fun addOrRemoveSound(sound: Sound) {
        dataStore.updateData { playerState ->
            val playingSounds = playerState.playingSounds.toPersistentList()
            playerState.copy(
                playingSounds = when {
                    playingSounds.contains(sound) -> playingSounds.remove(sound)
                    playingSounds.size < MAX_PLAYING_SOUNDS -> {
                        isPlaying.value = true
                        playingSounds.add(sound)
                    }
                    else -> playingSounds
                }
            )
        }
    }

    override suspend fun addMood(mood: Mood) {
        dataStore.updateData { playerState ->
            val playingSounds = playerState.playingSounds.toPersistentList()
            val moodSoundsList = mood.soundsList.filter { !playingSounds.contains(it) }
            playerState.copy(
                playingSounds = when {
                    playingSounds.size + moodSoundsList.size <= MAX_PLAYING_SOUNDS -> {
                        isPlaying.value = true
                        playingSounds.addAll(moodSoundsList)
                    }
                    else -> playingSounds
                }
            )
        }
    }

    override suspend fun removeAllSounds() {
        dataStore.updateData { it.copy(playingSounds = persistentListOf()) }
        isPlaying.value = false
    }

    companion object {
        private const val MAX_PLAYING_SOUNDS = 8

        private const val FILE_NAME = "player_state.json"

        private val Context.dataStore by dataStore(
            fileName = FILE_NAME,
            serializer = PlayerStateSerializer
        )
    }
}