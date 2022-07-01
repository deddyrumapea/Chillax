package com.romnan.chillax.domain.model

import kotlinx.collections.immutable.PersistentList

data class PlayerState(
    val phase: PlayerPhase,
    val playingSounds: PersistentList<Sound>,
    // TODO: add moodsList
)