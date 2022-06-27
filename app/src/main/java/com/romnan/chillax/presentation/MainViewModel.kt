package com.romnan.chillax.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.PlayerState
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.presentation.model.SoundState
import com.romnan.chillax.presentation.model.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    val playerPhase: StateFlow<PlayerPhase> = playerRepository.playerState.map { it.phase }
        .stateIn(viewModelScope, SharingStarted.Lazily, PlayerPhase.Stopped)

    val moodsList: StateFlow<List<Mood>> = playerRepository.moods
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val soundsList: StateFlow<List<SoundState>> = combine(
        playerRepository.sounds,
        playerRepository.playerState
    ) { sounds: List<Sound>, playerState: PlayerState ->
        sounds.map { it.toState().copy(isSelected = playerState.soundsList.contains(it)) }
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
            playerRepository.addOrRemoveSound(sound.toSound())
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
}