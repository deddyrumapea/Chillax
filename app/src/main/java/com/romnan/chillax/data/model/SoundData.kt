package com.romnan.chillax.data.model

import com.romnan.chillax.data.source.AppDataSource
import com.romnan.chillax.domain.model.UIText

interface SoundData {
    val name: String
    val readableName: UIText
    val iconResId: Int
    val audioResId: Int
    val volume: Float
}

fun SoundSerializable.toData() : SoundData?{
    val sound = AppDataSource.getSoundFromName(this.name) ?: return null
    return object: SoundData{
        override val name: String get() = this@toData.name
        override val readableName: UIText  get() = sound.readableName
        override val iconResId: Int get() = sound.iconResId
        override val audioResId: Int get() = sound.audioResId
        override val volume: Float get() = this@toData.volume
    }
}