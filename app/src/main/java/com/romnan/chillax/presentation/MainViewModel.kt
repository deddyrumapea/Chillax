package com.romnan.chillax.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.data.model.CategoryData
import com.romnan.chillax.data.model.MoodData
import com.romnan.chillax.data.model.SoundData
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.presentation.model.CategoryPresentation
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    val player: StateFlow<Player> = playerRepository.player
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Player(phase = PlayerPhase.STOPPED, sounds = persistentListOf())
        )

    val moods: StateFlow<List<MoodData>> = playerRepository.moods
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val categories: StateFlow<List<CategoryPresentation>> = combine(
        playerRepository.categories,
        playerRepository.player
    ) { categories: List<CategoryData>, player: Player ->

        val soundMapper: (SoundData) -> SoundPresentation = { sound ->
            sound.toPresentation(isSelected = player.sounds.any { it.name == sound.name })
        }

        categories.map { it.toPresentation(soundMapper = soundMapper) }

    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var onMoodClickedJob: Job? = null
    fun onMoodClicked(mood: MoodData) {
        onMoodClickedJob?.cancel()
        onMoodClickedJob = viewModelScope.launch {
            playerRepository.addMood(mood)
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
    fun onSoundVolumeChange(sound: SoundData, volume: Float) {
        onSoundVolumeChangeJob?.cancel()
        onSoundVolumeChangeJob = viewModelScope.launch {
            playerRepository.changeSoundVolume(soundName = sound.name, volume = volume)
        }
    }
}