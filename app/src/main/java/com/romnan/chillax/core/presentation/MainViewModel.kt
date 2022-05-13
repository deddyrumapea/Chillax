package com.romnan.chillax.core.presentation

import androidx.lifecycle.ViewModel
import com.romnan.chillax.core.domain.repository.CoreRepository
import com.romnan.chillax.core.presentation.model.MoodState
import com.romnan.chillax.core.presentation.model.SoundState
import com.romnan.chillax.core.presentation.model.toState
import com.romnan.chillax.core.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    coreRepository: CoreRepository
) : ViewModel() {

    private val _moodsList = MutableStateFlow(
        coreRepository.getMoods().map { it.toState() }
    )
    val moodsList = _moodsList.asStateFlow()

    private val _soundsList = MutableStateFlow(
        coreRepository.getSounds().map { it.toState() }
    )
    val soundsList = _soundsList.asStateFlow()

    fun onMoodClicked(mood: MoodState) {
        // TODO: implement this
        logcat { "Mood clicked ${mood.name}" }
    }

    fun onSoundClicked(sound: SoundState) {
        val listCopy = soundsList.value.map { it.copy() }.toMutableList()

        val itemIndex = soundsList.value.indexOf(sound)
        if (itemIndex == -1) return

        val playingSounds = soundsList.value.filter { it.isPlaying }

        if (!listCopy[itemIndex].isPlaying && playingSounds.size >= Constants.MAX_PLAYING_SOUNDS) {
            // Max playing sound reached, cannot play sound
            return
        }

        listCopy[itemIndex] = listCopy[itemIndex].copy(
            isPlaying = !listCopy[itemIndex].isPlaying
        )

        _soundsList.value = listCopy
    }
}