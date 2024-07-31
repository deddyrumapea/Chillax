package com.romnan.chillax.presentation.model

import com.romnan.chillax.R
import com.romnan.chillax.domain.constant.PlayerConstants
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.UIText

data class PlayerPresentation(
    val phase: PlayerPhase = PlayerPhase.STOPPED,
    val playingSounds: List<SoundPresentation> = emptyList(),
) {
    val phaseTitle: UIText
        get() = when (phase) {
            PlayerPhase.PLAYING -> UIText.StringResource(R.string.phase_title_playing)
            PlayerPhase.PAUSED -> UIText.StringResource(R.string.phase_title_paused)
            PlayerPhase.STOPPED -> UIText.StringResource(R.string.phase_title_stopped)
        }

    val soundsTitle: UIText
        get() = when (playingSounds.size) {
            0 -> UIText.StringResource(R.string.no_sound_is_playing)
            1 -> playingSounds[0].readableName
            in 2 until PlayerConstants.MAX_SOUNDS -> UIText.StringResource(
                id = R.string.count_sounds_format,
                playingSounds.size,
            )

            PlayerConstants.MAX_SOUNDS -> UIText.StringResource(
                id = R.string.max_sounds_format,
                playingSounds.size,
            )

            else -> UIText.Blank
        }
}