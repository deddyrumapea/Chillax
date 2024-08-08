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
import com.romnan.chillax.domain.repository.MoodRepository
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.domain.repository.SleepTimerRepository
import com.romnan.chillax.presentation.service.PlayerService
import com.zhuinden.flowcombinetuplekt.combineTuple
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class PlayerRepositoryImpl(
    private val appContext: Context,
    private val appScope: CoroutineScope,
    private val sleepTimerRepository: SleepTimerRepository,
    private val countDownTimer: CountDownTimer,
    private val appDataSource: AppDataSource,
    private val moodRepository: MoodRepository,
) : PlayerRepository {

    private val isPlaying = MutableStateFlow(false)

    override val sounds: Flow<List<Sound>>
        get() = flowOf(appDataSource.sounds)

    override val categories: Flow<List<Category>>
        get() = flowOf(appDataSource.categories)

    override val player: Flow<Player>
        get() = combineTuple(
            appContext.playerDataStore.data,
            isPlaying,
            moodRepository.moods,
        ).map { (
                    playerSerializable: PlayerSerializable,
                    isPlaying: Boolean,
                    moods: List<Mood>,
                ) ->
            Player(
                phase = when {
                    playerSerializable.sounds.isEmpty() -> PlayerPhase.STOPPED
                    !isPlaying -> PlayerPhase.PAUSED
                    else -> PlayerPhase.PLAYING
                },
                playingSounds = playerSerializable.sounds
                    .sortedBy { sound: PlayingSound -> sound.startedAt },
                playingMood = playerSerializable.lastPlayedMoodId?.let { moodId: String ->
                    moods.find { mood: Mood ->
                        mood.id == moodId && mood.soundIdToVolume.keys == playerSerializable.sounds
                            .map { sound: PlayingSound -> sound.id }.toSet()
                    }
                },
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

    override suspend fun pausePlayer() {
        isPlaying.update { false }
    }

    override suspend fun playOrPausePlayer() {
        isPlaying.value = !isPlaying.value
    }

    override suspend fun addOrRemoveSound(
        soundId: String,
    ) {
        val sound = appDataSource.sounds.find { it.id == soundId } ?: return

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
                lastPlayedMoodId = null,
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

    override suspend fun addMood(
        mood: Mood,
        autoplay: Boolean,
    ) {
        appContext.playerDataStore.updateData { playerState: PlayerSerializable ->
            playerState.copy(
                sounds = mood.soundIdToVolume.entries.mapIndexed { i: Int, (soundId: String, volume: Float) ->
                    PlayingSound(
                        id = soundId,
                        volume = volume,
                        startedAt = System.currentTimeMillis() + i,
                    )
                },
                lastPlayedMoodId = mood.id,
            )
        }

        if (autoplay) {
            isPlaying.value = true
        }
    }

    companion object {
        private const val FILE_NAME_PLAYER_DATA_STORE = "player.json"

        private val Context.playerDataStore by dataStore(
            fileName = FILE_NAME_PLAYER_DATA_STORE,
            serializer = PlayerSerializer,
        )
    }
}