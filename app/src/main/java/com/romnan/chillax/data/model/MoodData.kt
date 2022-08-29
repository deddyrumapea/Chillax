package com.romnan.chillax.data.model

import com.romnan.chillax.domain.model.UIText

interface MoodData {
    val readableName: UIText
    val imageResId: Int
    val sounds: List<SoundData>
}