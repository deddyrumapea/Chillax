package com.romnan.chillax.core.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Mood(
    @DrawableRes val image: Int,
    @StringRes val name: Int,
    val soundsList: List<Sound>
)