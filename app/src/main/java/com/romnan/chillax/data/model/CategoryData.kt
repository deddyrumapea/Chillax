package com.romnan.chillax.data.model

import com.romnan.chillax.domain.model.UIText

interface CategoryData {
    val readableName: UIText
    val description: UIText
    val sounds: List<SoundData>
}