package com.romnan.chillax.presentation.composable.moods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.repository.PlayerRepository
import com.romnan.chillax.presentation.model.MoodPresentation
import com.romnan.chillax.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodsViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
) : ViewModel() {

    val moods: StateFlow<List<MoodPresentation>> = playerRepository.moods
        .map { moodsList -> moodsList.map { moodItem -> moodItem.toPresentation() } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var onMoodClickJob: Job? = null
    fun onMoodClick(mood: MoodPresentation) {
        onMoodClickJob?.cancel()
        onMoodClickJob = viewModelScope.launch {
            playerRepository.addMood(mood.name)
        }
    }
}