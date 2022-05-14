package com.romnan.chillax.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.core.domain.model.Mood
import com.romnan.chillax.core.domain.model.PlayerState
import com.romnan.chillax.core.domain.model.Sound
import com.romnan.chillax.core.domain.repository.CoreRepository
import com.romnan.chillax.core.domain.repository.PlayerStateRepository
import com.romnan.chillax.core.presentation.model.SoundState
import com.romnan.chillax.core.presentation.model.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    coreRepository: CoreRepository,
    private val playerStateRepository: PlayerStateRepository
) : ViewModel() {

    val isPlaying: StateFlow<Boolean> = playerStateRepository.getState().map { it.isPlaying }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val moodsList: StateFlow<List<Mood>> = coreRepository.getMoods()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val soundsList: StateFlow<List<SoundState>> = combine(
        coreRepository.getSounds(),
        playerStateRepository.getState()
    ) { sounds: List<Sound>, playerState: PlayerState ->
        sounds.map {
            it.toState().copy(isSelected = playerState.soundsList.contains(it))
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var onMoodClickedJob: Job? = null
    fun onMoodClicked(mood: Mood) {
        onMoodClickedJob?.cancel()
        onMoodClickedJob = viewModelScope.launch {
            playerStateRepository.addMood(mood)
        }
    }

    private var onSoundClickedJob: Job? = null
    fun onSoundClicked(sound: SoundState) {
        onSoundClickedJob?.cancel()
        onSoundClickedJob = viewModelScope.launch {
            playerStateRepository.addOrRemoveSound(sound.toSound())
        }
    }

    private var onPlayPauseClickedJob: Job? = null
    fun onPlayPauseClicked() {
        onPlayPauseClickedJob?.cancel()
        onPlayPauseClickedJob = viewModelScope.launch {
            playerStateRepository.playOrPausePlayer()
        }
    }

    private var onStopClickedJob: Job? = null
    fun onStopClicked() {
        onStopClickedJob?.cancel()
        onStopClickedJob = viewModelScope.launch {
            playerStateRepository.removeAllSounds()
        }
    }
}