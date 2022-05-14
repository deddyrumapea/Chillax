package com.romnan.chillax.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.romnan.chillax.core.data.model.PlayerStateSerializable
import com.romnan.chillax.core.data.util.PlayerStateSerializer
import com.romnan.chillax.core.domain.model.Mood
import com.romnan.chillax.core.domain.model.PlayerState
import com.romnan.chillax.core.domain.model.Sound
import com.romnan.chillax.core.domain.repository.PlayerStateRepository
import com.romnan.chillax.core.util.Constants
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class DefaultPlayerStateRepository(
    appContext: Context
) : PlayerStateRepository {

    private val dataStore: DataStore<PlayerStateSerializable> = appContext.dataStore

    private val isPlaying = MutableStateFlow(false)

    override fun getState(): Flow<PlayerState> {
        return combine(
            dataStore.data,
            isPlaying
        ) { stateSerializable: PlayerStateSerializable, isPlaying: Boolean ->
            PlayerState(
                isPlaying = isPlaying,
                soundsList = stateSerializable.soundsList.toPersistentList()
            )
        }
    }

    override suspend fun playOrPausePlayer() {
        isPlaying.value = !isPlaying.value
    }

    override suspend fun addOrRemoveSound(sound: Sound) {
        dataStore.updateData { playerState ->
            val playingSounds = playerState.soundsList.toPersistentList()
            playerState.copy(
                soundsList = when {
                    playingSounds.contains(sound) -> playingSounds.remove(sound)
                    playingSounds.size < Constants.MAX_PLAYING_SOUNDS -> {
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
            val playingSounds = playerState.soundsList.toPersistentList()
            val moodSoundsList = mood.soundsList.filter { !playingSounds.contains(it) }
            playerState.copy(
                soundsList = when {
                    playingSounds.size + moodSoundsList.size <= Constants.MAX_PLAYING_SOUNDS -> {
                        playingSounds.addAll(moodSoundsList)
                    }
                    else -> playingSounds
                }
            )
        }
    }

    override suspend fun removeAllSounds() {
        dataStore.updateData { it.copy(soundsList = persistentListOf()) }
    }

    companion object {
        private const val FILE_NAME = "player_state.json"

        private val Context.dataStore by dataStore(
            fileName = FILE_NAME,
            serializer = PlayerStateSerializer
        )
    }
}