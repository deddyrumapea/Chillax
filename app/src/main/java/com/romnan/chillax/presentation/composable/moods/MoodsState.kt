package com.romnan.chillax.presentation.composable.moods

import com.romnan.chillax.domain.model.Mood

data class MoodsState(
    val moods: List<Mood> = emptyList(),
    val customMoodIdToShowDeleteButton: String? = null,
    val customMoodToDelete: Mood? = null,
)