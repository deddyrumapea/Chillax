package com.romnan.chillax.data.repository

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.PlayerSerializable
import com.romnan.chillax.data.model.SoundData
import com.romnan.chillax.data.serializer.PlayerSerializer
import com.romnan.chillax.data.source.AppDataSource
import com.romnan.chillax.data.util.CountDownTimer
import com.romnan.chillax.domain.constant.PlayerConstants
import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.domain.repository.SleepTimerRepository
import com.romnan.chillax.presentation.service.PlayerService
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class PlayerRepositoryImpl(
    appContext: Context,
    private val appScope: CoroutineScope,
    private val sleepTimerRepository: SleepTimerRepository,
    private val countDownTimer: CountDownTimer,
) : PlayerRepository {

    private val playerDataStore: DataStore<PlayerSerializable> = appContext.playerDataStore

    private val isPlaying = MutableStateFlow(false)

    override val moods: Flow<List<Mood>>
        get() = flowOf(AppDataSource.moods.map { it.toDomain() })

    override val categories: Flow<List<Category>>
        get() = flowOf(AppDataSource.categories.map { it.toDomain() })

    override val player: Flow<Player>
        get() = combine(
            playerDataStore.data,
            isPlaying,
        ) { playerSerializable: PlayerSerializable, isPlaying: Boolean ->
            Player(
                phase = when {
                    playerSerializable.sounds.isEmpty() -> PlayerPhase.STOPPED
                    !isPlaying -> PlayerPhase.PAUSED
                    else -> PlayerPhase.PLAYING
                },
                sounds = playerSerializable.sounds.sortedBy { it.startedAt }
                    .mapNotNull { it.toDomain() },
            )
        }

    init {
        appScope.launch {
            player.collectLatest {
                val serviceIntent = Intent(appContext, PlayerService::class.java)
                when (it.phase) {
                    PlayerPhase.PLAYING -> {
                        ContextCompat.startForegroundService(appContext, serviceIntent)
                        startTimer()
                    }

                    PlayerPhase.PAUSED -> {
                        ContextCompat.startForegroundService(appContext, serviceIntent)
                        pauseTimer()
                    }

                    PlayerPhase.STOPPED -> {
                        appContext.stopService(serviceIntent)
                        stopTimer()
                    }
                }
            }
        }
    }

    private suspend fun startTimer() {
        val sleepTimer = sleepTimerRepository.sleepTimer.firstOrNull() ?: return

        if (sleepTimer.timerRunning || sleepTimer.timeLeftInMillis <= 0) return

        countDownTimer.startTimer(
            durationMillis = sleepTimer.timeLeftInMillis,
            countDownInterval = 1000L,
            onTick = { millisUntilFinished ->
                sleepTimerRepository.updateTimeLeftInMillis(millisUntilFinished)
            },
            onFinish = {
                appScope.launch {
                    stopTimer()
                    isPlaying.value = false
                }
            },
        )

        sleepTimerRepository.updateTimerRunning(true)
    }

    private suspend fun pauseTimer() {
        val sleepTimer = sleepTimerRepository.sleepTimer.firstOrNull() ?: return

        if (!sleepTimer.timerRunning) return

        countDownTimer.cancelTimer()
        sleepTimerRepository.updateTimerRunning(false)
    }

    private suspend fun stopTimer() {
        countDownTimer.cancelTimer()
        sleepTimerRepository.updateTimerRunning(false)
        sleepTimerRepository.updateTimeLeftInMillis(0L)
    }

    override suspend fun playOrPausePlayer() {
        isPlaying.value = !isPlaying.value
    }

    override suspend fun addOrRemoveSound(soundName: String) {
        val sound = AppDataSource.getSoundFromName(soundName = soundName) ?: return

        playerDataStore.updateData { playerState ->
            val currSounds = playerState.sounds.toPersistentList()

            playerState.copy(
                sounds = when {
                    currSounds.any { it.name == sound.name } -> {
                        currSounds.removeAll { it.name == sound.name }
                    }

                    currSounds.size < PlayerConstants.MAX_SOUNDS -> {
                        isPlaying.value = true
                        currSounds.add(sound.toSerializable(startedAt = System.currentTimeMillis()))
                    }

                    else -> currSounds
                },
            )
        }
    }

    override suspend fun addMood(moodName: String) {
        val mood = AppDataSource.getMoodFromName(moodName = moodName) ?: return

        playerDataStore.updateData { playerState ->
            val currSounds = playerState.sounds.toPersistentList()

            val moodSounds = mood.sounds.filter { sound ->
                !currSounds.any { it.name == sound.name }
            }

            playerState.copy(
                sounds = when {
                    currSounds.size + moodSounds.size <= PlayerConstants.MAX_SOUNDS -> {
                        isPlaying.value = true
                        currSounds.addAll(moodSounds.mapIndexed { i: Int, sound: SoundData ->
                            sound.toSerializable(startedAt = System.currentTimeMillis() + i)
                        })
                    }

                    else -> currSounds
                }
            )
        }
    }

    override suspend fun setSleepTimer(durationInMillis: Long) {
        sleepTimerRepository.updateTimeLeftInMillis(timeLeftInMillis = durationInMillis)
        if (isPlaying.value) startTimer()
    }

    override suspend fun removeAllSounds() {
        playerDataStore.updateData { it.copy(sounds = persistentListOf()) }
        isPlaying.value = false
    }

    override suspend fun changeSoundVolume(soundName: String, volume: Float) {
        val scaledVolume = (volume * 20).roundToInt() / 20f

        playerDataStore.updateData { player ->
            val oldSound = player.sounds.find { it.name == soundName } ?: return@updateData player
            if (oldSound.volume == scaledVolume) return@updateData player

            player.copy(
                sounds = player.sounds.toPersistentList().remove(oldSound)
                    .add(oldSound.copy(volume = scaledVolume)),
            )
        }
    }

    companion object {
        private const val FILE_NAME = "player.json"

        private val Context.playerDataStore by dataStore(
            fileName = FILE_NAME,
            serializer = PlayerSerializer,
        )
    }
}