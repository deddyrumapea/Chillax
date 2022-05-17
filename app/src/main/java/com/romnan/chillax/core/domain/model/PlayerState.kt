package com.romnan.chillax.core.domain.model

import androidx.annotation.StringRes
import com.romnan.chillax.R
import kotlinx.collections.immutable.PersistentList

data class PlayerState(
    val phase: PlayerPhase,
    val soundsList: PersistentList<Sound>,
)