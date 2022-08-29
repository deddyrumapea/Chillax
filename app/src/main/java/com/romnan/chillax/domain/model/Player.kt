package com.romnan.chillax.domain.model

import com.romnan.chillax.R
import com.romnan.chillax.data.model.SoundData
import kotlinx.collections.immutable.PersistentList

data class Player(
    val phase: PlayerPhase,
    val sounds: PersistentList<SoundData>,
    // TODO: add moodsList
) {
    val soundsTitle: UIText
        get() = when (this.sounds.size) {
            0 -> UIText.StringResource(R.string.no_sound_is_playing)
            1 -> sounds[0].readableName
            in 2..Int.MAX_VALUE -> UIText.DynamicString("${sounds.size} sounds")
            else -> UIText.DynamicString("")
        }
}