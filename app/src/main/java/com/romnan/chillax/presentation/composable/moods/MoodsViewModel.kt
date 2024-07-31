package com.romnan.chillax.presentation.composable.moods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoodsViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
) : ViewModel() {

    val moods: StateFlow<List<Mood>> = playerRepository.moods
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )

    private var onMoodClickJob: Job? = null
    fun onMoodClick(mood: Mood) {
        onMoodClickJob?.cancel()
        onMoodClickJob = viewModelScope.launch {
            playerRepository.addMood(moodId = mood.id)
        }
    }
}