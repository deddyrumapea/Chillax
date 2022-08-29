package com.romnan.chillax.presentation.model

import com.romnan.chillax.R
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.UIText

data class PlayerPresentation(
    val phase: PlayerPhase,
    val sounds: List<SoundPresentation>,
) {
    val soundsTitle: UIText
        get() = when (this.sounds.size) {
            0 -> UIText.StringResource(R.string.no_sound_is_playing)
            1 -> sounds[0].readableName
            in 2..Int.MAX_VALUE -> UIText.DynamicString("${sounds.size} sounds")
            else -> UIText.DynamicString("")
        }
}

fun Player.toPresentation() = PlayerPresentation(
    phase = this.phase,
    sounds = this.sounds.map { it.toPresentation() },
)