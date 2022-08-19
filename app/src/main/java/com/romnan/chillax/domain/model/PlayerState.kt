package com.romnan.chillax.domain.model

import com.romnan.chillax.R
import kotlinx.collections.immutable.PersistentList

data class PlayerState(
    val phase: PlayerPhase,
    val playingSounds: PersistentList<Sound>,
    // TODO: add moodsList
) {
    val playingSoundsTitle: UIText
        get() = when (this.playingSounds.size) {
            0 -> UIText.StringResource(R.string.no_sound_is_playing)
            1 -> playingSounds[0].readableName
            in 2..Int.MAX_VALUE -> UIText.DynamicString("${playingSounds.size} sounds")
            else -> UIText.DynamicString("")
        }
}