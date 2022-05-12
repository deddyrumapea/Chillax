package com.romnan.chillax.core.presentation

import androidx.lifecycle.ViewModel
import com.romnan.chillax.core.domain.repository.CoreRepository
import com.romnan.chillax.core.presentation.model.SoundPresentation
import com.romnan.chillax.core.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    coreRepository: CoreRepository
) : ViewModel() {

    private val _soundsList = MutableStateFlow(
        coreRepository.getSounds().map { SoundPresentation(it) }
    )
    val soundsList = _soundsList.asStateFlow()

    fun onSoundClicked(sound: SoundPresentation) {
        val listCopy = soundsList.value
            .map { it.copy() }
            .apply {
                val itemIndex = soundsList.value.indexOf(sound)
                if (itemIndex == -1) return@apply

                val soundItem = get(itemIndex)

                val playingSounds = soundsList.value.filter { it.isPlaying }
                if (!soundItem.isPlaying && playingSounds.size >= Constants.MAX_PLAYING_SOUNDS) {
                    // Max playing sound reached, cannot play sound
                    return@apply
                }

                soundItem.isPlaying = !soundItem.isPlaying
            }

        _soundsList.value = listCopy
    }
}