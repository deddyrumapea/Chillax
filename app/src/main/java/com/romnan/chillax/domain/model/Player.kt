package com.romnan.chillax.domain.model

import com.romnan.chillax.data.model.PlayingSound

data class Player(
    val phase: PlayerPhase,
    val playingSounds: List<PlayingSound>,
    val playingMood: Mood?,
)