package com.romnan.chillax.data.model.serializable

import com.romnan.chillax.domain.model.ThemeMode
import kotlinx.serialization.Serializable

@Serializable
data class AppSettingsSerializable(
    val themeModeName: String = ThemeMode.Dark.name,
    val bedtimeInMillis: Long? = null,
)