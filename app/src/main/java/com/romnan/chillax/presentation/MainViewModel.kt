package com.romnan.chillax.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.R
import com.romnan.chillax.data.model.PlayingSound
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.model.ThemeMode
import com.romnan.chillax.domain.model.UIText
import com.romnan.chillax.domain.repository.AppSettingsRepository
import com.romnan.chillax.domain.repository.MoodRepository
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
    private val moodRepository: MoodRepository,
) : ViewModel() {

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val showSaveMoodDialog = MutableStateFlow(SaveMoodDialogState().showSaveMoodDialog)
    private val moodPresetImageUris = moodRepository.moodPresetImageUris
    private val moodCustomImageUris = moodRepository.moodCustomImageUris
    private val moodCustomImageUriToDelete =
        MutableStateFlow(SaveMoodDialogState().moodCustomImageUriToDelete)

    val saveMoodDialogState: StateFlow<SaveMoodDialogState> = combineTuple(
        showSaveMoodDialog,
        moodPresetImageUris,
        moodCustomImageUris,
        moodCustomImageUriToDelete,
    ).map { (
                showSaveMoodDialog,
                moodPresetImageUris,
                moodCustomImageUris,
                moodCustomImageUriToDelete,

            ) ->
        SaveMoodDialogState(
            showSaveMoodDialog = showSaveMoodDialog,
            moodPresetImageUris = moodPresetImageUris,
            moodCustomImageUris = moodCustomImageUris,
            moodCustomImageUriToDelete = moodCustomImageUriToDelete,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SaveMoodDialogState(),
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
        moodRepository.moods,
    ).map { (
                player: Player,
                sounds: List<Sound>,
                moods: List<Mood>,
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
            playingMood = player.playingMood,
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

    fun onClickSaveMood() {
        showSaveMoodDialog.update { true }
    }

    private var onConfirmSaveMoodClickJob: Job? = null
    fun onClickConfirmSaveMood(
        readableName: String,
        imageUri: String?,
    ) {
        onConfirmSaveMoodClickJob?.cancel()
        onConfirmSaveMoodClickJob = viewModelScope.launch {
            if (readableName.isBlank()) {
                _uiEvent.send(UIEvent.ShowToast(UIText.StringResource(R.string.please_enter_mood_name)))
                return@launch
            }

            if (imageUri.isNullOrBlank()) {
                _uiEvent.send(UIEvent.ShowToast(UIText.StringResource(R.string.please_select_mood_image)))
                return@launch
            }

            showSaveMoodDialog.update { false }
            val mood = moodRepository.saveCustomMood(
                readableName = readableName,
                imageUri = imageUri,
                soundIdToVolume = player.value.playingSounds
                    .associate { sound: SoundPresentation -> sound.id to sound.volume },
            )

            if (mood != null) {
                playerRepository.addMood(
                    mood = mood,
                    autoplay = false,
                )
            }
        }
    }

    fun onDismissSaveMoodDialog() {
        showSaveMoodDialog.update { false }
    }

    suspend fun onPickNewMoodCustomImage(uri: Uri): Uri {
        return moodRepository.saveMoodCustomImage(uri = uri)
    }

    fun onClickRemoveCustomImage(uri: String) {
        moodCustomImageUriToDelete.update { uri }
    }

    private var onClickConfirmDeleteMoodImageJob: Job? = null
    fun onClickConfirmDeleteMoodImage(uri: String) {
        onClickConfirmDeleteMoodImageJob?.cancel()
        onClickConfirmDeleteMoodImageJob = viewModelScope.launch {
            moodRepository.deleteMoodCustomImage(uri = uri)
            onDismissDeleteMoodImageDialog()
        }
    }

    fun onDismissDeleteMoodImageDialog() {
        moodCustomImageUriToDelete.update { null }
    }
}