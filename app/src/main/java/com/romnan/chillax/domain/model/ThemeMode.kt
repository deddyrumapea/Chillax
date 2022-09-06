package com.romnan.chillax.domain.model

import com.romnan.chillax.R

enum class ThemeMode(val readableName: UIText) {
    System(readableName = UIText.StringResource(R.string.theme_system)),
    Light(readableName = UIText.StringResource(R.string.theme_light)),
    Dark(readableName = UIText.StringResource(R.string.theme_dark)),
}