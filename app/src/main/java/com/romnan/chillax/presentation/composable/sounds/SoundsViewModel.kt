package com.romnan.chillax.presentation.composable.sounds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.presentation.model.CategoryPresentation
import com.romnan.chillax.presentation.model.SoundPresentation
import com.romnan.chillax.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoundsViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
) : ViewModel() {
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

    private var onSoundClickJob: Job? = null
    fun onSoundClick(sound: SoundPresentation) {
        onSoundClickJob?.cancel()
        onSoundClickJob = viewModelScope.launch {
            playerRepository.addOrRemoveSound(sound.name)
        }
    }
}