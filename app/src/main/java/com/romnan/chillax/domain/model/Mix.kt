package com.romnan.chillax.domain.model

import com.romnan.chillax.data.model.CustomMixSerializable

data class Mix(
    val id: String,
    val readableName: UIText,
    val imageUri: String,
    val soundIdToVolume: Map<String, Float>,
) {
    val isCustom: Boolean
        get() = id.startsWith(CUSTOM_MIX_ID_PREFIX)

    constructor(
        uuid: String,
        customMix: CustomMixSerializable,
    ) : this(
        id = CUSTOM_MIX_ID_PREFIX.plus(uuid),
        readableName = UIText.DynamicString(customMix.readableName),
        imageUri = customMix.imageUri,
        soundIdToVolume = customMix.soundIdToVolume,
    )

    companion object {
        const val CUSTOM_MIX_ID_PREFIX = "custom_"
    }
}