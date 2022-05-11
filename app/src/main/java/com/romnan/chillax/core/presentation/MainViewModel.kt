package com.romnan.chillax.core.presentation

import androidx.lifecycle.ViewModel
import com.romnan.chillax.core.domain.repository.CoreRepository
import com.romnan.chillax.core.presentation.model.PlayableSound
import com.romnan.chillax.core.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    coreRepository: CoreRepository
) : ViewModel() {

    private val _playableSoundsList = MutableStateFlow(
        coreRepository.getSounds().map { PlayableSound(it) }
    )
    val playableSoundsList = _playableSoundsList.asStateFlow()

    fun onSoundClicked(playableSound: PlayableSound) {
        val listCopy = playableSoundsList.value
            .map { it.copy() }
            .apply {
                val itemIndex = playableSoundsList.value.indexOf(playableSound)
                if (itemIndex == -1) return@apply

                val soundItem = get(itemIndex)

                val playingSounds = playableSoundsList.value.filter { it.isPlaying }
                if (!soundItem.isPlaying && playingSounds.size >= Constants.MAX_PLAYING_SOUNDS) {
                    // Max playing sound reached, cannot play sound
                    return@apply
                }

                soundItem.isPlaying = !soundItem.isPlaying
            }

        _playableSoundsList.value = listCopy
    }
}