package com.romnan.chillax.presentation.composable.moods

import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.Player

data class MoodsState(
    val player: Player? = null,
    val moods: List<Mood> = emptyList(),
    val customMoodToDelete: Mood? = null,
)