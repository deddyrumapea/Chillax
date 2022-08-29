package com.romnan.chillax.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.presentation.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    val player: StateFlow<PlayerPresentation> = playerRepository.player
        .map { it.toPresentation() }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            PlayerPresentation(phase = PlayerPhase.STOPPED, sounds = persistentListOf())
        )

    val moods: StateFlow<List<MoodPresentation>> = playerRepository.moods
        .map { moodsList -> moodsList.map { moodItem -> moodItem.toPresentation() } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val categories: StateFlow<List<CategoryPresentation>> = combine(
        playerRepository.player,
        playerRepository.categories
    ) { player: Player, categories: List<Category> ->
        categories.map { category ->
            category.toPresentation(soundToPresentation = { sound ->
                sound.toPresentation(isSelected = player.sounds.any { it.name == sound.name })
            })
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var onMoodClickedJob: Job? = null
    fun onMoodClicked(mood: MoodPresentation) {
        onMoodClickedJob?.cancel()
        onMoodClickedJob = viewModelScope.launch {
            playerRepository.addMood(mood.name)
        }
    }

    private var onSoundClickedJob: Job? = null
    fun onSoundClicked(sound: SoundPresentation) {
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

    private var onSoundVolumeChangeJob: Job? = null
    fun onSoundVolumeChange(sound: SoundPresentation, volume: Float) {
        onSoundVolumeChangeJob?.cancel()
        onSoundVolumeChangeJob = viewModelScope.launch {
            playerRepository.changeSoundVolume(soundName = sound.name, volume = volume)
        }
    }
}