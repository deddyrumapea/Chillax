package com.romnan.chillax.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.domain.repository.AppSettingsRepository
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.domain.repository.SleepTimerRepository
import com.romnan.chillax.presentation.model.PlayerPresentation
import com.romnan.chillax.presentation.model.SleepTimerPresentation
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.model.toPresentation
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
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val player: StateFlow<PlayerPresentation> = playerRepository.player.map { it.toPresentation() }
        .stateIn(viewModelScope, SharingStarted.Lazily, PlayerPresentation.defaultValue)

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
    fun onSoundVolumeChange(sound: SoundPresentation, volume: Float) {
        onSoundVolumeChangeJob?.cancel()
        onSoundVolumeChangeJob = viewModelScope.launch {
            playerRepository.changeSoundVolume(soundName = sound.name, volume = volume)
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