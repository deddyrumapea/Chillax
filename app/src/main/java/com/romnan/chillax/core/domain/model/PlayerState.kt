package com.romnan.chillax.core.domain.model

import kotlinx.collections.immutable.PersistentList

data class PlayerState(
    val isPlaying: Boolean,
    val soundsList: PersistentList<Sound>,
)