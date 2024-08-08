package com.romnan.chillax.domain.model

import com.romnan.chillax.data.model.CustomMoodSerializable

data class Mood(
    val id: String,
    val readableName: UIText,
    val imageUri: String,
    val soundIdToVolume: Map<String, Float>,
) {
    val isCustom: Boolean
        get() = id.startsWith(CUSTOM_MOOD_ID_PREFIX)

    constructor(
        uuid: String,
        customMood: CustomMoodSerializable,
    ) : this(
        id = CUSTOM_MOOD_ID_PREFIX.plus(uuid),
        readableName = UIText.DynamicString(customMood.readableName),
        imageUri = customMood.imageUri,
        soundIdToVolume = customMood.soundIdToVolume,
    )

    companion object {
        const val CUSTOM_MOOD_ID_PREFIX = "custom_"
    }
}