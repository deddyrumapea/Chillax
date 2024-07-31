package com.romnan.chillax.data.repository

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.datastore.dataStore
import com.romnan.chillax.data.model.PlayerSerializable
import com.romnan.chillax.data.model.PlayingSound
import com.romnan.chillax.data.serializer.PlayerSerializer
import com.romnan.chillax.data.source.AppDataSource
import com.romnan.chillax.data.util.CountDownTimer
import com.romnan.chillax.domain.constant.PlayerConstants
import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.Sound
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
    private val appContext: Context,
    private val appScope: CoroutineScope,
    private val sleepTimerRepository: SleepTimerRepository,
    private val countDownTimer: CountDownTimer,
) : PlayerRepository {

    private val isPlaying = MutableStateFlow(false)

    override val sounds: Flow<List<Sound>>
        get() = flowOf(AppDataSource.sounds)

    override val moods: Flow<List<Mood>>
        get() = flowOf(AppDataSource.moods)

    override val categories: Flow<List<Category>>
        get() = flowOf(AppDataSource.categories)

    override val player: Flow<Player>
        get() = combine(
            appContext.playerDataStore.data,
            isPlaying,
        ) { playerSerializable: PlayerSerializable, isPlaying: Boolean ->
            Player(
                phase = when {
                    playerSerializable.sounds.isEmpty() -> PlayerPhase.STOPPED
                    !isPlaying -> PlayerPhase.PAUSED
                    else -> PlayerPhase.PLAYING
                },
                playingSounds = playerSerializable.sounds
                    .sortedBy { sound: PlayingSound -> sound.startedAt },
            )
        }

    init {
        appScope.launch {
            player.collectLatest {
                val serviceIntent = Intent(appContext, PlayerService::class.java)

                when (it.phase) {
                    PlayerPhase.PLAYING -> {
                        ContextCompat.startForegroundService(appContext, serviceIntent)
                        startSleepTimer()
                    }

                    PlayerPhase.PAUSED -> {
                        ContextCompat.startForegroundService(appContext, serviceIntent)
                        pauseSleepTimer()
                    }

                    PlayerPhase.STOPPED -> {
                        appContext.stopService(serviceIntent)
                        stopSleepTimer()
                    }
                }
            }
        }
    }

    private suspend fun startSleepTimer() {
        val sleepTimer = sleepTimerRepository.sleepTimer.firstOrNull() ?: return

        if (sleepTimer.timerRunning || sleepTimer.timeLeftInMillis <= 0) return

        countDownTimer.startTimer(
            durationMillis = sleepTimer.timeLeftInMillis,
            countDownInterval = 1000L,
            onTick = { millisUntilFinished: Long ->
                sleepTimerRepository.updateTimeLeftInMillis(millisUntilFinished)
            },
            onFinish = {
                appScope.launch {
                    stopSleepTimer()
                    isPlaying.value = false
                }
            },
        )

        sleepTimerRepository.updateTimerRunning(true)
    }

    private suspend fun pauseSleepTimer() {
        val sleepTimer = sleepTimerRepository.sleepTimer.firstOrNull() ?: return

        if (!sleepTimer.timerRunning) return

        countDownTimer.cancelTimer()
        sleepTimerRepository.updateTimerRunning(false)
    }

    override suspend fun stopSleepTimer() {
        countDownTimer.cancelTimer()
        sleepTimerRepository.updateTimerRunning(false)
        sleepTimerRepository.updateTimeLeftInMillis(0L)
    }

    override suspend fun playOrPausePlayer() {
        isPlaying.value = !isPlaying.value
    }

    override suspend fun addOrRemoveSound(
        soundId: String,
    ) {
        val sound = AppDataSource.sounds.find { it.id == soundId } ?: return

        appContext.playerDataStore.updateData { playerState ->
            val playingSounds = playerState.sounds.toPersistentList()

            playerState.copy(
                sounds = when {
                    playingSounds.any { playingSound: PlayingSound ->
                        playingSound.id == sound.id
                    } -> {
                        playingSounds.removeAll { playingSound: PlayingSound ->
                            playingSound.id == sound.id
                        }
                    }

                    playingSounds.size < PlayerConstants.MAX_SOUNDS -> {
                        isPlaying.value = true

                        playingSounds.add(
                            PlayingSound(
                                id = soundId,
                                volume = PlayerConstants.DEFAULT_SOUND_VOLUME,
                                startedAt = System.currentTimeMillis(),
                            )
                        )
                    }

                    else -> playingSounds
                },
            )
        }
    }

    override suspend fun addMood(
        moodId: String,
    ) {
        val mood = AppDataSource.moods.find { it.id == moodId } ?: return

        appContext.playerDataStore.updateData { playerState ->
            val playingSounds = playerState.sounds.toPersistentList()

            val soundIdsToAdd = mood.soundIds.filter { soundId: String ->
                !playingSounds.any { playingSound: PlayingSound -> playingSound.id == soundId }
            }

            playerState.copy(
                sounds = when {
                    playingSounds.size + soundIdsToAdd.size <= PlayerConstants.MAX_SOUNDS -> {
                        isPlaying.value = true
                        playingSounds.addAll(
                            soundIdsToAdd.mapIndexed { i: Int, soundId: String ->
                                PlayingSound(
                                    id = soundId,
                                    volume = PlayerConstants.DEFAULT_SOUND_VOLUME,
                                    startedAt = System.currentTimeMillis() + i,
                                )
                            },
                        )
                    }

                    else -> playingSounds
                },
            )
        }
    }

    override suspend fun setSleepTimer(
        hours: Int,
        minutes: Int,
    ) {
        stopSleepTimer()
        val durationInMillis = hours * 60 * 60 * 1000L + minutes * 60 * 1000L
        if (durationInMillis <= 0) return
        sleepTimerRepository.updateTimeLeftInMillis(timeLeftInMillis = durationInMillis)
        if (isPlaying.value) startSleepTimer()
    }

    override suspend fun removeAllSounds() {
        appContext.playerDataStore.updateData { it.copy(sounds = persistentListOf()) }
        isPlaying.value = false
    }

    override suspend fun changeSoundVolume(
        soundId: String,
        newVolume: Float,
    ) {
        val scaledVolume = (newVolume * 20).roundToInt() / 20f

        appContext.playerDataStore.updateData { player ->
            val oldSound = player.sounds.find {
                it.id == soundId
            } ?: return@updateData player

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