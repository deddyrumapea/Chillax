package com.romnan.chillax.data.model

import com.romnan.chillax.domain.model.ThemeMode
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class AppSettingsSerializable(
    val themeModeName: String = ThemeMode.Dark.name,
    val bedtimeInMillis: Long? = null,
)