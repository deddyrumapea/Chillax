package com.romnan.chillax.presentation.composable.moods

import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.presentation.composable.moods.model.MoodType

data class MoodsState(
    val player: Player? = null,
    val moods: List<Mood> = emptyList(),
    val customMoodToDelete: Mood? = null,
    val moodTypes: List<MoodType> = MoodType.entries,
    val selectedMoodType: MoodType = MoodType.All,
)