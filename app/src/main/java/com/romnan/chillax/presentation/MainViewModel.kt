package com.romnan.chillax.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.R
import com.romnan.chillax.data.model.PlayingSound
import com.romnan.chillax.domain.model.Mix
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.domain.model.UIText
import com.romnan.chillax.domain.repository.AppSettingsRepository
import com.romnan.chillax.domain.repository.MixRepository
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.domain.repository.SleepTimerRepository
import com.romnan.chillax.presentation.model.PlayerPresentation
import com.romnan.chillax.presentation.model.SleepTimerPresentation
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.model.toPresentation
import com.romnan.chillax.presentation.util.UIEvent
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    sleepTimerRepository: SleepTimerRepository,
    appSettingsRepository: AppSettingsRepository,
    private val mixRepository: MixRepository,
) : ViewModel() {

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val showSaveMixDialog = MutableStateFlow(SaveMixDialogState().showSaveMixDialog)
    private val mixPresetImageUris = mixRepository.mixPresetImageUris
    private val mixCustomImageUris = mixRepository.mixCustomImageUris
    private val mixCustomImageUriToDelete =
        MutableStateFlow(SaveMixDialogState().mixCustomImageUriToDelete)

    val saveMixDialogState: StateFlow<SaveMixDialogState> = combineTuple(
        showSaveMixDialog,
        mixPresetImageUris,
        mixCustomImageUris,
        mixCustomImageUriToDelete,
    ).map { (
                showSaveMixDialog,
                mixPresetImageUris,
                mixCustomImageUris,
                mixCustomImageUriToDelete,

            ) ->
        SaveMixDialogState(
            showSaveMixDialog = showSaveMixDialog,
            mixPresetImageUris = mixPresetImageUris,
            mixCustomImageUris = mixCustomImageUris,
            mixCustomImageUriToDelete = mixCustomImageUriToDelete,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SaveMixDialogState(),
    )

    val themeMode: StateFlow<ThemeMode?> = appSettingsRepository.appSettings.map { it.themeMode }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    val player: StateFlow<PlayerPresentation> = combineTuple(
        playerRepository.player,
        playerRepository.sounds,
        mixRepository.mixes,
    ).map { (
                player: Player,
                sounds: List<Sound>,
                mixes: List<Mix>,
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
            playingMix = player.playingMix,
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
        sleepTimer.toPresentation(
            isPickerDialogVisible = isSleepTimerPickerDialogVisible,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = SleepTimerPresentation.defaultValue,
    )

    private var onPlayPauseClickJob: Job? = null
    fun onClickPlayPause() {
        onPlayPauseClickJob?.cancel()
        onPlayPauseClickJob = viewModelScope.launch {
            playerRepository.playOrPausePlayer()
        }
    }

    private var onStopClickJob: Job? = null
    fun onClickStop() {
        onStopClickJob?.cancel()
        onStopClickJob = viewModelScope.launch {
            playerRepository.removeAllSounds()
        }
    }

    private var onSoundVolumeChangeJob: Job? = null
    fun onChangeSoundVolume(
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
    fun onClickTimer() {
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
    fun onClickSetSleepTimer(pickedHours: Int, pickedMinutes: Int) {
        onSetSleepTimerClickJob?.cancel()
        onSetSleepTimerClickJob = viewModelScope.launch {
            _isSleepTimerPickerDialogVisible.value = false
            playerRepository.setSleepTimer(
                hours = pickedHours,
                minutes = pickedMinutes,
            )
        }
    }

    fun onClickSaveMix() {
        showSaveMixDialog.update { true }
    }

    private var onConfirmSaveMixClickJob: Job? = null
    fun onClickConfirmSaveMix(
        readableName: String,
        imageUri: String?,
    ) {
        onConfirmSaveMixClickJob?.cancel()
        onConfirmSaveMixClickJob = viewModelScope.launch {
            if (readableName.isBlank()) {
                _uiEvent.send(UIEvent.ShowToast(UIText.StringResource(R.string.please_enter_mix_name)))
                return@launch
            }

            if (imageUri.isNullOrBlank()) {
                _uiEvent.send(UIEvent.ShowToast(UIText.StringResource(R.string.please_select_mix_image)))
                return@launch
            }

            showSaveMixDialog.update { false }
            val mix = mixRepository.saveCustomMix(
                readableName = readableName,
                imageUri = imageUri,
                soundIdToVolume = player.value.playingSounds
                    .associate { sound: SoundPresentation -> sound.id to sound.volume },
            )

            if (mix != null) {
                playerRepository.addMix(
                    mix = mix,
                    autoplay = false,
                )
            }
        }
    }

    fun onDismissSaveMixDialog() {
        showSaveMixDialog.update { false }
    }

    suspend fun onPickNewMixCustomImage(uri: Uri): Uri {
        return mixRepository.saveMixCustomImage(uri = uri)
    }

    fun onClickRemoveCustomImage(uri: String) {
        mixCustomImageUriToDelete.update { uri }
    }

    private var onClickConfirmDeleteMixImageJob: Job? = null
    fun onClickConfirmDeleteMixImage(uri: String) {
        onClickConfirmDeleteMixImageJob?.cancel()
        onClickConfirmDeleteMixImageJob = viewModelScope.launch {
            mixRepository.deleteMixCustomImage(uri = uri)
            onDismissDeleteMixImageDialog()
        }
    }

    fun onDismissDeleteMixImageDialog() {
        mixCustomImageUriToDelete.update { null }
    }
}