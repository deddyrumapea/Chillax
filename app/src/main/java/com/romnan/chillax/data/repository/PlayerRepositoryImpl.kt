package com.romnan.chillax.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.*
import com.romnan.chillax.data.serializer.PlayerSerializer
import com.romnan.chillax.data.source.AppDataSource
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.repository.PlayerRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlin.math.roundToInt

class PlayerRepositoryImpl(
    appContext: Context
) : PlayerRepository {

    private val dataStore: DataStore<PlayerSerializable> = appContext.dataStore

    private val isPlaying = MutableStateFlow(false)

    override val moods: Flow<List<MoodData>>
        get() = flowOf(AppDataSource.moods)

    override val categories: Flow<List<CategoryData>>
        get() = flowOf(AppDataSource.categories)

    override val player: Flow<Player>
        get() = combine(
            dataStore.data,
            isPlaying
        ) { playerSerializable: PlayerSerializable, isPlaying: Boolean ->
            Player(
                phase = when {
                    playerSerializable.sounds.isEmpty() -> PlayerPhase.STOPPED
                    !isPlaying -> PlayerPhase.PAUSED
                    else -> PlayerPhase.PLAYING
                },
                sounds = playerSerializable.sounds
                    .mapNotNull { it.toData() }
                    .sortedBy { it.name }
                    .toPersistentList()
            )
        }

    override suspend fun playOrPausePlayer() {
        isPlaying.value = !isPlaying.value
    }

    override suspend fun addOrRemoveSound(soundName: String) {
        val sound = AppDataSource.getSoundFromName(soundName = soundName) ?: return

        dataStore.updateData { playerState ->
            val currSounds = playerState.sounds.toPersistentList()

            playerState.copy(
                sounds = when {
                    currSounds.any { it.name == sound.name } -> {
                        currSounds.removeAll { it.name == sound.name }
                    }

                    currSounds.size < MAX_PLAYING_SOUNDS -> {
                        isPlaying.value = true
                        currSounds.add(sound.toSerializable())
                    }

                    else -> currSounds
                }
            )
        }
    }

    override suspend fun addMood(mood: MoodData) {
        dataStore.updateData { playerState ->
            val currSounds = playerState.sounds.toPersistentList()

            val moodSounds = mood.sounds.filter { sound ->
                !currSounds.any { it.name == sound.name }
            }

            playerState.copy(
                sounds = when {
                    currSounds.size + moodSounds.size <= MAX_PLAYING_SOUNDS -> {
                        isPlaying.value = true
                        currSounds.addAll(moodSounds.map { it.toSerializable() })
                    }
                    else -> currSounds
                }
            )
        }
    }

    override suspend fun removeAllSounds() {
        dataStore.updateData { it.copy(sounds = persistentListOf()) }
        isPlaying.value = false
    }

    override suspend fun changeSoundVolume(soundName: String, volume: Float) {
        val scaledVolume = (volume * 20).roundToInt() / 20f

        dataStore.updateData { player ->
            val oldSound = player.sounds.find { it.name == soundName } ?: return@updateData player
            if (oldSound.volume == scaledVolume) return@updateData player

            player.copy(
                sounds = player.sounds
                    .toPersistentList()
                    .remove(oldSound)
                    .add(oldSound.copy(volume = scaledVolume))
            )
        }
    }

    companion object {
        private const val MAX_PLAYING_SOUNDS = 8 // TODO: store this in data store

        private const val FILE_NAME = "player_state.json"

        private val Context.dataStore by dataStore(
            fileName = FILE_NAME,
            serializer = PlayerSerializer
        )
    }
}