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

    private var onMoodClickJob: Job? = null
    fun onMoodClick(mood: MoodPresentation) {
        onMoodClickJob?.cancel()
        onMoodClickJob = viewModelScope.launch {
            playerRepository.addMood(mood.name)
        }
    }

    private var onSoundClickJob: Job? = null
    fun onSoundClick(sound: SoundPresentation) {
        onSoundClickJob?.cancel()
        onSoundClickJob = viewModelScope.launch {
            playerRepository.addOrRemoveSound(sound.name)
        }
    }

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
}