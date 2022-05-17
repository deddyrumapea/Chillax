package com.romnan.chillax.core.domain.model

import kotlinx.collections.immutable.PersistentList

data class PlayerState(
    val phase: PlayerPhase,
    val soundsList: PersistentList<Sound>,
    // TODO: add moodsList
)