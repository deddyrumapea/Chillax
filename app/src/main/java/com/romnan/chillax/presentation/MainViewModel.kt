package com.romnan.chillax.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.data.model.PlayingSound
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.domain.repository.AppSettingsRepository
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.domain.repository.SleepTimerRepository
import com.romnan.chillax.presentation.model.PlayerPresentation
import com.romnan.chillax.presentation.model.SleepTimerPresentation
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.model.toPresentation
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val sleepTimerRepository: SleepTimerRepository,
    appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode?> = appSettingsRepository.appSettings.map { it.themeMode }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    val player: StateFlow<PlayerPresentation> = combineTuple(
        playerRepository.player,
        playerRepository.sounds,
    ).map { (
                player: Player,
                sounds: List<Sound>,
            ) ->
        val soundById = sounds.associateBy { it.id }

        PlayerPresentation(
            phase = player.phase,
            playingSounds = player.playingSounds.mapNotNull { playingSound: PlayingSound ->
                when (val sound = soundById[playingSound.id]) {
                    null -> null
                    else -> {
                        SoundPresentation(
                            id = playingSound.id,
                            readableName = sound.readableName,
                            iconResId = sound.iconResId,
                            audioResId = sound.audioResId,
                            isPlaying = true,
                            volume = playingSound.volume,
                        )
                    }
                }
            },
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PlayerPresentation(),
        )

    private val _isSleepTimerPickerDialogVisible = MutableStateFlow(false)
    val sleepTimer: StateFlow<SleepTimerPresentation> = combine(
        sleepTimerRepository.sleepTimer,
        _isSleepTimerPickerDialogVisible,
    ) { sleepTimer, isSleepTimerPickerDialogVisible ->
        sleepTimer.toPresentation(isPickerDialogVisible = isSleepTimerPickerDialogVisible)
    }.stateIn(viewModelScope, SharingStarted.Lazily, SleepTimerPresentation.defaultValue)

    private var onPlayPauseClickJob: Job? = null
    fun onPlayPauseClick() {
        onPlayPauseClickJob?.cancel()
        onPlayPauseClickJob = viewModelScope.launch {
            playerRepository.playOrPausePlayer()
        }
    }

    private var onStopClickJob: Job? = null
    fun onStopClick() {
        onStopClickJob?.cancel()
        onStopClickJob = viewModelScope.launch {
            playerRepository.removeAllSounds()
        }
    }

    private var onSoundVolumeChangeJob: Job? = null
    fun onSoundVolumeChange(
        soundId: String,
        newVolume: Float,
    ) {
        onSoundVolumeChangeJob?.cancel()
        onSoundVolumeChangeJob = viewModelScope.launch {
            playerRepository.changeSoundVolume(
                soundId = soundId,
                newVolume = newVolume,
            )
        }
    }

    private var onTimerClickJob: Job? = null
    fun onTimerClick() {
        onTimerClickJob?.cancel()
        onTimerClickJob = viewModelScope.launch {
            if (sleepTimer.value.isEnabled) playerRepository.stopSleepTimer()
            else _isSleepTimerPickerDialogVisible.value = true
        }
    }

    fun onDismissSleepTimerPickerDialog() {
        _isSleepTimerPickerDialogVisible.value = false
    }

    private var onSetSleepTimerClickJob: Job? = null
    fun onSetSleepTimerClick(pickedHours: Int, pickedMinutes: Int) {
        onSetSleepTimerClickJob?.cancel()
        onSetSleepTimerClickJob = viewModelScope.launch {
            _isSleepTimerPickerDialogVisible.value = false
            playerRepository.setSleepTimer(
                hours = pickedHours,
                minutes = pickedMinutes,
            )
        }
    }
}