package com.romnan.chillax.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.PlayerStateSerializable
import com.romnan.chillax.data.serializer.PlayerStateSerializer
import com.romnan.chillax.data.source.AppDataSource
import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.PlayerState
import com.romnan.chillax.domain.repository.PlayerRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class PlayerRepositoryImpl(
    appContext: Context
) : PlayerRepository {

    private val dataStore: DataStore<PlayerStateSerializable> = appContext.dataStore

    private val isPlaying = MutableStateFlow(false)

    override val moods: Flow<List<Mood>>
        get() = flowOf(AppDataSource.moods)

    override val categories: Flow<List<Category>>
        get() = flowOf(AppDataSource.categories)

    override val playerState: Flow<PlayerState>
        get() = combine(
            dataStore.data,
            isPlaying
        ) { stateSerializable: PlayerStateSerializable, isPlaying: Boolean ->
            PlayerState(
                phase = when {
                    stateSerializable.playingSoundsNames.isEmpty() -> PlayerPhase.STOPPED
                    !isPlaying -> PlayerPhase.PAUSED
                    else -> PlayerPhase.PLAYING
                },
                playingSounds = stateSerializable.playingSoundsNames
                    .mapNotNull { AppDataSource.getSoundFromName(it) }
                    .toPersistentList()
            )
        }

    override suspend fun playOrPausePlayer() {
        isPlaying.value = !isPlaying.value
    }

    override suspend fun addOrRemoveSound(soundName: String) {
        if (AppDataSource.getSoundFromName(soundName = soundName) == null) return
        dataStore.updateData { playerState ->
            val playingSounds = playerState.playingSoundsNames.toPersistentList()
            playerState.copy(
                playingSoundsNames = when {
                    playingSounds.contains(soundName) -> playingSounds.remove(soundName)
                    playingSounds.size < MAX_PLAYING_SOUNDS -> {
                        isPlaying.value = true
                        playingSounds.add(soundName)
                    }
                    else -> playingSounds
                }
            )
        }
    }

    override suspend fun addMood(mood: Mood) {
        dataStore.updateData { playerState ->
            val playingSoundsNames = playerState.playingSoundsNames.toPersistentList()

            val moodSoundsNames = mood.sounds
                .map { it.name }
                .filter { !playingSoundsNames.contains(it) }

            playerState.copy(
                playingSoundsNames = when {
                    playingSoundsNames.size + moodSoundsNames.size <= MAX_PLAYING_SOUNDS -> {
                        isPlaying.value = true
                        playingSoundsNames.addAll(moodSoundsNames)
                    }
                    else -> playingSoundsNames
                }
            )
        }
    }

    override suspend fun removeAllSounds() {
        dataStore.updateData { it.copy(playingSoundsNames = persistentListOf()) }
        isPlaying.value = false
    }

    companion object {
        private const val MAX_PLAYING_SOUNDS = 8 // TODO: store this in data store

        private const val FILE_NAME = "player_state.json"

        private val Context.dataStore by dataStore(
            fileName = FILE_NAME,
            serializer = PlayerStateSerializer
        )
    }
}