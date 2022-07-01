package com.romnan.chillax.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.*
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.presentation.model.CategoryState
import com.romnan.chillax.presentation.model.SoundState
import com.romnan.chillax.presentation.model.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    val playerPhase: StateFlow<PlayerPhase> = playerRepository.playerState.map { it.phase }
        .stateIn(viewModelScope, SharingStarted.Lazily, PlayerPhase.STOPPED)

    val moods: StateFlow<List<Mood>> = playerRepository.moods
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val categories: StateFlow<List<CategoryState>> = combine(
        playerRepository.categories,
        playerRepository.playerState
    ) { categories: List<Category>, playerState: PlayerState ->

        val soundMapper: (Sound) -> SoundState = { sound ->
            sound.toState(isSelected = playerState.playingSounds.contains(sound))
        }

        categories.map { it.toState(soundMapper = soundMapper) }

    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var onMoodClickedJob: Job? = null
    fun onMoodClicked(mood: Mood) {
        onMoodClickedJob?.cancel()
        onMoodClickedJob = viewModelScope.launch {
            playerRepository.addMood(mood)
        }
    }

    private var onSoundClickedJob: Job? = null
    fun onSoundClicked(sound: SoundState) {
        onSoundClickedJob?.cancel()
        onSoundClickedJob = viewModelScope.launch {
            playerRepository.addOrRemoveSound(sound.name)
        }
    }

    private var onPlayPauseClickedJob: Job? = null
    fun onPlayPauseClicked() {
        onPlayPauseClickedJob?.cancel()
        onPlayPauseClickedJob = viewModelScope.launch {
            playerRepository.playOrPausePlayer()
        }
    }

    private var onStopClickedJob: Job? = null
    fun onStopClicked() {
        onStopClickedJob?.cancel()
        onStopClickedJob = viewModelScope.launch {
            playerRepository.removeAllSounds()
        }
    }

    fun onSliderValueChange(sound: SoundState, value: Float) {
        logcat { "${sound.name}: $value" }
    }
}