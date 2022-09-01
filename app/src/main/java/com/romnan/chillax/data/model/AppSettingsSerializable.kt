package com.romnan.chillax.data.model

import com.romnan.chillax.domain.model.AppSettings
import com.romnan.chillax.domain.model.ThemeMode
import kotlinx.serialization.Serializable

@Serializable
data class AppSettingsSerializable(
    val themeModeName: String,
    val bedTimeInMillis: Long?,
) {
    companion object {
        val defaultValue = AppSettingsSerializable(
            themeModeName = ThemeMode.Dark.name,
            bedTimeInMillis = null,
        )
    }
}