package com.romnan.chillax.presentation.model

import com.romnan.chillax.R
import com.romnan.chillax.domain.constant.PlayerConstants
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.PlayerPhase
import com.romnan.chillax.domain.model.UIText

data class PlayerPresentation(
    val phase: PlayerPhase = PlayerPhase.STOPPED,
    val playingSounds: List<SoundPresentation> = emptyList(),
    val playingMood: Mood? = null,
) {
    val contentTitle: UIText
        get() = when (phase) {
            PlayerPhase.PLAYING -> {
                when (playingMood == null) {
                    true -> UIText.StringResource(R.string.phase_title_playing)

                    false -> UIText.PluralsResource(
                        R.plurals.chillax_is_playing_n_sounds,
                        playingSounds.count(),
                        playingSounds.count().toString(),
                    )
                }
            }

            PlayerPhase.PAUSED -> UIText.StringResource(R.string.phase_title_paused)
            PlayerPhase.STOPPED -> UIText.StringResource(R.string.phase_title_stopped)
        }

    val soundsTitle: UIText
        get() = when {
            playingSounds.isEmpty() -> UIText.StringResource(R.string.no_sound_is_playing)

            playingMood != null -> playingMood.readableName

            playingSounds.size == 1 -> playingSounds.firstOrNull()?.readableName ?: UIText.Blank

            playingSounds.size in 2 until PlayerConstants.MAX_SOUNDS -> UIText.StringResource(
                id = R.string.count_sounds_format,
                playingSounds.size,
            )

            playingSounds.size == PlayerConstants.MAX_SOUNDS -> UIText.StringResource(
                id = R.string.max_sounds_format,
                playingSounds.size,
            )

            else -> UIText.Blank
        }
}