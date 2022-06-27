package com.romnan.chillax.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
data class Sound(
    val id: Int,
    @DrawableRes val icon: Int,
    @StringRes val name: Int,
    @RawRes val resource: Int,
)