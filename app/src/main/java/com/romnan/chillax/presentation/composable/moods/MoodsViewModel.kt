package com.romnan.chillax.presentation.composable.moods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.repository.MoodRepository
import com.romnan.chillax.domain.repository.PlayerRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodsViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val moodRepository: MoodRepository,
) : ViewModel() {

    private val player = playerRepository.player
    private val moods = moodRepository.moods
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )
    private val customMoodToDelete = MutableStateFlow(MoodsState().customMoodToDelete)

    val state: StateFlow<MoodsState> = combineTuple(
        player,
        moods,
        customMoodToDelete,
    ).map { (
                player,
                moods,
                customMoodToDelete,
            ) ->
        MoodsState(
            player = player,
            moods = moods,
            customMoodToDelete = customMoodToDelete,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = MoodsState(),
    )

    fun onClickDeleteMood(mood: Mood) {
        if (mood.isCustom) {
            customMoodToDelete.update { mood }
        }
    }

    fun onDismissDeleteMoodDialog() {
        customMoodToDelete.update { null }
    }

    private var onClickConfirmDeleteMoodJob: Job? = null
    fun onClickConfirmDeleteMood(mood: Mood) {
        onClickConfirmDeleteMoodJob?.cancel()
        onClickConfirmDeleteMoodJob = viewModelScope.launch {
            moodRepository.deleteCustomMood(moodId = mood.id)
            customMoodToDelete.update { null }
        }
    }

    private var onClickPlayOrPauseJob: Job? = null
    fun onClickPlayOrPause(mood: Mood) {
        onClickPlayOrPauseJob?.cancel()
        onClickPlayOrPauseJob = viewModelScope.launch {
            val player = state.value.player

            when {
                player?.playingMood?.id == mood.id && player.phase == PlayerPhase.PLAYING -> {
                    playerRepository.pausePlayer()
                }

                else -> {
                    customMoodToDelete.update { null }
                    playerRepository.addMood(mood = mood)
                }
            }
        }
    }
}