package com.romnan.chillax.data.model

import com.romnan.chillax.domain.model.ThemeMode
import kotlinx.serialization.Serializable

@Serializable
data class AppSettingsSerializable(
    val themeModeName: String,
    val bedtimeInMillis: Long?,
) {
    companion object {
        val defaultValue = AppSettingsSerializable(
            themeModeName = ThemeMode.Dark.name,
            bedtimeInMillis = null,
        )
    }
}