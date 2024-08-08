package com.romnan.chillax.presentation.composable.sounds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.presentation.model.CategoryPresentation
import com.romnan.chillax.presentation.model.SoundPresentation
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoundsViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
) : ViewModel() {

    val categories: StateFlow<List<CategoryPresentation>> = combineTuple(
        playerRepository.categories,
        playerRepository.sounds,
        playerRepository.player,
    ).map { (
                categories,
                sounds,
                player,
            ) ->

        val soundById = sounds.associateBy { it.id }
        val playingSoundById = player.playingSounds.associateBy { it.id }

        categories.map { category: Category ->
            CategoryPresentation(
                id = category.id,
                readableName = category.readableName,
                description = category.description,
                sounds = category.soundIds.mapNotNull { soundId: String ->
                    when (val sound = soundById[soundId]) {
                        null -> null
                        else -> SoundPresentation(
                            id = soundId,
                            readableName = sound.readableName,
                            iconResId = sound.iconResId,
                            audioResId = sound.audioResId,
                            isPlaying = playingSoundById.containsKey(soundId),
                            volume = playingSoundById[soundId]?.volume ?: 0f,
                        )
                    }
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    private var onClickSoundJob: Job? = null
    fun onClickSound(soundId: String) {
        onClickSoundJob?.cancel()
        onClickSoundJob = viewModelScope.launch {
            playerRepository.addOrRemoveSound(soundId = soundId)
        }
    }
}